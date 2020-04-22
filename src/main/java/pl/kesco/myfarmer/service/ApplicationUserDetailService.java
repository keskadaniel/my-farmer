package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.persistence.UserRepository;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var customUser = userRepository.findByEmailIgnoreCase(email);
        return customUser.map(user ->
                new User(user.getEmail(), user.getPassword(), user.isActivated(), true,
                        true, user.isEnabled(),
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
