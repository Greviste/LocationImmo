package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewRentalAdActivity extends AppCompatActivity {
    User user;
    ArrayList<String> ad_data = new ArrayList<>();
    DatabaseAccessService service;
    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder b) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DatabaseAccessService.LocalBinder binder = (DatabaseAccessService.LocalBinder) b;
            service = binder.getService();
            user = service.getUserByMailAndPassword(getIntent().getStringExtra("connectedMail"), getIntent().getStringExtra("connectedPassword"));

            Button save_btn = findViewById(R.id.buttonSave);
            if(user == null){
                save_btn.setVisibility(View.GONE);
            }else if(user.type.ordinal() == 1 || user.type.ordinal() == 2){
                save_btn.setVisibility(View.GONE);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            service = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rental_ad);


        ArrayList<TextView> tv_arr = new ArrayList<TextView>();
        tv_arr.add(findViewById(R.id.textViewTitle));
        tv_arr.add(findViewById(R.id.textViewCity));
        tv_arr.add(findViewById(R.id.textViewDesc));
        tv_arr.add(findViewById(R.id.textViewPrice));


        ad_data.add(getIntent().getStringExtra("title"));
        ad_data.add(getIntent().getStringExtra("city"));
        ad_data.add(getIntent().getStringExtra("description"));
        ad_data.add(getIntent().getStringExtra("price"));


        for(int i = 0; i < tv_arr.size(); i ++){
            tv_arr.get(i).setText(ad_data.get(i));
        }

        Button save_btn = findViewById(R.id.buttonSave);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add rentalAd to user, user to rental owner

                RentalAd ad_to_save = service.selectRentalAds(
                        ad -> ad.title.equals(ad_data.get(0)) &&
                        ad.address.equals(ad_data.get(1)) &&
                        ad.description.equals(ad_data.get(2)) &&
                        ad.price == Float.parseFloat(ad_data.get(3))
                )[0];

                ad_to_save.owner = user;
                user.ads.add(ad_to_save);

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("connectedMail", user.email);
                intent.putExtra("connectedPassword", user.password);
                startActivity(intent);
            }
        });

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