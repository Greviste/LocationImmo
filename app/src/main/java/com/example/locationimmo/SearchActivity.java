package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {
    //Load toutes les activitées de la bd
    ArrayList<RentalAd> rental_ads_list = new ArrayList<>();
    User user;
    DatabaseAccessService service;
    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder b) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DatabaseAccessService.LocalBinder binder = (DatabaseAccessService.LocalBinder) b;
            service = binder.getService();
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            fetch_ads();

            System.out.println("TAILKLLE: " + rental_ads_list.size());

            RecyclerAdapter adapter = new RecyclerAdapter(rental_ads_list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            service = null;
        }
    };

    public void fetch_ads(){
        RentalAd[] ads = service.getAllRentalAds();
        for(RentalAd ad: ads){
            rental_ads_list.add(ad);
            System.out.println("RENTAL " + ad.title);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        user = (User) getIntent().getSerializableExtra("connected");
        if(user != null){
            System.out.println("USER " + user.email);
            for(RentalAd ad : user.ads)
                System.out.println(ad.title);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, DatabaseAccessService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        service = null;
    }


}