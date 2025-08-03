package com.trvihnls.repositories;

import com.trvihnls.domains.EventScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventScoreRepository extends JpaRepository<EventScore, EventScore.EventScoreId> {
}
