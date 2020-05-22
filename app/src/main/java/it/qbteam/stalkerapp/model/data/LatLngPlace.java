package it.qbteam.stalkerapp.model.data;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.backend.dataBackend.Place;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;

public class LatLngPlace {

    private String name;
    private List<LatLng> polygon;
    private Long id;

    public void setPolygon(Place place) throws JSONException {
        String inline=place.getTrackingArea();
        polygon = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(inline);
        JSONArray jsonArray = (JSONArray) jsonObject.get("Organizzazioni");
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            double latitude = jsonObj.getDouble("lat");
            double longitude = jsonObj.getDouble("long");
            polygon.add(new LatLng(latitude, longitude));
        }
        System.out.print("Poligono"+polygon.toArray().toString());
    }


    public static List<LatLngPlace> updatePlace(Long orgID, Storage storage) throws JSONException, IOException, ClassNotFoundException {
        List<LatLngPlace> latLngPlaceList= new ArrayList<>();
        List<Place> list;
        list=storage.deserializePlaceInLocal();
        if(list!=null){
            for(int i=0;i<list.size();i++){
                LatLngPlace aux= new LatLngPlace();
                aux.setPolygon(list.get(i));
                aux.setName(list.get(i));
                aux.setId(list.get(i));
                latLngPlaceList.add(aux);
            }
        }
        return latLngPlaceList;
    }

    public void setName(Place place){
        name = place.getName();
    }

    public void setId(Place place) { id = place.getId();}

    public String getName(){
        return this.name;
    }
    //Returns the List of Places' latitude and longitude.
    public List<LatLng> getLatLng(){
        return this.polygon;
    }

    public Long getId(){
        return this.id;
    }
}
