package qbteam.stalkerapp.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.time.OffsetDateTime;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceAccess;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PlaceAccessTest {

    private final long id = 1L;
    private static final OffsetDateTime entranceTimestamp = OffsetDateTime.now();
    private static final OffsetDateTime exitTimestamp = OffsetDateTime.now();
    private static final String exitToken = "exitToken";
    private static final Long placeId = 112345678L;
    private static final String orgAuthServerId = "orgAuthServerId";

    private static final PlaceAccess pAcc = new PlaceAccess();
    private PlaceAccess placeAccess;

    @Before
    public void setUp() {
        pAcc.setId(id);
        pAcc.setEntranceTimestamp(entranceTimestamp);
        pAcc.setExitTimestamp(exitTimestamp);
        pAcc.setExitToken(exitToken);
        pAcc.placeId(placeId);
        pAcc.setOrgAuthServerId(orgAuthServerId);

        placeAccess = new PlaceAccess();
        placeAccess.setId(id);
        placeAccess.setEntranceTimestamp(entranceTimestamp);
        placeAccess.setExitTimestamp(exitTimestamp);
        placeAccess.setExitToken(exitToken);
        placeAccess.placeId(placeId);
        placeAccess.setOrgAuthServerId(orgAuthServerId);
    }

    @Test
    public void testEqualsHashCode() {
        assertEquals(pAcc, placeAccess);
        assertEquals(pAcc.toString(), pAcc.toString());
        assertEquals(pAcc.hashCode(), pAcc.hashCode());
    }

    @Test
    public void testGetterSetter() {
        assertEquals(pAcc.getId(), placeAccess.getId());
        assertEquals(pAcc.getEntranceTimestamp(), placeAccess.getEntranceTimestamp());
        assertEquals(pAcc.getExitTimestamp(), placeAccess.getExitTimestamp());
        assertEquals(pAcc.getExitToken(), placeAccess.getExitToken());
        assertEquals(pAcc.getPlaceId(), placeAccess.getPlaceId());
        assertEquals(pAcc.getOrgAuthServerId(), placeAccess.getOrgAuthServerId());
        assertEquals(pAcc.hashCode(), pAcc.hashCode());
        assertEquals(pAcc, pAcc);
    }
}