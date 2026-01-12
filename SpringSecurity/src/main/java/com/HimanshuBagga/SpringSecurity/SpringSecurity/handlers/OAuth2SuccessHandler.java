package com.HimanshuBagga.SpringSecurity.SpringSecurity.handlers;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.User;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.service.JwtSecurity;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtSecurity jwtSecurity;
    @Value("${deploy.env}")
    private String deployEnv;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token =
                (OAuth2AuthenticationToken) authentication;

        DefaultOAuth2User oAuth2User =
                (DefaultOAuth2User) token.getPrincipal();


        String email = oAuth2User.getAttribute("email");

        User user = userService.getUserByEmail(email);
        if(user == null){
            User newUser = User.builder()
                    .name(oAuth2User.getAttribute("name"))
                    .email(email)
                    .build();
            userService.save(newUser);
        }

        String accessToken = jwtSecurity.generateAccessToken(user);
        String refreshToken = jwtSecurity.generateRefreshToken(user);

        // sending refresh token
        Cookie cookie = new Cookie("refreshToken" , refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        // sending access token
        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;
        getRedirectStrategy().sendRedirect(request,response,frontEndUrl);
    }
}
