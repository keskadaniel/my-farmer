package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.persistence.UserRepository;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final UserRepository userRepository;


    public boolean validateIsEmailUnique(String email) {

        return userRepository.findByEmailIgnoreCase(email)
                .isEmpty();
    }
}
