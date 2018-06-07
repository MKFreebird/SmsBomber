package com.example.smsbomber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumber;
    EditText smsMessage;
    EditText amount;
    EditText interval;
    Button btnSpam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumber = findViewById(R.id.phoneNumber);
        smsMessage = findViewById(R.id.smsMessage);
        amount = findViewById(R.id.amount);
        interval = findViewById(R.id.interval);

        btnSpam = findViewById(R.id.spambutton);
        btnSpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpamActivity();
            }
        });
    }


    protected void startSpamActivity() {
        String receiver = phoneNumber.getText().toString();
        String message = smsMessage.getText().toString();
        String smsAmount = amount.getText().toString();
        String smsInterval = interval.getText().toString();

        Intent myIntent = new Intent(MainActivity.this, SpamActivity.class);

        myIntent.putExtra("phonenr", receiver); //Optional parameters
        myIntent.putExtra("message", message);
        myIntent.putExtra("amount", "2");
        myIntent.putExtra("interval", "5");
        MainActivity.this.startActivity(myIntent);
    }
}
