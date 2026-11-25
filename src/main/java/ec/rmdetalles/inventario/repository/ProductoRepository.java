package ec.rmdetalles.inventario.repository;

import ec.rmdetalles.inventario.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto,UUID> {
    List<Producto> findByNegocioIdAndControlaStockTrueAndActivoTrueOrderByNombreAsc(UUID negocioId);
    Optional<Producto> findByIdAndNegocioIdAndControlaStockTrueAndActivoTrue(UUID id,UUID negocioId);
}
