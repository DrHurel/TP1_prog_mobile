package com.example.tp1;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tp1.home.GridAdaptor;
import com.example.tp1.home.AppIcon;

import com.example.tp1.contact_form.ContactApp;
import com.example.tp1.sncf_info.SncfApp;
import com.example.tp1.calendar.CalendarApp;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init_home_grid();

        init_permission();
    }

    private void init_permission() {
        String access_fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
        String access_coarse_location = Manifest.permission.ACCESS_COARSE_LOCATION;
        int READ_STORAGE_PERMISSION_REQUEST = 123;
        if (!EasyPermissions.hasPermissions(this, access_fine_location)) {
            EasyPermissions.requestPermissions(this, "Our App Requires a permission to access your storage", READ_STORAGE_PERMISSION_REQUEST
                    , access_fine_location);
        }

        if (!EasyPermissions.hasPermissions(this, access_coarse_location)) {
            EasyPermissions.requestPermissions(this, "Our App Requires a permission to access your storage", READ_STORAGE_PERMISSION_REQUEST
                    , access_coarse_location);
        }
    }

    private void init_home_grid() {
        setContentView(R.layout.home_page);
        GridView app_grid;
        ArrayList<AppIcon> birdList = new ArrayList<>();
        app_grid = (GridView) findViewById(R.id.grid);
        birdList.add(new AppIcon("Contact",R.drawable.contact));
        birdList.add(new AppIcon("SNCF Info",R.drawable.sncf_info));
        birdList.add(new AppIcon("Calendar",R.drawable.calendar));

        GridAdaptor myAdapter=new GridAdaptor(this,R.layout.app_icon_wrapper,birdList);
        app_grid.setAdapter(myAdapter);

        app_grid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                Intent intention;
                switch (position){
                    case 0 :
                        intention= new Intent(MainActivity.this, ContactApp.class);
                        startActivity(intention);
                        break;

                    case 1 :
                        intention= new Intent(MainActivity.this, SncfApp.class);
                        startActivity(intention);
                        break;

                    case 2 :
                        intention= new Intent(MainActivity.this, CalendarApp.class);
                        startActivity(intention);
                        break;
                }
            }
        });
    }
}