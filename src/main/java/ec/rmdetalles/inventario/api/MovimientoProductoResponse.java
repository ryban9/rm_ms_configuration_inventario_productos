package ec.rmdetalles.inventario.api;

import ec.rmdetalles.inventario.domain.MovimientoProducto;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MovimientoProductoResponse(UUID id,UUID productoId,String tipoCodigo,int cantidad,String motivo,
                                          String referenciaTipo,UUID referenciaId,UUID registradoPor,OffsetDateTime creadoEn) {
    public static MovimientoProductoResponse from(MovimientoProducto m) {
        return new MovimientoProductoResponse(m.getId(),m.getProductoId(),m.getTipoCodigo(),m.getCantidad(),
                m.getMotivo(),m.getReferenciaTipo(),m.getReferenciaId(),m.getRegistradoPor(),m.getCreadoEn());
    }
}
