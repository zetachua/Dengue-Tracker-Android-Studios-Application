package Control;

import android.location.Location;

import Boundary.MapsActivity;
import Boundary.SearchActivity;

public class DistanceManager {

    public double calcDistance(double x, double y) {
        Location markerLoc = new Location("Marker");
        markerLoc.setLatitude(x);
        markerLoc.setLongitude(y);

        if (SearchActivity.searchInput==true){
            Location currentLoc = new Location("Current");
            currentLoc.setLatitude(SearchActivity.inputLoc.latitude);
            currentLoc.setLongitude(SearchActivity.inputLoc.longitude);
            double distance = currentLoc.distanceTo(markerLoc);
            return distance;
        }
        Location currentLoc = new Location("Current");
        currentLoc.setLatitude(MapsActivity.liveLocation.latitude);
        currentLoc.setLongitude(MapsActivity.liveLocation.longitude);
        double distance = currentLoc.distanceTo(markerLoc);
        return distance;

    }
}
