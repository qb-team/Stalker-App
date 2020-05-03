package it.qbteam.stalkerapp.model.tracking.trackingArea;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.qbteam.stalkerapp.model.backend.model.Organization;

public class LatLngOrganization {

    private String name;
    private ArrayList<LatLng> polygon;
    private String trackingMode;
    private long organizationID;

    public void setLatLng(Organization organization) throws JSONException {

        String inline=organization.getTrackingArea();
        System.out.println("INLINE  "+inline);
        polygon = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(inline);
        JSONArray jsonArray = (JSONArray) jsonObject.get("Organizzazioni");
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            double latitude = jsonObj.getDouble("lat");
            double longitude = jsonObj.getDouble("long");
            polygon.add(new LatLng(latitude, longitude));
        }


      System.out.println("ARRAY LATLNG"+ polygon);
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



    public String  getName(){
        return this.name;
    }

    public String getTrackingMode(){
        return this.trackingMode;
    }

}
