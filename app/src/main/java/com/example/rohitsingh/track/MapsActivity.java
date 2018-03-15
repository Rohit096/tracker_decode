package com.example.rohitsingh.track;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Service;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    Button b_1;
    TextView tv_1;
    public double latitude;
    public double longitude;
    TextView adrs ;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        b_1 = (Button)findViewById(R.id.b_1);
        tv_1 = (TextView)findViewById(R.id.tv_1);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        final Marker[] marker = new Marker[1];


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                 latitude = latLng.latitude;
                 longitude = latLng.longitude;

                if (marker[0] != null) {
                    marker[0].remove();
                }

                tv_1.setText("lat= "+latitude+"  lon= "+longitude);
               marker[0] = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("My Location").draggable(true).visible(true));

                try {
                    diplay_address(latitude,longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        b_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker[0] != null) {
                    marker[0].remove();
                }
                marker[0] = mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(19,77)).title("My Location").visible(true));

            }
        });


    }

    private void diplay_address(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            adrs=(TextView) findViewById(R.id.address);

            adrs.setText(String.format("%s\n%s\n%s", address, city, state));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


