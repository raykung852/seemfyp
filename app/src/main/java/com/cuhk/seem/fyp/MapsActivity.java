package com.cuhk.seem.fyp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.baoyachi.stepview.VerticalStepView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cuhk.seem.fyp.R.string.google_maps_key;
import static java.lang.Integer.parseInt;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private DatabaseReference mDatabase;
   Query mHotelWalk, mBusWalk;
    private String mKEY;
    private RecyclerView mList;
    LocationManager locationManager;
    private Button btnWalk, btnCurrentWalk;
    String Hotelname;
    double dlat, dlng, olat, olng, clat, clng;
    String sCost, sDistance, sRoute, sTime,sTransport;
    Location location;
    ImageButton btnShare;
    VerticalStepView verticalStepView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //setTitle(bundle.getString("PostKEY"));
            mKEY = bundle.getString("PostKEY");
            Hotelname = bundle.getString("EXTRA_Hotelname");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Route Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        btnWalk = (Button) findViewById(R.id.btnWalk);
        btnWalk.setTransformationMethod(null);
        String s1 = "Walk to this Hotel";
        String s2 = "(From Bus Stop)";


        /***********************START*************/
        verticalStepView = (VerticalStepView) findViewById(R.id.verticalStepView);
        List<String> sources = new ArrayList<>();
        sources.add("Start");
        sources.add("Design");
        sources.add("Coding");
        sources.add("Testing");
        sources.add("Maintenance");

        verticalStepView.setStepsViewIndicatorComplectingPosition(0)
                .reverseDraw(false)
                .setStepViewTexts(sources)
                .setLinePaddingProportion(0.55f)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#750f6d"))
                .setStepViewComplectedTextColor(Color.parseColor("#750f6d"))
                .setStepViewUnComplectedTextColor(Color.parseColor("#dda300"))
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#4a4a4a"))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this,R.drawable.ic_destinationicon))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this,R.drawable.ic_destinationicon))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this,R.drawable.ic_origin));



        /** if(parseInt(mKEY)<45 || parseInt(mKEY)>88) {
             btnWalk.setVisibility(View.GONE);
         }*/

        Spannable spannable = new SpannableString(s1+"\n"+s2);
        spannable.setSpan(new RelativeSizeSpan(0.8f), s1.length(), (s1.length()+s2.length()+1),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        btnWalk.setText(spannable);


        btnCurrentWalk = (Button) findViewById(R.id.btnCurrentWalk);
        btnCurrentWalk.setTransformationMethod(null);
        String s3 ="Walk to this Hotel";
        String s4 = "(From Current Location)";
        Spannable spannable2 = new SpannableString(s3+"\n"+s4);
        spannable2.setSpan(new RelativeSizeSpan(0.8f), s3.length(), (s3.length()+s4.length()+1),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        btnCurrentWalk.setText(spannable2);

        btnShare=(ImageButton) findViewById(R.id.btnShare);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getCurrentLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





        mDatabase = FirebaseDatabase.getInstance().getReference().child("routeplanning");

        mList = (RecyclerView) findViewById(R.id.route_list);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.btnShare:
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String s1 = "Transportation method: " + sTransport +"\n" ;
                String shareBody;
                if (!sRoute.equals("Taxi")) {
                    String s2 = "Stop: " +sRoute +"\n";
                    String s3="Time: " +sTime + " mins\n"
                            +"Cost: $"+sCost+"\n"
                            +"Walking Distance: " +sDistance + " m\n"
                            +"View it on SEEMFYP!";
                    shareBody = s1+s2+s3;
                }
                else {
                    String s3="Time: " +sTime + " mins\n"
                            +"Cost: $"+sCost+"\n"
                            +"Walking Distance: " +sDistance + " m\n"
                            +"View it on SEEMFYP!";
                    shareBody = s1+s3;
                }

                String shareSub = "Suggested Route:";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onStart() {
      
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
                viewHolder.setTransport(getApplicationContext(), model.getTransport());
                viewHolder.setTime(model.getTime());
                viewHolder.setCost(model.getCost());
                viewHolder.setDistance(model.getDistance());

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
            } else {
                post_route.setVisibility(View.VISIBLE);
                post_route.setText(route);
            }

        }

        public void setTransport(Context ctx, String transport) {


                TextView post_transport = (TextView) mView.findViewById(R.id.post_transport2);
            //ImageView image_transport = (ImageView) mView.findViewById(R.id.image_transport);

            post_transport.setText(transport);
        }

        public void setTime(String time) {
            TextView post_time = (TextView) mView.findViewById(R.id.post_time2);
            post_time.setText(time + " mins");
        }

        public void setCost(String cost) {
            TextView post_cost = (TextView) mView.findViewById(R.id.post_cost2);
            post_cost.setText("$"+cost );

        }

        public void setDistance(String distance) {
            TextView post_distance = (TextView) mView.findViewById(R.id.post_distance2);
            post_distance.setText(distance+" m");

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
        getLocation();

        //Add a marker in Airport

       // LatLng busStop = new LatLng(22.301439, 114.176726);



        //mMap.addMarker(new MarkerOptions().position(busStop).title("Hong Kong Science Museum"));
        /*mMap.addPolyline(new PolylineOptions().add(
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
        );*/




    }

    void getLocation() {
        Log.v("E_VALUE","hotelname: " + Hotelname);
        mHotelWalk = FirebaseDatabase.getInstance().getReference("hotelnamever2").orderByChild("hotelname").equalTo(Hotelname);

        mHotelWalk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ArrayList<Hotelinfo> list = new ArrayList<>();
               for(DataSnapshot ds: dataSnapshot.getChildren()) {

                    //Hotelinfo hotelinfo = ds.getValue(Hotelinfo.class);

                    dlat = Double.parseDouble(ds.child("latitude").getValue(String.class));
                    dlng = Double.parseDouble(ds.child("longitude").getValue(String.class));


                    //hotel = new LatLng(dlat,dlng);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(dlat,dlng)).title(Hotelname));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dlat,dlng), 16));
                }


                // dlat = Double.parseDouble(list.get(0).getLatitude());
                //dlng = Double.parseDouble(list.get(0).getLongitude());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBusWalk = FirebaseDatabase.getInstance().getReference("routeplanning").orderByKey().equalTo(mKEY);

        mBusWalk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Log.v("E_VALUE","data: " + ds.getValue());
                    olat = Double.parseDouble(ds.child("latitude").getValue(String.class));
                    olng = Double.parseDouble(ds.child("longitude").getValue(String.class));
                    sCost =ds.child("cost").getValue(String.class);
                    sDistance=ds.child("distance").getValue(String.class);
                    sTransport=ds.child("transport").getValue(String.class);
                    sRoute=ds.child("route").getValue(String.class);
                    sTime=ds.child("time").getValue(String.class);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // if (location != null) {
                // final double lat = location.getLatitude();
                //final double lng = location.getLongitude();
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + olat + "," + olng +
                        "&destination=" + dlat + "," + dlng + "&travelmode=walking");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);


            }
            // }

        });

       /* btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


        mMap.getUiSettings().setMapToolbarEnabled(false);
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
    if (parseInt(mKEY) <45 ) {
            btnWalk.setVisibility(View.GONE);

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

                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                }
                break;


        }
    }

    void getCurrentLocation() {
        //Log.v("E_VALUE","hotelname: " + Hotelname);



        btnCurrentWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST);
                } else {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        clat = location.getLatitude();
                        clng = location.getLongitude();
                        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + clat + "," + clng +
                                "&destination=" + dlat + "," + dlng + "&travelmode=walking");

                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                        mapIntent.setPackage("com.google.android.apps.maps");

                        startActivity(mapIntent);
                }



            }
            }

        });


    }


    /*
    public void displayDirection(String[] directionsList)
    {

        int count = directionsList.length;
        for(int i = 0;i<count;i++)
        {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));

            mMap.addPolyline(options);
        }*/

}
