package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText mail;
    EditText pwd_1;
    EditText pwd_2;
    RadioButton[] type;
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
        setContentView(R.layout.activity_sign_up);
        mail = findViewById(R.id.editTextTextEmailAddressSU);
        pwd_1 = findViewById(R.id.editTextTextPasswordSU);
        pwd_2 = findViewById(R.id.editTextTextPasswordConfirm);
        type = new RadioButton[]{
                findViewById(R.id.radioButtonTenant),
                findViewById(R.id.radioButtonIndividual),
                findViewById(R.id.radioButtonProfessional),
        };
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

    public HashMap<String, String> getFields() {
        HashMap<String, String> fields = new HashMap<String, String>();
        fields.put("e-mail", mail.getText().toString());
        fields.put("pwd", pwd_1.getText().toString());
        for(RadioButton radio : type) {
            if(radio.isChecked()){
                fields.put("type", radio.getText().toString());
            }
        }

        for(Map.Entry<String, String> entry : fields.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return fields;
    }

    public void resetDB(View view){
        if(service == null) return;
        service.resetDB();
    }

    public void updateJson(View view) {
        if(service == null) return;
    }
}