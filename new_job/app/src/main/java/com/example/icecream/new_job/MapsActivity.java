package com.example.icecream.new_job;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.icecream.new_job.entity.DirectionRequest;
import com.example.icecream.new_job.util.CustomAsyncTask;
import com.example.icecream.new_job.util.JsonParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String origin;
    private String destination;
    private String nameLocation;
    private ArrayList<LatLng> directionResult;
    private DirectionRequest directionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        origin = getIntent().getStringExtra("origin");
        destination=getIntent().getStringExtra("destination");
        nameLocation=getIntent().getStringExtra("name_location");
        directionRequest = new DirectionRequest();
        directionRequest.setOrigin(origin);
        directionRequest.setDestination(destination);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDataResult();
    }

    private void getDataResult()
    {
        directionResult = new ArrayList<>();
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(new CustomAsyncTask.OnCustomEventListener() {
            @Override
            public void onEvent(String s) {
                //get data from json
                directionResult = JsonParser.getListStep(s);
                if (directionResult.size() >0)
                {
                    int size = directionResult.size();
                    // get 2 point origin and des
                    LatLng yourLocation = directionResult.get(0);
                    LatLng yourDes = directionResult.get(size-1);
                    mMap.addMarker(new MarkerOptions().position(yourLocation).title("Your Location"));
                    mMap.addMarker(new MarkerOptions().position(yourDes).title(nameLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(yourDes));
                }
                PolylineOptions lineOptions = new PolylineOptions();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i=0;i< directionResult.size();i++)
                {
                    lineOptions.add(directionResult.get(i));
                    builder.include(directionResult.get(i));
                }
                // set bounder all point
                LatLngBounds bounds = builder.build();
                int padding = 100; // offset from edges of the map in pixels
                // set line option
                lineOptions.width(10);
                lineOptions.color(Color.RED);
                mMap.addPolyline(lineOptions);

                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            }
        });
        customAsyncTask.execute(directionRequest.getUrlRequest());
    }


}
