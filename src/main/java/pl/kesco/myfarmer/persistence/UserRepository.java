package pl.kesco.myfarmer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kesco.myfarmer.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}