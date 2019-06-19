package com.example.asus.login_screen.main.more;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
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
import com.example.asus.login_screen.R;
import com.example.asus.login_screen.main.more.AddProduct.AddProduct;
import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.model.Product;
import com.example.asus.login_screen.model.TypeOfProduct;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MoreTypeAdapter extends RecyclerView.Adapter<MoreTypeAdapter.TypeViewHolder> {
    Context context;
    public ArrayList<TypeOfProduct> listType;
    private DatabaseReference databaseReference;


    public MoreTypeAdapter(Context context, ArrayList<TypeOfProduct> listType, DatabaseReference databaseReference) {
        this.context = context;
        this.listType = listType;
        this.databaseReference=databaseReference;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_more_type_adapter,null);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder productViewHolder, final int i) {
        productViewHolder.tv_Name.setText(listType.get(i).getType());
        productViewHolder.tv_ID.setText(listType.get(i).getID());
        productViewHolder.img_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
                        .setTitle("Thông báo")
                        .setMessage("Bạn chắc chắn muốn xóa loại sản phẩm?")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child(listType.get(i).getID()).removeValue();
                            }
                        })
                        .show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return listType.size();
    }
    public void updateList(ArrayList<TypeOfProduct>tmp)
    {
        listType.clear();
        listType.addAll(tmp);
        notifyDataSetChanged();
    }
    class TypeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ID,tv_Name;
        ImageView img_Clear;
        TypeViewHolder( View itemView) {
            super(itemView);
            img_Clear=itemView.findViewById(R.id.deleteImg);
            tv_ID=itemView.findViewById(R.id.id);
            tv_Name=itemView.findViewById(R.id.nameType);
        }
    }
}
