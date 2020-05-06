package it.qbteam.stalkerapp.model.tracking.trackingArea;

import com.google.android.gms.maps.model.LatLng;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import it.qbteam.stalkerapp.model.backend.modelBackend.Organization;

public class LatLngOrganization {

    private String name;
    private ArrayList<LatLng> polygon;
    private String trackingMode;
    private String orgAuthServerID;
    private OffsetDateTime timeStamp;
    private long organizationID;

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

    public void setName(Organization organization){
        name=organization.getName();
    }

    public void setTrackingMode(Organization organization){
        this.trackingMode=organization.getTrackingMode().toString();
    }

    public void setOrganizationID(Organization organization){
        this.organizationID=organization.getId();
    }

    public ArrayList<LatLng> getLatLng(){
        return this.polygon;
    }

    public void setOrgAuthServerid(Organization organization){
        this.orgAuthServerID=organization.getAuthenticationServerURL();
    }

    public void setTimeStamp(Organization organization){
        this.timeStamp=organization.getCreationDate();
    }

    public OffsetDateTime getTimeStamp(){ return timeStamp; }

    public String getOrgAuthServerID(){return orgAuthServerID;}

    public String  getName(){
        return this.name;
    }

    public long getOrgID(){return this.organizationID;}

    public String getTrackingMode(){
        return this.trackingMode;
    }

}
