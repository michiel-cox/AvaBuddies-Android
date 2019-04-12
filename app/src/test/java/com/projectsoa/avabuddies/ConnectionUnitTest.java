package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.MessageResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.ConnectionsResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendResponse;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.FriendService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConnectionUnitTest extends BaseUnitTest {


    @Mock
    FriendService friendService;

    @Mock
    LoginRepository loginRepository;

    @Mock
    UserRepository userRepository;

    FriendRepository friendRepository;

    private User user1, user2, user3;
    private ArrayList<User> users = new ArrayList<>();

    @Before
    public void setUp(){
        user1 = new User("1", "mpjcox@avans.nl", "Michiel Cox");
        user2 = new User("2", "tom.smolenaers@avans.nl", "Tom Smolenaers");
        user3 = new User("2", "peter@bouwers.nl", "Peter Bouwers");

        users.add(user1);
        users.add(user2);
        users.add(user3);

        friendRepository = new FriendRepository(friendService, loginRepository, userRepository);
        when(loginRepository.getLoggedInUser()).thenReturn(new LoggedInUser("<Token>"){{
            setUser(user1);
        }});
        when(loginRepository.isLoggedIn()).thenReturn(true);
        when(userRepository.getList()).thenReturn(Single.just(users));
        for (User user : users) {
            when(userRepository.getUser(user.getId())).thenReturn(Single.just(user));
        }
    }


    @Test
    public void connection_onRequest(){

        when(friendService.doRequest(user2.getId())).thenReturn(Single.just(new MessageResponse(){{
            message = "success";
        }}));

        friendRepository.request(user2.getId()).blockingGet();

        verify(friendService, times(1)).doRequest(user2.getId());
    }

    @Test
    public void connection_onCancelRequest(){

        when(friendService.doCancelRequest(user2.getId())).thenReturn(Single.just(new MessageResponse(){{
            message = "success";
        }}));

        friendRepository.cancelRequest(user2.getId()).blockingGet();

        verify(friendService, times(1)).doCancelRequest(user2.getId());
    }

    @Test
    public void connection_onDenyRequest(){

        when(friendService.doDenyRequest(user2.getId())).thenReturn(Single.just(new MessageResponse(){{
            message = "success";
        }}));

        friendRepository.denyRequest(user2.getId()).blockingGet();

        verify(friendService, times(1)).doDenyRequest(user2.getId());
    }

    @Test
    public void connection_onAcceptRequest(){

        when(friendService.doAcceptRequest(user2.getId())).thenReturn(Single.just(new MessageResponse(){{
            message = "success";
        }}));

        friendRepository.acceptRequest(user2.getId()).blockingGet();

        verify(friendService, times(1)).doAcceptRequest(user2.getId());
    }

}