package ec.rmdetalles.inventario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name="tipos_movimiento_producto",schema="rm_detalles")
@IdClass(TipoMovimientoProductoId.class)
public class TipoMovimientoProducto {
    @Id @Column(name="negocio_id",nullable=false) private UUID negocioId;
    @Id @Column(nullable=false,length=20) private String codigo;
    @Column(nullable=false,length=80) private String nombre;
    @Column(name="factor_stock",nullable=false) private short factorStock;
    @Column(name="permite_negativo",nullable=false) private boolean permiteNegativo;
    @Column(nullable=false) private boolean activo;
    protected TipoMovimientoProducto() { }
    public UUID getNegocioId() { return negocioId; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public short getFactorStock() { return factorStock; }
    public boolean isPermiteNegativo() { return permiteNegativo; }
    public boolean isActivo() { return activo; }
}
