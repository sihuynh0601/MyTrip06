package com.ptit.mytrip;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Tien Si Huynh on 3/10/2018.
 */

public class ChangePasswordFragment extends Fragment {

    private final static String TAG = ChangePasswordFragment.class.getSimpleName();

    private TextView txtBack, txtSend;
    private EditText edtNewPassword;

    public ChangePasswordFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View changePasswordView = inflater.inflate(R.layout.fragment_change_password, container,false);
        txtBack = changePasswordView.findViewById(R.id.txtBack);
        txtSend = changePasswordView.findViewById(R.id.txtSend);
        edtNewPassword = changePasswordView.findViewById(R.id.edtNewPassword);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, settingsFragment).commit();
            }
        });

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = edtNewPassword.getText().toString().trim();

                if (password.isEmpty()) {
                    Log.d(TAG, "Password bị bỏ trống !");
                    Toast.makeText(getContext(), "Password bị bỏ trống !",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (password.length() < 6) {
                        Log.d(TAG, "Password ít hơn 6 ký tự !");
                        Toast.makeText(getContext(), "Password ít hơn 6 ký tự !",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show(getActivity(),"Please wait","Loading ...",true,false);
                user.updatePassword(password)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),
                                            "Đã cập nhật mật khẩu.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return changePasswordView;
    }

}

