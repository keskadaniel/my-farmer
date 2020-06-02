package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.persistence.UserRepository;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductService productService;


    public boolean validateIsEmailUnique(String email) {

        return userRepository.findByEmailIgnoreCase(email)
                .isEmpty();
    }

    public boolean validateIsProductNotOwnedByUser(Long productId) {

        final var loggedUser = userService.getLoggedUser();

        return productService.readById(productId)
                .filter(product -> product.getUserId().getId() == loggedUser.getId())
                .isEmpty();
    }


}
