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

import com.example.loginpage.Model.Users;
import com.example.loginpage.Model.Wholesaler;
import com.example.loginpage.retailerwholesaler.Wholesaler_sell;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Wholesaler_LoginActivity extends AppCompatActivity
{
    private Button LoginWholesalerButton;
    private EditText InputWholesalerUsername,InputWholesalerPassword;
    private ProgressDialog loadingBar;

    private String parentDbName = "Wholesaler";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler__login);

        LoginWholesalerButton= findViewById(R.id.wholesaler_login_button);
        InputWholesalerPassword= findViewById(R.id.wholesaler_login_password_input);
        InputWholesalerUsername= findViewById(R.id.wholesaler_login_username_input);
        loadingBar= new ProgressDialog(this);

        LoginWholesalerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginWholesaler();
            }
        });
    }

    private void LoginWholesaler()
    {
        String username = InputWholesalerUsername.getText().toString();
        String password = InputWholesalerPassword.getText().toString();

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
                Wholesaler usersData= snapshot.child(parentDbName).child(username).getValue(Wholesaler.class);
                if(snapshot.child(parentDbName).child(username).exists())
                {
                    if(usersData.getUsername().equals(username))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(Wholesaler_LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(Wholesaler_LoginActivity.this, Wholesaler_sell.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(Wholesaler_LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }
                else
                {
                    Toast.makeText(Wholesaler_LoginActivity.this, "Account with this username does not exits.", Toast.LENGTH_SHORT).show();
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