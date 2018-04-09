package com.ptit.mytrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

/**
 * Created by Tien Si Huynh on 3/10/2018.
 */

public class LoginActivity extends AppCompatActivity {

    public final static String TAG = LoginActivity.class.getSimpleName();
    private final static int RC_SIGN_IN = 100;

    private EditText edtEmail, edtPassword;
    private Button btnDangNhap;
    private CheckBox chkHienPassword;
    private TextView txtQuenPassword,txtDangky;
    private LoginButton loginButton;
    private SignInButton signInButton;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
//    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Log.d(TAG, "onCreate");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
        //mData = FirebaseDatabase.getInstance().getReference(Constant.USERS);
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        addControls();
        addEvents();

    }

    private void addControls() {
        edtEmail = findViewById(R.id.login_emailid);
        edtPassword = findViewById(R.id.login_password);
        btnDangNhap = findViewById(R.id.loginBtn);
        chkHienPassword = findViewById(R.id.show_hide_password);
        txtDangky = findViewById(R.id.createAccount);
        txtQuenPassword = findViewById(R.id.forgot_password);
        loginButton = findViewById(R.id.btnLoginFacebook);
        loginButton.setReadPermissions("email", "public_profile");
        signInButton = findViewById(R.id.btnLoginGoogle);
        signInButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_LIGHT);
    }

    private void addEvents() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra dữ liệu được nhập.
                if(edtEmail.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Chưa nhập vào Email",Toast.LENGTH_LONG).show();
                    return;
                }
                if (edtPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Chưa nhập vào Password",Toast.LENGTH_LONG).show();
                    return;
                }
                // Đăng nhập bằng tài khoản MyTrip.
                signInFirebaseWithAnEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString());
            }
        });

        txtDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        });

        // Xử lý thay đổi sự kiện chọn hiện password
        chkHienPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    edtPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    edtPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        txtQuenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPasswordIntent = new Intent(LoginActivity.this,
                        ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        });

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnSignInWithGoogleAcccount: onClick");
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.i(TAG, "Google sign-in intent: onActivityResult");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void signInFirebaseWithAnEmailAndPassword(String email, String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(this,"Please wait","Loading ...",true,false);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithMyTripEmail:success");
                            progressDialog.dismiss();
                            //Điều hướng đến trang chủ
                            goToMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithMyTripEmail:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Đăng nhập không thành công",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        signInFirebaseWithCredential(credential);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            // Đăng nhập Firebase sau khi xác minh tài khoản thành công
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInGoogleResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this,"Đăng nhập Google thất bại",Toast.LENGTH_SHORT).show();
            //updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        signInFirebaseWithCredential(credential);
    }

    private void signInFirebaseWithCredential(AuthCredential credential){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(this,"Please wait","Loading ...",true,false);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithCredential:success");
                            progressDialog.dismiss();
                            goToMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainActivityIntent);
        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
        finish();
    }
}
