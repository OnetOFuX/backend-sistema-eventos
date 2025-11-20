package pe.edu.upeu.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.eventos.entity.UsuarioEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByCodigo(String codigo);

    Optional<UsuarioEntity> findByEmailOrCodigo(String email, String codigo);

    Boolean existsByEmail(String email);

    Boolean existsByCodigo(String codigo);

    List<UsuarioEntity> findByRol(UsuarioEntity.RolEnum rol);

    List<UsuarioEntity> findByActivoTrue();

    List<UsuarioEntity> findByRolAndActivoTrue(UsuarioEntity.RolEnum rol);
}
