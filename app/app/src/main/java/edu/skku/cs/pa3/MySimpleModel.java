package edu.skku.cs.pa3;

import static java.lang.System.exit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.util.FusedLocationSource;

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
    private AWSmodel awsModel = new AWSmodel();
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


    private class AWSmodel{
        private int success = -1;
        public int getSuccess(){
            return success;
        }
        private void awsLogin(String id, String pw){
            OkHttpClient client = new OkHttpClient();
            DataModel dataModel = new DataModel();


            Gson gson = new Gson();
            dataModel.setName(id);
            dataModel.setPasswd(pw);

            String json = gson.toJson(dataModel, DataModel.class);

            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://zbwb2v5pz0.execute-api.ap-northeast-2.amazonaws.com/dev/login").newBuilder();

            String url = urlBuilder.build().toString();

            Request req = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"),json))
                    .build();


            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Gson gson = new GsonBuilder().create();
                    final DataModel data1 = gson.fromJson(myResponse,  DataModel.class);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(data1.getSuccess()){
                                success = 1;
                            }
                            else{
                                success = 0;
                            }
                        }
                    });

                }

            });

        }


    }
}