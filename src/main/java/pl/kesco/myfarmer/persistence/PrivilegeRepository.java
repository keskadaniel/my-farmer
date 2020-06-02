package pl.kesco.myfarmer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kesco.myfarmer.model.entity.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);
}
