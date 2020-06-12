package it.qbteam.stalkerapp.model.tracking;

import android.location.Location;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import java.util.List;

import it.qbteam.stalkerapp.model.data.LatLngOrganization;

public class TrackingDistance {

    public int checkDistance(Location location, List<LatLngOrganization> latLngOrganizations){

        int prioritySet=0;
        LatLng nearestPoint=null;
        String name="";
        double distance = 0;
        LatLng test = new LatLng(location.getLatitude(), location.getLongitude());


        for(int i=0;i<latLngOrganizations.size();i++) {
            final List<LatLng> poligono = latLngOrganizations.get(i).getLatLng();
            nearestPoint = findNearestPoint(test, poligono);
            if (distance==0 || distance>SphericalUtil.computeDistanceBetween(test, nearestPoint)){
                System.out.println("ehyla amico");
                name=latLngOrganizations.get(i).getName();
                distance = SphericalUtil.computeDistanceBetween(test, nearestPoint);
            }
        }

        Log.e("NEAREST POINT: ", "" + nearestPoint +"NOME "+name); // lat/lng: (3.0,2.0)
        Log.e("DISTANCE: ", "" + distance); // 222085.35856591124

        if (distance<=150){
            prioritySet = 1;
        }

        else if (distance<=500){
            prioritySet = 2;
        }

        else if (distance<=1000){
            prioritySet = 3;
        }

        else if(distance<=15000){
            prioritySet = 4;
        }

        else
            prioritySet = 5;

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

        System.out.println("minimumDistancePoint" + minimumDistancePoint);

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



}
