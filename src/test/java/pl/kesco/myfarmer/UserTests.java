apackage pl.kesco.myfarmer;

import org.junit.Test;

import pl.kesco.myfarmer.persistence.UserRepository;
import pl.kesco.myfarmer.persistence.entity.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


public class UserTests {


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
