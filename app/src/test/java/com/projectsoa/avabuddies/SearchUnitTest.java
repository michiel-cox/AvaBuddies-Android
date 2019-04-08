package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.LoginResponse;
import com.projectsoa.avabuddies.data.models.responses.ProfileResponse;
import com.projectsoa.avabuddies.data.models.responses.SignupResponse;
import com.projectsoa.avabuddies.data.models.responses.UserResponse;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.AuthService;
import com.projectsoa.avabuddies.data.services.UserService;
import com.projectsoa.avabuddies.screens.main.search.UsersAdapter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;
import io.reactivex.Single;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SearchUnitTest extends BaseUnitTest {

    private UsersAdapter usersAdapter;
    private List<User> users;
    private User user1, user2, user3;

    @Before
    public void setUp(){
        usersAdapter = new UsersAdapter(null, null);
        users = new ArrayList<>();
        user1 = new User("1", "mpjcox@avans.nl", "Michiel Cox");
        user2 = new User("2", "tom.smolenaers@avans.nl", "Tom Smolenaers");
        user3 = new User("2", "peter@bouwers.nl", "Peter Bouwers");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        usersAdapter.setUserList(users);
    }

    @Test
    public void search_onAll(){
        User[] expectedUsers = new User[] { user1, user2, user3 };

        User[] actualUsers = usersAdapter.filter("").toArray(new User[0]);

        assertArrayEquals(actualUsers, expectedUsers);

    }

    @Test
    public void search_onSingleName(){

        User[] expectedUsers = new User[] { user1 };


        User[] actualUsers = usersAdapter.filter("Michiel").toArray(new User[0]);

        assertArrayEquals(actualUsers, expectedUsers);

    }

    @Test
    public void search_onMultipleEmail(){
        User[] expectedUsers = new User[] { user1, user2 };

        User[] actualUsers = usersAdapter.filter("@avans.nl").toArray(new User[0]);


        assertArrayEquals(actualUsers, expectedUsers);
    }


    @Test
    public void search_onNone(){
        User[] expectedUsers = new User[] { };

        User[] actualUsers = usersAdapter.filter("ThisDoesNotExist").toArray(new User[0]);

        assertArrayEquals(actualUsers, expectedUsers);
    }


}