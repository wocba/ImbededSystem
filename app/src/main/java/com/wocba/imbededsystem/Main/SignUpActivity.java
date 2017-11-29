package com.wocba.imbededsystem.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wocba.imbededsystem.R;

/**
 * Created by hyuk2 on 2017. 11. 27..
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail, editTextPassword;
    ProgressBar progressbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewSignIn).setOnClickListener(this);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()) {
            editTextEmail.setError("이메일을 입력하세요");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("유효한 이메일을 입력하세요");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("패스워드를 입력하세요");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("6자 이상 입력해주세요");
            editTextPassword.requestFocus();
            return;
        }

        progressbar.setVisibility(View.GONE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다. 로그인창으로 이동합니다", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewSignIn:
                startActivity(new Intent(this, SignInActivity.class));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}
