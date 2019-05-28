package com.example.asus.login_screen.main.sale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.asus.login_screen.Main_Screen;
import com.example.asus.login_screen.R;
import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductInCartAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<Product> productOrderedList=new ArrayList<>();
    private ItemClickListener itemClickListener;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductInCartAdapter(Context mContext, List<Product> productOrderedList) {
        this.mContext = mContext;
        this.productOrderedList = productOrderedList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder;
        View view=inflater.inflate(R.layout.row_product_cart,viewGroup,false);
        holder= new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final Product product=productOrderedList.get(i);
        final ProductInCartAdapter.ProductViewHolder temp1=(ProductInCartAdapter.ProductViewHolder)viewHolder;
        ((ProductInCartAdapter.ProductViewHolder)viewHolder).tv_name.setText(product.getName());
        ((ProductInCartAdapter.ProductViewHolder)viewHolder).tv_price.setText(String.format("%1$,.0f",product.getSalePrice()));
        ((ProductInCartAdapter.ProductViewHolder)viewHolder).tv_amount.setText(String.valueOf(product.getQuantity()));
        ((ProductInCartAdapter.ProductViewHolder)viewHolder).tv_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=((ProductInCartAdapter.ProductViewHolder)temp1).tv_name.getText().toString();
                int temp=Integer.parseInt(((ProductInCartAdapter.ProductViewHolder)temp1).tv_amount.getText().toString());
                temp++;
                if(temp<= Local_Cache_Store.getListOfProductType().get(product.getIdType()).getProductList().get(product.getId()).getQuantity())
                {
                    for (Product product: Main_Screen.orderedProductList) {
                        if(product.getName()==name)
                        {
                            product.setQuantity(product.getQuantity()+1);
                        }

                    }
                ((ProductInCartAdapter.ProductViewHolder)temp1).tv_amount.setText(String.valueOf(temp));
                ((ProductInCartAdapter.ProductViewHolder)temp1).tv_amount.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.bounce));
                if (itemClickListener != null) itemClickListener.onClick(v,i,"+"+product.getSalePrice());
                }
            }
        });
        ((ProductInCartAdapter.ProductViewHolder)viewHolder).tv_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=((ProductInCartAdapter.ProductViewHolder)temp1).tv_name.getText().toString();
                for (Product product:Main_Screen.orderedProductList) {
                    if(product.getName()==name)
                    {
                        product.setQuantity(product.getQuantity()-1);
                        if (product.getQuantity()==0) Main_Screen.orderedProductList.remove(product);
                        break;
                    }

                }
                int temp=Integer.parseInt(((ProductInCartAdapter.ProductViewHolder)temp1).tv_amount.getText().toString());
                temp--;
                if(temp==0)
                {
                    productOrderedList.remove(product);
                    notifyDataSetChanged();

                }
                ((ProductInCartAdapter.ProductViewHolder)temp1).tv_amount.setText(String.valueOf(temp));
                ((ProductInCartAdapter.ProductViewHolder)temp1).tv_amount.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.bounce));
                if (itemClickListener != null) itemClickListener.onClick(v,i,"-"+product.getSalePrice());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productOrderedList.size();
    }



    class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_price,tv_amount;
        TextView tv_inc,tv_dec;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_price=itemView.findViewById(R.id.tv_price);
            tv_amount=itemView.findViewById(R.id.tv_amount);
            tv_inc=itemView.findViewById(R.id.btn_increment);
            tv_dec=itemView.findViewById(R.id.btn_decrement);
        }

        public void setTv_amount(TextView tv_amount) {
            this.tv_amount = tv_amount;
        }
    }
}
