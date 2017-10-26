package com.cuhk.seem.fyp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * Created by cwkun_000 on 2017/10/22.
 */

public class ViewRoute extends AppCompatActivity {
    private RecyclerView mRouteList;

    private DatabaseReference mDatabase;
    String mHotelname;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.view_route);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null ) {
            setTitle(bundle.getString("HotelName"));
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("routeplanning");




        mRouteList = (RecyclerView) findViewById(R.id.route_text);
        mRouteList.setHasFixedSize(true);
        mRouteList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
       mHotelname = getTitle().toString();

        super.onStart();
        FirebaseRecyclerAdapter<RouteInformation, RouteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RouteInformation, RouteViewHolder>(

                RouteInformation.class,
                R.layout.route_row,
                RouteViewHolder.class,
                mDatabase.orderByChild("hotel").equalTo(mHotelname)
        ) {
            @Override
            protected void populateViewHolder(RouteViewHolder viewHolder, RouteInformation model, int position) {
                viewHolder.setRID(model.getRID());
                //viewHolder.setRoute(model.getRoute());
                viewHolder.setTime(model.getTime());
                viewHolder.setCost(model.getCost());
                viewHolder.setDistance(model.getDistance());
                viewHolder.setTransport(getApplicationContext(), model.getTransport());

            }
        };
        mRouteList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;

        public RouteViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setRID(String RID) {
            TextView post_RID = (TextView) mView.findViewById(R.id.post_RID);
            int iRID;
            iRID = Integer.parseInt(RID);
            if (iRID > 88) {
                iRID = 3 ;
            }
            else if (iRID>44) {
                iRID = 2;
            }
            else iRID = 1;
            post_RID.setText("Route\n" + String.valueOf(iRID));

        }
       /* public void setRoute(String route) {
            TextView post_route = (TextView) mView.findViewById(R.id.post_route);

            if (route.equals("taxi")) {
                post_route.setVisibility(View.GONE);
            }
            else {
                post_route.setVisibility(View.VISIBLE);
                post_route.setText(route);
            }

        }*/

        public void setTime(String time) {
            TextView post_time = (TextView) mView.findViewById(R.id.post_time);
            post_time.setText(time);

        }
        public void setCost(String cost) {
            TextView post_cost = (TextView) mView.findViewById(R.id.post_cost);
            post_cost.setText(cost);

        }
        public void setDistance(String distance) {
            TextView post_distance = (TextView) mView.findViewById(R.id.post_distance);
            post_distance.setText(distance);

        }
        public void setTransport(Context ctx, String transport) {


            TextView post_transport = (TextView) mView.findViewById(R.id.post_transport);
            ImageView image_transport = (ImageView) mView.findViewById(R.id.image_transport);

            post_transport.setText(transport);

            if (transport.equals("Taxi")) {
                Picasso.with(ctx).load(R.drawable.ic_local_taxi_black_48dp).resize(50,50).into(image_transport);
            }
            else if(transport.equals("Bus")) {
                Picasso.with(ctx).load(R.drawable.ic_directions_bus_black_48dp).resize(50,50).into(image_transport);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}