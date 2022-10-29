package edu.skku.cs.mapreview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySimpleModel implements MySimpleContract.ContractForModel {
    private int value;
    public Boolean success;
    private MySimplePresenter presenter;
    private Activity activity;
    private LocationManager lm;


    public MySimpleModel(MySimpleContract.ContractForPresenter presenter, Activity activity) {
        this.presenter = (MySimplePresenter) presenter;
        this.activity = activity;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    public Boolean getSuccess() {
        return success;
    }

    @Override
    public Activity getActivity() {
        return this.activity;
    }

    @Override
    public double getNearest(double lat1, double lon1, double lat2, double lon2){
        return distance(lat1,lon1,lat2,lon2);
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))* Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        return dist;
    }

    private static double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }
    private static double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }

    @Override
    public double[] doCurrent() {
        double[] lo = new double[2];
        lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return lo;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        lo[0] = lat;
        lo[1] = lon;
        return lo;
    }
    @Override
    public void doLogin(String id, String pw, Activity activity) {
    }



    @Override
    public void doSign() {
    }



}