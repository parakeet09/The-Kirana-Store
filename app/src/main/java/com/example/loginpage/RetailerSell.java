package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RetailerSell extends AppCompatActivity
{
    private ImageView fruits, vegetables;
    private ImageView dairy_products, grains;

    private Button LogoutBtn, CheckOrderBtn, maintainProductsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_sell);

        LogoutBtn= (Button) findViewById(R.id.retailer_logout_btn);
        CheckOrderBtn= (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn= (Button) findViewById(R.id.maintain_btn);


        maintainProductsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent =new Intent(RetailerSell.this, HomeActivity.class);
                intent.putExtra("Retailer", "Retailer");
                startActivity(intent);


            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent =new Intent(RetailerSell.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        CheckOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent =new Intent(RetailerSell.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });


        fruits = (ImageView) findViewById(R.id.fruits);
        vegetables = (ImageView) findViewById(R.id.vegetables);
        dairy_products = (ImageView) findViewById(R.id.dairy_products);
        grains = (ImageView) findViewById(R.id.grains_and_beans);

        fruits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(RetailerSell.this,RetailerAddNewProductActivity.class);
                intent.putExtra("category","fruits");
                startActivity(intent);
            }
        });

        vegetables.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(RetailerSell.this,RetailerAddNewProductActivity.class);
                intent.putExtra("category","vegetables");
                startActivity(intent);
            }
        });
        dairy_products.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(RetailerSell.this,RetailerAddNewProductActivity.class);
                intent.putExtra("category","dairy_products");
                startActivity(intent);
            }
        });
        grains.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(RetailerSell.this,RetailerAddNewProductActivity.class);
                intent.putExtra("category","grains");
                startActivity(intent);
            }
        });

    }
}