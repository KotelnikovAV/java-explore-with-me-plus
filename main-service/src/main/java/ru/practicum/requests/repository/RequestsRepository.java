package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.Status;

import java.util.List;
import java.util.Set;

@Repository
public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findByEventId(long eventId);

    List<Request> findByIdIn(Set<Long> id);

    List<Request> findAllByIdInAndStatus(List<Long> ids, Status status);
}