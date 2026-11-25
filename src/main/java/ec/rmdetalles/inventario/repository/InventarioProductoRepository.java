package ec.rmdetalles.inventario.repository;

import ec.rmdetalles.inventario.domain.InventarioProducto;
import ec.rmdetalles.inventario.domain.InventarioProductoId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface InventarioProductoRepository extends JpaRepository<InventarioProducto,InventarioProductoId> {
    Optional<InventarioProducto> findByNegocioIdAndProductoId(UUID negocioId,UUID productoId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventarioProducto i where i.negocioId=:negocioId and i.productoId=:productoId")
    Optional<InventarioProducto> bloquearPorNegocioYProducto(@Param("negocioId") UUID negocioId,@Param("productoId") UUID productoId);
    @Modifying
    @Query(value="INSERT INTO rm_detalles.inventario_productos (negocio_id,producto_id,stock_actual) VALUES (:negocioId,:productoId,0) ON CONFLICT (negocio_id,producto_id) DO NOTHING",nativeQuery=true)
    void crearFilaSiFalta(@Param("negocioId") UUID negocioId,@Param("productoId") UUID productoId);
}
