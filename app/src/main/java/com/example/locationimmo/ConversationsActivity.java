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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ConversationsActivity extends AppCompatActivity {
    DatabaseAccessService service;
    User user;
    LinearLayout users_wrapper;
    ArrayList<Button> conversations = new ArrayList<>();
    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder b) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DatabaseAccessService.LocalBinder binder = (DatabaseAccessService.LocalBinder) b;
            service = binder.getService();
            user = service.getUserByMailAndPassword(getIntent().getStringExtra("us_mail"), getIntent().getStringExtra("us_password"));
            users_wrapper.removeAllViews();

            System.out.println("CONVERSATIONS OF: " + user.email);

            User[] talking_to = service.getOpenConversationsFor(user);

            if(talking_to.length == 0){
                System.out.println("NO CONVERSIONS FOUND");
                TextView tv_warning = new TextView(getApplicationContext());
                tv_warning.setText("PAS DE CONVERSATIONS");
                tv_warning.setTextSize(24);
                users_wrapper.addView(tv_warning);
            }else{
                for(User u : talking_to){
                    System.out.println("USER: " + u.email +" ISA TALKING TO US");
                    if(!u.email.equals(user.email)){
                        Button clickable_user = new Button(getApplicationContext());
                        clickable_user.setText(u.email);
                        conversations.add(clickable_user);
                        users_wrapper.addView(clickable_user);
                    }

                }

                for(Button clickable_u : conversations){
                    String them_mail = clickable_u.getText().toString();
                    clickable_u.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            //Charger la conversation avec l'user
                            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                            intent.putExtra("us_mail", user.email);
                            intent.putExtra("us_password", user.password);
                            intent.putExtra("them_mail", them_mail);

                            startActivity(intent);
                        }
                    });
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            service = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        users_wrapper = findViewById(R.id.conversations_layout);
        users_wrapper.removeAllViews();
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