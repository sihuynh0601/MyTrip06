package com.ptit.mytrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tien Si Huynh on 3/10/2018.
 */

public class MyPlacesFragment extends Fragment {

    public MyPlacesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myPlacesView = inflater.inflate(R.layout.fragment_passed_places, container, false);

        return myPlacesView;
    }
}

