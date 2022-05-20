package com.example.locationimmo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.util.Predicate;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseAccessService extends Service {
    public class LocalBinder extends Binder {
        DatabaseAccessService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DatabaseAccessService.this;
        }
    }
    private final IBinder binder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    private ArrayList<User> users;
    private ArrayList<ChatMessage> messages;
    private static class SaveData implements Serializable {
        User[] users;
        ChatMessage[] messages;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ObjectInputStream ois = new ObjectInputStream(openFileInput("database.dat"));
            SaveData saveData = (SaveData) ois.readObject();
            users = new ArrayList<>(Arrays.asList(saveData.users));
            messages = new ArrayList<>(Arrays.asList(saveData.messages));
            Toast.makeText(this, "Database loaded", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to load database, resetting to empty...", Toast.LENGTH_LONG).show();
            resetDB();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SaveData saveData = new SaveData();
        saveData.users = users.toArray(new User[0]);
        saveData.messages = messages.toArray(new ChatMessage[0]);
        try {
            ObjectOutputStream ous = new ObjectOutputStream(openFileOutput("database.dat", MODE_PRIVATE));
            ous.writeObject(saveData);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDB() {
        users = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public User getNewUser() {
        User user = new User();
        user.ads = new ArrayList<>();
        users.add(user);
        return user;
    }

    public User[] getAllUsers() {
        return users.toArray(new User[0]);
    }

    public User getUserByMailAndPassword(String email, String password) {
        for(User user : users) {
            if(user.email.equals(email) && user.password.equals(password)) return user;
        }
        return null;
    }

    public void postMessage(ChatMessage message) {
        messages.add(message);
    }

    public User[] getOpenConversationsFor(User user) {
        ArrayList<User> inConv = new ArrayList<>();
        for(ChatMessage message : messages) {
            User toAdd = null;
            if(message.from == user) toAdd = message.to;
            if(message.to == user) toAdd = message.from;
            if(toAdd != null && !inConv.contains(toAdd)) {
                inConv.add(toAdd);
            }
        }
        return inConv.toArray(new User[0]);
    }

    public ChatMessage[] getConversation(User a, User b) {
        ArrayList<ChatMessage> conv = new ArrayList<>();
        for(ChatMessage message : messages) {
            if(message.from != a && message.from != b || message.to != a && message.to != b) continue;
            conv.add(message);
        }
        return conv.toArray(new ChatMessage[0]);
    }

    public RentalAd[] selectRentalAds(Predicate<RentalAd> predicate) {
        ArrayList<RentalAd> ads = new ArrayList<>();
        for(User user : users) {
            if(user.type == UserType.Client) continue;
            for(RentalAd ad : user.ads) {
                if(predicate.test(ad)) ads.add(ad);
            }
        }
        return ads.toArray(new RentalAd[0]);
    }

    public RentalAd[] getAllRentalAds() {
        return selectRentalAds(ignored -> true);
    }
}