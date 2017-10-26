package com.cuhk.seem.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by cwkun_000 on 2017/10/20.
 */

public class SelectHotel extends AppCompatActivity {
    private ListView lstSearch;
    private EditText edtSearch;
    private ArrayAdapter<String> adapter;

    private DatabaseReference mDatabase;
    private ArrayList<String> data= new ArrayList<>();
    private DatabaseReference mHotelname;
    //String data[] ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_hotel);
        setTitle("Select the hotel");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mHotelname = mDatabase.child("hotelname").getRef();

        lstSearch = (ListView) findViewById(R.id.lstSearch);
        lstSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SelectHotel.this, ViewRoute.class);
                intent.putExtra("HotelName",lstSearch.getItemAtPosition(i).toString());
                startActivity(intent);            }
        });
        edtSearch = (EditText) findViewById(R.id.edtSearch);

        //data = getResources().getStringArray(R.array.hotel_name);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item,R.id.textView,data);
        lstSearch.setAdapter(adapter);

        mHotelname.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                data.add(value);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SelectHotel.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




}
