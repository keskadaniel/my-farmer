package pl.kesco.myfarmer.model.dto;

import lombok.*;
import pl.kesco.myfarmer.model.Unit;
import pl.kesco.myfarmer.model.UserRole;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDto {

    @NotBlank(message = "Podaj nazwę produktu")
    private String name;

    @NotEmpty(message = "Opisz jak będziesz sprzedawać produkt")
    private String description;

    private Unit unit;

    private UserRole role;

    @NotNull(message = "Podaj jaką ilość chcesz sprzedać")
    private Long quantity;

    @Digits(integer = 4, fraction = 2, message = "Zły format ceny. Uzyj kropki dla dziesiętnych.")
    @NotNull
    private Double price;


}
