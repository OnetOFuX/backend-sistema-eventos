package pe.edu.upeu.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.eventos.entity.EventoEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<EventoEntity, Long> {

    List<EventoEntity> findByActivoTrue();

    List<EventoEntity> findByTipo(EventoEntity.TipoEventoEnum tipo);

    List<EventoEntity> findByTipoAndActivoTrue(EventoEntity.TipoEventoEnum tipo);

    List<EventoEntity> findByCreadorId(Long creadorId);

    List<EventoEntity> findByEsPago(Boolean esPago);

    List<EventoEntity> findByEsPagoAndActivoTrue(Boolean esPago);

    @Query("SELECT e FROM EventoEntity e JOIN e.fechas f WHERE f.fechaInicio >= :fechaInicio AND f.fechaInicio <= :fechaFin")
    List<EventoEntity> findEventosByRangoFechas(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT e FROM EventoEntity e WHERE e.activo = true AND " +
           "EXISTS (SELECT f FROM e.fechas f WHERE f.fechaInicio > :now)")
    List<EventoEntity> findEventosProximos(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(i) FROM InscripcionEntity i WHERE i.evento.id = :eventoId")
    Integer countInscripcionesByEventoId(@Param("eventoId") Long eventoId);
}
