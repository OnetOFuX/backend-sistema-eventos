package pe.edu.upeu.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.eventos.entity.InscripcionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<InscripcionEntity, Long> {

    Optional<InscripcionEntity> findByCodigoQr(String codigoQr);

    List<InscripcionEntity> findByUsuarioId(Long usuarioId);

    List<InscripcionEntity> findByEventoId(Long eventoId);

    List<InscripcionEntity> findByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);

    Boolean existsByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);

    List<InscripcionEntity> findByEstado(InscripcionEntity.EstadoInscripcionEnum estado);

    List<InscripcionEntity> findByEventoIdAndEstado(Long eventoId, InscripcionEntity.EstadoInscripcionEnum estado);

    @Query("SELECT i FROM InscripcionEntity i WHERE i.evento.id = :eventoId AND i.estado = 'CONFIRMADA'")
    List<InscripcionEntity> findInscripcionesConfirmadasByEventoId(@Param("eventoId") Long eventoId);

    @Query("SELECT COUNT(i) FROM InscripcionEntity i WHERE i.evento.id = :eventoId AND i.estado IN ('CONFIRMADA', 'PAGADA')")
    Integer countInscripcionesActivasByEventoId(@Param("eventoId") Long eventoId);
}
