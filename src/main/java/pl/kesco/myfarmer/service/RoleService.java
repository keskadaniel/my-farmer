package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Role;
import pl.kesco.myfarmer.persistence.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;


    public Role readByName(String roleName){

        return roleRepository.findByName(roleName);
    }



}
