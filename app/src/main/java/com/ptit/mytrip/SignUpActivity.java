package com.ptit.mytrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tien Si Huynh on 3/10/2018.
 */

public class SignUpActivity extends AppCompatActivity {

    private static String TAG = SignUpActivity.class.getSimpleName();

    private EditText edtFullName, edtEmail,edtPassword,edtConfirmPassword;
    private CheckBox chkTermsAndConditions;
    private Button btnSignUp;
    private TextView txtSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        mAuth = FirebaseAuth.getInstance();

        addControls();
        addEvents();
    }

    private void addControls() {
        edtFullName = findViewById(R.id.fullName);
        edtEmail = findViewById(R.id.userEmailId);
        edtPassword = findViewById(R.id.password);
        edtConfirmPassword = findViewById(R.id.confirmPassword);
        chkTermsAndConditions = findViewById(R.id.terms_conditions);
        btnSignUp = findViewById(R.id.signUpBtn);
        txtSignIn = findViewById(R.id.already_user);
    }

    private void addEvents() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hoTen = edtFullName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirm_password = edtConfirmPassword.getText().toString().trim();

                if (hoTen.isEmpty()){
                    Log.d(TAG, "Họ tên bị bỏ trống !");
                    Toast.makeText(SignUpActivity.this,"Họ tên bị bỏ trống !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty()){
                    Log.d(TAG, "Email tên bị bỏ trống !");
                    Toast.makeText(SignUpActivity.this,"Email bị bỏ trống !", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Pattern p = Pattern.compile(Constant.regEx);
                    Matcher m = p.matcher(email);
                    if (!m.find()){
                        Log.d(TAG , "Email INVALIDED");
                        Toast.makeText(SignUpActivity.this,
                                "Email chứa ký tự không hợp lệ",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(password.isEmpty()){
                    Log.d(TAG, "Password bị bỏ trống !");
                    Toast.makeText(SignUpActivity.this,"Password bị bỏ trống !",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(password.length() < 6){
                        Log.d(TAG, "Password ít hơn 6 ký tự !");
                        Toast.makeText(SignUpActivity.this,"Password ít hơn 6 ký tự !",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        if(!password.equals(confirm_password)){
                            Log.d(TAG, "Xác nhận password không khớp!");
                            Toast.makeText(SignUpActivity.this,"Xác nhận password không khớp !",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                if(!chkTermsAndConditions.isChecked()){
                    Log.d(TAG, "Chưa đồng ý với điều khoản");
                    Toast.makeText(SignUpActivity.this,"Bạn chưa đồng ý với điều khoản của chúng tôi !",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                dangKy(hoTen,email,password);
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLoginActivity();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gotoLoginActivity();
    }

    private void dangKy(final String hoTen, final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(this,"Please wait","Loading ...",true,false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Authentication successful.");
                            updateDisplayName(hoTen);
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Đăng ký tài khoản thành công !.",
                                    Toast.LENGTH_SHORT).show();
                            Intent mainActivityIntent = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(mainActivityIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "Authentication failed.");
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Đăng ký tài khoản thất bại !.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateDisplayName(String displayName) {
        FirebaseUser currentUser= mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName).build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User display name updated.");
                        }
                    }
                });
    }

    public void gotoLoginActivity(){
        Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }
}

