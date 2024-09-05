package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final StatClient statClient;
    @Value("${stat-server.url}")
    private String statServerUrl;

    @PostMapping
    void post(HttpServletRequest request) {
        statClient.saveHit(statServerUrl, request);
    }
}
