package com.service.user.config;

import com.service.user.entity.User;
import com.service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email));

        // Convert role and permissions to authorities
        Collection<? extends GrantedAuthority> authorities = getAuthorities(user);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getIsActive(),        // enabled
                true,                      // accountNonExpired
                true,                      // credentialsNonExpired
                !user.getIsActive(),       // accountNonLocked (if inactive = locked)
                authorities
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        if (user.getRole() == null) {
            return Collections.emptyList();
        }

        // Extract permissions from role
        String permissions = user.getRole().getPermissions();
        if (permissions == null || permissions.isEmpty()) {
            return Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
            );
        }

        // Split permissions and create authorities
        // Format: "USER_READ,USER_WRITE,STUDENT_CREATE"
        return permissions.split(",")
                .stream()
                .map(permission -> new SimpleGrantedAuthority("PERMISSION_" + permission))
                .collect(Collectors.toList());
    }
}