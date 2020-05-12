package it.qbteam.stalkerapp.model.data;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;

public class LatLngOrganization {

    private String name;
    private ArrayList<LatLng> polygon;
    private String trackingMode;
    private String orgAuthServerID;
    private OffsetDateTime timeStamp;
    private long organizationID;

    //Sets the latitude and the longitude of the organization's tracking area.
    public void setLatLng(Organization organization) throws JSONException {

        String inline=organization.getTrackingArea();
        polygon = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(inline);
        JSONArray jsonArray = (JSONArray) jsonObject.get("Organizzazioni");
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            double latitude = jsonObj.getDouble("lat");
            double longitude = jsonObj.getDouble("long");
            polygon.add(new LatLng(latitude, longitude));
        }

    }

    //Sets organization's name.
    public void setName(Organization organization){
        name = organization.getName();
    }

    //Sets organization's tracking area.
    public void setTrackingMode(Organization organization){
        this.trackingMode = organization.getTrackingMode().toString();
    }

    //Sets organization's ID.
    public void setOrganizationID(Organization organization){
        this.organizationID = organization.getId();
    }

    //Returns the List of organizations' latitude and longitude.
    public ArrayList<LatLng> getLatLng(){
        return this.polygon;
    }

    //Sets organization's authentication server URL.
    public void setOrgAuthServerid(Organization organization){
        this.orgAuthServerID = organization.getAuthenticationServerURL();
    }

    //Sets organization's timestamp.
    public void setTimeStamp(Organization organization){
        this.timeStamp = organization.getCreationDate();
    }

    //Returns the organization's authentication server URL.
    public String getOrgAuthServerID(){return orgAuthServerID;}

    //Returns the organization's name.
    public String  getName(){
        return this.name;
    }

    //Returns the organization's ID.
    public long getOrgID(){return this.organizationID;}

}
