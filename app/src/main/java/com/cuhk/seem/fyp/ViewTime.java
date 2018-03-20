package com.cuhk.seem.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ViewTime  extends AppCompatActivity {
    private ListView ltSearch2;
    private EditText etSearch2;
    private TextView timeA21, timeExpress;
    private ArrayAdapter<String> adapter;

    private DatabaseReference mDatabase;
    private ArrayList<String> data= new ArrayList<>();

    Query mTimeA21, mTimeExpress;
    String time, time2;
    Date date,date2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_time);

        setTitle("Departure Time");
        getCurrentTime();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        timeA21 = (TextView) findViewById(R.id.timeA21);
        timeExpress = (TextView) findViewById(R.id.timeExpress);
    }


    public void getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");
       String strDate = mFormat.format(calendar.getTime());

        mTimeA21 = FirebaseDatabase.getInstance().getReference("time_A21").orderByChild("time").startAt(strDate).limitToFirst(1);

        mTimeA21.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ArrayList<Hotelinfo> list = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {

                    //Hotelinfo hotelinfo = ds.getValue(Hotelinfo.class);
                   // Log.v("E_VALUE","Time(database): " + ds.getValue());

                    time = ds.child("time").getValue(String.class);

                   // timeA21.setText(time);
                    try {
                       date = mFormat.parse(time);
                        Log.v("E_VALUE","Time(database): " + date);

                    } catch (ParseException e) {

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Timer updateTimer = new Timer();
        updateTimer.schedule(new TimerTask(){
            public void run() {
                try {
                    Calendar calendar2 = Calendar.getInstance();
                    String strDate2 = mFormat.format(calendar2.getTime());
                    Date currentDate = mFormat.parse(strDate2);

                    long mills =  date.getTime()-currentDate.getTime() ;
                    Log.v("E_VALUE","Time: " + date.getTime());
                    Log.v("E_VALUE","Time2: " + currentDate.getTime());
                    //int hours = (int) (mills/(1000*60*60));
                    int mins = (int) (mills/(1000*60))%60;
                    String diff =  +mins +" mins";
                    timeA21.setText(diff);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },0,1000);

        mTimeExpress = FirebaseDatabase.getInstance().getReference("time_express").orderByChild("time").startAt(strDate).limitToFirst(1);

        mTimeExpress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ArrayList<Hotelinfo> list = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {


                    Log.v("E_VALUE","Time: " + ds.getValue());

                    time2 = ds.child("time").getValue(String.class);
                    //timeExpress.setText(time2);
                    try {
                         date2 = mFormat.parse(time2);


                    } catch (ParseException e) {

                    }

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Timer updateTimer2 = new Timer();
        updateTimer2.schedule(new TimerTask(){
            public void run() {
                try {
                    Calendar calendar3 = Calendar.getInstance();
                    String strDate3 = mFormat.format(calendar3.getTime());
                    Date currentDate = mFormat.parse(strDate3);

                    long mills =  date2.getTime()-currentDate.getTime();

                    //int hours = (int) (mills/(1000*60*60));
                    int mins = (int) (mills/(1000*60))%60;
                    String diff = mins+" mins";
                    timeExpress.setText(diff);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },0,1000);

    }


}