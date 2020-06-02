package pl.kesco.myfarmer.setup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.kesco.myfarmer.model.entity.Privilege;
import pl.kesco.myfarmer.model.entity.Role;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.persistence.PrivilegeRepository;
import pl.kesco.myfarmer.persistence.RoleRepository;
import pl.kesco.myfarmer.persistence.UserRepository;

import javax.transaction.Transactional;
import java.util.*;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SetupAdminData implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        Optional<User> adminExists = userRepository.findByEmailIgnoreCase("admin@admin.pl");

        if (adminExists.isPresent()) {
            return;
        } else {
            Privilege readPrivilege
                    = createPrivilegeIfNotFound("READ");
            Privilege writePrivilege
                    = createPrivilegeIfNotFound("WRITE");

            var allPrivileges = new HashSet<Privilege>();
            allPrivileges.add(readPrivilege);
            allPrivileges.add(writePrivilege);

            createRoleIfNotFound("ROLE_ADMIN", allPrivileges);
            createRoleIfNotFound("ROLE_CUSTOMER", allPrivileges);
            createRoleIfNotFound("ROLE_SELLER", allPrivileges);

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");

            userRepository.save(User.builder()
                    .email("admin@admin.pl")
                    .password(passwordEncoder.encode("admin"))
                    .name("admin")
                    .activated(true)
                    .enabled(true)
                    .phoneNumber("123456789")
                    .roles(new HashSet<>(Arrays.asList(adminRole)))
                    .build());

            log.info("Admin user set up! Remember to change password!");
        }
    }

    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilegeRepository.save(Privilege.builder()
                    .name(name)
                    .build());
        }
        return privilege;
    }

    private Role createRoleIfNotFound(
            String name, Set<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            roleRepository.save(Role.builder()
                    .name(name)
                    .privileges(privileges)
                    .build());
        }
        return role;
    }

}
