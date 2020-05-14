package qbteam.stalkerapp.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.data.LatLngOrganization;
import static it.qbteam.stalkerapp.model.backend.dataBackend.Organization.TrackingModeEnum.anonymous;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class LatLngOrganizationTest {

    private Organization organization;
    private Double latitude;
    private Double longitude;
    private LatLng latLng;
    private LatLngOrganization latLngOrganization;
    private Organization org = new Organization();
    private List<LatLng> lPolygon = new ArrayList<>();

    @Before
    public void setUp() throws JSONException {
        latLngOrganization = new LatLngOrganization();
        latitude = 5.5;
        longitude = 3.5;
        latLng = new LatLng(latitude,longitude);
        organization=mock(Organization.class);

        org.setName("Imola");
        org.setTrackingMode("anonymous");
        org.setId((long)3);
        org.setAuthenticationServerURL("789");
        org.setCreationDate(OffsetDateTime.now());

        latLngOrganization.setName(org);
        latLngOrganization.setTrackingMode(org);

    }

    @Test
    public void insertLocation() {
        LatLng latitudeLongitude = new LatLng(5.5,3.5);
        assertEquals(latLng,latitudeLongitude);
    }

    @Test
    public void setterGetterOrganization() throws JSONException {
        when(organization.getName()).thenReturn("Imola");
        when(organization.getTrackingMode()).thenReturn(anonymous);
        when(organization.getId()).thenReturn((long)3);
        when(organization.getAuthenticationServerURL()).thenReturn("789");
        when(organization.getCreationDate()).thenReturn(OffsetDateTime.now());
        assertEquals(organization.getName(), org.getName());
        assertEquals(organization.getTrackingMode(), org.getTrackingMode());
        assertEquals(organization.getId(), org.getId());
        assertEquals(organization.getAuthenticationServerURL(), org.getAuthenticationServerURL());
    }
}