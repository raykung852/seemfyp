package com.cuhk.seem.fyp;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeFragment extends Fragment {

    private TextView timeA21, timeExpress;
    Query mTimeA21, mTimeExpress;
    String time, time2;
    Date date,date2, currentDate;
    Calendar calendar2;
    String strDate2;
    Handler handler,handler2;
    public TimeFragment() {






    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        getActivity().setTitle("Departure Time");
        timeA21 = (TextView) view.findViewById(R.id.timeA21);
        timeExpress = (TextView) view.findViewById(R.id.timeExpress);
        getCurrentTime();
        return view;
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
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    time = ds.child("time").getValue(String.class);

                    // timeA21.setText(time);
                    try {
                        date = mFormat.parse(time);


                    } catch (ParseException e) {

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    calendar2 = Calendar.getInstance();
                    strDate2 = mFormat.format(calendar2.getTime());
                    currentDate = mFormat.parse(strDate2);

                    long mills = date.getTime() - currentDate.getTime();


                    int mins = (int) (mills / (1000 * 60)) % 60;
                    if (mins < 2) {
                        timeA21.setText("Departuring");
                    } else {
                        String diff = mins + " mins";
                        timeA21.setText(diff);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);

     /*   Timer updateTimer = new Timer();
        updateTimer.schedule(new TimerTask(){
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        calendar2 = Calendar.getInstance();
                        strDate2 = mFormat.format(calendar2.getTime());
                        currentDate = mFormat.parse(strDate2);

                        long mills =  date.getTime()-currentDate.getTime() ;


                        int mins = (int) (mills/(1000*60))%60;
                        if (mins<2) {
                            timeA21.setText("Departuring");
                        } else
                        {
                            String diff = mins+" mins";
                            timeA21.setText(diff);
                        }
                    }

                },1000,1000);*/
               /* try {
                    calendar2 = Calendar.getInstance();
                    strDate2 = mFormat.format(calendar2.getTime());
                    currentDate = mFormat.parse(strDate2);

                    long mills =  date.getTime()-currentDate.getTime() ;


                    int mins = (int) (mills/(1000*60))%60;
                    if (mins<2) {
                        timeA21.setText("Departuring");
                    } else
                    {
                        String diff = mins+" mins";
                        timeA21.setText(diff);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },0,10000);*/

        mTimeExpress = FirebaseDatabase.getInstance().getReference("time_express").orderByChild("time").startAt(strDate).limitToFirst(1);

        mTimeExpress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ArrayList<Hotelinfo> list = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {



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
        handler2 = new Handler();
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                handler2.postDelayed(this, 1000);
                try {


                    long mills =  date2.getTime()-currentDate.getTime();

                    int mins = (int) (mills/(1000*60))%60;
                    if (mins<2) {
                        timeExpress.setText("Departuring");
                    } else
                    {
                        String diff = mins+" mins";
                        timeExpress.setText(diff);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler2.postDelayed(runnable2, 0);
        /*Timer updateTimer2 = new Timer();
        updateTimer2.schedule(new TimerTask(){
            public void run() {
                try {


                    long mills =  date2.getTime()-currentDate.getTime();

                    int mins = (int) (mills/(1000*60))%60;
                    if (mins<2) {
                        timeExpress.setText("Departuring");
                    } else
                    {
                        String diff = mins+" mins";
                        timeExpress.setText(diff);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },0,10000);*/

    }

}
