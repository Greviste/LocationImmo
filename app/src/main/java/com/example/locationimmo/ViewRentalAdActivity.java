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
    DatabaseAccessService service;
    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder b) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DatabaseAccessService.LocalBinder binder = (DatabaseAccessService.LocalBinder) b;
            service = binder.getService();
            user = service.getUserByMailAndPassword(getIntent().getStringExtra("connectedMail"), getIntent().getStringExtra("connectedPassword"));


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

        ArrayList<String> data = new ArrayList<>();
        data.add(getIntent().getStringExtra("title"));
        data.add(getIntent().getStringExtra("city"));
        data.add(getIntent().getStringExtra("description"));
        data.add(getIntent().getStringExtra("price"));


        for(int i = 0; i < tv_arr.size(); i ++){
            tv_arr.get(i).setText(data.get(i));
        }

        Button save_btn = findViewById(R.id.buttonSave);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add rentalAd to user, user to rental owner
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