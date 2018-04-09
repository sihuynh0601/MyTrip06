package com.ptit.mytrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by LENOVO on 3/26/2018.
 */

public class ResortFragment extends Fragment {

    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
        setHasOptionsMenu( true );
        return inflater.inflate( R.layout.fragment_resort,container,false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate( R.menu.menu_resorts,menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.action_resort){
            Toast.makeText( getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT ).show();
        }
        return true;
    }
}
