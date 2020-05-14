package qbteam.stalkerapp.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.time.OffsetDateTime;
import it.qbteam.stalkerapp.model.backend.dataBackend.Favorite;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class FavoriteTest {
    private static final String userId = "userId";
    private static final Long organizationId = 12345678L;
    private static final String orgAuthServerId = "orgAuthServerId";
    private static final OffsetDateTime creationDate = OffsetDateTime.now();

    private static final Favorite fav = new Favorite();
    private Favorite favorite;

    @Before
    public void setUp() {
        fav.setUserId(userId);
        fav.setOrganizationId(organizationId);
        fav.setOrgAuthServerId(orgAuthServerId);
        fav.setCreationDate(creationDate);

        favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setOrganizationId(organizationId);
        favorite.setOrgAuthServerId(orgAuthServerId);
        favorite.setCreationDate(creationDate);
    }

    @Test
    public void testEqualsHashCode() {
        assertEquals(fav, favorite);
        assertEquals(fav.toString(), fav.toString());
        assertEquals(fav.hashCode(), fav.hashCode());
    }

    @Test
    public void testGetterSetter() {
        assertEquals(fav.getUserId(), favorite.getUserId());
        assertEquals(fav.getOrganizationId(), favorite.getOrganizationId());
        assertEquals(fav.getOrgAuthServerId(), favorite.getOrgAuthServerId());
        assertEquals(fav.getCreationDate(), favorite.getCreationDate());
        assertEquals(fav.hashCode(), fav.hashCode());
        assertEquals(fav, fav);
    }
}