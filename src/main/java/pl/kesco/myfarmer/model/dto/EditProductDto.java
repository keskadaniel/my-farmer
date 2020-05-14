package pl.kesco.myfarmer.model.dto;

import lombok.*;
import pl.kesco.myfarmer.model.Unit;
import pl.kesco.myfarmer.model.UserRole;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditProductDto {

    private String name;

    private String description;

    private Unit unit;

    private Long quantity;

    private Double price;


}
