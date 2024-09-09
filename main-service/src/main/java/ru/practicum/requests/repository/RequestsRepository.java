package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.RequestView;
import ru.practicum.requests.model.Status;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface RequestsRepository extends JpaRepository<Request, Long> {
    @Query("select count(r.id) " +
            "from Request as r " +
            "join r.event as e " +
            "where e = :event and r.status = :status")
    long countByEvent(@Param("event") Event event, @Param("status") Status status);

    @Query("select new ru.practicum.requests.model.RequestView(e.id, count(r.id)) " +
            "from Request as r " +
            "join r.event as e " +
            "where e in (:events) ")
    Stream<RequestView> countByEventIn(@Param("events") List<Event> events);

    List<Request> findByEventId(long eventId);

    List<Request> findByIdIn(List<Long> id);
    List<Request> findAllByIdInAndStatus(List<Long> ids, Status status);
}
