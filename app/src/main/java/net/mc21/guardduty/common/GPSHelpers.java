package net.mc21.guardduty.common;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import java.util.List;

public class GPSHelpers {
    public static void enableGPS(Context context) {
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(i);
    }

    public static boolean GPSIsEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static Location getLastKnownLocation(Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;

        for(String provider: providers) {
            // It is okay if the bottom line is underlined... depending on how you use it
            Location l = mLocationManager.getLastKnownLocation(provider);

            if(l == null) {
                continue;
            }

            if(bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }

        return bestLocation;
    }
}
