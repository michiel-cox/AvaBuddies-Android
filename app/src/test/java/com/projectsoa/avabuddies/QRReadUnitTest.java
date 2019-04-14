package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.friend.ConnectionsResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.RequestsResponse;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.FriendService;
import com.projectsoa.avabuddies.screens.main.qrshow.QRShowFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class QRReadUnitTest extends BaseUnitTest {

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

        when(friendService.fetchRequests()).thenReturn(Single.just(new RequestsResponse(){{
            ownRequests = new ArrayList<FriendResponse>(){{
                add(new FriendResponse(){{
                    friend1 = user1.getId();
                    friend2 = user2.getId();
                }});
            }};
            requests = new ArrayList<FriendResponse>(){{
                add(new FriendResponse(){{
                    friend1 = user3.getId();
                    friend2 = user1.getId();
                }});
            }};
        }}));

        when(friendService.fetchConnections()).thenReturn(Single.just(new ConnectionsResponse(){{
            connections = new ArrayList<FriendResponse>(){{
                add(new FriendResponse(){{
                    friend1 = user1.getId();
                    friend2 = user2.getId();
                }});
                add(new FriendResponse(){{
                    friend1 = user3.getId();
                    friend2 = user1.getId();
                }});
            }};
        }}));
    }

    @Test
    public void validRequest_onValidOneSecond(){
        Date creationDate = new Date(0);
        Date nowDate = new Date(1000); // one second later

        TestObserver<Void> test = friendRepository.isValidRequest(user2.getId(), creationDate, nowDate).test();

        test.assertNoErrors();
        test.assertComplete();
    }

    @Test
    public void validRequest_onInvalidOneYear(){
        Date creationDate = new Date(0);
        Date nowDate = new Date(31536000000L); // one year later

        TestObserver<Void> test = friendRepository.isValidRequest(user2.getId(), creationDate, nowDate).test();

        test.assertNotComplete();
    }


    @Test
    public void validRequest_onInvalidNotSend(){
        Date creationDate = new Date(0);
        Date nowDate = new Date(31536000000L); // one year later

        TestObserver<Void> test = friendRepository.isValidRequest(user3.getId(), creationDate, nowDate).test();

        test.assertNotComplete();
    }
}
