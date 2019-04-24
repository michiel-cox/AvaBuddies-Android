package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.Tag;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.UserService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateTagsTest extends BaseUnitTest {

    UserRepository userRepository;
    @Mock
    UserService userService;
    List<Tag> tags = new ArrayList<>();;
    List<String> tagsId = new ArrayList<>();;

    @Before
    public void setUp(){
        userRepository = new UserRepository(userService);
        this.tags.add(new Tag(){{
            name = "Wandelen";
            _id = "1";
        }});
        this.tags.add(new Tag(){{
            name = "C#";
            _id = "2";
        }});
        this.tags.add(new Tag(){{
            name = "C++";
            _id = "3";
        }});
        this.tagsId.add("1");
        this.tagsId.add("2");
        this.tagsId.add("3");
    }

    @Test
    public void updateTags_onSucces() {
        User user = new User("1", "tjgsmole@avans.nl", "Tom");
        user.setTags(tags);
        when(userService.updateProfile(user.getAboutme(), user.isSharelocation(), tagsId)).thenReturn(Completable.complete());
        Throwable throwable = userRepository.update(user).blockingGet();
        assertNull(throwable);
        verify(userService, times(1)).updateProfile(user.getAboutme(), user.isSharelocation(), tagsId);
    }

    @Test
    public void updateTags_onFail() {
        User user = new User("1", "tjgsmole@avans.nl", "Tom");
        user.setTags(tags);
        when(userService.updateProfile(user.getAboutme(), user.isSharelocation(), tagsId)).thenReturn(Completable.error(new Exception()));
        Throwable throwable = userRepository.update(user).blockingGet();
        assertNotNull(throwable);
        verify(userService, times(1)).updateProfile(user.getAboutme(), user.isSharelocation(), tagsId);
    }
}
