package pe.edu.upeu.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.eventos.entity.AsistenciaEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<AsistenciaEntity, Long> {

    List<AsistenciaEntity> findByUsuarioId(Long usuarioId);

    List<AsistenciaEntity> findByFechaEventoId(Long fechaEventoId);

    Optional<AsistenciaEntity> findByUsuarioIdAndFechaEventoId(Long usuarioId, Long fechaEventoId);

    Boolean existsByUsuarioIdAndFechaEventoId(Long usuarioId, Long fechaEventoId);

    List<AsistenciaEntity> findByTipoRegistro(AsistenciaEntity.TipoRegistroEnum tipoRegistro);

    @Query("SELECT a FROM AsistenciaEntity a WHERE a.fechaEvento.evento.id = :eventoId")
    List<AsistenciaEntity> findByEventoId(@Param("eventoId") Long eventoId);

    @Query("SELECT COUNT(a) FROM AsistenciaEntity a WHERE a.fechaEvento.id = :fechaEventoId")
    Integer countByFechaEventoId(@Param("fechaEventoId") Long fechaEventoId);
}
