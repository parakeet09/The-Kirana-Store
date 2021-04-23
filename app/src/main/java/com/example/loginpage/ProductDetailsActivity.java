package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.DragStartHelper;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginpage.Model.RetailerProducts;
import com.example.loginpage.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.internal.cache.DiskLruCache;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addToCart;
    private ImageView productImage;
    private EditText numberButton;
    private TextView productPrice, productDescription, productName, productQuantity;
    private String productID="", state="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID=getIntent().getStringExtra("pid");

        addToCart= (Button) findViewById(R.id.add_product_to_cart);
        numberButton = findViewById(R.id.product_quantity_required_details);
        productImage = findViewById(R.id.product_image_details);
        productPrice = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_description_details);
        productName = findViewById(R.id.product_name_details);
        productQuantity = findViewById(R.id.product_quantity_details);

        getProductDetails(productID);

        addToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this,"You can purchase more products once your order is shipped or confirmed.",Toast.LENGTH_LONG).show();
                }
                else
                    {
                        addingToCartList();
                    }
            }
        });
        

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();
    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd yyyy");
        saveCurrentDate= currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(callForDate.getTime());

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String , Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getText().toString());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getUsername())
                .child("Products").child(productID)
        .updateChildren(cartMap)
        .addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    cartListRef.child("Retailer View").child(Prevalent.currentOnlineUser.getUsername())
                            .child("Products").child(productID)
                            .updateChildren(cartMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Toast.makeText(ProductDetailsActivity.this,"Added to cart List.",Toast.LENGTH_SHORT).show();

                                    Intent intent= new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                    startActivity(intent);

                                }
                            });
                }

            }
        });

    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Retailer Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    RetailerProducts products =snapshot.getValue(RetailerProducts.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    productQuantity.setText(products.getQuantity());
                    Picasso.get().load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getUsername());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String shippingState= snapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        state= "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                       state="Order Placed";
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}