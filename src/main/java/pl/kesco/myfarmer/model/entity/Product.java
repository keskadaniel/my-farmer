package pl.kesco.myfarmer.model.entity;

import lombok.*;
import pl.kesco.myfarmer.model.Unit;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Podaj nazwę produktu")
    private String name;

    @NotEmpty(message = "Opisz jak będziesz sprzedawać produkt")
    private String description;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @NotNull(message = "Podaj jaką ilość chcesz sprzedać")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Digits(integer = 3, fraction = 2)
    private Double price;
    private boolean deleted;
    @Column(name = "create_date")
    private ZonedDateTime createDate;


}
