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

public class RegisterActivity extends AppCompatActivity
{
    private Button CreateAccountButton, RetailerRegisterPage, WholesalerRegisterPage;
    private EditText InputName,InputPhoneNumber,InputPassword,ConfirmPassword,InputUsername;
    private ProgressDialog loadingBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton= findViewById(R.id.register_button);
        InputName= findViewById(R.id.register_name_input);
        InputPhoneNumber= findViewById(R.id.register_phonenumber_input);
        InputPassword= findViewById(R.id.register_password_input);
        ConfirmPassword= findViewById(R.id.register_confirm_password_input);
        InputUsername= findViewById(R.id.register_username_input);
        loadingBar= new ProgressDialog(this);
        RetailerRegisterPage= findViewById(R.id.retailer_register_page_button);
        WholesalerRegisterPage= findViewById(R.id.wholesaler_register_page_button);

        RetailerRegisterPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RegisterActivity.this, RetailerRegister.class);
                startActivity(intent);
            }
        });

        WholesalerRegisterPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RegisterActivity.this, WholesalerRegister.class);
                startActivity(intent);
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount()
    {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        String confirm_password = ConfirmPassword.getText().toString();
        String username= InputUsername.getText().toString();

        if  (TextUtils.isEmpty(name))
            {
                Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
            }
        else if (TextUtils.isEmpty(username))
            {
                Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
            }
        else if (TextUtils.isEmpty(phone))
            {
                Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
            }
        else if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
            }
        else if (TextUtils.isEmpty(confirm_password))
            {
                Toast.makeText(this, "empty box",Toast.LENGTH_SHORT).show();
            }
        else if (!(password.equals(confirm_password)))
            {
                Toast.makeText(this, "password and confirm password not same",Toast.LENGTH_SHORT).show();
            }
        else
            {
                loadingBar.setTitle(("Create Account"));
                loadingBar.setMessage("Please wait, while we are checking the credentials.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                ValidatephoneNumber(name,phone,password,username);
            }
    }

    private void ValidatephoneNumber(String name, String phone, String password, String username)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if  (!(snapshot.child("Users").child(username).exists()))
                {
                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("phone", phone);
                        userdataMap.put("username", username);
                        userdataMap.put("password", password);
                        userdataMap.put("name", name);

                        RootRef.child("Users").child(username).updateChildren(userdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this, "Congratulations, Your account has been created", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this, loginActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                            {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Network Error: Please Try Again", Toast.LENGTH_SHORT).show();
                                            }
                                    }
                                });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This " + username + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try a new Username", Toast.LENGTH_SHORT).show();
                }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}