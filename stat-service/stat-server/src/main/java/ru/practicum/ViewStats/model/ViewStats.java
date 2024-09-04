package ru.practicum.ViewStats.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}