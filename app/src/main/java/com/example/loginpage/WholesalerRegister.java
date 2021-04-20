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

public class WholesalerRegister extends AppCompatActivity
{
    private Button CreateWholesalerAccountButton;
    private EditText WholesalerInputName,WholesalerInputPhoneNumber,WholesalerInputPassword,WholesalerConfirmPassword,WholesalerInputUsername;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler_register);

        CreateWholesalerAccountButton= findViewById(R.id.wholesaler_register_button);
        WholesalerInputName= findViewById(R.id.wholesaler_register_name_input);
        WholesalerInputPhoneNumber= findViewById(R.id.wholesaler_register_phonenumber_input);
        WholesalerInputPassword= findViewById(R.id.wholesaler_register_password_input);
        WholesalerConfirmPassword= findViewById(R.id.wholesaler_register_confirm_password_input);
        WholesalerInputUsername= findViewById(R.id.wholesaler_register_username_input);
        loadingBar= new ProgressDialog(this);

        CreateWholesalerAccountButton.setOnClickListener(new View.OnClickListener()
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
        String wholesalername = WholesalerInputName.getText().toString();
        String wholesalerphone = WholesalerInputPhoneNumber.getText().toString();
        String wholesalerpassword = WholesalerInputPassword.getText().toString();
        String wholesalerconfirm_password = WholesalerConfirmPassword.getText().toString();
        String wholesalerusername= WholesalerInputUsername.getText().toString();

        if  (TextUtils.isEmpty(wholesalername))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(wholesalerusername))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(wholesalerphone))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(wholesalerpassword))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(wholesalerconfirm_password))
        {
            Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
        }
        else if (!(wholesalerpassword.equals(wholesalerconfirm_password)))
        {
            Toast.makeText(this, "password and confirm password not same",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle(("Create Account"));
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(wholesalername,wholesalerphone,wholesalerpassword,wholesalerusername);
        }
    }

    private void ValidatephoneNumber(String wholesalername, String wholesalerphone, String wholesalerpassword, String wholesalerusername)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if  (!(snapshot.child("Wholesaler").child(wholesalerusername).exists() ))
                {
                    HashMap<String, Object> userdataMap=new HashMap<>();
                    userdataMap.put("phone", wholesalerphone);
                    userdataMap.put("username", wholesalerusername);
                    userdataMap.put("password", wholesalerpassword);
                    userdataMap.put("name", wholesalername);

                    RootRef.child("Wholesaler").child(wholesalerusername).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(WholesalerRegister.this, "Congratulations, Your account has been created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(WholesalerRegister.this, loginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(WholesalerRegister.this, "Network Error: Please Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(WholesalerRegister.this, "This " + wholesalerphone + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(WholesalerRegister.this, "Please login using the Number", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(WholesalerRegister.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}