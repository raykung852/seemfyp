package com.cuhk.seem.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    private long backPressedTime;
    private Toast backToast;

    private EditText etDestination;
    private Button btnSelectHotel;

    private Button btnViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
       /* Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setTitle("Home");*/
    /*
        Button btnMap;
        btnMap = (Button) findViewById(R.id.button2);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        }); */
        initInstance();

    }

    @Override
    public void onBackPressed() {


        if(backPressedTime+2000>System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
           backToast =  Toast.makeText(getBaseContext(),"Press back again to exit", Toast.LENGTH_SHORT);
           backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }

    public void initInstance() {

        etDestination = (EditText) findViewById(R.id.text_destination);
        String striDestination = etDestination.getText().toString();
        btnSelectHotel = (Button) findViewById(R.id.button1);
        btnViewTime = (Button) findViewById(R.id.button2);
        if (TextUtils.equals(striDestination, "Your destination")) {
            btnSelectHotel.setVisibility(View.GONE);
        }
        else {
            btnSelectHotel.setVisibility(View.VISIBLE);
        }


        btnSelectHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strDestination = etDestination.getText().toString();
                if (TextUtils.equals(strDestination, "Your destination")) {

                    Toast.makeText(getApplicationContext(), "Please select your destination.", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(MainActivity.this, ViewRoute.class);
                    intent.putExtra("destination", etDestination.getText().toString());
                    startActivity(intent);
                }

            }
        });

        btnViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ViewTime.class);

                startActivity(intent);
            }

            });



        etDestination.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    Intent intent = new Intent(getApplicationContext(), SelectHotel.class);
                    startActivityForResult(intent, 1);
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btnSelectHotel = (Button) findViewById(R.id.button1);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String receivedHotel = data.getStringExtra("HotelName");
            etDestination.setText(receivedHotel);
            btnSelectHotel.setVisibility(View.VISIBLE);
        }
    }


}
