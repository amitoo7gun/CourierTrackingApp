package com.example.amit.porterapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MapsFragment extends android.support.v4.app.Fragment{

    static final String LOC_LAT = "12.927200";
    static final String LOC_LNG = "80.235669";
    public String map_loc_lat,map_loc_lng;
    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            map_loc_lat = arguments.getString(MapsFragment.LOC_LAT);
            map_loc_lng = arguments.getString(MapsFragment.LOC_LNG);

        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }



}
