package it.qbteam.stalkerapp.ui.view;

import org.json.JSONException;

import java.io.IOException;

import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;

public interface FragmentListener {
     void addOrganization(Organization organization) throws IOException, JSONException;
}
