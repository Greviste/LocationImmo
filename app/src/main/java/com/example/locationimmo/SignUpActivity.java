package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
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
    JSONObject db = new JSONObject();

    EditText mail;
    EditText pwd_1;
    EditText pwd_2;
    RadioButton[] type;


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

        try {
            db.put("user","");
            db.put("ad","");;
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        try {
            System.out.println("Resetting db ...");
            FileOutputStream out_stream = getApplicationContext().openFileOutput("database.json", Context.MODE_PRIVATE);
            out_stream.write("".getBytes());
            out_stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateJson(View view) {
        HashMap<String, String> data = getFields();

        FileInputStream in_stream = null;

        //Read DB
        try {
            in_stream = getApplicationContext().openFileInput("database.json");
            String file_content = "";
            int current_c;
            while((current_c = in_stream.read()) != -1){
                file_content += (char)current_c;
            }
            in_stream.close();
            JSONObject json_fc = new JSONObject(file_content);
            db.put("user", json_fc.get("user"));
            System.out.println("DB state before update: " + db.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Update DB
        try {

            db.accumulate("user", new JSONObject(data));
            System.out.println("DB after update? " + db.toString());
            FileOutputStream out_stream = getApplicationContext().openFileOutput("database.json", Context.MODE_PRIVATE);
            out_stream.write(db.toString().getBytes());
            out_stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}