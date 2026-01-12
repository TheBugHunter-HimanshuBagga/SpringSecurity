package com.HimanshuBagga.SpringSecurity.SpringSecurity.service;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.Exception.ResourceNotFoundException;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.Repository.UserRepository;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.SignUpDTO;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.UserDTO;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new BadCredentialsException("User with email : " + username + " not found"));
    }

    public UserDTO signup(SignUpDTO signUpDTO){
        Optional<User> user = userRepository.findByEmail(signUpDTO.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User with email already exists");
        }
        User toBeCreate = modelMapper.map(signUpDTO , User.class);
        toBeCreate.setPassword(passwordEncoder.encode(toBeCreate.getPassword()));// secure the password
        User savedUser = userRepository.save(toBeCreate);
        return modelMapper.map(savedUser , UserDTO.class);
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID : " + userId + " not found"));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public User save(User newUser) {
        return userRepository.save(newUser);
    }
}
