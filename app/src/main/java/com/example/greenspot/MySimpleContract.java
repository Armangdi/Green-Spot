package com.example.greenspot;

import android.app.Activity;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;

public interface MySimpleContract {
    interface ContractForView{
        void setMarker(Marker marker, double lat, double lon, int resourceID);
        void onLoginResult(Boolean result);
        void displayValue(int value);
    }

    interface ContractForModel{
        int getValue();
        void doLogin(String id, String pw, Activity activity);
        void doSign();
        double[] doCurrent();
        Boolean getSuccess();
        Activity getActivity();
        double getNearest(double lat1, double lon1, double lat2, double lon2);

        //void addOne(OnValueChangedListener listener);
        interface OnValueChangedListener{
            void onChanged();
        }
    }

    interface ContractForPresenter{
        void setMarkers(NaverMap naverMap);
        void onCurrentClicked(NaverMap naverMap);
        void onListClicked(NaverMap naverMap);
        void onViewClicked(NaverMap naverMap);

        void onSentiClicked(String content);

        void onLoginClicked(String id, String pw);
        void onSignClicked();
        void onSignUpClicked(String id, String pw);

    }
}
