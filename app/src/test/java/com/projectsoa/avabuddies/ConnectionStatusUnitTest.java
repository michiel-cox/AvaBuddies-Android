package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.MessageResponse;
import com.projectsoa.avabuddies.data.models.responses.auth.LoginResponse;
import com.projectsoa.avabuddies.data.models.responses.auth.SignupResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.ConnectionsResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendsResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.RequestsResponse;
import com.projectsoa.avabuddies.data.models.responses.user.ProfileResponse;
import com.projectsoa.avabuddies.data.models.responses.user.UserListResponse;
import com.projectsoa.avabuddies.data.models.responses.user.UserResponse;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.AuthService;
import com.projectsoa.avabuddies.data.services.FriendService;
import com.projectsoa.avabuddies.data.services.UserService;
import com.projectsoa.avabuddies.screens.main.search.UsersAdapter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ConnectionStatusUnitTest extends BaseUnitTest {


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
    public void connectionStatus_onSend(){
        when(friendService.fetchConnections()).thenReturn(Single.just(new ConnectionsResponse(){{
            connections = new ArrayList<FriendResponse>(){{
                add(new FriendResponse(){{
                    friend1 = user1.getId();
                    friend2 = user2.getId();
                    confirmed = false;
                }});
            }};
        }}));

        FriendRepository.ConnectionStatus status = friendRepository.getConnectionStatus(user2.getId()).blockingGet();


        assertEquals(FriendRepository.ConnectionStatus.SEND, status);
    }

    @Test
    public void connectionStatus_onAccepted(){
        when(friendService.fetchConnections()).thenReturn(Single.just(new ConnectionsResponse(){{
            connections = new ArrayList<FriendResponse>(){{
                add(new FriendResponse(){{
                    friend1 = user1.getId();
                    friend2 = user2.getId();
                    confirmed = true;
                }});
            }};
        }}));

        FriendRepository.ConnectionStatus status = friendRepository.getConnectionStatus(user2.getId()).blockingGet();


        assertEquals(FriendRepository.ConnectionStatus.ACCEPTED, status);
    }

    @Test
    public void connectionStatus_onReceived(){
        when(friendService.fetchConnections()).thenReturn(Single.just(new ConnectionsResponse(){{
            connections = new ArrayList<FriendResponse>(){{
                add(new FriendResponse(){{
                    friend1 = user2.getId();
                    friend2 = user1.getId();
                    confirmed = false;
                }});
            }};
        }}));

        FriendRepository.ConnectionStatus status = friendRepository.getConnectionStatus(user2.getId()).blockingGet();


        assertEquals(FriendRepository.ConnectionStatus.RECEIVED, status);
    }
    @Test
    public void connectionStatus_onUnknown(){
        when(friendService.fetchConnections()).thenReturn(Single.just(new ConnectionsResponse(){{
            connections = new ArrayList<>();
        }}));

        FriendRepository.ConnectionStatus status = friendRepository.getConnectionStatus(user2.getId()).blockingGet();


        assertEquals(FriendRepository.ConnectionStatus.UNKNOWN, status);
    }

}