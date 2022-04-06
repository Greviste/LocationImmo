package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}