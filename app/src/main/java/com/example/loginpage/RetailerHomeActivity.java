package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RetailerHomeActivity extends AppCompatActivity
{
    private Button SellToCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_home);

        SellToCustomer= findViewById(R.id.sell_to_customer);

        SellToCustomer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RetailerHomeActivity.this, RetailerSell.class);
                startActivity(intent);
            }
        });
    }
}