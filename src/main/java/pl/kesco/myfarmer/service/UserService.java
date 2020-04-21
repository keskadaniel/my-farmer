package pl.kesco.myfarmer.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.persistence.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepo;


    public User create(User user){
        var createdUser = userRepo.save(user);

        //TODO: Auth, email

        return createdUser;
    }

}
