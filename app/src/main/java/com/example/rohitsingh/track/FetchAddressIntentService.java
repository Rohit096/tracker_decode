package com.example.rohitsingh.track;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

@SuppressLint("Registered")
public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver mReceiver;
   public final class Constants {
       public static final int SUCCESS_RESULT = 0;
       public static final int FAILURE_RESULT = 1;
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

        String errorMessage = "";

        try {
            addresses =   geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException ignored) {
        }
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        }
        else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragment= new ArrayList<String>();
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragment.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragment));
        }

    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }


}
