package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText mail;
    EditText pwd;
    User user;
    DatabaseAccessService service;
    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder b) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DatabaseAccessService.LocalBinder binder = (DatabaseAccessService.LocalBinder) b;
            service = binder.getService();

            for(RentalAd ad : service.getAllRentalAds())
                System.out.println("AD IN BD: " + ad.title);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mail = findViewById(R.id.editTextTextEmailAddress);
        pwd = findViewById(R.id.editTextTextPassword);
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

    public void loadSearch(View view) {
       Intent intent = new Intent(this, SearchActivity.class);
       //Intent intent = new Intent(this, EditRentalAdActivity.class); //test

        startActivity(intent);
    }

    public void loadSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public boolean checkAuth() {
        String str_mail = mail.getText().toString();
        String str_pwd = pwd.getText().toString();

        user = service.getUserByMailAndPassword(str_mail, str_pwd);

        return user != null;
    }

    public void signIn(View view) {
        //authentification
        boolean granted = checkAuth();

        if(granted){
            System.out.println("ACCESS GRANTED");
            Intent intent = new Intent(this, EditRentalAdActivity.class);
            intent.putExtra("connected", user);
            startActivity(intent);
        }else{
            Toast tst = new Toast(this);
            tst.setText("CREATE AN ACCOUNT FIRST.");
            tst.show();
        }

    }
}