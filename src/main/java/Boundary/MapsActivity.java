package Boundary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.searchlocationapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Control.ClinicManager;
import Control.ClusterManager;
import Control.NotificationManager;
import Entity.Cluster;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,PopupMenu.OnMenuItemClickListener {


    public static GoogleMap mMap;
    public static int valInMenu;
    public static LatLng liveLocation;
    public static Marker curMarker;
    public static int DEFAULT_ZOOM = 14;
    SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private LocationListener locationListener;



    SearchActivity searchManager = new SearchActivity(this);
    ClusterManager clusterManager = new ClusterManager(this);
    ClinicManager clinicManager = new ClinicManager();
    ClinicUI clinicUI= new ClinicUI(this);


    //live location permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                }

            }
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        searchManager.searhView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        CentralizerButton centralizerButton = new CentralizerButton(this);
        Button centralizer = findViewById(R.id.centralizer);
        centralizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centralizerButton.buttonClick();
            }
        });

        OverviewButton overviewButton = new OverviewButton();
        Button overview = findViewById(R.id.mapOverview);
        overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overviewButton.buttonClick();
            }
        });

        ClusterButton clusterButton = new ClusterButton(this);
        Button clusterBtn = findViewById(R.id.clusterButton);
        clusterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clusterButton.buttonClick();
            }
        });


        searchManager.searhView.setOnQueryTextListener(searchManager.searchView);


        // Navigation Bar
        // Initialise and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set Map selected
        bottomNavigationView.setSelectedItemId(R.id.nav_map);
        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_map:
                        return true;
                    //break;
                    case R.id.nav_symptom:
                        startActivity(new Intent(getApplicationContext()
                                , SymptomCheckerActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    default:
                        return false;
                }
            }
        });
        mapFragment.getMapAsync(this);
    }


    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }


    public boolean onMenuItemClick(MenuItem item) {
        valInMenu = Integer.valueOf((String) item.getTitle());
        if(SearchActivity.searchInput==true){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SearchActivity.inputLoc, DEFAULT_ZOOM));
        }
        else if (SearchActivity.searchInput==false) {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(liveLocation, DEFAULT_ZOOM));
        }

        clinicManager.removeClinicObjects();
        clusterManager.removeClusterObjects();
        clinicUI.displayNearClinics();

        return true;
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds sgBounds = new LatLngBounds(
                new LatLng(1.2046, 103.5292), // SW bounds
                new LatLng(1.4585, 104.0490)  // NE bounds
        );




        NotificationManager sendNoti = new NotificationManager(this);
        Cluster cluster = new Cluster(this);

        //Live location
        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                liveLocation = new LatLng(location.getLatitude(),location.getLongitude());
                if(curMarker!=null){
                    curMarker.remove();
                }
                curMarker= mMap.addMarker(new MarkerOptions().position(liveLocation).title("Live Location")
                        .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_livemarker)));

                ArrayList<JSONObject> allCluster = cluster.allClusterInfo();

                for (JSONObject cluster: allCluster) {
                    try {
                        double dist = cluster.getDouble("distance");
                        if (dist<1000 && SearchActivity.searchInput==false) {
                           sendNoti.sendNotification();

                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        if(Build.VERSION.SDK_INT<23){
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //could be any number*//*);
            } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = null;
                    while (lastKnownLocation==null) {
                        lastKnownLocation=locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                        if (lastKnownLocation!=null){
                            liveLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                            break;
                        }
                }
                }
        }


        //from checkbox class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (SearchActivity.searchInput == true) {
                SearchActivity.inputMarker.remove();
            }
            valInMenu = extras.getInt(SymptomCheckerActivity.EXTRA_INT);

            clinicUI.displayNearClinics();

        }

        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        clusterManager.getClusterShape(this);
        mMap.setLatLngBoundsForCameraTarget(sgBounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(liveLocation, DEFAULT_ZOOM));
        mMap.setMinZoomPreference(10.0f);


    }


}

