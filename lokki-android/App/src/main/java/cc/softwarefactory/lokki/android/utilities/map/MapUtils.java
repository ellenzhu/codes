package cc.softwarefactory.lokki.android.utilities.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class MapUtils {
    /**
     * Determines if the location should be used or discarded depending on the current accuracy and
     * the distance to the last location
     * @param newLocation New location to be checked
     * @return true if the new location should be used
     */
    public static boolean useNewLocation(Location newLocation, Location lastLocation, long timeInterval ) {
        return (lastLocation == null || (newLocation.getTime() - lastLocation.getTime() > timeInterval) ||
                lastLocation.distanceTo(newLocation) > 5 || lastLocation.getAccuracy() - newLocation.getAccuracy() > 5);
    }


    public static Location convertToLocation(LatLng latlng, String name) {
        Location location = new Location(name);
        location.setLatitude(latlng.latitude);
        location.setLongitude(latlng.longitude);
        return location;
    }
}