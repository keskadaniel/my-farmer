package pl.kesco.myfarmer.model.entity;

import lombok.*;
import org.springframework.context.annotation.EnableMBeanExport;
import pl.kesco.myfarmer.model.UserRole;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;

    private boolean activated;
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
