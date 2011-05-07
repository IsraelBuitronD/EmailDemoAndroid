package com.neoriddle.emaildemo;

import javax.mail.MessagingException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity implements OnClickListener {

    public static final String DEBUG_TAG = "EmailDemoAndroid";

    private Button sendButton;
    private TextView logText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sendButton = (Button)findViewById(R.id.startButton);
        sendButton.setOnClickListener(this);

        logText = (TextView)findViewById(R.id.logText);
    }

    @Override
    public void onClick(View v) {
        Mail mail = new Mail();

        mail.setSmtpHost("smtp.gmail.com"); // SMTP server
        mail.setPort("465");                // SMTP Port 465 or 587
        mail.setSPort("587");               // SocketFactory Port 465 or 587
        mail.setAuth(true);                 // SMTP authentication
        mail.setDebuggable(true);           // Debug mode

        mail.setUser("MYEMAIL");            // Username
        mail.setPasswd("MYPASS");           // Password

        mail.setSender("MYEMAIL");          // Email sender
        String[] recipients = new String[] {"ALL_RECIPIENTS"};
        mail.setRecipients(recipients);            // Email recipients
        mail.setSubject("EmailDemo testing mail"); // Email subject
        mail.setBody("EmailDemo testing mail");    // Email body

        try {
            mail.send();
            logText.setText("Mail sent successfully");
            logText.setBackgroundColor(Color.rgb(0x00, 0x66, 0x00));
        } catch (MessagingException e) {
            Log.e(DEBUG_TAG, e.getMessage(), e);

            logText.setText(e.getMessage());
            logText.setBackgroundColor(Color.rgb(0xAA, 0x00, 0x00));
        }
    }
}
