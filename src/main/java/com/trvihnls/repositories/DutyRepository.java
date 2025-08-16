package com.trvihnls.repositories;

import com.trvihnls.domains.Duty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DutyRepository extends JpaRepository<Duty, Integer> {
    boolean existsByName(String name);
}
