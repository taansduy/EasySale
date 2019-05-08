package com.example.asus.login_screen.Activity_of_MoreComponent.AddProduct;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.login_screen.MainActivity;
import com.example.asus.login_screen.Model.Product;
import com.example.asus.login_screen.Model.TypeOfProduct;
import com.example.asus.login_screen.Model.Upload;
import com.example.asus.login_screen.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddProduct extends AppCompatActivity {
    EditText edt_Name,edt_Count,edt_CostPrice,edt_SalePrice,edt_Manufacturer,edt_Description;
    Spinner spi_Type;
    ImageView img_Back,img_Ok,img_1,img_2,img_3,img_4;
//    ArrayList<ImageView> listImage;
    Uri[] mImageUri=new Uri[4];
    int numberImg;
    Bundle bundle=new Bundle();
    ArrayList<String> arrSpi_Type=new ArrayList<String>();
    AdapterSpinner adapter;
    ProgressBar mProgressBar;

    StorageReference mStorageRef;
    DatabaseReference mDatabase;
    StorageTask mUploadTask;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        bundle= getIntent().getBundleExtra("bundle");

        mDatabase = FirebaseDatabase.getInstance().getReference("stores/"+bundle.getString("shopName")+"/listOfProductType");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrSpi_Type.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    TypeOfProduct post = postSnapshot.getValue(TypeOfProduct.class);
                    Log.d("Get Data", post.getType());
                    arrSpi_Type.add(post.getType());
                }

                adapter=new AdapterSpinner(AddProduct.this,R.layout.spinner_item,arrSpi_Type);
                spi_Type.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads/"+bundle.getString("shopName"));


        edt_CostPrice=(EditText)findViewById(R.id.costPrice);
        edt_Count=(EditText)findViewById(R.id.count);
        edt_Description=(EditText)findViewById(R.id.description);
        edt_Name=(EditText)findViewById(R.id.name);
        edt_SalePrice=(EditText)findViewById(R.id.salePrice);
        edt_Manufacturer=(EditText)findViewById(R.id.manufacturer);
        spi_Type=findViewById(R.id.spinner);
        mProgressBar = findViewById(R.id.progress_bar);
        img_Back=findViewById(R.id.Back);
        img_Ok=findViewById(R.id.Ok );
        img_1=findViewById(R.id.Image1);
        img_2=findViewById(R.id.Image2);
        img_3=findViewById(R.id.Image3);
        img_4=findViewById(R.id.Image4);
//        listImage.add(img_1);
//        listImage.add(img_2);
//        listImage.add(img_3);
//        listImage.add(img_4);
        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberImg=1;
                openFileChooser();
            }
        });
        img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberImg=2;
                openFileChooser();
            }
        });
        img_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberImg=3;
                openFileChooser();
            }
        });
        img_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberImg=4;
                openFileChooser();
            }
        });

        img_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //up child san pham
                // udi.crca
                product=new Product("1", "1", Integer.parseInt(edt_Count.getText().toString()),
                        Double.parseDouble(edt_CostPrice.getText().toString()),
                        edt_Manufacturer.getText().toString(),
                        Double.parseDouble(edt_SalePrice.getText().toString()),
                        edt_Name.getText().toString(),
                        null,edt_Description.getText().toString());

                mDatabase.orderByChild("type").equalTo(spi_Type.getSelectedItem().toString()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        mDatabase.child(dataSnapshot.getKey()).child("listOfProduct").push().setValue(product);

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //up hinh cua san pham & add to listImage on database
                int i=0;
                while(i<4)
                {
                    synchronized (this){
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(AddProduct.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadFile(i);
                        i++;
                    }

                    }

                }



            }
        });
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //Thiết lập adapter cho Spinner




    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri[numberImg-1]=data.getData();
            switch (numberImg)
            {
                case 1:
                    Picasso.with(this).load(data.getData()).into(img_1);
                    mImageUri[numberImg-1]=data.getData();
                    break;
                case 2:
                    Picasso.with(this).load(data.getData()).into(img_2);
                    mImageUri[numberImg-1]=data.getData();
                    break;
                case 3:
                    Picasso.with(this).load(data.getData()).into(img_3);
                    mImageUri[numberImg-1]=data.getData();
                    break;
                case 4:
                    Picasso.with(this).load(data.getData()).into(img_4);
                    mImageUri[numberImg-1]=data.getData();
                    break;
            }

        }
        else
        {
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public void uploadFile(int pos) {
        if (mImageUri[pos] != null) {
            final String key_tmp;
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri[pos]));

            mUploadTask = fileReference.putFile(mImageUri[pos])
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Log.d("TAG",taskSnapshot.getUploadSessionUri().toString());
                            edt_Name.append(taskSnapshot.getUploadSessionUri().toString());
                            mDatabase = FirebaseDatabase.getInstance().getReference("stores/"+bundle.getString("shopName")+"/listOfProductType");
                            mDatabase.orderByChild("type").equalTo(spi_Type.getSelectedItem().toString()).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Log.d("TAG1",dataSnapshot.getKey());
                                    mDatabase = FirebaseDatabase.getInstance().getReference("stores/"+bundle.getString("shopName")+"/listOfProductType/"+dataSnapshot.getKey());
                                    mDatabase.child("listOfProduct").orderByChild("id").equalTo(1).addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                            Log.d("TAG2",dataSnapshot.getKey());

                                            mDatabase.child("listOfProduct").child(dataSnapshot.getKey()).child("listImage").push().setValue(taskSnapshot.getUploadSessionUri().toString());
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }
}
