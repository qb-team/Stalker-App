package qbteam.stalkerapp.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.time.OffsetDateTime;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class OrganizationAccessTest {

    private static final Long id = 1L;
    private static final OffsetDateTime entranceTimestamp= OffsetDateTime.now();
    private static final OffsetDateTime exitTimestamp= OffsetDateTime.now();
    private static final String exitToken = "exitToken";
    private static final Long organizationId = 12345678L;
    private static final String orgAuthServerId = "orgAuthServerId";

    private static final OrganizationAccess orgAcc = new OrganizationAccess();
    private OrganizationAccess organizationAccess;

    @Before
    public void setUp(){
        orgAcc.setId(id);
        orgAcc.setEntranceTimestamp(entranceTimestamp);
        orgAcc.setExitTimestamp(exitTimestamp);
        orgAcc.setExitToken(exitToken);
        orgAcc.setOrganizationId(organizationId);
        orgAcc.setOrgAuthServerId(orgAuthServerId);

        organizationAccess = new OrganizationAccess();
        organizationAccess.setId(id);
        organizationAccess.setEntranceTimestamp(entranceTimestamp);
        organizationAccess.setExitTimestamp(exitTimestamp);
        organizationAccess.setExitToken(exitToken);
        organizationAccess.setOrganizationId(organizationId);
        organizationAccess.setOrgAuthServerId(orgAuthServerId);
    }

    @Test
    public void testEqualsHashCode() {
        assertEquals(orgAcc, organizationAccess);
        assertEquals(orgAcc.toString(), orgAcc.toString());
        assertEquals(orgAcc.hashCode(), orgAcc.hashCode());
    }

    @Test
    public void testGetterSetter() {
        assertEquals(orgAcc.getId(), organizationAccess.getId());
        assertEquals(orgAcc.getEntranceTimestamp(), organizationAccess.getEntranceTimestamp());
        assertEquals(orgAcc.getExitTimestamp(), organizationAccess.getExitTimestamp());
        assertEquals(orgAcc.getExitToken(), organizationAccess.getExitToken());
        assertEquals(orgAcc.getOrganizationId(), organizationAccess.getOrganizationId());
        assertEquals(orgAcc.getOrgAuthServerId(), organizationAccess.getOrgAuthServerId());
        assertEquals(orgAcc.hashCode(), orgAcc.hashCode());
        assertEquals(orgAcc, orgAcc);
    }
}