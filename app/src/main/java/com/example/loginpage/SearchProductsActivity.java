package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.loginpage.Model.RetailerProducts;
import com.example.loginpage.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity
{
    private Button SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;

    private String SearchInput;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);


        inputText= findViewById(R.id.search_product_name);
        SearchBtn= findViewById(R.id.search_btn);
        searchList= findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SearchInput= inputText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Retailer Products");

        FirebaseRecyclerOptions<RetailerProducts> options =
                new FirebaseRecyclerOptions.Builder<RetailerProducts>()
                        .setQuery(reference.orderByChild("pname").startAt(SearchInput).endAt(SearchInput), RetailerProducts.class)
                .build();

        FirebaseRecyclerAdapter<RetailerProducts, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<RetailerProducts, ProductViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull RetailerProducts model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = "+ model.getPrice()+ " Rs");

                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent= new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false );
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;

                    }
                };

        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}