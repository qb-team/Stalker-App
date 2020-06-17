package qbteam.stalkerapp.model;

import android.provider.ContactsContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.OffsetDateTime;

import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class OrganizationTest {
    private static final Long id = 12345678L;
    private static final String name = "Imola Informatica";
    private static final String description = "Azienda di imola informatica";
    private static final String image ="image.png" ;
    private static final String street = "via ugo foscolo";
    private static final String number = "14/a";
    private static final Integer postCode = 35030;
    private static final String city = "Imola";
    private static final String country = "Italy";
    private static final OffsetDateTime creationDate= OffsetDateTime.now() ;
    private static final OffsetDateTime lastChangeDate = OffsetDateTime.now() ;
    private static final String trackingArea = "trackingArea";
    private static final String trackingMode = "authenticated";
    private static final String authenticationServerURL="www.LDAP.com";
    private static final Organization org = new Organization();
    private  Organization organization;

    @Before
    public void setUp(){
        org.setId(id);
        org.setName(name);
        org.setDescription(description);
        org.setImage(image);
        org.setStreet(street);
        org.setNumber(number);
        org.setPostCode(postCode);
        org.setCity(city);
        org.setCountry(country);
        org.setCreationDate(creationDate);
        org.setLastChangeDate(lastChangeDate);
        org.setTrackingArea(trackingArea);
        org.setTrackingMode(trackingMode);
        org.setAuthenticationServerURL(authenticationServerURL);

        organization= new Organization();
        organization.setId(id);
        organization.setName(name);
        organization.setDescription(description);
        organization.setImage(image);
        organization.setStreet(street);
        organization.setNumber(number);
        organization.setPostCode(postCode);
        organization.setCity(city);
        organization.setCountry(country);
        organization.setCreationDate(creationDate);
        organization.setLastChangeDate(lastChangeDate);
        organization.setTrackingArea(trackingArea);
        organization.setTrackingMode(trackingMode);
        organization.setAuthenticationServerURL(authenticationServerURL);
    }

    @Test
    public void testEqualsHashCode() {
        assertEquals(org, organization);
        assertEquals(org.toString(), org.toString());
        assertEquals(org.hashCode(), org.hashCode());

    }
    @Test
    public void testGetterSetter() {

        assertEquals(org.getId(), organization.getId());
        assertEquals(org.getName(), organization.getName());
        assertEquals(org.getDescription(), organization.getDescription());
        assertEquals(org.getImage(), organization.getImage());
        assertEquals(org.getName(), organization.getName());
        assertEquals(org.getStreet(), organization.getStreet());
        assertEquals(org.getNumber(), organization.getNumber());
        assertEquals(org.getPostCode(), organization.getPostCode());
        assertEquals(org.getCity(), organization.getCity());
        assertEquals(org.getCountry(), organization.getCountry());
        assertEquals(org.getCreationDate(), organization.getCreationDate());
        assertEquals(org.getLastChangeDate(), organization.getLastChangeDate());
        assertEquals(org.getTrackingArea(), organization.getTrackingArea());
        assertEquals(org.getTrackingMode(), organization.getTrackingMode());
        assertEquals(org.getAuthenticationServerURL(), organization.getAuthenticationServerURL());
        assertEquals(org.hashCode(), org.hashCode());
        assertEquals(org, org);

    }
}
