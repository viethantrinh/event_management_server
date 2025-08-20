package com.trvihnls.repositories;

import com.trvihnls.domains.User;
import com.trvihnls.dtos.user.UserEventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
    
    boolean existsByWorkEmail(String workEmail);

    @Query("SELECT new com.trvihnls.dtos.user.UserEventParticipation(" +
           "e.id, e.name, e.description, e.startDate, e.endDate, " +
           "d.name, d.description, es.score) " +
           "FROM EventDetail ed " +
           "JOIN ed.event e " +
           "JOIN ed.duty d " +
           "LEFT JOIN EventScore es ON es.eventId = e.id AND es.dutyId = d.id " +
           "WHERE ed.userId = :userId " +
           "ORDER BY e.startDate DESC")
    List<UserEventParticipation> findUserEventParticipations(@Param("userId") String userId);

    @Query("SELECT u.fullName, u.academicRank, u.academicDegree, " +
           "COUNT(DISTINCT ed.eventId), COALESCE(SUM(es.score), 0.0) " +
           "FROM User u " +
           "LEFT JOIN EventDetail ed ON u.id = ed.userId " +
           "LEFT JOIN EventScore es ON ed.eventId = es.eventId AND ed.dutyId = es.dutyId " +
           "GROUP BY u.id, u.fullName, u.academicRank, u.academicDegree " +
           "ORDER BY COALESCE(SUM(es.score), 0.0) DESC")
    List<Object[]> findOverviewReportData();
}
