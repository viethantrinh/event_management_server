package com.trvihnls.repositories;

import com.trvihnls.domains.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

    boolean existsByJwtId(String jwtId);
}
