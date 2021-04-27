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
import com.example.loginpage.Prevalent.Prevalent;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class  loginActivity extends AppCompatActivity
{
    private Button GetOTPVerification, LoginUserButton , RetailerLoginButton, WholesalerLoginButton, signInGoogle;
    private EditText InputUsername,InputPassword;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GetOTPVerification= (Button) findViewById(R.id.otp_verification_button);
        RetailerLoginButton= (Button) findViewById(R.id.retailer_login_Button);
        WholesalerLoginButton= (Button) findViewById(R.id.wholesaler_login_button);
        signInGoogle= (Button) findViewById(R.id.send_google_sign_in_btn) ;

        signInGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(loginActivity.this, GoogleSignInActivity.class);
                startActivity(intent);
            }
        });

        GetOTPVerification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(loginActivity.this, OtpVerification.class);
                startActivity(intent);
            }
        });


        RetailerLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(loginActivity.this, Retailer_Login_Activity.class);
                startActivity(intent);
            }
        });

        WholesalerLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(loginActivity.this, Wholesaler_LoginActivity.class);
                startActivity(intent);
            }
        });

        LoginUserButton= findViewById(R.id.login_button);
        InputPassword= findViewById(R.id.login_password_input);
        InputUsername= findViewById(R.id.login_username_input);
        loadingBar= new ProgressDialog(this);

        Paper.init(this);


        LoginUserButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });
    }



    private void LoginUser()
    {
        String username = InputUsername.getText().toString();
        String password = InputPassword.getText().toString();

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
        Paper.book().write(Prevalent.UserUsernameKey, username);
        Paper.book().write(Prevalent.UserPasswordKey, password);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Users usersData= snapshot.child(parentDbName).child(username).getValue(Users.class);
                if(snapshot.child(parentDbName).child(username).exists())
                {
                    if(usersData.getUsername().equals(username))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(loginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(loginActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(loginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }
                else
                {
                    Toast.makeText(loginActivity.this, "Account with this username does not exits.", Toast.LENGTH_SHORT).show();
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