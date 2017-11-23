package com.cuhk.seem.fyp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static com.cuhk.seem.fyp.R.string.google_maps_key;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private DatabaseReference mDatabase;
    private String mKEY;
    private RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String origin = "Hong Kong Science Museum";
        String destination = "Acesite Knutsford Hotel";

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setTitle(bundle.getString("PostKEY"));
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("routeplanning");
        mList = (RecyclerView) findViewById(R.id.route_list);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        mKEY = getTitle().toString();

        super.onStart();

        final FirebaseRecyclerAdapter<RouteInformation, MapsActivity.RouteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RouteInformation, MapsActivity.RouteViewHolder>(
                RouteInformation.class,
                R.layout.row,
                MapsActivity.RouteViewHolder.class,
                mDatabase.orderByKey().equalTo(mKEY)
        ) {
            @Override
            protected void populateViewHolder(MapsActivity.RouteViewHolder viewHolder, RouteInformation model, int position) {

                viewHolder.setRoute(model.getRoute());

            }
        };
        mList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public RouteViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setRoute(String route) {
            TextView post_route = (TextView) mView.findViewById(R.id.post_route);

            if (route.equals("Taxi")) {
                post_route.setVisibility(View.GONE);
            }
            else {
                post_route.setVisibility(View.VISIBLE);
                post_route.setText(route);
            }

        }


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

        //Add a marker in Airport
        LatLng hotel = new LatLng(22.301696, 114.174848);
        LatLng busStop = new LatLng(22.301439, 114.176726);
        // Add a marker in Sydney and move the camera

        mMap.addMarker(new MarkerOptions().position(hotel).title("Acesite Knutsford Hotel"));
        mMap.addMarker(new MarkerOptions().position(busStop).title("Hong Kong Science Museum"));
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                }
                break;


        }
    }

}
