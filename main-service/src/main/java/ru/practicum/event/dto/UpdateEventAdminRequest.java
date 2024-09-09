package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.enums.StateActionAdmin;
import ru.practicum.event.model.Location;
import ru.practicum.utility.Constants;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {
    String annotation;
    Long category;
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
    LocalDateTime eventDate;
    Location location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    String title;
    StateActionAdmin stateAction;
}
