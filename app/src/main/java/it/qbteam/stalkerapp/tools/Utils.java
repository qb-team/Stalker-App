package it.qbteam.stalkerapp.tools;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.preference.PreferenceManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.PolyUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.maps.android.SphericalUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import it.qbteam.stalkerapp.R;

public class Utils {

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Stampa a schermo la latitudine e la longitudine
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

    public static String isInside(Location location) {
        //Polygon Torre; --> come si usa Polygon?
        final ArrayList<LatLng> polygon = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        polygon.add(new LatLng(45.4139244815,11.8809040336));
        polygon.add(new LatLng(45.4137732038,11.8812763624));
        polygon.add(new LatLng(45.4134925404,11.8810503718));
        polygon.add(new LatLng(45.4136378199,11.8806753327));

        for (LatLng point : polygon) {
            builder.include(point);
        }
        System.out.println("creo builder:  " + builder);

        LatLng test = new LatLng(location.getLatitude(), location.getLongitude());
        String isInsideString;

        boolean isInsideBoundary = builder.build().contains(test); // true se il test point è all'interno del confine
        boolean isInsideBoolean = PolyUtil.containsLocation(test, polygon, true); // false se il punto è all'esterno del polygon

        if (isInsideBoundary && isInsideBoolean == true )
        {
            isInsideString = "sei dentro";

        }
        else {
            isInsideString = "sei fuori";
        }

        return isInsideString;
    }



}