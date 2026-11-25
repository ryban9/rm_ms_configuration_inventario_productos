package ec.rmdetalles.inventario.service;

import ec.rmdetalles.inventario.api.InventarioProductoResponse;
import ec.rmdetalles.inventario.api.MovimientoProductoResponse;
import ec.rmdetalles.inventario.api.MovimientoRegistradoResponse;
import ec.rmdetalles.inventario.api.RegistrarMovimientoRequest;
import ec.rmdetalles.inventario.config.NegocioContext;
import ec.rmdetalles.inventario.domain.InventarioProducto;
import ec.rmdetalles.inventario.domain.MovimientoProducto;
import ec.rmdetalles.inventario.domain.Producto;
import ec.rmdetalles.inventario.domain.TipoMovimientoProducto;
import ec.rmdetalles.inventario.repository.InventarioProductoRepository;
import ec.rmdetalles.inventario.repository.MovimientoProductoRepository;
import ec.rmdetalles.inventario.repository.ProductoRepository;
import ec.rmdetalles.inventario.repository.TipoMovimientoProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class InventarioProductoService {
    private final ProductoRepository productos;
    private final InventarioProductoRepository inventarios;
    private final TipoMovimientoProductoRepository tipos;
    private final MovimientoProductoRepository movimientos;

    public InventarioProductoService(ProductoRepository productos,InventarioProductoRepository inventarios,
                                    TipoMovimientoProductoRepository tipos,MovimientoProductoRepository movimientos) {
        this.productos=productos; this.inventarios=inventarios; this.tipos=tipos; this.movimientos=movimientos;
    }

    @Transactional(readOnly=true)
    public List<InventarioProductoResponse> listar() {
        UUID negocioId=NegocioContext.getRequired();
        return productos.findByNegocioIdAndControlaStockTrueAndActivoTrueOrderByNombreAsc(negocioId).stream()
                .map(p -> InventarioProductoResponse.from(p,inventarios.findByNegocioIdAndProductoId(negocioId,p.getId()).orElse(null)))
                .toList();
    }

    @Transactional(readOnly=true)
    public InventarioProductoResponse obtener(UUID productoId) {
        UUID negocioId=NegocioContext.getRequired();
        Producto producto=resolverProducto(productoId,negocioId);
        return InventarioProductoResponse.from(producto,inventarios.findByNegocioIdAndProductoId(negocioId,productoId).orElse(null));
    }

    @Transactional(readOnly=true)
    public List<MovimientoProductoResponse> listarMovimientos(UUID productoId) {
        UUID negocioId=NegocioContext.getRequired();
        resolverProducto(productoId,negocioId);
        return movimientos.findByNegocioIdAndProductoIdOrderByCreadoEnDesc(negocioId,productoId).stream()
                .map(MovimientoProductoResponse::from).toList();
    }

    public MovimientoRegistradoResponse registrar(UUID productoId,RegistrarMovimientoRequest request) {
        UUID negocioId=NegocioContext.getRequired();
        Producto producto=resolverProducto(productoId,negocioId);
        if (request.cantidad()<=0) throw new IllegalArgumentException("La cantidad debe ser un entero positivo.");
        String tipoCodigo=request.tipoCodigo().trim().toUpperCase(Locale.ROOT);
        TipoMovimientoProducto tipo=tipos.findByNegocioIdAndCodigoAndActivoTrue(negocioId,tipoCodigo)
                .orElseThrow(() -> new IllegalArgumentException("El tipo de movimiento no está activo para este negocio."));
        int delta;
        try { delta=Math.multiplyExact(request.cantidad(),(int)tipo.getFactorStock()); }
        catch (ArithmeticException ex) { throw new IllegalArgumentException("La cantidad excede el límite permitido."); }

        inventarios.crearFilaSiFalta(negocioId,productoId);
        InventarioProducto inventario=inventarios.bloquearPorNegocioYProducto(negocioId,productoId)
                .orElseThrow(() -> new IllegalStateException("No se pudo preparar el registro de inventario."));
        long nuevoStock=(long)inventario.getStockActual()+delta;
        if (nuevoStock<0) throw new ResponseStatusException(HttpStatus.CONFLICT,"El movimiento dejaría el stock en negativo.");
        if (nuevoStock>Integer.MAX_VALUE) throw new ResponseStatusException(HttpStatus.CONFLICT,"El stock supera el máximo permitido.");
        inventario.aplicarCambio(delta);

        String motivo=limpiar(request.motivo());
        String referenciaTipo=limpiar(request.referenciaTipo());
        MovimientoProducto movimiento=movimientos.saveAndFlush(new MovimientoProducto(negocioId,producto.getId(),tipoCodigo,
                request.cantidad(),motivo,referenciaTipo,request.referenciaId(),usuarioActual()));
        return MovimientoRegistradoResponse.from(movimiento,delta,inventario.getStockActual());
    }

    private Producto resolverProducto(UUID id,UUID negocioId) {
        return productos.findByIdAndNegocioIdAndControlaStockTrueAndActivoTrue(id,negocioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un producto activo con control de stock en este negocio."));
    }

    private UUID usuarioActual() {
        var auth=SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwt) {
            try { return UUID.fromString(jwt.getToken().getSubject()); }
            catch (RuntimeException ignored) { return null; }
        }
        return null;
    }

    private String limpiar(String value) { return value==null||value.isBlank()?null:value.trim(); }
}
