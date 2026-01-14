package com.HimanshuBagga.SpringSecurity.SpringSecurity.utils;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.enums.Permissions;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.enums.Permissions.*;
import static com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.enums.Role.*;
// hardcoded user and permission connection using roles
public class PermissionMapping {
    private static final Map<Role, Set<Permissions>> map = Map.of(
            USER, Set.of(USER_VIEW , POST_VIEW),
            CREATOR , Set.of(POST_VIEW , USER_UPDATE , POST_UPDATE),
            ADMIN , Set.of(POST_VIEW , USER_UPDATE , POST_UPDATE , USER_DELETE , POST_DELETE , USER_CREATE)
    );
    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
        return map.get(role)
                .stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.name()))
                .collect(Collectors.toSet());
    }
}
