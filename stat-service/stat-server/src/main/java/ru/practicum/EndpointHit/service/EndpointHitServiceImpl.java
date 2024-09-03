package ru.practicum.EndpointHit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHit.model.EndpointHit;
import ru.practicum.EndpointHit.repository.EndpointHitRepository;
import ru.practicum.ViewStats.model.ViewStats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Transactional
    @Override
    public void save(EndpointHit endpointHit) {
        log.info("The beginning of the process of creating a statistics record");
        endpointHitRepository.save(endpointHit);
        log.info("The statistics record has been created");
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> findByParams(String start, String end, List<String> uris, boolean unique) {
        log.info("The beginning of the process of obtaining statistics of views");
        List<ViewStats> listViewStats;

        if (unique) {
            listViewStats = findByUnique(start, end, uris);
        } else {
            listViewStats = findByNotUnique(start, end, uris);
        }

        log.info("Getting the statistics of the views is completed");
        return listViewStats;
    }

    private List<ViewStats> findByUnique(String start, String end, List<String> uris) {
        List<ViewStats> listViewStats = new ArrayList<>();

        if (uris == null || uris.isEmpty()) {
            uris = endpointHitRepository.findUniqueUri();
        }

        for (String uri : uris) {
            long hit = endpointHitRepository.findCountHitByStartAndEndAndUriAndUniqueIp(decodeTime(start),
                    decodeTime(end),
                    uri);
            ViewStats viewStats = ViewStats.builder()
                    .app("ewm-main-service")
                    .uri(uri)
                    .hits(hit)
                    .build();
            listViewStats.add(viewStats);
        }

        return listViewStats.stream()
                .sorted(Comparator.comparingLong(value -> value.getHits() * -1))
                .toList();
    }

    private List<ViewStats> findByNotUnique(String start, String end, List<String> uris) {
        List<ViewStats> listViewStats = new ArrayList<>();

        if (uris == null || uris.isEmpty()) {
            uris = endpointHitRepository.findUniqueUri();
        }

        for (String uri : uris) {
            long hit = endpointHitRepository.findCountHitByStartAndEndAndUri(decodeTime(start), decodeTime(end), uri);
            ViewStats viewStats = ViewStats.builder()
                    .app("ewm-main-service")
                    .uri(uri)
                    .hits(hit)
                    .build();
            listViewStats.add(viewStats);
        }

        return listViewStats.stream()
                .sorted(Comparator.comparingLong(value -> value.getHits() * -1))
                .toList();
    }

    private LocalDateTime decodeTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String decodeTime = URLDecoder.decode(time, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodeTime, formatter);
    }
}
