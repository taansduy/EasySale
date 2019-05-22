package com.example.asus.login_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.login_screen.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mContext;
    private List<Product> productList=new ArrayList<>();

    public ProductAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.row_search_product,null);
        ProductViewHolder holder= new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        Product product=productList.get(i);
        productViewHolder.tv_name.setText(product.getName());
        productViewHolder.tv_price.setText(String.format("%1$,.0f",product.getSalePrice()));
        productViewHolder.tv_id.setText(product.getId());
        productViewHolder.tv_quantity.setText(String.valueOf(product.getQuantity()));
        if(product.getListImage().size()!=0)
        {
            Glide.with(mContext).load(product.getListImage().values().toArray()[0]).into(productViewHolder.img);
        }
        else
        {
            productViewHolder.img.setImageResource(android.R.drawable.ic_menu_report_image);
        }


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tv_name,tv_price,tv_id,tv_quantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_product);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_price=itemView.findViewById(R.id.tv_price);
            tv_id=itemView.findViewById(R.id.tv_id);
            tv_quantity=itemView.findViewById(R.id.tv_quantity);
        }
    }
    public void updateData( List<Product> tmp)
    {
        productList=tmp;
    }

}

