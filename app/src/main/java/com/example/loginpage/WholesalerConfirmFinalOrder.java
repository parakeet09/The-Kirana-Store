package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginpage.retailerwholesaler.HomeRetailerActivity;

public class WholesalerConfirmFinalOrder extends AppCompatActivity
{
    Button Confirmfinalorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler_confirm_final_order);

        Confirmfinalorder= findViewById(R.id.confirm_final_order_btn_wholesaler);

        Confirmfinalorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(WholesalerConfirmFinalOrder.this, "Your order has been received.", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(WholesalerConfirmFinalOrder.this, HomeRetailerActivity.class);
                startActivity(intent);

            }
        });
    }
}