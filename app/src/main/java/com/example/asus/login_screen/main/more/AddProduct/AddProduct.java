package com.example.asus.login_screen.main.more.AddProduct;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asus.login_screen.main.more.AddProduct.AddType.AddType;
import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.model.Product;
import com.example.asus.login_screen.model.TypeOfProduct;
import com.example.asus.login_screen.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.os.Environment.getExternalStorageDirectory;

public class AddProduct extends AppCompatActivity {
    EditText edt_Name,edt_Count,edt_CostPrice,edt_SalePrice,edt_Manufacturer,edt_Description;

    Spinner spi_Type;

    TextInputLayout til_Name,til_Count,til_CostPrice,til_SalePrice,til__Manufacturer, til_Description;

    ImageView img_Back,img_Ok,img_1,img_2,img_3,img_4;

    Button btn_X1,btn_X2,btn_X3,btn_X4;

    Uri[] mImageUri=new Uri[4];

    int numberImg;

    Bundle bundle=new Bundle();

    ArrayList<String> arrSpi_Type= new ArrayList<>();

    AdapterSpinner adapter;

    StorageReference mStorageRef;

    DatabaseReference mDatabase;

    StorageTask mUploadTask;

    String id;

    String idType;

    File cameraFile = null;

    File cameraImg1=null,cameraImg2=null,cameraImg3=null,cameraImg4=null;
    //xu ly edit thong tin
    Product tmp;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        cameraImg1 = new File(getExternalStorageDirectory(), "temp1.png");
        cameraImg2 = new File(getExternalStorageDirectory(), "temp2.png");
        cameraImg3 = new File(getExternalStorageDirectory(), "temp3.png");
        cameraImg4 = new File(getExternalStorageDirectory(), "temp4.png");


        mDatabase = FirebaseDatabase.getInstance().getReference("stores/"+ Local_Cache_Store.getShopName() +"/listOfProductType");

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads/"+Local_Cache_Store.getShopName());

        //******* Anh xa edittext*******************************//
        edt_CostPrice= findViewById(R.id.costPrice);
        edt_Count= findViewById(R.id.count);
        edt_Description= findViewById(R.id.description);
        edt_Name= findViewById(R.id.name);
        edt_SalePrice= findViewById(R.id.salePrice);
        edt_Manufacturer= findViewById(R.id.manufacturer);
        //*******************************************************//

