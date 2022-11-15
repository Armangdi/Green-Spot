package com.example.greenspot;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.map.overlay.Marker;

public class RegisterActivity extends AppCompatActivity implements MySimpleContract.ContractForView{
    private View decorView;
    private int	uiOption;
    private MySimplePresenter presenter;
    private Button btn_signup;
    private EditText edit_id, edit_pw;

    @Override
    public void setMarker(Marker marker, double lat, double lon, int resourceID) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);

        presenter = new MySimplePresenter(this, new MySimpleModel(presenter, RegisterActivity.this));
        init();

    }

    private void init(){
        btn_signup = findViewById(R.id.btn_sign);
        edit_id = findViewById(R.id.edit_id);
        edit_pw = findViewById(R.id.edit_pw);

        btn_signup.setOnClickListener(view -> {
            String id = edit_id.getText().toString();
            String pw = edit_pw.getText().toString();
            edit_id.setText(null);
            edit_pw.setText(null);
            presenter.onSignUpClicked(id, pw);
        });
    }

    @Override
    public void onLoginResult(Boolean result) {
        //btn.setText("Value: " + value); // update UI\
    }

    @Override
    public void displayValue(int value) {
        //btn.setText("Value: " + value); // update UI
    }
}
