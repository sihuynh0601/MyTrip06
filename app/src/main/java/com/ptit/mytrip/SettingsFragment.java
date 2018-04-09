package com.ptit.mytrip;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Tien Si Huynh on 3/10/2018.
 */

public class SettingsFragment extends Fragment {

    private TextView txtCurrentUserInfo, txtLogout, txtChangePassword;

    public SettingsFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        txtCurrentUserInfo = settingsView.findViewById(R.id.txtCurrentUserInfo);
        txtLogout = settingsView.findViewById(R.id.txtLogout);
        txtChangePassword = settingsView.findViewById(R.id.txtChangePassword);

        txtCurrentUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagerFragment fragment = new UserManagerFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent signInIntent = new Intent(getActivity(),LoginActivity.class);
                startActivity(signInIntent);
                getActivity().finish();
            }
        });

        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, changePasswordFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return settingsView;
    }

}
