package ru.practicum.ViewStats.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ViewStats.model.ViewStats;
import ru.practicum.ViewStatsDto;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {
    ViewStatsDto viewStatsToViewStatsDto (ViewStats viewStats);
}
