package com.example.loginpage.retailerwholesaler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.loginpage.R;
import com.example.loginpage.WholesalerProductDetails;

public class HomeRetailerActivity extends AppCompatActivity {
    private Button Apple;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_retailer);

        Apple= findViewById(R.id.product_image_wholesaler);
        Apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HomeRetailerActivity.this, WholesalerProductDetails.class);
                startActivity(intent);
            }
        });
    }
}