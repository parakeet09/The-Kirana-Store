package com.example.loginpage.retailerwholesaler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.loginpage.MainActivity;
import com.example.loginpage.R;
import com.example.loginpage.RetailerAddNewProductActivity;
import com.example.loginpage.RetailerSell;
import com.example.loginpage.WholesalerAddNewProductActivity;

public class Wholesaler_sell extends AppCompatActivity
{
    private Button LogoutBtn;
    private ImageView fruits;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler_sell);
        LogoutBtn= (Button) findViewById(R.id.retailer_logout_btn_wholesaler);

        fruits = (ImageView) findViewById(R.id.fruits_wholesaler);

        fruits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(Wholesaler_sell.this, WholesalerAddNewProductActivity.class);
                intent.putExtra("category","fruits");
                startActivity(intent);
            }
        });


        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent =new Intent(Wholesaler_sell.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
    }
}