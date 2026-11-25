package ec.rmdetalles.inventario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name="productos",schema="rm_detalles")
public class Producto {
    @Id private UUID id;
    @Column(name="negocio_id",nullable=false) private UUID negocioId;
    @Column(nullable=false,length=50) private String codigo;
    @Column(nullable=false,length=160) private String nombre;
    @Column(name="controla_stock",nullable=false) private boolean controlaStock;
    @Column(nullable=false) private boolean activo;
    protected Producto() { }
    public UUID getId() { return id; }
    public UUID getNegocioId() { return negocioId; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public boolean isControlaStock() { return controlaStock; }
    public boolean isActivo() { return activo; }
}
