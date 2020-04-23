package pl.kesco.myfarmer.model.entity;

import lombok.*;
import pl.kesco.myfarmer.model.Unit;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    //NEW
    private Double price;
    private boolean deleted;

}
