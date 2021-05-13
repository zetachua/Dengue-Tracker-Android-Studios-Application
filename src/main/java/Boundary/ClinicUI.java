package Boundary;

import android.content.Context;

import Control.ClinicManager;

import com.example.searchlocationapp.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ClinicUI {
    ClinicManager clinicManager = new ClinicManager();
    private Context clinicUIContext;
    public ClinicUI(Context context) {
        this.clinicUIContext = context;
    }

    public void displayNearClinics() {
        try {
            GeoJsonLayer displayNearestClinicLayer = new GeoJsonLayer(MapsActivity.mMap, R.raw.clinic_info_geojson, clinicUIContext);
            clinicManager.printClinicInfo(clinicManager.nearestClinics(MapsActivity.valInMenu, displayNearestClinicLayer));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}