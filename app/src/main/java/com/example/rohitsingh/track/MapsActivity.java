package com.example.rohitsingh.track;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,
        GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    Button b_1;
    TextView tv_1,address;
    public double latitude;
    public double longitude;

    private AddressResultReceiver mResultReceiver;



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
               marker[0] = mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(latitude,
                                        longitude)).title("My Location")

                        .draggable(true).visible(true));
                startIntentService();

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

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER,mResultReceiver);

        startService(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }

            String mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            displayAddressOutput(mAddressOutput);

            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {
                Toast.makeText(MapsActivity.this, R.string.address_found, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void displayAddressOutput(String mAddressOutput) {

        address.setText(mAddressOutput);

    }

}


