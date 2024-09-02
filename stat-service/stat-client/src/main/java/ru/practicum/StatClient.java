package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class StatClient {

    private final RestClient restClient;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatClient(String serverUrl) {
        this.restClient = RestClient.create(serverUrl);
    }

    public void saveHit(String app, HttpServletRequest request) {
        log.info("Saving hit for {}", app);
        EndpointHitDto endpointHitDto = toDto(app, request);
        ResponseEntity<Void> response = restClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(endpointHitDto)
                .retrieve()
                .toBodilessEntity();
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Posted hit with code {}", response.getStatusCode());
        } else {
            log.error("Posted hit with error code {}", response.getStatusCode());
        }
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(LocalDateTime start, LocalDateTime end,
                                                       List<String> uris, boolean unique) {
        log.info("Getting stats for {}", uris);
        return restClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("start", start.format(formatter))
                                .queryParam("end", end.format(formatter))
                                .queryParam("uris", uris)
                                .queryParam("unique", unique)
                                .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) ->
                                log.error("Getting stats for {} with error code {}", uris, response.getStatusCode()))
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private EndpointHitDto toDto(String app, HttpServletRequest request) {
        return EndpointHitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
