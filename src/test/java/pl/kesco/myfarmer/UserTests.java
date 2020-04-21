package pl.kesco.myfarmer;

import org.hibernate.mapping.Any;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import pl.kesco.myfarmer.persistence.UserRepository;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserTests {

    @Mock
    UserService userService;


    @Test
    public void should_return_user(){
        //given
        var userRepo = mock(UserRepository.class);
        given(userRepo.findAll()).willReturn(prepareUsersList());

        //when
        var users = userRepo.findAll();

        //then
        assertEquals(users.size(), 1);

    }

    @Test
    public void should_add_user(){
        //given
        var user = prepareUsersList().get(0);
        when(userService.create(Mockito.any(User.class))).thenReturn(user);

        //when
        var createduser = userService.create(User.builder().build());

        //then
    }

    private List<User> prepareUsersList(){
        var users = new ArrayList<User>();
        var user = User.builder()
                .email("user@user.pl")
                .name("marek")
                .phoneNumber("32132121")
                .password("pass")
                .build();
        users.add(user);
        return users;
    }

}
