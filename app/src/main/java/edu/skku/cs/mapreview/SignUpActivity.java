package edu.skku.cs.mapreview;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.naver.maps.map.overlay.Marker;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements MySimpleContract.ContractForView{
    private View decorView;
    private int	uiOption;
    private MySimplePresenter presenter;
    private Button btn_signup, btn_login;
    private EditText edit_sid, edit_id, edit_pw, edit_cpw;
    private FirebaseAuth mAuth;

    @Override
    public void setMarker(Marker marker, double lat, double lon, int resourceID) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);

        presenter = new MySimplePresenter(this, new MySimpleModel(presenter, SignUpActivity.this));
        init();

        btn_login.setOnClickListener(view -> {
            presenter.onFirstLoginClicked();
        });
        btn_signup.setOnClickListener(view -> {

            String email = edit_id.getText().toString();
            String password = edit_pw.getText().toString();
            String studid = edit_sid.getText().toString();
            String cpassword = edit_cpw.getText().toString();
            if (email.length() == 0 || password.length() == 0|| studid.length() == 0|| cpassword.length() == 0) {
                Toast.makeText(getApplicationContext(), "빈 항목이 없어야합니다", Toast.LENGTH_LONG).show();
                return;
            }
            try
            {
                Integer.parseInt(studid);
                if (studid.length() != 10) {
                    Toast.makeText(getApplicationContext(), "잘못된 학번 양식", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (NumberFormatException ex)
            {
                Toast.makeText(getApplicationContext(), "잘못된 학번 양식", Toast.LENGTH_LONG).show();
                return;
            }
            if (password.length() < 4) {
                Toast.makeText(getApplicationContext(), "비밀번호는 4자리 이상이어야 합니다", Toast.LENGTH_LONG).show();
                return;
            }
            if (!password.equals(cpassword)) {
                Toast.makeText(getApplicationContext(), "비밀번호 인증 실패", Toast.LENGTH_LONG).show();
                return;
            }
            if (!Pattern.matches("^[\\w-\\.]+@g\\.skku\\.edu$", email)) {
                Toast.makeText(getApplicationContext(), "이메일 형식이 틀렸습니다", Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "이메일 전송! 인증해주세요", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "이미 사용되고 있는 이메일 입니다", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }

    private void init(){
        btn_signup = findViewById(R.id.sign_first);
        btn_login = findViewById(R.id.login_first);
        edit_sid = findViewById(R.id.stu_id);
        edit_id = findViewById(R.id.email);
        edit_pw = findViewById(R.id.password);
        edit_cpw = findViewById(R.id.c_password);

        btn_signup.setOnClickListener(view -> { // Sign Up process
            String id = edit_id.getText().toString();
            String pw = edit_pw.getText().toString();
            edit_id.setText(null);
            edit_pw.setText(null);
            presenter.onSignUpClicked(id, pw); // cpw and sid are not included
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
