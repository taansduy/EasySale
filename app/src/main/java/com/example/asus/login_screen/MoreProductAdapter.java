package com.example.asus.login_screen;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.asus.login_screen.Model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoreProductAdapter extends RecyclerView.Adapter<MoreProductAdapter.ProductViewHolder> {
    Context context;
    ArrayList<Product> listProduct;

    public MoreProductAdapter(Context context, ArrayList<Product> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_more_product_adapter,null);
        ProductViewHolder productViewHolder=new ProductViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        Glide.with(context).load(listProduct.get(i).getListImage().values().toArray()[0]).into(productViewHolder.img_Product);
        productViewHolder.img_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        productViewHolder.img_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Name,tv_Price,tv_Count,tv_Type;
        ImageView img_Product;
        ImageView img_Delete;
        ImageView img_Edit;
        public ProductViewHolder(@NonNull View itemView) {
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
