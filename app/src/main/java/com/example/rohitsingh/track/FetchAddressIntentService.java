package com.example.rohitsingh.track;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

@SuppressLint("Registered")
public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver mReceiver;
   public final class Constants {

       public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
       public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
       public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

   }

    public FetchAddressIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        List<Address> addresses =null;
        Geocoder  geocoder = new Geocoder(this, Locale.getDefault());
         MapsActivity latitude = new MapsActivity();
         double lat=latitude.latitude;
         double lon = latitude.longitude;



        try {
            addresses =   geocoder.getFromLocation(lat, lon, 1);

            Address address = addresses.get(0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i)).append("\n");
            }
            sb.append(address.getLocality()).append("\n");
            sb.append(address.getPostalCode()).append("\n");
            sb.append(address.getCountryName());
            String result = sb.toString();


                deliverResultToReceiver(result);

            Log.i(TAG, getString(R.string.address_found));
        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);

        }
        /*catch (IllegalArgumentException ignored) {
        }
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        }
        else {


        }
        */

    }

    private void deliverResultToReceiver(String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(0, bundle);
    }


}
