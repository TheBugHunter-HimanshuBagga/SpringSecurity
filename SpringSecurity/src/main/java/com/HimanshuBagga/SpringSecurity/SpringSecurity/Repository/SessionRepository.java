package com.HimanshuBagga.SpringSecurity.SpringSecurity.Repository;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.Session;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session , Long> {

    List<Session> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);

}
