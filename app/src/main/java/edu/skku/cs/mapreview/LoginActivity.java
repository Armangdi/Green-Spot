package edu.skku.cs.mapreview;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.naver.maps.map.overlay.Marker;

public class LoginActivity extends AppCompatActivity
        implements MySimpleContract.ContractForView{
    private View decorView;
    private int	uiOption;
    private MySimplePresenter presenter;
    private Button btn_login, btn_signup;
    private EditText edit_id, edit_pw;
    private ImageView imageView;
    private FirebaseAuth mAuth;
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
        imageView.setImageResource(R.drawable.ic_launcher_foreground);

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
        EditText e_mail = (EditText) findViewById(R.id.edit_id);
        EditText pw = (EditText) findViewById(R.id.edit_pw);
        String email = e_mail.getText().toString();
        String password = pw.getText().toString();
        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), "빈 항목이 없어야합니다", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void displayValue(int value) {
        //btn.setText("Value: " + value); // update UI
    }
}