package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RetailerAddNewProductActivity extends AppCompatActivity
{
    private String CategoryName, Description, Price, Pname, Quantity, saveCurrentDate, saveCurrentTime ;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice, InputProductQuantity;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRetailerRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_add_new_product);

        CategoryName= getIntent().getExtras().get("category").toString();
        Toast.makeText(this,CategoryName,Toast.LENGTH_SHORT).show();
        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRetailerRef= FirebaseDatabase.getInstance().getReference().child("Retailer Products");


        AddNewProductButton= findViewById(R.id.add_new_product);
        InputProductImage= findViewById(R.id.select_product_image);
        InputProductName= findViewById(R.id.product_name);
        InputProductDescription= findViewById(R.id.product_description);
        InputProductPrice= findViewById(R.id.product_price);
        InputProductQuantity= findViewById(R.id.product_quantity);
        loadingBar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateProductData();
            }
        });

    }

    private void OpenGallery()
    {
        Intent galleryIntent= new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData()
    {
        Description= InputProductDescription.getText().toString();
        Price= InputProductPrice.getText().toString();
        Pname= InputProductName.getText().toString();
        Quantity= InputProductQuantity.getText().toString();


        if(ImageUri == null)
        {
            Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Specify the price of product", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Quantity))
        {
            Toast.makeText(this, "Please write product quantity", Toast.LENGTH_SHORT).show();
        }
        else
            {
                StoreProductInformation();
            }


    }

    private void StoreProductInformation()
    {

        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calender =Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate= currentDate.format(calender.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calender.getTime());


        productRandomKey= saveCurrentDate + saveCurrentTime;

        StorageReference filePath= ProductImagesRef.child(ImageUri.getLastPathSegment()+ productRandomKey + ".jpg");


        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message= e.toString();
                Toast.makeText(RetailerAddNewProductActivity.this,"Error"+ message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(RetailerAddNewProductActivity.this,"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();


                Task<Uri> uriTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl= filePath.getDownloadUrl().toString();
                        return  filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(RetailerAddNewProductActivity.this,"Getting prod image url", Toast.LENGTH_SHORT).show();

                            saveProductInfotoDatabase();

                        }

                    }
                });
            }
        });
    }

    private void saveProductInfotoDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",Price);
        productMap.put("pname",Pname);
        productMap.put("quantity",Quantity);

        ProductsRetailerRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(RetailerAddNewProductActivity.this, RetailerSell.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(RetailerAddNewProductActivity.this,"Product is added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                                loadingBar.dismiss();
                                String message =task.getException().toString();
                                Toast.makeText(RetailerAddNewProductActivity.this,"Error"+ message, Toast.LENGTH_SHORT).show();
                            }

                    }
                });
    }
}