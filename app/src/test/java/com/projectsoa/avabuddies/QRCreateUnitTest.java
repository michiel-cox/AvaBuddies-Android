package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.friend.ConnectionsResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendResponse;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class QRCreateUnitTest extends BaseUnitTest {

    private User user1, user2;

    @Before
    public void setUp(){
        user1 = new User("1", "mpjcox@avans.nl", "Michiel Cox");
        user2 = new User("2", "tom.smolenaers@avans.nl", "Tom Smolenaers");
    }


    @Test
    public void qrCreate_onCorrect2001(){

        Date date = new Date(1000000000000L);
        String dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
        String expectedValue = "{\"dateTime\":\"" + dateString +"\",\"id\":\"" +  user1.getId() + "\"}";

        String actualValue = QRShowFragment.createQRValue(date, user1);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void qrCreate_onCorrectNow(){

        Date date = new Date();
        String dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
        String expectedValue = "{\"dateTime\":\"" + dateString +"\",\"id\":\"" +  user2.getId() + "\"}";

        String actualValue = QRShowFragment.createQRValue(date, user2);

        assertEquals(expectedValue, actualValue);
    }

}
