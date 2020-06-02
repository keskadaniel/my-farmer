package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.persistence.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;


    public User create(User user) {
        var createdUser = userRepo.save(
                user
                        .toBuilder()
                        .password(encoder.encode(user.getPassword()))
                        .activated(true)
                        .enabled(true)
                        .build());

        return createdUser;
    }


    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();

        return userRepo.findByEmailIgnoreCase(loggedUsername).get();
    }

    public List<User> readAllUsers(){

        return userRepo.findAll();
    }

}
