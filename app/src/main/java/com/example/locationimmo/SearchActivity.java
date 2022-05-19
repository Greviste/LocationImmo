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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

            user = service.getUserByMailAndPassword(getIntent().getStringExtra("connectedMail"), getIntent().getStringExtra("connectedPassword"));

            //Display en fonction du type de l'utilisateur
            FloatingActionButton floating_btn = findViewById(R.id.user_search_btn);

            if(user == null){
                floating_btn.hide();
            }


            floating_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showMenu(view, R.menu.popup_menu);
                }
            });

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

    private void showMenu(View v, int menu){
        PopupMenu popup = (PopupMenu) new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        if(user.type.ordinal() == 0){
            //client (acheteur) -> annonces sauvegardées
            popup.getMenu().removeItem(R.id.show_posted);
        }else{
            //annonces déposées
            popup.getMenu().removeItem(R.id.show_fav);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //Si click sur un item != R.id.show_all -> afficher la liste des ads de l'utilisateur
                System.out.println("CLICKED ON " + menuItem.toString());
                return false;
            }
        });


        popup.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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