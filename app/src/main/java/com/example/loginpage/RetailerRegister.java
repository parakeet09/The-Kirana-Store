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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RetailerRegister extends AppCompatActivity
{
    private Button CreateRetailerAccountButton;
    private EditText RetailerInputName,RetailerInputPhoneNumber,RetailerInputPassword,RetailerConfirmPassword,RetailerInputUsername;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_register);

        CreateRetailerAccountButton= findViewById(R.id.retailer_register_button);
        RetailerInputName= findViewById(R.id.retailer_register_name_input);
        RetailerInputPhoneNumber= findViewById(R.id.retailer_register_phonenumber_input);
        RetailerInputPassword= findViewById(R.id.retailer_register_password_input);
        RetailerConfirmPassword= findViewById(R.id.retailer_register_confirm_password_input);
        RetailerInputUsername= findViewById(R.id.retailer_register_username_input);
        loadingBar= new ProgressDialog(this);

            CreateRetailerAccountButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CreateAccount();
                }
            });
    }

    private void CreateAccount()
    {
        String retailername = RetailerInputName.getText().toString();
        String retailerphone = RetailerInputPhoneNumber.getText().toString();
        String retailerpassword = RetailerInputPassword.getText().toString();
        String retailerconfirm_password = RetailerConfirmPassword.getText().toString();
        String retailerusername= RetailerInputUsername.getText().toString();

        if  (TextUtils.isEmpty(retailername))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(retailerusername))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(retailerphone))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(retailerpassword))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(retailerconfirm_password))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (!(retailerpassword.equals(retailerconfirm_password)))
        {
            Toast.makeText(this, "password and confirm password not same",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle(("Create Account"));
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(retailername,retailerphone,retailerpassword,retailerusername);
        }
    }

    private void ValidatephoneNumber(String retailername, String retailerphone, String retailerpassword, String retailerusername)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if  (!(snapshot.child("Retailer").child(retailerusername).exists() ))
                {
                    HashMap<String, Object> userdataMap=new HashMap<>();
                    userdataMap.put("phone", retailerphone);
                    userdataMap.put("username", retailerusername);
                    userdataMap.put("password", retailerpassword);
                    userdataMap.put("name", retailername);

                    RootRef.child("Retailer").child(retailerusername).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RetailerRegister.this, "Congratulations, Your account has been created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RetailerRegister.this, loginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(RetailerRegister.this, "Network Error: Please Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(RetailerRegister.this, "This " + retailerphone + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RetailerRegister.this, "Please login using the Number", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RetailerRegister.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}