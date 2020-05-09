package it.qbteam.stalkerapp.model.tracking;

import android.location.Location;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.model.data.LatLngOrganization;

public class TrackingDistance {

    public static int checkDistance(Location location, ArrayList<LatLngOrganization> latLngOrganizations){

        int prioritySet=0;
        LatLng nearestPoint=null;
        double distance = 0;
        LatLng test = new LatLng(location.getLatitude(), location.getLongitude());

        for(int i=0;i<latLngOrganizations.size();i++) {
            final ArrayList<LatLng> poligono = latLngOrganizations.get(i).getLatLng();
            if (distance==0){
                nearestPoint = findNearestPoint(test, poligono);
                distance = SphericalUtil.computeDistanceBetween(test, nearestPoint);
            }
            if (distance>SphericalUtil.computeDistanceBetween(test, nearestPoint)){
                nearestPoint = findNearestPoint(test, poligono);
                distance = SphericalUtil.computeDistanceBetween(test, nearestPoint);
            }
        }

        Log.e("NEAREST POINT: ", "" + nearestPoint); // lat/lng: (3.0,2.0)
        Log.e("DISTANCE: ", "" + SphericalUtil.computeDistanceBetween(test, nearestPoint)); // 222085.35856591124

        if (distance<=150){
            prioritySet = 0;
        }
        else if (distance<=500){
            prioritySet = 1;
        }
        else if (distance>500){
            prioritySet = 2;
        }

        return prioritySet;
    }

    private static LatLng findNearestPoint(LatLng test, List<LatLng> target) {
        double distance = -1;
        LatLng minimumDistancePoint = test;

        if (test == null || target == null) {
            return minimumDistancePoint;
        }

        for (int i = 0; i < target.size(); i++) {
            LatLng point = target.get(i);

            int segmentPoint = i + 1;
            if (segmentPoint >= target.size()) {
                segmentPoint = 0;
            }

            double currentDistance = PolyUtil.distanceToLine(test, point, target.get(segmentPoint));
            if (distance == -1 || currentDistance < distance) {
                distance = currentDistance;
                minimumDistancePoint = findNearestPoint(test, point, target.get(segmentPoint));
            }
        }

        return minimumDistancePoint;
    }

    /**
     * Based on `distanceToLine` method from
     * https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
     */
    private static LatLng findNearestPoint(final LatLng p, final LatLng start, final LatLng end) {
        if (start.equals(end)) {
            return start;
        }

        final double s0lat = Math.toRadians(p.latitude);
        final double s0lng = Math.toRadians(p.longitude);
        final double s1lat = Math.toRadians(start.latitude);
        final double s1lng = Math.toRadians(start.longitude);
        final double s2lat = Math.toRadians(end.latitude);
        final double s2lng = Math.toRadians(end.longitude);

        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
                / (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
        if (u <= 0) {
            return start;
        }
        if (u >= 1) {
            return end;
        }

        return new LatLng(start.latitude + (u * (end.latitude - start.latitude)),
                start.longitude + (u * (end.longitude - start.longitude)));


    }

    ArrayList<LatLng> polygon = new ArrayList<>();
    LatLngBounds.Builder areaBuilder = new LatLngBounds.Builder();

    public LatLngBounds.Builder buildPolygonArea(ArrayList<LatLng> sides){

        for(int i=0; i<sides.size(); i++){
            polygon.add(sides.get(i));
        }

        //costruttore area organizzazione
        for (LatLng point : polygon) {
            areaBuilder.include(point);
        }

        return areaBuilder;
    }


}