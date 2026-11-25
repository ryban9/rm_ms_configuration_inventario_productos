package ec.rmdetalles.inventario.repository;

import ec.rmdetalles.inventario.domain.TipoMovimientoProducto;
import ec.rmdetalles.inventario.domain.TipoMovimientoProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TipoMovimientoProductoRepository extends JpaRepository<TipoMovimientoProducto,TipoMovimientoProductoId> {
    Optional<TipoMovimientoProducto> findByNegocioIdAndCodigoAndActivoTrue(UUID negocioId,String codigo);
}
