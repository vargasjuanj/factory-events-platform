package com.factory.events.repository;

import com.factory.events.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByEventId(String eventId);

    List<Event> findAllByOrderByTimestampDesc();
}
