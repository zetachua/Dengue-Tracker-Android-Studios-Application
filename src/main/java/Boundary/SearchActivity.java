package Boundary;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

import Control.ClusterManager;

public class SearchActivity {
    SearchView searhView;
    public static Boolean searchInput = false;
    public static LatLng inputLoc;
    private Context searchContext;
    public static Marker inputMarker;


    public SearchActivity(Context context) {
        this.searchContext = context;
    }

    ClusterManager clusterManager = new ClusterManager(searchContext);

    public SearchView.OnQueryTextListener searchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            String oldLocation = searhView.getQuery().toString();
            String location = oldLocation + "Singapore";
            List<Address> addressList = null;
            searchInput=true;

            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(searchContext);

                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Address address = addressList.get(0);
                    inputLoc = new LatLng(address.getLatitude(), address.getLongitude());
                    MapsActivity.mMap.clear();
                    clusterManager.getClusterShape(searchContext);
                    MarkerOptions searchLocOptions = new MarkerOptions()
                            .position(inputLoc)
                            .title("Current Input Location")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));


                    inputMarker = MapsActivity.mMap.addMarker(searchLocOptions);
                    MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inputLoc, MapsActivity.DEFAULT_ZOOM));
                } catch (Exception e) {
                    System.out.println("Failed");
                    Toast.makeText(searchContext, "Location not found, please enter another nearby location", Toast.LENGTH_SHORT).show();
                    //default location if crash
                    MapsActivity.mMap.clear();
                    inputLoc=MapsActivity.liveLocation;

                    MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inputLoc, MapsActivity.DEFAULT_ZOOM));
                }

            }

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };
}
