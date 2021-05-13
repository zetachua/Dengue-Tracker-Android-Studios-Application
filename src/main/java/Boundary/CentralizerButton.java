package Boundary;

import android.content.Context;

import Control.ClinicManager;
import Control.ClusterManager;

import com.google.android.gms.maps.CameraUpdateFactory;

public class CentralizerButton implements AppStrategy {
    private Context centralizerContext;

    public CentralizerButton(Context context) {
        this.centralizerContext = context;
    }

    ClinicManager clinicManager = new ClinicManager();


    @Override
    public void buttonClick() {
        ClusterManager clickClusterManager = new ClusterManager(centralizerContext);

        if(ClinicManager.clinicObjects!=null && ClusterManager.clusterObjects!=null){
            clickClusterManager.removeClusterObjects();
            clinicManager.removeClinicObjects();
        }

        if (SearchActivity.inputMarker!=null) {
            SearchActivity.inputMarker.remove();
        }

        SearchActivity.searchInput=false;
        MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MapsActivity.liveLocation,MapsActivity.DEFAULT_ZOOM));
    }
}
