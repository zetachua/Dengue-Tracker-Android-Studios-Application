package Control;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Boundary.ClinicUI;
import Boundary.MapsActivity;

import static java.lang.Double.parseDouble;

public class ClinicManager {
    DistanceManager distanceManager = new DistanceManager();
    public static ArrayList<Marker> clinicObjects = new ArrayList<>();
    Marker clinicMarker;

    public ArrayList<JSONObject> nearestClinics(int n, GeoJsonLayer layer) throws JSONException {
        ArrayList<JSONObject> allClinics = new ArrayList<JSONObject>();

        for (GeoJsonFeature feature : layer.getFeatures()) {
            JSONObject clinics = new JSONObject();
            String allInfo = feature.getProperty("Description");

            //Getting coordinates of each clinic
            Geometry coordinates = feature.getGeometry();
            String coord = coordinates.toString();
            String coordinate = coord.substring(coord.indexOf("("), coord.indexOf(")"));
            double lat = parseDouble(coordinate.substring(1, coordinate.indexOf(",")));
            double longi = parseDouble(coordinate.substring(coordinate.indexOf(",") + 1));

            //Getting name of each clinic
            int startClinicName = allInfo.indexOf("HCI_NAME");
            String cut = allInfo.substring(startClinicName + 18);
            int endClinicName = cut.indexOf("</td>");
            String clinicName = cut.substring(0, endClinicName);

            //Getting tele number of each clinic
            int startClinicNo = allInfo.indexOf("HCI_TEL");
            String clinicNo = allInfo.substring(startClinicNo + 17, startClinicNo + 25);

            //Getting postal code of each clinic
            int startPostal = allInfo.indexOf("POSTAL_CD");
            String clinicPostal = allInfo.substring(startPostal + 19, startPostal + 25);

            //add all clinic details to its JSONObject
            clinics.put("name", clinicName);
            clinics.put("number", clinicNo);
            clinics.put("postal", clinicPostal);
            clinics.put("Latitude", lat);
            clinics.put("Longitude", longi);

            //add each JSONObject to ArrayList
            allClinics.add(clinics);
        }

        boolean repeated;
        int index;
        int indexClinic = 0;
        ArrayList<JSONObject> nearestclinics = new ArrayList<JSONObject>();


        //getting the JSONObjects of the nearest n number of clinics
        for (int i = 0; i < n; i++) {
            index = -1;
            repeated = false;
            JSONObject min = allClinics.get(0);
            for (JSONObject clinic : allClinics) {
                index++;
                double lati = (double) clinic.get("Latitude");
                double longit = (double) clinic.get("Longitude");
                double point = distanceManager.calcDistance(lati, longit);
                String postalcode = clinic.getString("postal");


                double minlati = (double) min.get("Latitude");
                double minlongi = (double) min.get("Longitude");
                double currentMin = distanceManager.calcDistance(minlati, minlongi);

                if (point < currentMin) {
                    for (JSONObject a : nearestclinics) {
                        if (a.get("postal").equals(postalcode)) {
                            repeated = true;
                            break;
                        }
                    }
                    if (repeated == false) {
                        min = clinic;
                        indexClinic = index;
                    }
                }
            }
            nearestclinics.add(min);
            allClinics.remove(indexClinic);
        }

        return nearestclinics;
    }

    public void printClinicInfo(ArrayList<JSONObject> c) throws JSONException {
        for (JSONObject clinics : c) {
            String clinicName = clinics.getString("name");
            String clinicNo = clinics.getString("number");
            String postal = clinics.getString("postal");
            LatLng clinicPos = new LatLng(clinics.getDouble("Latitude"),clinics.getDouble("Longitude"));

            MarkerOptions nearestClinicOptions = new MarkerOptions()
                    .position(clinicPos)
                    .title(clinicName)
                    .snippet("TEL: "+ clinicNo + ", Postal: "+ postal)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            clinicMarker = MapsActivity.mMap.addMarker(nearestClinicOptions);
            clinicObjects.add(clinicMarker);

        }
    }

    public void removeClinicObjects(){
        for(Marker marker: clinicObjects){
            marker.remove();
        }
    }

}