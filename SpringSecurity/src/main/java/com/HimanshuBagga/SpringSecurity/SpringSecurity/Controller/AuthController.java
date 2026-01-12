package com.HimanshuBagga.SpringSecurity.SpringSecurity.Controller;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.LoginDTO;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.LoginResponseDTO;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.SignUpDTO;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.UserDTO;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.service.AuthService;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    @Value("${deploy.env}")
    private String deployEnv;
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignUpDTO signUpDTO){
        UserDTO userDTO = userService.signup(signUpDTO);
        return ResponseEntity.ok(userDTO);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO , HttpServletRequest request ,  HttpServletResponse response){
        LoginResponseDTO loginResponseDTO = authService.login(loginDTO);

        Cookie cookie = new Cookie("refreshToken" , loginResponseDTO.getRefreshToken()); // storing refresh jwt inside a coookie
        cookie.setHttpOnly(true); // no one can access this cookie
        cookie.setSecure("production".equals(deployEnv));// if production set it secure
        response.addCookie(cookie); // add cookie inside the http response

        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/refresh") // when the access token expires the client calls /request api , from cookies it reads the request token and generate a new access token
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies()). // take all the cookies from the request go theough them one by one
                filter(cookie -> "refreshToken".equals(cookie.getName())) // keeps cookie with the name as refreshCookie
                .findFirst()// give me first cookie that matches
                .map(cookie -> cookie.getValue()) // cookie has 2 fiels name and value from which i need its value
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the cookies")); // No refresh token found throw error
        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
