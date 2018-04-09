package com.ptit.mytrip;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toolbar;


/**
 * Created by LENOVO on 3/26/2018.
 */

public class Favorite extends AppCompatActivity {

    private Button likedButton;
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.favorite );

        final Toolbar toolbar = findViewById( R.id.toolbar );
        toolbar.setTitle( getResources().getString( R.string.app_name ) );
        final TabLayout tabLayout = findViewById(R.id.tablayout);
        TabItem tabTour = findViewById( R.id.tabtour );
        TabItem tabFood = findViewById( R.id.tabfood );
        TabItem tabResort = findViewById( R.id.tabresort );

        final ViewPager viewPager = findViewById( R.id.viewPager );

        PageAdapter pageAdapter = new PageAdapter( getSupportFragmentManager(),tabLayout.getTabCount() );

        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( tabLayout ) );
    }


}
