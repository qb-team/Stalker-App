package qbteam.stalkerapp.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.time.OffsetDateTime;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceMovement;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PlaceMovementTest {
    private static final String exitToken = "exitToken";
    private static final OffsetDateTime timestamp = OffsetDateTime.now();
    private static final Integer movementType = 1;
    private static final Long placeId = 12345678L;
    private static final String orgAuthServerId = "orgAuthServerId";

    private static final PlaceMovement pMov = new PlaceMovement();
    private PlaceMovement placeMovement;

    @Before
    public void setUp(){
        pMov.setExitToken(exitToken);
        pMov.setTimestamp(timestamp);
        pMov.setMovementType(movementType);
        pMov.setPlaceId(placeId);
        pMov.setOrgAuthServerId(orgAuthServerId);

        placeMovement = new PlaceMovement();
        placeMovement.setExitToken(exitToken);
        placeMovement.setTimestamp(timestamp);
        placeMovement.setMovementType(movementType);
        placeMovement.setPlaceId(placeId);
        placeMovement.setOrgAuthServerId(orgAuthServerId);
    }

    @Test
    public void testEqualsHashCode() {
        assertEquals(pMov, placeMovement);
        assertEquals(pMov.toString(), pMov.toString());
        assertEquals(pMov.hashCode(), pMov.hashCode());
    }

    @Test
    public void testGetterSetter() {
        assertEquals(pMov.getExitToken(), placeMovement.getExitToken());
        assertEquals(pMov.getTimestamp(), placeMovement.getTimestamp());
        assertEquals(pMov.getMovementType(), placeMovement.getMovementType());
        assertEquals(pMov.getPlaceId(), placeMovement.getPlaceId());
        assertEquals(pMov.getOrgAuthServerId(), placeMovement.getOrgAuthServerId());
        assertEquals(pMov.hashCode(), pMov.hashCode());
        assertEquals(pMov, pMov);
    }
}