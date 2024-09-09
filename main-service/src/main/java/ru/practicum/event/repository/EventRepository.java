package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    @Query(value = "select * " +
            "from events as e " +
            "where e.id = :userId " +
            "limit :size " +
            "offset :from", nativeQuery = true)
    List<Event> findAllEventsByUserId(@Param("userId") long userId,
                                      @Param("from") long from,
                                      @Param("size") long size);
}
