package pe.edu.upeu.eventos.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.eventos.dto.UsuarioDTO;
import pe.edu.upeu.eventos.entity.UsuarioEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO usuarioEntityToUsuarioDTO(UsuarioEntity usuarioEntity);

    List<UsuarioDTO> usuarioEntitiesToUsuarioDTOs(List<UsuarioEntity> usuarioEntities);

    UsuarioEntity usuarioDTOToUsuarioEntity(UsuarioDTO usuarioDTO);
}
