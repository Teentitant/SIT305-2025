package com.example.lostandfoundapp.ui;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lostandfoundapp.R;
import com.example.lostandfoundapp.model.Advert;
import com.example.lostandfoundapp.viewmodel.AdvertViewModel;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String EXTRA_LATITUDE = "com.example.lostandfoundapp.EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "com.example.lostandfoundapp.EXTRA_LONGITUDE";
    public static final String EXTRA_ADVERT_TITLE = "com.example.lostandfoundapp.EXTRA_ADVERT_TITLE";
    public static final String EXTRA_ADVERT_SNIPPET = "com.example.lostandfoundapp.EXTRA_ADVERT_SNIPPET";

    private double itemLatitude;
    private double itemLongitude;
    private String itemTitle;
    private String itemSnippet;

    private AdvertViewModel advertViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        advertViewModel = new ViewModelProvider(this).get(AdvertViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Observe adverts and add markers
        advertViewModel.getAllAdverts().observe(this, adverts -> {
            if (adverts != null && !adverts.isEmpty()) {
                mMap.clear(); // Clear previous markers
                LatLng firstAdvertLocation = null;
                for (Advert advert : adverts) {
                    if (advert.latitude != 0.0 || advert.longitude != 0.0) {
                        LatLng itemLocation = new LatLng(advert.latitude, advert.longitude);
                        mMap.addMarker(new MarkerOptions()
                                .position(itemLocation)
                                .title(advert.postType + ": " + advert.name)
                                .snippet(advert.location));
                        if (firstAdvertLocation == null) {
                            firstAdvertLocation = itemLocation;
                        }
                    }
                }
                // Move camera to the first item or a default location
                if (firstAdvertLocation != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstAdvertLocation, 10f));
                } else {
                    // Default location (e.g., your city) if no items or no valid locations
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-37.8136, 144.9631), 10f)); // Melbourne example
                }
            }
        });

        // Optional: Enable My Location layer if permission granted
        // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        // mMap.setMyLocationEnabled(true);
        // }
    }
}