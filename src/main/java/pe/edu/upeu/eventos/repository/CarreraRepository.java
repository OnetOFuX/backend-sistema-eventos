package pe.edu.upeu.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.eventos.entity.CarreraEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<CarreraEntity, Long> {

    Optional<CarreraEntity> findByNombre(String nombre);

    List<CarreraEntity> findByActivoTrue();

    List<CarreraEntity> findByFacultadIdAndActivoTrue(Long facultadId);

    boolean existsByNombre(String nombre);
}