        //*********Anh xa TextInputlayout***********************//
        til_Name= findViewById(R.id.nametWrapper);
        til__Manufacturer= findViewById(R.id.manufacturerWrapper);
        til_CostPrice= findViewById(R.id.costPriceWrapper);
        til_Count= findViewById(R.id.countWrapper);
        til_Description= findViewById(R.id.descriptionWrapper);
        til_SalePrice= findViewById(R.id.salePriceWrapper);
        //******************************************************//
        //******************************************************//
        btn_X1=findViewById(R.id.x1);
        btn_X2=findViewById(R.id.x2);
        btn_X3=findViewById(R.id.x3);
        btn_X4=findViewById(R.id.x4);
        //******************************************************//
        //**********Set Click for button "x"********************//
        btn_X1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_1.setImageResource(R.drawable.camera);
                btn_X1.setVisibility(View.INVISIBLE);
                mImageUri[0]=null;
            }
        });
        btn_X2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_2.setImageResource(R.drawable.camera);
                btn_X2.setVisibility(View.INVISIBLE);
                mImageUri[0]=null;
            }
        });
        btn_X3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_3.setImageResource(R.drawable.camera);
                btn_X3.setVisibility(View.INVISIBLE);
                mImageUri[0]=null;
            }
        });
        btn_X4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_4.setImageResource(R.drawable.camera);
                btn_X4.setVisibility(View.INVISIBLE);
                mImageUri[0]=null;
            }
        });
        //******************************************************//

        spi_Type=findViewById(R.id.spinner);
        img_Back=findViewById(R.id.Back);
        img_Ok=findViewById(R.id.Ok );
        img_1=findViewById(R.id.Image1);
        img_2=findViewById(R.id.Image2);
        img_3=findViewById(R.id.Image3);
        img_4=findViewById(R.id.Image4);
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
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrSpi_Type.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    TypeOfProduct post = postSnapshot.getValue(TypeOfProduct.class);
                    assert post != null;
                    Log.d("Get Data", post.getType());
                    arrSpi_Type.add(post.getType());
                }
                if(arrSpi_Type.size()==0)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProduct.this,AlertDialog.THEME_HOLO_LIGHT);
                    builder1.setMessage("Bạn cần thêm loại sản phẩm!");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intent=new Intent(AddProduct.this, AddType.class);
                                    intent.putStringArrayListExtra("listtype",null);
                                    startActivity(intent);
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                adapter=new AdapterSpinner(AddProduct.this,R.layout.spinner_item,arrSpi_Type);
                spi_Type.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try{
            if(bundle!=null)
            {
                tmp=(Product)getIntent().getExtras().getSerializable("bundle");
                edt_Name.setText(tmp.getName());
                edt_CostPrice.setText(String.valueOf(tmp.getCostPrice()));
                edt_Count.setText(String.valueOf(tmp.getQuantity()));
                edt_Description.setText(tmp.getDescription());
                edt_Manufacturer.setText(tmp.getManufacturer());
                edt_SalePrice.setText(String.valueOf(tmp.getSalePrice()));
                //get ten loai
                mDatabase.child(tmp.getIdType()).child("type").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("DDD",dataSnapshot.getValue().toString());
                        spi_Type.setSelection(adapter.getPosition(dataSnapshot.getValue().toString()));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }catch (Exception e){
        }

        img_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set dk add product
                if (edt_Name.getText().toString().equals("")) {
                    til_Name.setErrorTextAppearance(R.style.error_appearance);
                    til_Name.setError("Vui lòng nhập Tên");
                } else {
                    til_Name.setError(null);
                }
                if (edt_Count.getText().toString().equals("")) {
                    til_Count.setErrorTextAppearance(R.style.error_appearance);
                    til_Count.setError("Vui lòng nhập Số");
                } else {
                    til_Count.setError(null);
                }
                if (edt_CostPrice.getText().toString().equals("")) {
                    til_CostPrice.setErrorTextAppearance(R.style.error_appearance);
                    til_CostPrice.setError("Vui lòng nhập Giá vốn");
                } else {
                    til_CostPrice.setError(null);
                }
                if (edt_Manufacturer.getText().toString().equals("")) {
                    til__Manufacturer.setErrorTextAppearance(R.style.error_appearance);
                    til__Manufacturer.setError("Vui lòng nhập Hãng");
                } else {
                    til__Manufacturer.setError(null);
                }
                if (edt_SalePrice.getText().toString().equals("")) {
                    til_SalePrice.setErrorTextAppearance(R.style.error_appearance);
                    til_SalePrice.setError("Vui lòng nhập Giá bán");
                } else {
                    til_SalePrice.setError(null);
                }
                if (edt_Description.getText().toString().equals("")) {
                    til_Description.setErrorTextAppearance(R.style.error_appearance);
                    til_Description.setError("Vui lòng nhập Mô tả");
                } else {
                    til_Description.setError(null);
                }
                if(Double.parseDouble(edt_CostPrice.getText().toString())>Double.parseDouble(edt_SalePrice.getText().toString())) {
                    til_SalePrice.setErrorTextAppearance(R.style.error_appearance);
                    til_CostPrice.setErrorTextAppearance(R.style.error_appearance);

                    til_CostPrice.setError("Giá bán không phù hợp");
                    til_SalePrice.setError("Giá bán không phù hợp");
                } else {
                    til_SalePrice.setError(null);
                    til_CostPrice.setError(null);
                }
                if(!edt_CostPrice.getText().toString().equals("")
                        && !edt_Name.getText().toString().equals("")
                        && !edt_Description.getText().toString().equals("")
                        && !edt_SalePrice.getText().toString().equals("")
                        && !edt_Name.getText().toString().equals("")
                        && !edt_Manufacturer.getText().toString().equals("")
                        && !(Double.parseDouble(edt_CostPrice.getText().toString())>Double.parseDouble(edt_SalePrice.getText().toString()))) {
                    if(null != tmp)
                    {
                        mDatabase.child(tmp.getIdType()).child("productList").child(tmp.getId()).removeValue();
                    }
                   mDatabase.orderByChild("type").equalTo(spi_Type.getSelectedItem().toString()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            idType=dataSnapshot.getKey();
                            id=mDatabase.child(dataSnapshot.getKey()).child("productList").push().getKey();
                            Product product=new Product(id, idType, Integer.parseInt(edt_Count.getText().toString()),
                                    Double.parseDouble(edt_CostPrice.getText().toString()),
                                    edt_Manufacturer.getText().toString(),
                                    Double.parseDouble(edt_SalePrice.getText().toString()),
                                    edt_Name.getText().toString(),
                                    null,edt_Description.getText().toString());
                            Log.d("DDDD",product.getName());
                            mDatabase.child(dataSnapshot.getKey()).child("productList").child(id).setValue(product);

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
//                        synchronized (this){
                            if (mUploadTask != null && mUploadTask.isInProgress()) {
                            } else {
                                uploadFile(i);
                                i++;
                            }
//                        }

                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProduct.this,AlertDialog.THEME_HOLO_LIGHT);
                    builder1.setMessage("Bạn đã thêm sản phẩm thành công!");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    onBackPressed();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }


            }
        });
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Log.d("DDDD","Create");

    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.d("DDDD","Resume");
    }

    private void openFileChooser() {
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraFile = new File(getExternalStorageDirectory(), "temp.jpg");
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
        Intent chooser= Intent.createChooser(intentGallery,"Choice");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{intentCamera});
        startActivityForResult(chooser, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap;
        Uri cameraPictureUri;
        if (requestCode == 1&&resultCode==RESULT_OK) {
            if(data!=null){
                switch (numberImg)
                {
                    case 1:
                        Picasso.with(this).load(data.getData()).into(img_1);
                        mImageUri[numberImg-1]=data.getData();
                        btn_X1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        Picasso.with(this).load(data.getData()).into(img_2);
                        mImageUri[numberImg-1]=data.getData();
                        btn_X2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        Picasso.with(this).load(data.getData()).into(img_3);
                        mImageUri[numberImg-1]=data.getData();
                        btn_X3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        Picasso.with(this).load(data.getData()).into(img_4);
                        mImageUri[numberImg-1]=data.getData();
                        btn_X4.setVisibility(View.VISIBLE);
                        break;
                }

            }else{
                cameraPictureUri = Uri.fromFile(cameraFile);
                switch (numberImg)
                {
                    case 1:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            try {
                                copy(cameraFile,cameraImg1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Picasso.with(this).load(cameraImg1).into(img_1);
                        mImageUri[numberImg-1]=Uri.fromFile(cameraImg1);
                        btn_X1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            try {
                                copy(cameraFile,cameraImg2);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Picasso.with(this).load(cameraImg2).into(img_2);
                        mImageUri[numberImg-1]=Uri.fromFile(cameraImg2);
                        btn_X2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            try {
                                copy(cameraFile,cameraImg3);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Picasso.with(this).load(cameraImg3).into(img_3);
                        mImageUri[numberImg-1]=Uri.fromFile(cameraImg3);
                        btn_X3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            try {
                                copy(cameraFile,cameraImg4);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Picasso.with(this).load(cameraImg4).into(img_4);
                        mImageUri[numberImg-1]=Uri.fromFile(cameraImg4);
                        btn_X4.setVisibility(View.VISIBLE);
                        break;
                }

            }


        }
        else
        {

        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public void uploadFile(final int pos) {
        if (mImageUri[pos] != null) {

            final String key_tmp;
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri[pos]));
            mUploadTask = fileReference.putFile(mImageUri[pos])
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                    @Override
                                                                                    public void onSuccess(Uri uri) {
                                                                                        FirebaseDatabase.getInstance()
                                                                                                .getReference("stores/" + Local_Cache_Store.getShopName() + "/listOfProductType")
                                                                                                .child(idType)
                                                                                                .child("productList")
                                                                                                .child(id)
                                                                                                .child("listImage")
                                                                                                .push()
                                                                                                .setValue(uri.toString());
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
//
                        }
                    });
        }

    }
}
