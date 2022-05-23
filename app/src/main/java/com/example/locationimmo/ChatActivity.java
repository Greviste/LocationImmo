package com.example.locationimmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {
    User us, them;
    DatabaseAccessService service;
    LinearLayout history;
    EditText text_input;

    private void updateHistory(){
        history.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 10,  5, 10);

        for(ChatMessage msg : service.getConversation(us, them)){
            LinearLayout msg_item = new LinearLayout(this);
            System.out.println("MESSAGE " + msg.message);
            TextView msg_tv = new TextView(getApplicationContext());
            msg_tv.setText(msg.message);
            msg_tv.setLayoutParams(params);

            msg_item.addView(msg_tv);

            if(msg.from.equals(us)){
                //layout aligner le message à gauche
                msg_tv.setBackgroundColor(Color.WHITE);

                msg_item.setGravity(Gravity.LEFT);
            }else{
                //layout aligner le msg à droite
                msg_tv.setBackgroundColor(Color.LTGRAY);
                msg_item.setGravity(Gravity.RIGHT);

            }

            history.addView(msg_item);
        }
    }


    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder b) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DatabaseAccessService.LocalBinder binder = (DatabaseAccessService.LocalBinder) b;
            service = binder.getService();

            us = service.getUserByMailAndPassword(getIntent().getStringExtra("us_mail"), getIntent().getStringExtra("us_password"));
            them = service.getUserByMail(getIntent().getStringExtra("them_mail"));

            System.out.println("CONVERSATION OPEN: " + us.email + " (us) talking to " + them.email);
            history = findViewById(R.id.messageHistory);
            updateHistory();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            service = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        text_input = findViewById(R.id.editTextMessage);

        ImageButton send_btn = findViewById(R.id.imageButtonSend);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg_str = text_input.getText().toString();
                ChatMessage msg = new ChatMessage();
                msg.from = us;
                msg.to = them;
                msg.message = msg_str;

                service.postMessage(msg);

                updateHistory();
                text_input.setText("");
                text_input.clearFocus();
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