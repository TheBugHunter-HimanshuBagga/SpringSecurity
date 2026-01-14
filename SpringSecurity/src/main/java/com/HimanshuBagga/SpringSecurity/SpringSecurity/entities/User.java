package com.HimanshuBagga.SpringSecurity.SpringSecurity.entities;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.enums.Permissions;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.enums.Role;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)// fetch roles as soon as users
    @Enumerated(EnumType.STRING)
    private Set<Role> role; // fetch roles as soon as fetching user

//    @ElementCollection(fetch = FetchType.EAGER)
//    @Enumerated(EnumType.STRING)
//    private Set<Permissions> permissions;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {// authorities is as an umbrella inside which we have role and permission
//        Set<SimpleGrantedAuthority> authorities = role.stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
//                .collect(Collectors.toSet());
//        permissions.forEach(
//                permissions -> authorities.add(new SimpleGrantedAuthority(permissions.name()))
//        );
//        return  authorities;
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        role.forEach(
                role -> {
                    Set<SimpleGrantedAuthority> permissions = PermissionMapping.getAuthoritiesForRole(role);
                    authorities.addAll(permissions);
                    authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
                }
        );
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }


}
