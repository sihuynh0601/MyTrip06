package com.ptit.mytrip;

/**
 * Created by Tien Si Huynh on 3/10/2018.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    private final static String TAG = ForgotPasswordActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private EditText edtRegisteredEmailid;
    private TextView txtSend;
    private TextView txtBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();

        addControls();
        addEvents();

    }

    private void addControls() {
        edtRegisteredEmailid = findViewById(R.id.registered_emailid);
        txtBack = findViewById(R.id.txtBackToLogin);
        txtSend = findViewById(R.id.txtSend);
    }

    private void addEvents() {
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(ForgotPasswordActivity.this,
                        LoginActivity.class);
                startActivity(loginIntent);
                overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                finish();
            }
        });

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = edtRegisteredEmailid.getText().toString().trim();
                if(emailAddress.isEmpty()){
                    Log.d(TAG , "Email EMPTY");
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Email bị bỏ trống",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Pattern p = Pattern.compile(Constant.regEx);
                    Matcher m = p.matcher(emailAddress);
                    if (!m.find()){
                        Log.d(TAG , "Email INVALIDED");
                        Toast.makeText(ForgotPasswordActivity.this,
                                "Email chứa ký tự không hợp lệ",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show(ForgotPasswordActivity.this,"Please wait",
                        "Loading ...",true,false);
                mAuth.setLanguageCode("vi");
                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    progressDialog.dismiss();
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            "Đã gửi Email",Toast.LENGTH_SHORT).show();
                                    Intent loginIntent = new Intent(ForgotPasswordActivity.this,
                                            LoginActivity.class);
                                    overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                                    startActivity(loginIntent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginIntent = new Intent(ForgotPasswordActivity.this,
                LoginActivity.class);
        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
        startActivity(loginIntent);
        finish();
    }
}
