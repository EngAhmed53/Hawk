package com.shouman.apps.hawk.ui.main.salesUI.add.addActivity;

import android.app.Application;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationViewModel extends AndroidViewModel {

    private MutableLiveData<LatLng> latLongMutableLiveData;
    private FusedLocationProviderClient fusedLocation;


    public LocationViewModel(@NonNull Application application) {
        super(application);

        latLongMutableLiveData = new MutableLiveData<>();

        fusedLocation = LocationServices.getFusedLocationProviderClient(application);

        fusedLocation.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    //get the current location
                    Location location = task.getResult();
                    if (location != null) {
                        //get the longitude and latitude
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        latLongMutableLiveData.setValue(latLng);
                    }
                } else {
                    //location is not enabled so we have to ask user to enable it
                    requestCurrentLocationUpdate();
                }
            }
        });
    }

    public void requestCurrentLocationUpdate() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location mLastLocation = locationResult.getLastLocation();
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                latLongMutableLiveData.setValue(latLng);
            }
        };
        fusedLocation.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    public LiveData<LatLng> getLatLongMutableLiveData() {
        return latLongMutableLiveData;
    }
}
