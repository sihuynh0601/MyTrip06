package com.ptit.mytrip;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by LENOVO on 3/26/2018.
 */

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    PageAdapter(FragmentManager fm, int numOfTabs) {
        super( fm );
        this.numOfTabs = numOfTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TourFragment();
            case 1:
                return new FoodFragment();
            case 2:
                return new ResortFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}

