package com.example.greenspot;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText stud_id, e_mail, pw, c_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        stud_id = (EditText) findViewById(R.id.stu_id);
        e_mail = (EditText) findViewById(R.id.email);
        pw = (EditText) findViewById(R.id.password);
        c_password = (EditText) findViewById(R.id.c_password);
    }

    public void checkInputFieldIfValidSendEmail(View view) {
        String email = e_mail.getText().toString();
        String password = pw.getText().toString();
        String studid = stud_id.getText().toString();
        String cpassword = c_password.getText().toString();
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
    }

    public void changeToSignInPage(View view) {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

}