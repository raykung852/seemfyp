package com.cuhk.seem.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    private Button btnSelectHotel;
    private Button btnMap;
    private EditText etDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        btnMap = (Button) findViewById(R.id.button2);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        initInstance();

    }
    public void initInstance() {
        btnSelectHotel = (Button) findViewById(R.id.button1);
        etDestination = (EditText) findViewById(R.id.text_destination);
        btnSelectHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String strDestination = etDestination.getText().toString();
                if(TextUtils.equals(strDestination, "Your destination")) {
                    Toast.makeText(getApplicationContext(),"Please select your destination.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent intent = new Intent(MainActivity.this, ViewRoute.class);
                    intent.putExtra("destination", etDestination.getText().toString());
                    startActivity(intent);
                }

            }
        });

        etDestination.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    Intent intent = new Intent(getApplicationContext(), SelectHotel.class);
                    startActivityForResult(intent,1);
                }
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK) {
            String receivedHotel = data.getStringExtra("HotelName");
            etDestination.setText(receivedHotel);

        }
    }


}
