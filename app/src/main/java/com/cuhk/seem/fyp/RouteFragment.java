package com.cuhk.seem.fyp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class RouteFragment extends Fragment {


    private EditText etDestination;
    private Button btnSelectHotel;

    public RouteFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route, container, false);

        getActivity().setTitle("Home");
        etDestination = (EditText) view.findViewById(R.id.text_destination);
        String striDestination = etDestination.getText().toString();
        btnSelectHotel = (Button) view.findViewById(R.id.button1);

        if (TextUtils.equals(striDestination, "Your destination")) {
            btnSelectHotel.setVisibility(View.GONE);
        } else {
            btnSelectHotel.setVisibility(View.VISIBLE);
        }


        btnSelectHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strDestination = etDestination.getText().toString();
                if (TextUtils.equals(strDestination, "Your destination")) {

                    Toast.makeText(getActivity().getApplicationContext(), "Please select your destination.", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(getActivity(), ViewRoute.class);
                    intent.putExtra("destination", etDestination.getText().toString());
                    startActivity(intent);
                }

            }
        });


        etDestination.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SelectHotel.class);
                    startActivityForResult(intent, 1);
                }
                return true;
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btnSelectHotel = (Button) getView().findViewById(R.id.button1);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String receivedHotel = data.getStringExtra("HotelName");
            etDestination.setText(receivedHotel);
            btnSelectHotel.setVisibility(View.VISIBLE);
        }
    }
}