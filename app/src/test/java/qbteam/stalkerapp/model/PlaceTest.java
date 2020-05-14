package qbteam.stalkerapp.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import it.qbteam.stalkerapp.model.backend.dataBackend.Place;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PlaceTest {

    private static final Long id = 1L;
    private static final String name = "Imola Informatica";
    private static final Long organizationId = 12345678L;
    private static final String trackingArea = "trackingArea";

    private static final Place p = new Place();
    private Place place;

    @Before
    public void setUp() {
        p.setId(id);
        p.setName(name);
        p.setOrganizationId(organizationId);
        p.setTrackingArea(trackingArea);

        place = new Place();
        place.setId(id);
        place.setName(name);
        place.setOrganizationId(organizationId);
        place.setTrackingArea(trackingArea);
    }

    @Test
    public void testEqualsHashCode() {
        assertEquals(p, place);
        assertEquals(p.toString(), p.toString());
        assertEquals(p.hashCode(), p.hashCode());
    }

    @Test
    public void testGetterSetter() {
        assertEquals(p.getId(), place.getId());
        assertEquals(p.getName(), place.getName());
        assertEquals(p.getOrganizationId(), place.getOrganizationId());
        assertEquals(p.getTrackingArea(), place.getTrackingArea());
        assertEquals(p.hashCode(), p.hashCode());
        assertEquals(p, p);
    }
}