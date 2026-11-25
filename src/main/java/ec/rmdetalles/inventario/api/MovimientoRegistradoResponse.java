package ec.rmdetalles.inventario.api;

import ec.rmdetalles.inventario.domain.MovimientoProducto;

public record MovimientoRegistradoResponse(MovimientoProductoResponse movimiento,int variacionStock,int stockActual) {
    public static MovimientoRegistradoResponse from(MovimientoProducto movimiento,int delta,int stock) {
        return new MovimientoRegistradoResponse(MovimientoProductoResponse.from(movimiento),delta,stock);
    }
}
