package com.cuhk.seem.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    private Button btnSelectHotel;
    private Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        btnSelectHotel = (Button) findViewById(R.id.button1);


        btnSelectHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(MainActivity.this, SelectHotel.class);
                startActivity(intent);
            }
        });

        btnMap = (Button) findViewById(R.id.button2);


        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }
}
