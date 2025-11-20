package pe.edu.upeu.eventos.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.eventos.dto.EventoDTO;
import pe.edu.upeu.eventos.entity.EventoEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FechaEventoMapper.class})
public interface EventoMapper {

    EventoDTO eventoEntityToEventoDTO(EventoEntity eventoEntity);

    List<EventoDTO> eventoEntitiesToEventoDTOs(List<EventoEntity> eventoEntities);

    EventoEntity eventoDTOToEventoEntity(EventoDTO eventoDTO);
}
