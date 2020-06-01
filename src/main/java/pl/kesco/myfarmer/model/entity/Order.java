package pl.kesco.myfarmer.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customerId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User sellerId;

    private ZonedDateTime date;

    private boolean ordered;

    private boolean completed;

    private Double total;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "order")
    private Set<BasketPosition> basketPositions = new HashSet<>();


}
