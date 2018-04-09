package com.ptit.mytrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by LENOVO on 4/6/2018.
 */

public class BackStackActivity extends AppCompatActivity{
    private Button popularButton , myPlaceButton , favoriteButton, settingButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate( savedInstanceState );

        setContentView(R.layout.activity_backstack );
        popularButton =(Button) findViewById( R.id.action_popular );
        popularButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View viewPopular) {
                addFragment( new PopularFragment() );
            }
        } );

        myPlaceButton = (Button) findViewById( R.id.action_my_places );
        myPlaceButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View viewMyPlace) {
                /*replaceFragmentContent( new  );*/
            }
        } );

        favoriteButton = (Button) findViewById( R.id.action_favorites );
        favoriteButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View viewFavorite) {
                addFragment( new FavoritesFragment() );
            }
        } );

        settingButton = (Button) findViewById( R.id.action_settings );
        settingButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View viewSetting) {
                addFragment( new SettingsFragment() );
            }
        } );
        initFragment();
    }

    private void initFragment(){
        FavoritesFragment firstFragment = new FavoritesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace( R.id.container_body,firstFragment );
        ft.commit();
    }

    protected void replaceFragmentContent(Fragment fragment){
        if(fragment != null){
            FragmentManager fmgr = getSupportFragmentManager();
            FragmentTransaction ft = fmgr.beginTransaction();
            ft.replace( R.id.container_body,fragment );
            ft.commit();
        }
    }

    protected void addFragment(Fragment fragment){
        FragmentManager fmgr = getSupportFragmentManager();
        FragmentTransaction ft = fmgr.beginTransaction();
        ft.add( R.id.container_body,fragment );
        ft.addToBackStack( fragment.getClass().getSimpleName() );
        ft.commit();
    }
}
