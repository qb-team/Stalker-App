package it.qbteam.stalkerapp.model.tracking;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class TrackingDistance {

    public static int checkDistance(Location location){

        int i=0;

        final ArrayList<LatLng> poligono = new ArrayList<>();

        poligono.add(new LatLng(45.4139244815,11.8809040336));
        poligono.add(new LatLng(45.4137732038,11.8812763624));
        poligono.add(new LatLng(45.4134925404,11.8810503718));
        poligono.add(new LatLng(45.4136378199,11.8806753327));

        LatLng test = new LatLng(location.getLatitude(), location.getLongitude());

        LatLng nearestPoint = findNearestPoint(test, poligono);
        double distance = SphericalUtil.computeDistanceBetween(test, nearestPoint);

        Log.e("NEAREST POINT: ", "" + nearestPoint); // lat/lng: (3.0,2.0)
        Log.e("DISTANCE: ", "" + SphericalUtil.computeDistanceBetween(test, nearestPoint)); // 222085.35856591124

        if (distance<=150){
            i = 0;
        }
        else if (distance<=500){
            i = 1;
        }
        else if (distance>500){
            i = 2;
        }

        return i;
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


}
