package edu.skku.cs.mapreview;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;

public class AddWriteActivity extends AppCompatActivity implements MySimpleContract.ContractForView {

    private ImageButton btn_back;
    private MySimplePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewadd);

        presenter = new MySimplePresenter(this, new MySimpleModel(presenter, AddWriteActivity.this));

        btn_back = findViewById(R.id.imageButton3);
        btn_back.setOnClickListener(view -> {
            presenter.onBackClicked();
        });
    }

    @Override
    public void setMarker(Marker marker, double lat, double lon, int resourceID) {

    }

    @Override
    public void onLoginResult(Boolean result) {

    }

    @Override
    public void displayValue(int value) {

    }
}
