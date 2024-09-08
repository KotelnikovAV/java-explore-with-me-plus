package ru.practicum.requests.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.Request;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestMapper {
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto listReegquestToListParticipationRequestDto(Request request);

    List<ParticipationRequestDto> listRequestToListParticipationRequestDto(List<Request> request);
}
