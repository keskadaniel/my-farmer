package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.persistence.UserRepository;

import java.util.List;
import java.util.Optional;

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

    public List<User> readAllUsers() {

        return userRepo.findAll();
    }

    public Optional<User> readById(Long userId) {

        return userRepo.findById(userId);
    }

    @Transactional
    public boolean update(Long userId, User user) {

        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isEmpty()) {
            return false;
        }

        optionalUser.map(user1 ->
                user1.toBuilder()
                        .roles(user.getRoles())
                        .phoneNumber(user.getPhoneNumber())
                        .enabled(user.isEnabled())
                        .activated(user.isActivated())
                        .name(user.getName())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .build()
        ).ifPresent(userRepo::save);

        return true;
    }

}
