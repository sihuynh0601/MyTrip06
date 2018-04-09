package com.ptit.mytrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LENOVO on 3/30/2018.
 */

public class LikedFragment extends Fragment {

    public LikedFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View likedView = inflater.inflate( R.layout.favorite, container , false);
        return likedView;
    }
}
