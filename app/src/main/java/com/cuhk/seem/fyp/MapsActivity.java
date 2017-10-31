package com.cuhk.seem.fyp;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
       /** try {
            new DirectionFinder(this,origin,destination).execute();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } */
    }
    /**public DirectionFinder(DirectionFinderListener listener, String origin, String destination ) {
        this.listener=listener;
        this.origin=origin;
        this.destination=destination;
    }
    public void execute() throws UnsupportedEncodingException {
        listener.onDirectionFinderStart();
        new DownloadRawData().execute(createUrl());
    }
    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder(origin, "utf-8");
        String urlDestination = URLEncoder(destination, "utf-8");
        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination +"&key=" + google_maps_key;

    }
    private class DownloadRawData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.OpenConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while((line=reader.readLine())!=null) {
                    buffer.append(line + "\n");
                }
                return buffer.toString();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null) return;

        List<Route> routes = new ArrayList
    }
*/


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
