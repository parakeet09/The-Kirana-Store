package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.loginpage.retailerwholesaler.Wholesaler_sell;
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

public class WholesalerAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, Pname, Quantity, saveCurrentDate, saveCurrentTime ;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice, InputProductQuantity;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesWRef;
    private DatabaseReference ProductsWholesalerRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaler_add_new_product);

        CategoryName= getIntent().getExtras().get("category").toString();
        Toast.makeText(this,CategoryName,Toast.LENGTH_SHORT).show();
        ProductImagesWRef= FirebaseStorage.getInstance().getReference().child("Product WS Images");
        ProductsWholesalerRef= FirebaseDatabase.getInstance().getReference().child("Wholesaler Products");


        AddNewProductButton= findViewById(R.id.add_new_product_wholesaler);
        InputProductImage= findViewById(R.id.select_product_image_wholesaler);
        InputProductName= findViewById(R.id.product_name_wholesaler);
        InputProductDescription= findViewById(R.id.product_description_wholesaler);
        InputProductPrice= findViewById(R.id.product_price_wholesaler);
        InputProductQuantity= findViewById(R.id.product_quantity_wholesaler);
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

        StorageReference filePath= ProductImagesWRef.child(ImageUri.getLastPathSegment()+ productRandomKey + ".jpg");


        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message= e.toString();
                Toast.makeText(WholesalerAddNewProductActivity.this,"Error"+ message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(WholesalerAddNewProductActivity.this,"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();


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
                            Toast.makeText(WholesalerAddNewProductActivity.this,"Getting prod image url", Toast.LENGTH_SHORT).show();

                            saveProductInfotoDatabase();

                        }

                    }
                });
            }
        });
    }

    private void saveProductInfotoDatabase()
    {
        HashMap<String, Object> productWMap = new HashMap<>();
        productWMap.put("pid",productRandomKey);
        productWMap.put("date",saveCurrentDate);
        productWMap.put("time",saveCurrentTime);
        productWMap.put("description",Description);
        productWMap.put("image",downloadImageUrl);
        productWMap.put("category",CategoryName);
        productWMap.put("price",Price);
        productWMap.put("pname",Pname);
        productWMap.put("quantity",Quantity);

        ProductsWholesalerRef.child(productRandomKey).updateChildren(productWMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(WholesalerAddNewProductActivity.this, Wholesaler_sell.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(WholesalerAddNewProductActivity.this,"Product is added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message =task.getException().toString();
                            Toast.makeText(WholesalerAddNewProductActivity.this,"Error"+ message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}