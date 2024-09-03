package ru.practicum.EndpointHit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.EndpointHit.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select count(distinct eh.ip) " +
            "from EndpointHit as eh " +
            "where eh.timestamp > :start and eh.timestamp < :end and eh.uri = :uri")
    long findCountHitByStartAndEndAndUriAndUniqueIp(@Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end,
                                                    @Param("uri") String uri);

    @Query("select count(eh.ip) " +
            "from EndpointHit as eh " +
            "where eh.timestamp > :start and eh.timestamp < :end and eh.uri = :uri")
    long findCountHitByStartAndEndAndUri(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end,
                                         @Param("uri") String uri);

    @Query("select distinct eh.uri " +
            "from EndpointHit as eh ")
    List<String> findUniqueUri();
}
