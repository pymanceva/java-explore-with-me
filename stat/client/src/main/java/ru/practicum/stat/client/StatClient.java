package ru.practicum.stat.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stat.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatClient extends BaseClient {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final String HIT_API_PREFIX = "/hit";
    public static final String STATS_API_PREFIX = "/stats";

    @Autowired
    public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .build()
        );
    }

    public ResponseEntity<Object> saveHit(String app, String uri, String ip, LocalDateTime timestamp) {
        log.info("Client sent POST request to save hit");

        EndpointHitDto endpointHitDto = new EndpointHitDto(null, app, uri, ip, timestamp);

        return post(HIT_API_PREFIX, endpointHitDto);
    }

    public ResponseEntity<Object> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Client sent GET request for ViewStats");

        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Wrong time parameter.");
        }

        StringBuilder uriBuilder = new StringBuilder(STATS_API_PREFIX + "?start={start}&end={end}");
        Map<String, Object> parameters = Map.of(
                "start", start.format(DATE_FORMATTER),
                "end", end.format(DATE_FORMATTER)
        );

        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                uriBuilder.append("&uris=").append(uri);
            }
        }
        if (unique != null) {
            uriBuilder.append("&unique=").append(unique);
        }

        return get(uriBuilder.toString(), parameters);
    }

    public ResponseEntity<Object> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        return getViewStats(start, end, uris, null);
    }

    public ResponseEntity<Object> getViewStats(LocalDateTime start, LocalDateTime end) {
        return getViewStats(start, end, null, null);
    }

    public ResponseEntity<Object> getViewStats(LocalDateTime start, LocalDateTime end, Boolean unique) {
        return getViewStats(start, end, null, unique);
    }
}
