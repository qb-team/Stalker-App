package it.qbteam.stalkerapp.tools;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.PolyUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        final ArrayList<LatLng> poligono = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        poligono.add(new LatLng(45.4139244815,11.8809040336));
        poligono.add(new LatLng(45.4137732038,11.8812763624));
        poligono.add(new LatLng(45.4134925404,11.8810503718));
        poligono.add(new LatLng(45.4136378199,11.8806753327));

        for (LatLng point : poligono) {
            builder.include(point);
        }
        System.out.println("creo builder:  " + builder);

        LatLng test = new LatLng(location.getLatitude(), location.getLongitude());
        String isInsideString;

        boolean isInsideBoundary = builder.build().contains(test); // true se il test point è all'interno del confine
        boolean isInsideBoolean = PolyUtil.containsLocation(test, poligono, true); // false se il punto è all'esterno del poligono

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