package Boundary;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class OverviewButton implements AppStrategy {

    @Override
    public void buttonClick() {
        LatLng centerSG = new LatLng(1.3521,103.8198);
        MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerSG, 11));
    }
}
