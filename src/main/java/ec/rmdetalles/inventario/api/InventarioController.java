package ec.rmdetalles.inventario.api;

import ec.rmdetalles.inventario.service.InventarioProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventario/productos")
public class InventarioController {
    private final InventarioProductoService service;
    public InventarioController(InventarioProductoService service) { this.service=service; }
    @GetMapping public List<InventarioProductoResponse> listar() { return service.listar(); }
    @GetMapping("/{productoId}") public InventarioProductoResponse obtener(@PathVariable UUID productoId) { return service.obtener(productoId); }
    @GetMapping("/{productoId}/movimientos") public List<MovimientoProductoResponse> movimientos(@PathVariable UUID productoId) { return service.listarMovimientos(productoId); }
    @PostMapping("/{productoId}/movimientos") @ResponseStatus(HttpStatus.CREATED)
    public MovimientoRegistradoResponse registrar(@PathVariable UUID productoId,@Valid @RequestBody RegistrarMovimientoRequest request) {
        return service.registrar(productoId,request);
    }
}
