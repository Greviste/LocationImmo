package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class EditRentalAdActivity extends AppCompatActivity {
    String[] classification_items = {"Appartement", "Studio", "Maison", "Loft"};
    AutoCompleteTextView classification_drop_down;
    ArrayAdapter<String> adapter;
    User user;
    ImageButton select_range_btn;
    TextView availability_tv;
    TextInputLayout title_tv;
    TextInputLayout city_tv;
    TextInputLayout desc_tv;
    TextInputLayout price_tv;

    Button send_btn;

    //DB
    DatabaseAccessService service;
    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder b) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DatabaseAccessService.LocalBinder binder = (DatabaseAccessService.LocalBinder) b;
            service = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rental_ad);

        title_tv = findViewById(R.id.title_input);
        city_tv = findViewById(R.id.city_input);
        desc_tv= findViewById(R.id.description_input);
        price_tv= findViewById(R.id.price_input);



        //User informations
        user = (User) getIntent().getSerializableExtra("connected");

        //Drop-down menu
        adapter = new ArrayAdapter<String>(this, R.layout.ad_specs_item, classification_items);
        classification_drop_down = findViewById(R.id.classif_dd);
        classification_drop_down.setAdapter(adapter);

        //Range date availability
        availability_tv = (TextView) findViewById(R.id.availability_tv);
        select_range_btn = findViewById(R.id.select_date_btn);
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds())).build();

        select_range_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "Material_Range");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        availability_tv.setText(datePicker.getHeaderText());
                        System.out.println(datePicker.getHeaderText());

                    }
                });
            }
        });


        //Send modifications / add an ad (button)
        send_btn = findViewById(R.id.send_ad_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add ad to user, user to ad -> switch view to user's ads list.
                String city_str = city_tv.getEditText().getText().toString();
                String avail_str = availability_tv.getText().toString();
                String title_str = title_tv.getEditText().getText().toString();
                String specs_str = classification_drop_down.getText().toString();
                Float price = Float.parseFloat(price_tv.getEditText().getText().toString());
                RentalAd new_ad = new RentalAd();
                new_ad.address = city_str;
                new_ad.availability = avail_str;
                new_ad.title = title_str;
                new_ad.specs = specs_str;
                new_ad.price = price;
                new_ad.owner = user;

                if(user.ads == null){
                    ArrayList<RentalAd> new_ads = new ArrayList<>();
                    new_ads.add(new_ad);
                    user.ads = new_ads;
                }else{
                    user.ads.add(new_ad);
                }

                System.out.println("USER MAIL " + user.email);
                System.out.println(city_str);
                System.out.println(avail_str);
                System.out.println(title_str);
                System.out.println(specs_str);
                System.out.println(price);

       /*         Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("connected", user);
                startActivity(intent);*/
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