package com.trvihnls.repositories;

import com.trvihnls.domains.EventDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDetailRepository extends JpaRepository<EventDetail, Integer> {
}
