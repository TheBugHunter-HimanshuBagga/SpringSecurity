    package com.HimanshuBagga.SpringSecurity.SpringSecurity.service;

    import com.HimanshuBagga.SpringSecurity.SpringSecurity.Repository.UserRepository;
    import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.LoginDTO;
    import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.LoginResponseDTO;
    import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.Session;
    import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.User;
    import lombok.RequiredArgsConstructor;
    import org.modelmapper.ModelMapper;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class AuthService {
        private final AuthenticationManager authenticationManager;
        private final JwtSecurity jwtSecurity;
        private final UserRepository userRepository;
        private final SessionService sessionService;
        // string as we will get jwt token in a String form
        public LoginResponseDTO login(LoginDTO loginDTO) {// this just takes email + password from the user verifies them if verifies gives then a JWT
            Authentication authentication  = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail() , loginDTO.getPassword())
            );
            User user = (User) authentication.getPrincipal(); // give me the details of the user
            String accessToken =  jwtSecurity.generateAccessToken(user);
            String RefreshToken = jwtSecurity.generateRefreshToken(user);

            sessionService.generateNewSession(user,RefreshToken); // session service comes in play

            return new LoginResponseDTO(user.getId() , accessToken , RefreshToken);
        }

        public LoginResponseDTO refreshToken(String refreshToken) {
            Long userId = jwtSecurity.getUserIdFromToken(refreshToken); // read userId from refresh token
            sessionService.validateSession(refreshToken); // check if refresh token exists in db
            User user = userRepository.getUserById(userId); // load user from db

            String accessToken = jwtSecurity.generateAccessToken(user); // new access token generated
            return new LoginResponseDTO(user.getId() , accessToken , refreshToken);
        }
    }
