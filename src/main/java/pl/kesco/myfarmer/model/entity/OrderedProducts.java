package pl.kesco.myfarmer.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "ordered_products")
public class OrderedProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orderId;

    @OneToMany
    @JoinColumn(name = "id")
    private List<Product> productId = new ArrayList<>();

    private Long quantity;

}
