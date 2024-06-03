package org.example.buysell.services;

import lombok.RequiredArgsConstructor;
import org.example.buysell.configuration.CustomUserDetails;
import org.example.buysell.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(
                userRepository.findByEmail(username).orElseThrow(
                        () -> new UsernameNotFoundException("User not found")
                )
        );
    }
}
