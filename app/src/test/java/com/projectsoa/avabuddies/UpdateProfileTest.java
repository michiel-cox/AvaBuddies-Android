package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.UserService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Completable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateProfileTest extends BaseUnitTest {

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
        when(userService.updateProfile(user.getAboutme(),user.isSharelocation())).thenReturn(Completable.complete());
        Throwable throwable = userRepository.update(user).blockingGet();
        assertNull(throwable);
        verify(userService, times(1)).updateProfile(user.getAboutme(),user.isSharelocation());
    }
    @Test
    public void delete_onFail(){
        User user = new User("1", "tjgsmole@avans.nl", "Tom");
        when(userService.updateProfile(user.getAboutme(),user.isSharelocation())).thenReturn(Completable.error(new Exception()));
        Throwable throwable = userRepository.update(user).blockingGet();
        assertNotNull(throwable);
        verify(userService, times(1)).updateProfile(user.getAboutme(),user.isSharelocation());
    }

    @Test
    public void updateImage_onSucces() {
        User user = new User("1", "tjgsmole@avans.nl", "Tom");
        user.setImage("example");
        when(userService.updateProfilePicture(user.getImage())).thenReturn(Completable.complete());
        Throwable throwable = userRepository.updateProfilePicture(user).blockingGet();
        assertNull(throwable);
        verify(userService, times(1)).updateProfilePicture(user.getImage());
    }

    @Test
    public void updateImage_onFail() {
        User user = new User("1", "tjgsmole@avans.nl", "Tom");
        user.setImage("example");
        when(userService.updateProfilePicture(user.getImage())).thenReturn(Completable.error(new Exception()));
        Throwable throwable = userRepository.updateProfilePicture(user).blockingGet();
        assertNotNull(throwable);
        verify(userService, times(1)).updateProfilePicture(user.getImage());
    }

}
