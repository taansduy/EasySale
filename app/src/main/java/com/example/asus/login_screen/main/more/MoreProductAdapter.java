package com.example.asus.login_screen.main.more;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.asus.login_screen.main.more.AddProduct.AddProduct;
import com.example.asus.login_screen.model.Product;
import com.example.asus.login_screen.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MoreProductAdapter extends RecyclerView.Adapter<MoreProductAdapter.ProductViewHolder> {
    Context context;
    public ArrayList<Product> listProduct;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public MoreProductAdapter(Context context, ArrayList<Product> listProduct, DatabaseReference databaseReference, StorageReference storageReference) {
        this.context = context;
        this.listProduct = listProduct;
        this.databaseReference=databaseReference;
        this.storageReference=storageReference;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_more_product_adapter,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, final int i) {
        try {
            Glide.with(context)
                    .load(Uri.parse((String) listProduct.get(i).getListImage().values().toArray()[0]))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.camera)
                    .error(R.drawable.camera)
                    .into(productViewHolder.img_Product);
        }
        catch (Exception e){}

        productViewHolder.img_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddProduct.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("bundle", listProduct.get(i));
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
        productViewHolder.img_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
                        .setTitle("Thông báo")
                        .setMessage("Bạn chắc chắn muốn xóa sản phẩm?")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child(listProduct.get(i).getIdType()).child("productList").child(listProduct.get(i).getId()).removeValue();
                            }
                        })
                        .show();
            }
        });
        productViewHolder.tv_Price.setText(String.valueOf(listProduct.get(i).getCostPrice()));
        productViewHolder.tv_Count.setText(String.valueOf(listProduct.get(i).getQuantity()));
        productViewHolder.tv_Name.setText(listProduct.get(i).getName());
        productViewHolder.tv_Type.setText(listProduct.get(i).getIdType());
    }


    @Override
    public int getItemCount() {
        return listProduct.size();
    }
    public void updateList(ArrayList<Product>tmp)
    {
        listProduct.clear();
        listProduct.addAll(tmp);
        notifyDataSetChanged();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Name,tv_Price,tv_Count,tv_Type;
        ImageView img_Product;
        ImageView img_Delete;
        ImageView img_Edit;
        ProductViewHolder( View itemView) {
            super(itemView);
            tv_Type=itemView.findViewById(R.id.type);
            tv_Name=itemView.findViewById(R.id.name);
            tv_Count=itemView.findViewById(R.id.count);
            tv_Price=itemView.findViewById(R.id.price);
            img_Delete=itemView.findViewById(R.id.delete);
            img_Edit=itemView.findViewById(R.id.edit);
            img_Product=itemView.findViewById(R.id.image);
        }
    }
}
