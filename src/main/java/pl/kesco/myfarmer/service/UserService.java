package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Transactional
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


    public boolean update(Long userId, User user) {

        Optional<User> optionalUser = readById(userId);

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

    public boolean delete(Long userId) {

        Optional<User> optionalUser = readById(userId);

        if(optionalUser.isPresent()){
            Long userToDeleteId = optionalUser.get().getId();
            optionalUser.ifPresent(userRepo::delete);

            log.info("User with id {} was deleted!", userToDeleteId);
            return true;
        }

        return false;
    }

    public Optional<User> readById(Long userId) {

        return userRepo.findById(userId);
    }

}
