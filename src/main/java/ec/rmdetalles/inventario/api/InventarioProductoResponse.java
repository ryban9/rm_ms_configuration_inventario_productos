package ec.rmdetalles.inventario.api;

import ec.rmdetalles.inventario.domain.InventarioProducto;
import ec.rmdetalles.inventario.domain.Producto;
import java.time.OffsetDateTime;
import java.util.UUID;

public record InventarioProductoResponse(UUID productoId,String codigo,String nombre,int stockActual,OffsetDateTime actualizadoEn) {
    public static InventarioProductoResponse from(Producto producto,InventarioProducto inventario) {
        return new InventarioProductoResponse(producto.getId(),producto.getCodigo(),producto.getNombre(),
                inventario==null?0:inventario.getStockActual(),inventario==null?null:inventario.getActualizadoEn());
    }
}
