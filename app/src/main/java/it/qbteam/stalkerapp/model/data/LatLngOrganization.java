package it.qbteam.stalkerapp.model.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.service.Storage;

public class LatLngOrganization {

    private String name;
    private List<LatLng> polygon;
    private String orgAuthServerID;
    private Long organizationID;


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

    public static List<LatLngOrganization> checkUpdateList(Storage storage, SharedPreferences mpref) throws JSONException {
         List<LatLngOrganization> latLngOrganizationList= new ArrayList<>();
         List<Organization> list;
         list=storage.performCheckFileLocal(HomePageActivity.getPath()+"/Preferiti.txt",mpref);
        if(list!=null) {
            for (int i = 0; i < list.size(); i++) {
                LatLngOrganization latLngOrganization = new LatLngOrganization();
                latLngOrganization.setLatLng(list.get(i));
                latLngOrganization.setName(list.get(i));
                latLngOrganization.setOrganizationID(list.get(i));
                latLngOrganization.setOrgAuthServerId(list.get(i));
                latLngOrganizationList.add(latLngOrganization);
            }

        }
        return latLngOrganizationList;
     }

    //Sets organization's name.
    public void setName(Organization organization){
        name = organization.getName();
    }


    //Sets organization's ID.
    public void setOrganizationID(Organization organization){
        this.organizationID = organization.getId();
    }

    //Returns the List of organizations' latitude and longitude.
    public List<LatLng> getLatLng(){
        return this.polygon;
    }

    //Sets organization's authentication server URL.
    public void setOrgAuthServerId(Organization organization){
        System.out.print("ORGAUTH  "+organizationID);
        this.orgAuthServerID = organization.getOrgAuthServerId();
    }

    //Returns the organization's authentication server URL.
    public String getOrgAuthServerID(){return orgAuthServerID;}

    //Returns the organization's name.
    public String  getName(){
        return this.name;
    }

    //Returns the organization's ID.
    public Long getOrgID(){return this.organizationID;}


}
