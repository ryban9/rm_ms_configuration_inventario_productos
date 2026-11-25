package ec.rmdetalles.inventario.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class InventarioProductoId implements Serializable {
    private UUID negocioId;
    private UUID productoId;
    public InventarioProductoId() { }
    public InventarioProductoId(UUID negocioId,UUID productoId) { this.negocioId=negocioId; this.productoId=productoId; }
    @Override public boolean equals(Object o) {
        if (this==o) return true;
        if (!(o instanceof InventarioProductoId that)) return false;
        return Objects.equals(negocioId,that.negocioId) && Objects.equals(productoId,that.productoId);
    }
    @Override public int hashCode() { return Objects.hash(negocioId,productoId); }
}
