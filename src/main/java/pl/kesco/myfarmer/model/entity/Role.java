package pl.kesco.myfarmer.model.entity;

import lombok.*;
import pl.kesco.myfarmer.model.UserRole;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
//    @ManyToMany
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;




}
