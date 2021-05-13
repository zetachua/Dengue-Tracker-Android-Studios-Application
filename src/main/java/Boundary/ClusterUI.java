package Boundary;

import android.content.Context;
import android.view.View;

import Entity.Cluster;
import Control.ClusterManager;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Double.parseDouble;

public class ClusterUI {

    private Context clusterUIContext;

    public ClusterUI(View.OnClickListener onClick, Context context) {
        this.clusterUIContext = context;
    }
    public ClusterUI(Context context) {
        this.clusterUIContext = context;
    }

    public void displayClusterInfo() throws JSONException {
        ClusterManager clusterManager = new ClusterManager(clusterUIContext);
        clusterManager.getClusterShape(clusterUIContext);
        clusterManager.printClusterInfo();
    }
}
