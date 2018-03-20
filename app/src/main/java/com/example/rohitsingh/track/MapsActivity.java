package com.example.rohitsingh.track;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    Button b_1;
    TextView tv_1;
    public double latitude;
    public double longitude;
    TextView adrs ;
    EditText search;
    ImageView search_img;



    DatabaseReference database;
    //DatabaseReference myRef;
    //DatabaseReference ref = database.getReference();


    //DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    //DatabaseReference myRef = database.child("profiles/");



    public static class User1{
        double Lat;
        double Lon;
        String Time;
        String ip;

        public User1() {
        }

        public User1(double lat, double lon, String time, String ip) {
            this.Lat = lat;
            this.Lon = lon;
            this.Time = time;
            this.ip = ip;
        }

        public double getLat() {
            return Lat;
        }

        public double getLon() {
            return Lon;
        }

        public String getTim() {
            return Time;
        }

        public String getIp() {
            return ip;
        }

        public void setLat(double lat) {
            Lat = lat;
        }

        public void setLon(double lon) {
            Lon = lon;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String time) {
            Time = time;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

    User1 user1 = new User1();

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
        adrs = (TextView) findViewById(R.id.address);


        //myRef = database.child("User 1/");
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

                /*try {
                    diplay_address(latitude,longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/


            }
        });


        b_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              database = FirebaseDatabase.getInstance().getReference();
                DatabaseReference db;
                db = database ;

                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adrs.setText("bca");
                        user1 = dataSnapshot.getValue(User1.class);
                        latitude = user1.getLat();
                        longitude = user1.getLon();
                        adrs.setText(""+latitude+" "+longitude);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                db.addValueEventListener(postListener);



              /* database.child("User 1").addListenerForSingleValueEvent(new ValueEventListener() {

                    /*@Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        user1 = dataSnapshot.getValue(User1.class);
                        latitude = user1.getLat();
                        longitude = user1.getLon();

                        adrs.setText("This Is "+user1.getTime());
                        marker[0] = mMap.addMarker(new MarkerOptions()
                                .position(
                                        new LatLng(latitude,longitude)).title("My Location").visible(true));


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        adrs.setText("Not Found");
                    }
                });*/




                //startActivity(new Intent(MapsActivity.this,retrive.class));
                //retrive location = new retrive();
                /*ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user1 = dataSnapshot.getValue(User1.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        adrs.setText("Error");
                    }
                });*/



                /*ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {}

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                        user1ch = dataSnapshot.getValue(User1.class);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });*/



               /* try {
                    diplay_address(latitude,longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                }
        });


    }




   /*private void diplay_address(double latitude, double longitude) throws IOException {
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
    }*/


}


