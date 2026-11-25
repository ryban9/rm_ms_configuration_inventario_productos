package ec.rmdetalles.inventario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="movimientos_productos",schema="rm_detalles")
public class MovimientoProducto {
    @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
    @Column(name="negocio_id",nullable=false,updatable=false) private UUID negocioId;
    @Column(name="producto_id",nullable=false,updatable=false) private UUID productoId;
    @Column(name="tipo_codigo",nullable=false,length=20,updatable=false) private String tipoCodigo;
    @Column(nullable=false,updatable=false) private int cantidad;
    @Column(length=240,updatable=false) private String motivo;
    @Column(name="referencia_tipo",length=30,updatable=false) private String referenciaTipo;
    @Column(name="referencia_id",updatable=false) private UUID referenciaId;
    @Column(name="registrado_por",updatable=false) private UUID registradoPor;
    @CreationTimestamp @Column(name="creado_en",nullable=false,updatable=false) private OffsetDateTime creadoEn;
    protected MovimientoProducto() { }
    public MovimientoProducto(UUID negocioId,UUID productoId,String tipoCodigo,int cantidad,String motivo,
                              String referenciaTipo,UUID referenciaId,UUID registradoPor) {
        this.negocioId=negocioId; this.productoId=productoId; this.tipoCodigo=tipoCodigo; this.cantidad=cantidad;
        this.motivo=motivo; this.referenciaTipo=referenciaTipo; this.referenciaId=referenciaId; this.registradoPor=registradoPor;
    }
    public UUID getId() { return id; }
    public UUID getProductoId() { return productoId; }
    public String getTipoCodigo() { return tipoCodigo; }
    public int getCantidad() { return cantidad; }
    public String getMotivo() { return motivo; }
    public String getReferenciaTipo() { return referenciaTipo; }
    public UUID getReferenciaId() { return referenciaId; }
    public UUID getRegistradoPor() { return registradoPor; }
    public OffsetDateTime getCreadoEn() { return creadoEn; }
}
