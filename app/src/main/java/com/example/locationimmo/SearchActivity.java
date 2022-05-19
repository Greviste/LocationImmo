package com.example.locationimmo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements RecyclerInterface{
    //Load toutes les activitées de la bd
    ArrayList<RentalAd> rental_ads_list;
    User user;
    DatabaseAccessService service;
    RecyclerAdapter adapter;

    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder b) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DatabaseAccessService.LocalBinder binder = (DatabaseAccessService.LocalBinder) b;
            service = binder.getService();

            user = service.getUserByMailAndPassword(getIntent().getStringExtra("connectedMail"), getIntent().getStringExtra("connectedPassword"));

            //Display en fonction du type de l'utilisateur
            FloatingActionButton floating_add_btn = findViewById(R.id.ad_add_btn);
            FloatingActionButton floating_search_btn = findViewById(R.id.user_search_btn);

            if(user == null){
                floating_search_btn.hide();
                floating_add_btn.hide();
            }else{
                if(user.type.ordinal() == 0)
                    floating_add_btn.hide();
            }

            floating_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), EditRentalAdActivity.class);
                    intent.putExtra("connectedMail", user.email);
                    intent.putExtra("connectedPassword", user.password);
                    startActivity(intent);
                }
            });


            floating_search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showMenu(view, R.menu.popup_menu);
                }
            });

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            fetch_ads();

            adapter.updateData(rental_ads_list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            service = null;
        }
    };

    public void fetch_ads(){
        if(rental_ads_list == null)
            rental_ads_list = new ArrayList<RentalAd>();

        RentalAd[] ads = service.getAllRentalAds();
        for(RentalAd ad : ads){
            if(!rental_ads_list.contains(ad)){
                rental_ads_list.add(ad);
            }

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
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
/*
        fetch_ads();
*/

        adapter = new RecyclerAdapter(this);
        /*recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, DatabaseAccessService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        boolean b = service != null;
        System.out.println("SEARCH STARTING, SERVICE? " + b );
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        service = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("RESUME SEARCH ACTIVITY?");


    }

    @Override
    public void onItemClick(int pos) {
        RentalAd clicked_ad = rental_ads_list.get(pos);

        Intent intent = new Intent(this, ViewRentalAdActivity.class);
        intent.putExtra("title", clicked_ad.title);
        intent.putExtra("city", clicked_ad.address);
        intent.putExtra("price", clicked_ad.price.toString());
        intent.putExtra("description", clicked_ad.description);

        //client -> possibilité de save l'annonce
        if(user.type.ordinal() == 0){
            intent.putExtra("connectedMail", user.email);
            intent.putExtra("connectedPassword", user.password);
        }
        startActivity(intent);
    }
}