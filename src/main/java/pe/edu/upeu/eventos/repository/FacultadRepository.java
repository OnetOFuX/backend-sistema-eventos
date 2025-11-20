package pe.edu.upeu.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.eventos.entity.FacultadEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultadRepository extends JpaRepository<FacultadEntity, Long> {

    Optional<FacultadEntity> findByNombre(String nombre);

    List<FacultadEntity> findByActivoTrue();

    boolean existsByNombre(String nombre);
}

