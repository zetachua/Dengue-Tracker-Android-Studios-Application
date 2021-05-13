package Control;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import Boundary.MapsActivity;
import com.example.searchlocationapp.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import Boundary.SearchActivity;
import Entity.Cluster;

import static java.lang.Double.parseDouble;

public class ClusterManager {
    private Context clusterManagerContext;

    public static String title;
    public static String liveSnippet;
    public static String inputSnippet;
    public static LatLng clusterLocation;

    public ClusterManager(View.OnClickListener onClick, Context context) {
        this.clusterManagerContext = context;
    }

    public ClusterManager(Context context) {
        this.clusterManagerContext = context;
    }

    public static ArrayList<Marker> clusterObjects = new ArrayList<>();
    Marker clusterLiveMarker, clusterInputMarker;


    // getting shape of all the clusters
    public void getClusterShape(Context mContext) {
        try {
            GeoJsonLayer clusterLayer = new GeoJsonLayer(MapsActivity.mMap, R.raw.dengue_clusters_geojson, mContext);
            GeoJsonPolygonStyle polyStyle = clusterLayer.getDefaultPolygonStyle();
            polyStyle.setStrokeColor(Color.MAGENTA);
            polyStyle.setStrokeWidth(2);

            clusterLayer.addLayerToMap();
        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void removeClusterObjects(){
        for(Marker marker: clusterObjects){
            marker.remove();
        }
    }


    public void printClusterInfo() throws JSONException {
        Cluster cluster = new Cluster(clusterManagerContext);
        ArrayList<JSONObject> allCluster = cluster.allClusterInfo();

        for (JSONObject clusterObject : allCluster) {
            String clusterName = clusterObject.getString("name");
            String clusterSize = clusterObject.getString("size");
            Double clusterDist = parseDouble(clusterObject.getString("distance"));
            Double clusterLat = parseDouble(clusterObject.getString("lat"));
            Double clusterLong = parseDouble(clusterObject.getString("long"));
            clusterLocation = new LatLng(clusterLat, clusterLong);
            String displayDist = String.format("%.2f", clusterDist);

            title = clusterName;
            liveSnippet = clusterSize + " cases, " + displayDist + "m away from Live";
            inputSnippet = clusterSize + " cases, " + displayDist + "m away from Input";
            if (SearchActivity.searchInput == true) {
                MarkerOptions clusterOptions = new MarkerOptions()
                        .position(clusterLocation)
                        .title(title)
                        .snippet(inputSnippet)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                clusterInputMarker = MapsActivity.mMap.addMarker(clusterOptions);
                clusterObjects.add(clusterInputMarker);
            } else {
                MarkerOptions clusterOptions = new MarkerOptions()
                        .position(clusterLocation)
                        .title(title)
                        .snippet(liveSnippet)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                clusterLiveMarker = MapsActivity.mMap.addMarker(clusterOptions);
                clusterObjects.add(clusterLiveMarker);
            }

        }
    }
}
