package com.example.amit.porterapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by amit on 7/27/2015.
 */
public class DetailActivity extends ActionBarActivity implements OnMapReadyCallback,DetailFragment.Callback {
//
    GoogleMap m_map;
    boolean mapReady=false;
    MarkerOptions packageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

//            Bundle loc_arguments= new Bundle();
//            loc_arguments.putString(MapsFragment.LOC_LAT,"12.927200");
//            loc_arguments.putString(MapsFragment.LOC_LNG,"80.235669");
//            MapsFragment map_fragment= new MapsFragment();
           // map_fragment.setArguments(loc_arguments);

            transaction.add(R.id.product_detail_container,fragment);
            transaction.commit();
//            transaction.add(R.id.maps_fragment_container,map_fragment);
//            MapFragment map_fragment=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
//            map_fragment.getMapAsync(this);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_map=googleMap;
        mapReady=true;
        packageLocation=new MarkerOptions().position(new LatLng(DetailFragment.loc_lat,DetailFragment.loc_lng))
                .title("Package Location");
        m_map.addMarker(packageLocation);
        LatLng chennai=new LatLng(DetailFragment.loc_lat,DetailFragment.loc_lng);
        CameraPosition target= CameraPosition.builder().target(chennai).zoom(14).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }

    @Override
    public void loadmap() {
        MapFragment map_fragment=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
        map_fragment.getMapAsync(this);
    }
}
