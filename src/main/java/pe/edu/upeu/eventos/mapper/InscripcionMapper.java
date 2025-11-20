package pe.edu.upeu.eventos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.eventos.dto.InscripcionDTO;
import pe.edu.upeu.eventos.entity.InscripcionEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InscripcionMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.nombre", target = "usuarioNombre")
    @Mapping(source = "usuario.apellido", target = "usuarioApellido")
    @Mapping(source = "usuario.codigo", target = "usuarioCodigo")
    @Mapping(source = "evento.id", target = "eventoId")
    @Mapping(source = "evento.titulo", target = "eventoTitulo")
    InscripcionDTO inscripcionEntityToInscripcionDTO(InscripcionEntity inscripcionEntity);

    List<InscripcionDTO> inscripcionEntitiesToInscripcionDTOs(List<InscripcionEntity> inscripcionEntities);

    InscripcionEntity inscripcionDTOToInscripcionEntity(InscripcionDTO inscripcionDTO);
}
