package pl.kesco.myfarmer.model.dto;

import lombok.*;
import pl.kesco.myfarmer.model.entity.Product;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductToBasketDto {

    private Long quantity;


}
