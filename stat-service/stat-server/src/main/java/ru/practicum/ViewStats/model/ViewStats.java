package ru.practicum.ViewStats.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}