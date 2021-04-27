package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.loginpage.retailerwholesaler.HomeRetailerActivity;

public class WholesalerProductDetails extends AppCompatActivity
{
    private Button AddtocartWholesaler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler_product_details);

        AddtocartWholesaler= findViewById(R.id.add_product_to_cart_wholesaler);

        AddtocartWholesaler.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(WholesalerProductDetails.this, WholesalerConfirmFinalOrder.class);
                startActivity(intent);

            }
        });
    }
}