package com.HimanshuBagga.SpringSecurity.SpringSecurity.filters;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.User;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.service.JwtSecurity;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.service.UserService;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { // customised filter runs one per request
    // once per request means that it will run once for a particular request
    // filters are used to verifies the credentials
    private final JwtSecurity jwtSecurity;
    private final UserService userService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
        final String requestTokenHeader = request.getHeader("Authorization"); // Reads the jwt  , it always starts with (Bearer xxxxx)
        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) { // token does
            filterChain.doFilter(request, response);
            return;
        }
        // Bearer esdxcfesdrgthyxvcderfsd now Bearer index 0 and other 1
        String token = requestTokenHeader.split("Bearer ")[1]; // Extract  jwt token
        Long userId = jwtSecurity.getUserIdFromToken(token); // validate jwt token
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.getUserById(userId);// Load user from DB
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request) // remote Ip address and sessi`on ID`
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);// spring security believes the user
        }
        filterChain.doFilter(request, response);
    }
    catch (Exception ex){
        handlerExceptionResolver.resolveException(request,response,null,ex);
        }
    }
}
// JWT FILTER -> extract the jwt token from the request -> first validate and get the userId from it -> then tells spring security this request came from here