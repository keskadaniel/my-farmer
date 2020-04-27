package pl.kesco.myfarmer.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductToBasket {

    private Long orderId;
    private Long quantity;
}
