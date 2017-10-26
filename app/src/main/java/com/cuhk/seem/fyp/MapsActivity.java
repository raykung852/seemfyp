package com.cuhk.seem.fyp;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        //Add a marker in Airport
        LatLng hotel = new LatLng(22.301696, 114.174848);
        LatLng busStop = new LatLng(22.301439, 114.176726);
        // Add a marker in Sydney and move the camera

        mMap.addMarker(new MarkerOptions().position(hotel).title("Acesite Knutsford Hotel"));
        mMap.addMarker(new MarkerOptions().position(hotel).title("Hong Kong Science Museum"));
        mMap.addPolyline(new PolylineOptions().add(
                busStop,
                new LatLng(22.300493, 114.176324),
                new LatLng(22.300528, 114.176126),
                new LatLng(22.300851, 114.176158),
                new LatLng(22.301021, 114.175587),
                new LatLng(22.301229, 114.175037),
                new LatLng(22.301541, 114.174613),
                new LatLng(22.301750, 114.174734),
                hotel
            )
                .width(10)
                .color(Color.RED)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hotel, 16));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }


    }
}
