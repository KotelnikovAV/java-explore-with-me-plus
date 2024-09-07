package ru.practicum.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toDto(User user);

    User toUser(UserDto userDto);

    User fromUserRequestDto(UserRequestDto userRequestDto);

    List<UserDto> toDtos(List<User> users);

    UserShortDto toUserShortDto(User user);
}
