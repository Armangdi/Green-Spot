package edu.skku.cs.pa3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.naver.maps.map.overlay.Marker;

public class LoginActivity extends AppCompatActivity
        implements MySimpleContract.ContractForView{
    private View decorView;
    private int	uiOption;
    private MySimplePresenter presenter;
    private Button btn_login, btn_signup;
    private EditText edit_id, edit_pw;
    private ImageView imageView;
    @Override
    public void setMarker(Marker marker, double lat, double lon, int resourceID){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);

        presenter = new MySimplePresenter(this, new MySimpleModel(presenter, LoginActivity.this));
        init();
    }


    private void init(){
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_sign);
        edit_id = findViewById(R.id.edit_id);
        edit_pw = findViewById(R.id.edit_pw);
        imageView = findViewById(R.id.image_main_logo);
        imageView.setImageResource(R.drawable.logo3);

        btn_login.setOnClickListener(view -> {
            String id = edit_id.getText().toString();
            String pw = edit_pw.getText().toString();
            edit_id.setText(null);
            edit_pw.setText(null);
            presenter.onLoginClicked(id, pw);
        });
        btn_signup.setOnClickListener(view -> {
            edit_id.setText(null);
            edit_pw.setText(null);
            presenter.onSignClicked();});
    }

    @Override
    public void onLoginResult(Boolean result) {
        //btn.setText("Value: " + value); // update UI
        if(!result){
            Toast.makeText(this, "Yaho!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void displayValue(int value) {
        //btn.setText("Value: " + value); // update UI
    }
}