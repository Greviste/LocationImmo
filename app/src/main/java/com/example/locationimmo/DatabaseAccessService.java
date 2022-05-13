package com.example.locationimmo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DatabaseAccessService extends Service {
    public class LocalBinder extends Binder {
        DatabaseAccessService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DatabaseAccessService.this;
        }
    }
    private final IBinder binder = new LocalBinder();
    private JSONObject db = new JSONObject();

    @Override
    public void onCreate()
    {
        super.onCreate();

        try {
            db.put("user","");
            db.put("ad","");;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void updateJson(HashMap<String, String> data) {
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

    public void resetDB(){
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
}