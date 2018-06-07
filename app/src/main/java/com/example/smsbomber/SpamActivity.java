package com.example.smsbomber;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.*;

public class SpamActivity extends AppCompatActivity {

    int messageCounter = 0;
    boolean cancelled = false;

    String receiverPhoneNumber;
    String textMessage;
    String amount;
    String interval;

    TextView labelReceiverPhoneNumber;
    TextView labelTextMessage;
    TextView labelAmount;
    TextView labelInterval;

    TextView labelMessageCounter;

    Future longRunningTaskFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam);

        labelReceiverPhoneNumber = findViewById(R.id.labelReceiver);
        labelTextMessage = findViewById(R.id.labelMessage);
        labelAmount = findViewById(R.id.labelAmount);
        labelInterval = findViewById(R.id.labelInterval);

        labelMessageCounter = findViewById(R.id.labelMessageCounter);

        final Button btnStop = findViewById(R.id.stopbutton);
        btnStop.setOnClickListener(new View.OnClickListener() {
            int clicks = 1;
            @Override
            public void onClick(View view) {
                longRunningTaskFuture.cancel(true);
                btnStop.setText(Integer.toString(clicks++) + " Clicks");
            }
        });

        Bundle extras = getIntent().getExtras();

        receiverPhoneNumber = extras.getString("phonenr");
        textMessage = extras.getString("message");
        amount = extras.getString("amount");
        interval = extras.getString("interval");

        labelReceiverPhoneNumber.setText(receiverPhoneNumber);
        labelTextMessage.setText(textMessage);
        labelAmount.setText(amount);
        labelInterval.setText(interval);
        labelMessageCounter.setText(String.valueOf(messageCounter));

        spam(receiverPhoneNumber, textMessage, amount, interval);

    }


    protected void spam(String phoneNumber, String message, String amount, String interval) {

        final int messageAmount = Integer.parseInt(amount);
        final int delay = Integer.parseInt(interval) * 1000;
        final String textMessage = message;
        final String phoneNr = phoneNumber;

        ExecutorService threadPoolExecutor = newSingleThreadExecutor();
        final Handler handler = new Handler();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                updateCounter();
                //checkAmount(messageAmount);
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(
                        phoneNr,
                        null,
                        textMessage,
                        null,
                        null);
                handler.postDelayed(this, delay);
            }
        };
        // submit task to threadpool:
        longRunningTaskFuture = threadPoolExecutor.submit(task);
        // At some point in the future, if you want to kill the task:

    }

    protected void checkAmount(int messageAmount) {
        if (messageAmount <= this.messageCounter) {
            longRunningTaskFuture.cancel(true);
        }
    }

    protected void updateCounter() {
        this.messageCounter += 1;
        labelMessageCounter.setText(String.valueOf(this.messageCounter));
    }
}
