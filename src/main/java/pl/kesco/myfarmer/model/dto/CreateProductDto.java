package pl.kesco.myfarmer.model.dto;

import lombok.*;
import pl.kesco.myfarmer.model.Unit;
import pl.kesco.myfarmer.model.UserRole;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDto {

    private String name;

    private String description;

    private Unit unit;

    private UserRole role;

    private Long quantity;

    private Double price;


}
