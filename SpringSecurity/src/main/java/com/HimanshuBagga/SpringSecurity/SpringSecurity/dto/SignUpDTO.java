package com.HimanshuBagga.SpringSecurity.SpringSecurity.dto;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDTO {
    private String email;
    private String password;
    private String name;
    private Set<Role> role;
}
