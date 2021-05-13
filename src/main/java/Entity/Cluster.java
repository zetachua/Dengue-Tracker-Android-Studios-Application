package Entity;

import android.content.Context;

import Control.DistanceManager;
import Boundary.MapsActivity;
import com.example.searchlocationapp.R;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;

public class Cluster {

    private Context clusterContext;
    DistanceManager distanceManager = new DistanceManager();


    public Cluster(Context context) {
        this.clusterContext = context;
    }



    //getting information of all the clusters
    public ArrayList<JSONObject> allClusterInfo() {
        ArrayList<JSONObject> allCluster = new ArrayList<>();
        try {
            GeoJsonLayer clusterLayer = new GeoJsonLayer(MapsActivity.mMap, R.raw.dengue_clusters_geojson, clusterContext);

            for (GeoJsonFeature feature : clusterLayer.getFeatures()) {
                JSONObject cluster = new JSONObject();
                //takes the first coordinate of every cluster as the cluster marker
                Geometry coordinates = feature.getGeometry();
                String allInfo = feature.getProperty("Description");
                String coord = coordinates.toString();
                double firstLat = parseDouble(coord.substring(coord.indexOf("(") + 1, coord.indexOf(",")));
                double firstLong = parseDouble(coord.substring(coord.indexOf(",") + 1, coord.indexOf(")")));
                double distance = distanceManager.calcDistance(firstLat, firstLong);

                //Getting Cluster name of each cluster
                int startClusterName = allInfo.indexOf("LOCALITY");
                String cut = allInfo.substring(startClusterName + 18);
                int endClusterName = cut.indexOf("</td>");
                String clusterName = cut.substring(0, endClusterName);

                //Getting no. of cases in each cluster
                int startClusterNo = allInfo.indexOf("CASE_SIZE");
                String no = allInfo.substring(startClusterNo + 19);
                int endClusterNo = no.indexOf("</td>");
                String clusterNo = no.substring(0, endClusterNo);

                cluster.put("name", clusterName);
                cluster.put("size", clusterNo);
                cluster.put("distance", distance);
                cluster.put("lat",firstLat);
                cluster.put("long",firstLong);
                //cluster.put("location", clusterLoc);

                allCluster.add(cluster);
            }
            return allCluster;
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return allCluster;
    }
}
