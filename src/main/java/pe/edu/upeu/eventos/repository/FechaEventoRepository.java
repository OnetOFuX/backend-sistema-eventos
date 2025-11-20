package pe.edu.upeu.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.eventos.entity.FechaEventoEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FechaEventoRepository extends JpaRepository<FechaEventoEntity, Long> {

    List<FechaEventoEntity> findByEventoId(Long eventoId);

    List<FechaEventoEntity> findByEventoIdOrderByFechaInicioAsc(Long eventoId);

    List<FechaEventoEntity> findByFechaInicioBetween(LocalDateTime inicio, LocalDateTime fin);

    List<FechaEventoEntity> findByFechaInicioAfter(LocalDateTime fecha);

    List<FechaEventoEntity> findByFechaInicioBefore(LocalDateTime fecha);
}
