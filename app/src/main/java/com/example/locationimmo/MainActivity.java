package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    JSONObject db = new JSONObject();
    EditText mail;
    EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mail = findViewById(R.id.editTextTextEmailAddress);
        pwd = findViewById(R.id.editTextTextPassword);
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

    public boolean checkAuth(EditText mail, EditText pwd) {
        String mail_str = mail.getText().toString();
        String pwd_str = pwd.getText().toString();
        boolean grant_access = false;

        System.out.println(db.names());

        try {
            JSONArray usr_arr = db.getJSONArray("user");
            for(int i = 1; i < usr_arr.length(); i ++){
                JSONObject usr = usr_arr.getJSONObject(i);

                if(usr.get("e-mail").equals(mail_str) && usr.get("pwd").equals(pwd_str))
                    grant_access = true;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return grant_access;
    }

    public void signIn(View view) {
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
            db = new JSONObject(file_content);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("DB LOADED: " + db.toString());
        //authentification
        boolean granted = checkAuth(mail, pwd);

        if(granted){
            System.out.println("ACCESS GRANTED");
            Intent intent = new Intent(this, EditRentalAdActivity.class);
            startActivity(intent);
        }else{
            Toast tst = new Toast(this);
            tst.setText("CREATE AN ACCOUNT FIRST.");
            tst.show();
        }

    }
}