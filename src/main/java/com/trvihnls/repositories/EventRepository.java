package com.trvihnls.repositories;

import com.trvihnls.domains.Event;
import com.trvihnls.dtos.event.EventParticipantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    boolean existsByName(String name);

    @Query("SELECT new com.trvihnls.dtos.event.EventParticipantInfo(" +
           "0, u.id, u.fullName, d.name, es.score) " +
           "FROM EventDetail ed " +
           "JOIN ed.user u " +
           "JOIN ed.duty d " +
           "LEFT JOIN EventScore es ON es.eventId = ed.eventId AND es.dutyId = ed.dutyId " +
           "WHERE ed.eventId = :eventId " +
           "ORDER BY u.fullName")
    List<EventParticipantInfo> findEventParticipants(@Param("eventId") Integer eventId);

    @Query("SELECT d.name, COUNT(ed.userId) " +
           "FROM EventDetail ed " +
           "JOIN ed.duty d " +
           "WHERE ed.eventId = :eventId " +
           "GROUP BY d.name")
    List<Object[]> findDutyDistribution(@Param("eventId") Integer eventId);
}
