package com.cuhk.seem.fyp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {


    public PhotoFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        getActivity().setTitle("Bus Location Map");
        final PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        Picasso.with(getActivity().getApplicationContext()).load(R.drawable.bus_location_map).fit().centerInside().into(photoView);
        return view;
    }

}
