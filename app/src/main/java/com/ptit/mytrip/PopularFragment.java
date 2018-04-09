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

public class PopularFragment extends Fragment {
    public PopularFragment() {

    }

    /*Inflates the fragment layout and sets any image resources */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout  for this fragment
        View popularView = inflater.inflate(R.layout.fragment_popuar, container, false);

        return popularView;


    }
}
