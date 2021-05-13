package Entity;

import android.content.Context;

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

public class Clinic {
    private Context clinicContext;

    public Clinic(Context context) {
        this.clinicContext = context;
    }

    public ArrayList<JSONObject> allClinicInfo() {
        ArrayList<JSONObject> allClinics = new ArrayList<JSONObject>();
        try {
            GeoJsonLayer clinicLayer = new GeoJsonLayer(MapsActivity.mMap, R.raw.clinic_info_geojson, clinicContext);

            for (GeoJsonFeature feature : clinicLayer.getFeatures()) {
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
            return allClinics;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allClinics;
    }

}
