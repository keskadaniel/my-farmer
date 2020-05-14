package pl.kesco.myfarmer.model.entity;

import lombok.*;
import org.aspectj.bridge.IMessage;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.lang.NonNull;
import pl.kesco.myfarmer.model.UserRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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

    @NotEmpty
    private String name;

    @Email(message = "Podaj prawidłowy adres email")
    private String email;

    @NotEmpty(message = "Hasło jest wymagane")
    @Size(min = 4, message = "Hasło musi mieć minimum 4 znaki")
    private String password;
    @Column(name = "phone_number")
    @Size(min = 9, max = 14, message = "Podaj prawidłowy numer telefonu")
    private String phoneNumber;

    private boolean activated;
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
