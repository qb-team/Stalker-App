package it.qbteam.stalkerapp.tools;

import org.json.JSONException;
import java.io.IOException;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;

public interface FragmentListenerFeatures {
    void disableScroll(boolean enable);
    void addOrganization(Organization organization) throws IOException, JSONException;
    boolean deleteAuthButton(String orgName);
}
