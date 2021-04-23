package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginpage.Model.Cart;
import com.example.loginpage.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Adminuserproductsactivity extends AppCompatActivity {

    private RecyclerView productlist;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartlistref;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminuserproductsactivity);

        userID = getIntent().getStringExtra("uid");

        productlist = findViewById(R.id.products_list);
        productlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productlist.setLayoutManager(layoutManager);

        cartlistref = FirebaseDatabase.getInstance().getReference()
                .child("cart list").child("Admin view").child(userID).child("products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options =
                new  FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartlistref,Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText("Quantity =" + model.getQuantity());
                holder.txtProductPrice.setText("Price ="+ model.getPrice()+ "Rs");
                holder.txtProductName.setText(model.getPname());


            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder= new CartViewHolder(view);
                return holder;

            }
        };
        productlist.setAdapter(adapter);
        adapter.startListening();
    }
}
