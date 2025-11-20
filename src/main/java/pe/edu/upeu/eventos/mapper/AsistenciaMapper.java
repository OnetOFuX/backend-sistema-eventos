package pe.edu.upeu.eventos.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.eventos.dto.AsistenciaDTO;
import pe.edu.upeu.eventos.entity.AsistenciaEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AsistenciaMapper {

    AsistenciaDTO asistenciaEntityToAsistenciaDTO(AsistenciaEntity asistenciaEntity);

    List<AsistenciaDTO> asistenciaEntitiesToAsistenciaDTOs(List<AsistenciaEntity> asistenciaEntities);

    AsistenciaEntity asistenciaDTOToAsistenciaEntity(AsistenciaDTO asistenciaDTO);
}
