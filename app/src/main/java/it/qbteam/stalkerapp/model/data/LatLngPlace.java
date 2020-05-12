package it.qbteam.stalkerapp.model.data;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import it.qbteam.stalkerapp.model.backend.dataBackend.Place;

public class LatLngPlace {

    private String name;
    private ArrayList<LatLng> polygon;
    private long id;

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
    }
    public void setName(Place place){
        name = place.getName();
    }

    public void setId(Place place) { id = place.getId();}
}
