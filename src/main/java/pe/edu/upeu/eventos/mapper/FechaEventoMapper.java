package pe.edu.upeu.eventos.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.eventos.dto.FechaEventoDTO;
import pe.edu.upeu.eventos.entity.FechaEventoEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FechaEventoMapper {

    FechaEventoDTO fechaEventoEntityToFechaEventoDTO(FechaEventoEntity fechaEventoEntity);

    List<FechaEventoDTO> fechaEventoEntitiesToFechaEventoDTOs(List<FechaEventoEntity> fechaEventoEntities);

    FechaEventoEntity fechaEventoDTOToFechaEventoEntity(FechaEventoDTO fechaEventoDTO);
}
