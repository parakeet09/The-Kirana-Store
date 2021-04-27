package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity
{
    private EditText edit1, edit2;
    private Button sendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        edit1= (EditText) findViewById(R.id.edit1);
        edit2= (EditText) findViewById(R.id.edit2);
        sendFeedback= (Button) findViewById(R.id.give_feedback_btn);

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("message/html");
                intent.putExtra(Intent.EXTRA_EMAIL,new String("prateek4599@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from Customer");
                intent.putExtra(Intent.EXTRA_TEXT, "Product Name: "+edit1.getText()+"\n Feedback: "+edit2.getText());
                try
                {
                    startActivity(intent.createChooser(intent, "Please Select Email"));
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(FeedbackActivity.this, "There are no Email Client", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}