package ec.rmdetalles.inventario.repository;

import ec.rmdetalles.inventario.domain.MovimientoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface MovimientoProductoRepository extends JpaRepository<MovimientoProducto,UUID> {
    List<MovimientoProducto> findByNegocioIdAndProductoIdOrderByCreadoEnDesc(UUID negocioId,UUID productoId);
}
