package edu.skku.cs.pa3;

import static java.lang.Math.round;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySimplePresenter implements MySimpleContract.ContractForPresenter{

    public ArrayList<Double> lats = new ArrayList<Double>();
    public ArrayList<Double> lons = new ArrayList<Double>();
    public ArrayList<String> names = new ArrayList<String>();
    private MySimpleContract.ContractForView view;
    private MySimpleContract.ContractForModel model;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private double near_lat, near_lon;
    private int map_mode = 0;
    private int dark = 0;
    private ArrayList<Marker> markers = new ArrayList<Marker>();

    public MySimplePresenter(MySimpleContract.ContractForView view,
                             MySimpleContract.ContractForModel model){
        this.view = view;
        this.model = model;
    }


    @Override
    public void onCurrentClicked(NaverMap naverMap){
        double[] lo;

        lo = model.doCurrent();

        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(new LatLng(lo[0], lo[1]),15).animate(CameraAnimation.Fly, 2000);;
        naverMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onNearClicked(NaverMap naverMap){
        Log.i("j!",String.valueOf(lats.size()));
        for(int i =0; i<lats.size(); i++){
            Marker marker = new Marker();
            marker.setIconPerspectiveEnabled(true);
            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_baseline_place_24));
            marker.setAlpha(0.8f);
            marker.setPosition(new LatLng(lats.get(i), lons.get(i)));
            marker.setMap(naverMap);
            markers.add(marker);
        }

        double[] lo;

        lo = model.doCurrent();
        double meter = 100000;
        int min = 0;
        for(int i =0; i<lats.size(); i++) {
            double tmp = model.getNearest(lo[0], lo[1], lats.get(i), lons.get(i));
            if(meter > tmp){
                min = i;
                meter = tmp;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(model.getActivity());
        builder.setMessage("가장 가까운 응급센터는 " + "'" +
                names.get(min)+"("+String.valueOf(round(meter))+ "m)' 입니다.");
        near_lat = lats.get(min);
        near_lon = lons.get(min);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(new LatLng(near_lat,near_lon),15).animate(CameraAnimation.Fly, 2000);;
                naverMap.moveCamera(cameraUpdate);
            }
        });
        builder.create().show();
    }

    @Override
    public void setMarkers(NaverMap naverMap){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://openapi.gg.go.kr/EmgncyMedcareInstStus").newBuilder();
        urlBuilder.addQueryParameter("Key", "a016e5b035bb4692ba086980bec59911");
        urlBuilder.addQueryParameter("Type", "Json");
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder()
                .url(url)
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(myResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jArray = null;
                JSONObject jArray2 = null;
                JSONArray jArray3 = null;

                try {
                    jArray = jObject.getJSONArray("EmgncyMedcareInstStus");
                    jArray2 = jArray.getJSONObject(1);
                    jArray3 = jArray2.getJSONArray("row");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JSONArray jArray4 = jArray3;

                for (int i = 0; i < jArray4.length(); i++) {
                    JSONObject obj = null;
                    try {
                        obj = jArray4.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String lat="", lon="", Center_name="";
                    if(i==34 || i==39 || i==56) continue;
                    try {
                        Center_name = obj.getString("HOSPTL_CENTER_NM");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        lat = obj.getString("REFINE_WGS84_LAT");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        lon = obj.getString("REFINE_WGS84_LOGT");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Double d_lat = Double.parseDouble(lat);
                    Double d_lon = Double.parseDouble(lon);
                    lats.add(d_lat);
                    lons.add(d_lon);
                    names.add(Center_name);

                }

            }

        });
    }

    @Override
    public int onDarkClicked(NaverMap naverMap){
        naverMap.setMapType(NaverMap.MapType.Navi);
        map_mode = 1;
        if(dark == 0){
            naverMap.setNightModeEnabled(true);
            dark++;
        }
        else{
            naverMap.setNightModeEnabled(false);
            dark--;
        }
        return dark;
    }

    @Override
    public void onViewClicked(NaverMap naverMap){
        map_mode = (map_mode + 1) % 5;
        switch(map_mode){
            case 0:
                naverMap.setMapType(NaverMap.MapType.Basic);
                break;
            case 1:
                naverMap.setMapType(NaverMap.MapType.Navi);
                break;
            case 2:
                naverMap.setMapType(NaverMap.MapType.Satellite);
                break;
            case 3:
                naverMap.setMapType(NaverMap.MapType.Hybrid);
                break;
            case 4:
                naverMap.setMapType(NaverMap.MapType.Terrain);
                break;
        }
    }

    @Override
    public void onLoginClicked(String id, String pw) {
        OkHttpClient client = new OkHttpClient();
        DataModel dataModel = new DataModel();

        Gson gson = new Gson();
        dataModel.setName(id);
        dataModel.setPasswd(pw);

        String json = gson.toJson(dataModel, DataModel.class);
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://yrrb12azqe.execute-api.ap-northeast-2.amazonaws.com/dev/login").newBuilder();
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
                model.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(data1.getSuccess()){
                            Toast.makeText(model.getActivity(),"Login Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(model.getActivity(), MapActivity.class);
                            model.getActivity().startActivity(intent);
                        }
                        else{
                            Toast.makeText(model.getActivity(),"Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });


    }

    @Override
    public void onSignClicked(){
        Intent intent = new Intent(model.getActivity(), RegisterActivity.class);
        model.getActivity().startActivity(intent);
    }

    @Override
    public void onSignUpClicked(String id, String pw){
        OkHttpClient client = new OkHttpClient();
        DataModel dataModel = new DataModel();

        Gson gson = new Gson();
        dataModel.setName(id);
        dataModel.setPasswd(pw);

        String json = gson.toJson(dataModel, DataModel.class);
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://yrrb12azqe.execute-api.ap-northeast-2.amazonaws.com/dev/adduser").newBuilder();
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
                model.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(data1.getSuccess()){
                            Toast.makeText(model.getActivity(),"Register Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(model.getActivity(), LoginActivity.class);
                            model.getActivity().startActivity(intent);
                        }
                        else{
                            Toast.makeText(model.getActivity(),"Register Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });
    }

}
