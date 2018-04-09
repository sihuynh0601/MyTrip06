package com.ptit.mytrip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    public final static String TAG = SplashScreenActivity.class.getSimpleName();

    FirebaseUser currentUser;

    BroadcastReceiver internetConnectitiveReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView txtWarning = findViewById(R.id.txtWarning);
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo  = cm.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                txtWarning.setText("");
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                handleResult(currentUser);
            }else{
                txtWarning.setText("Rất tiếc! Không có kết nối Internet.");
            }
        }
    };

    private void handleResult(final FirebaseUser currentUser) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentUser != null){
                    // Điều hướng đến main activity
                    Intent mainActivity = new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(mainActivity);
                }else{
                    // Điều hướng đến LoginActivity
                    Intent loginIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                }
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        Log.d(TAG, "onCreate");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetConnectitiveReceiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(internetConnectitiveReceiver);
    }
}


