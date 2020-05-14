package qbteam.stalkerapp.model;

import org.junit.Before;
import org.junit.Test;

import it.qbteam.stalkerapp.model.data.User;

import static org.junit.Assert.*;

public class UserTest {

    private String token = "1A" ;
    private String uid = "1";
    private User userMock = new User(token,uid);

    User user;

    @Before
    public void setUp() {
        user = new User();
        user.setToken(token);
        user.setUid(uid);
    }

    @Test
    public void GetterSetter() {
        assertEquals(userMock.getToken(),user.getToken());
        assertEquals(userMock.getUid(),user.getUid());
    }
}