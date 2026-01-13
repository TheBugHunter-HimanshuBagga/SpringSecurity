package com.HimanshuBagga.SpringSecurity.SpringSecurity.service;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.Repository.SessionRepository;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.Session;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 2;
    public void generateNewSession(User user , String refreshToken ){
        List<Session> userSession = sessionRepository.findByUser(user);// give me all the active sessions that belongs to this user
        if(userSession.size() == SESSION_LIMIT){
            userSession.sort(Comparator.comparing(Session::getLastUsedAt)); // oldest first

            Session leastRecentlyUsedSession = userSession.getFirst(); // inactive for the longest time
            sessionRepository.delete(leastRecentlyUsedSession);
        }// if my session limit is not exceeded but less than sessionLimit
        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);
    }
    // is the refresh token still allowed to be used
    public void validateSession(String refreshToken){ // if there is a session according to refresh token in my db
        Session session = sessionRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new SessionAuthenticationException("Session not found for refresh token"));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
