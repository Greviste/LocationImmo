package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class EditRentalAdActivity extends AppCompatActivity {
    String[] classification_items = {"Appartement", "Studio", "Maison", "Loft"};
    AutoCompleteTextView classification_drop_down;
    ArrayAdapter<String> adapter;

    ImageButton select_range_btn;
    TextView availability_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rental_ad);

        //Drop-down menu
        adapter = new ArrayAdapter<String>(this, R.layout.ad_specs_item, classification_items);
        classification_drop_down = findViewById(R.id.classif_dd);
        classification_drop_down.setAdapter(adapter);

        //Range date availability
        availability_tv = findViewById(R.id.availability_tv);
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
    }
}