package qbteam.stalkerapp.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.time.OffsetDateTime;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class OrganizationMovementTest {
    private static final String exitToken = "exitToken";
    private static final OffsetDateTime timestamp = OffsetDateTime.now();
    private static final Integer movementType = 1;
    private static final Long organizationId = 12345678L;
    private static final String orgAuthServerId = "orgAuthServerId";

    private static final OrganizationMovement orgMov = new OrganizationMovement();
    private OrganizationMovement organizationMovement;

    @Before
    public void setUp() {
        orgMov.setExitToken(exitToken);
        orgMov.setTimestamp(timestamp);
        orgMov.setMovementType(movementType);
        orgMov.setOrganizationId(organizationId);
        orgMov.setOrgAuthServerId(orgAuthServerId);

        organizationMovement = new OrganizationMovement();
        organizationMovement.setExitToken(exitToken);
        organizationMovement.setTimestamp(timestamp);
        organizationMovement.setMovementType(movementType);
        organizationMovement.setOrganizationId(organizationId);
        organizationMovement.setOrgAuthServerId(orgAuthServerId);
    }

    @Test
    public void testEqualsHashCode() {
        assertEquals(orgMov, organizationMovement);
        assertEquals(orgMov.toString(), orgMov.toString());
        assertEquals(orgMov.hashCode(), orgMov.hashCode());
    }

    @Test
    public void testGetterSetter() {
        assertEquals(orgMov.getExitToken(), organizationMovement.getExitToken());
        assertEquals(orgMov.getTimestamp(), organizationMovement.getTimestamp());
        assertEquals(orgMov.getMovementType(), organizationMovement.getMovementType());
        assertEquals(orgMov.getOrganizationId(), organizationMovement.getOrganizationId());
        assertEquals(orgMov.getOrgAuthServerId(), organizationMovement.getOrgAuthServerId());
        assertEquals(orgMov.hashCode(), orgMov.hashCode());
        assertEquals(orgMov, orgMov);
    }
}