package pl.kesco.myfarmer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.kesco.myfarmer.model.entity.BasketPosition;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.persistence.BasketRepository;
import pl.kesco.myfarmer.service.BasketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BasketTests {


    @InjectMocks
    private BasketService basketService;

    @Mock
    private BasketRepository basketRepository;

    @Test
    public void should_read_all_basket_positions() {

        given(basketRepository.findAll()).willReturn(addBasketPosition());

        //when
        //find out how to mock logged user
        //List<BasketPosition> basketPositionList =basketService.readAllBasketPositions();
        List<BasketPosition> basketPositionList = basketRepository.findAll();

        //then
        Assert.assertEquals(basketPositionList.size(), 1);

    }

    @Test
    public void should_read_by_id() {

        Optional<BasketPosition> givenBasket = Optional.ofNullable(BasketPosition.builder()
                .id(1L)
                .build());

        given(basketRepository.findById(anyLong())).willReturn(givenBasket);

        //when
        Optional<BasketPosition> returned = basketService.readById(1L);

        //then
        Assert.assertTrue(returned.isPresent());
        Assert.assertEquals(givenBasket, returned);
    }


    private List<BasketPosition> addBasketPosition() {

        List<BasketPosition> basketPositions = new ArrayList<>();

        BasketPosition basketPosition = BasketPosition.builder()
                .quantity(2L)
                .order(Order.builder()
                        .customerId(User.builder()
                                .id(22L)
                                .build())
                        .build())
                .product(Product.builder().build())
                .id(2L)
                .build();

        basketPositions.add(basketPosition);

        return basketPositions;

    }


}
