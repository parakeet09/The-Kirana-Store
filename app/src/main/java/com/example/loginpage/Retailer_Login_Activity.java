package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginpage.Model.Retailer;
import com.example.loginpage.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Retailer_Login_Activity extends AppCompatActivity
{
    private Button LoginRetailerButton;
    private EditText InputRetailerUsername,InputRetailerPassword;
    private ProgressDialog loadingBar;

    private String parentDbName = "Retailer";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer__login_);

        LoginRetailerButton= findViewById(R.id.retailer_login_button);
        InputRetailerPassword= findViewById(R.id.retailer_login_password_input);
        InputRetailerUsername= findViewById(R.id.retailer_login_username_input);
        loadingBar= new ProgressDialog(this);


        LoginRetailerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginRetailer();
            }
        });
    }
    private void LoginRetailer()
    {
        String username = InputRetailerUsername.getText().toString();
        String password = InputRetailerPassword.getText().toString();

        if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle(("Login Account"));
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(username,password);
        }
    }



    private void AllowAccessToAccount(String username, String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Retailer usersData= snapshot.child(parentDbName).child(username).getValue(Retailer.class);
                if(snapshot.child(parentDbName).child(username).exists())
                {
                    if(usersData.getUsername().equals(username))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(Retailer_Login_Activity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(Retailer_Login_Activity.this, RetailerHomeActivity.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(Retailer_Login_Activity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }
                else
                {
                    Toast.makeText(Retailer_Login_Activity.this, "Account with this username does not exits.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}