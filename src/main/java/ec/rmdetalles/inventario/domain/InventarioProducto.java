package ec.rmdetalles.inventario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="inventario_productos",schema="rm_detalles")
@IdClass(InventarioProductoId.class)
public class InventarioProducto {
    @Id @Column(name="negocio_id",nullable=false) private UUID negocioId;
    @Id @Column(name="producto_id",nullable=false) private UUID productoId;
    @Column(name="stock_actual",nullable=false) private int stockActual;
    @UpdateTimestamp @Column(name="actualizado_en",nullable=false) private OffsetDateTime actualizadoEn;
    protected InventarioProducto() { }
    public int getStockActual() { return stockActual; }
    public OffsetDateTime getActualizadoEn() { return actualizadoEn; }
    public void aplicarCambio(int variacion) { this.stockActual=Math.addExact(stockActual,variacion); }
}
