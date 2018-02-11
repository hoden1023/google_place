package com.example.icecream.new_job;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icecream.new_job.data.PlaceTypes;
import com.example.icecream.new_job.entity.PlaceRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;

/**
 * Created by IceCream on 2/9/2018.
 */

public class CategoryFragment extends Fragment  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private SeekBar seekRadius;
    private TextView txtRadius;
    private EditText edtSearchType;
    private ListView listCategory;
    //
    private ArrayAdapter adapter;
    private ArrayList<String> lstNameCategory;

    protected GoogleApiClient mGoogleApiClient;
    private int connectGMS = 0;
    private String location;
    private int radius;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        findView();
        getLocation();
        loadListType("");
        setupSeek();
        setupEditText();
        setupListView();
    }

    private void findView() {
        seekRadius = (SeekBar) getActivity().findViewById(R.id.seek_radius);
        txtRadius = (TextView) getActivity().findViewById(R.id.txt_radius);
        edtSearchType = (EditText) getActivity().findViewById(R.id.edt_search_type);
        listCategory = (ListView) getActivity().findViewById(R.id.list_category);
    }

    private void loadListType(String type) {
        lstNameCategory = new ArrayList<>();
        if ("".equals(type)) {
            // full list
            for (int i = 0; i < PlaceTypes.LIST_TYPE.length; i++) {
                lstNameCategory.add(PlaceTypes.LIST_TYPE[i][1]);
            }
        } else {
            for (int i = 0; i < PlaceTypes.LIST_TYPE.length; i++) {
                if (PlaceTypes.LIST_TYPE[i][1].toLowerCase().contains(type.toLowerCase()))
                    lstNameCategory.add(PlaceTypes.LIST_TYPE[i][1]);
            }
        }
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lstNameCategory);
        listCategory.setAdapter(adapter);
    }

    private void setupSeek()
    {
        seekRadius.setProgress(20);
        txtRadius.setText("1.0 km");
        radius=1000;
        seekRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius = i*50;
                if(radius < 1000)
                    txtRadius.setText(radius+" m");
                else {
                    float ra =radius/1000f;
                    txtRadius.setText(ra + " km");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setupEditText()
    {
        edtSearchType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadListType(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupListView(){
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (connectGMS)
                {
                    case 0: {
                        getLocation();
                        Toast.makeText(getActivity(),"loading location, try again!",Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case -1: Toast.makeText(getActivity(),"false to get location",Toast.LENGTH_SHORT).show();
                    break;
                    case 1: {
                        PlaceRequest placeRequest = new PlaceRequest();
                        placeRequest.setType(getTypePlace(i));
                        if ("".equals(placeRequest.getType()))
                        {
                            Toast.makeText(getActivity(),"something wrong, try again later!",Toast.LENGTH_SHORT).show();
                        }else {
                            placeRequest.setLocation(location);
                            placeRequest.setRadius(String.valueOf(radius));
                            //

                            SubCategoryFragment newFragment = new SubCategoryFragment(placeRequest,lstNameCategory.get(i));
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            // Replace whatever is in the fragment_container view with this fragment,
                            // and add the transaction to the back stack so the user can navigate back
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.addToBackStack(null);

                            // Commit the transaction
                            transaction.commit();
                        }
                    }
                }
            }
        });
    }

    private String getTypePlace(int pos)
    {
        for (int i=0; i< PlaceTypes.LIST_TYPE.length;i++)
        {
            if (PlaceTypes.LIST_TYPE[i][1].equals(lstNameCategory.get(pos)))
                return PlaceTypes.LIST_TYPE[i][0];
        }
        return "";
    }

    private void getLocation() {

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    getActivity(), 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mGoogleApiClient != null) {

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                String lat = String.valueOf(mLastLocation.getLatitude());
                String lng = String.valueOf(mLastLocation.getLongitude());
                connectGMS = 1;
                location =lat+","+lng;
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        connectGMS = -1;

    }

}
