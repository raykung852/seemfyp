package com.cuhk.seem.fyp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    private long backPressedTime;
    private Toast backToast;

    private Button btnViewTime, btnViewPhoto;
    private FrameLayout mMainFrame;
    private BottomNavigationView mMainNav;

    private RouteFragment routeFragment;
    private TimeFragment timeFragment;
    private PhotoFragment photoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        routeFragment = new RouteFragment();
        timeFragment = new TimeFragment();
        photoFragment = new PhotoFragment();

        setFragment(routeFragment);
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_directions:
                        setFragment(routeFragment);
                        return true;

                    case R.id.action_departure:
                        setFragment(timeFragment);
                        //Intent intent = new Intent(MainActivity.this, ViewTime.class);

                        //startActivity(intent);
                        return true;
                    case R.id.action_location_map:
                        setFragment(photoFragment);
                        //Intent intent2 = new Intent(MainActivity.this, ViewPhoto.class);

                        //startActivity(intent2);
                        return true;


                }
                return false;
            }


        });
    }

    @Override
    public void onBackPressed() {


        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}