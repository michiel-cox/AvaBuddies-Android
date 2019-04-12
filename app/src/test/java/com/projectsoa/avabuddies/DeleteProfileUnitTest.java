package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.UserService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Completable;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DeleteProfileUnitTest extends BaseUnitTest {

    UserRepository userRepository;
    @Mock
    UserService userService;

    @Before
    public void setUp(){
        userRepository = new UserRepository(userService);
    }

    @Test
    public void delete_onSucces(){
        User user = new User("1", "tjgsmole@avans.nl", "Tom");
        when(userService.deleteUser(user.getId())).thenReturn(Completable.complete());
        Throwable throwable = userRepository.delete(user).blockingGet();
        assertNull(throwable);
        verify(userService, times(1)).deleteUser(user.getId());
    }
    @Test
    public void delete_onFail(){
        User user = new User("1", "tjgsmole@avans.nl", "Tom");
        when(userService.deleteUser(user.getId())).thenReturn(Completable.error(new Exception()));
        Throwable throwable = userRepository.delete(user).blockingGet();
        assertNotNull(throwable);
        verify(userService, times(1)).deleteUser(user.getId());
    }

}
