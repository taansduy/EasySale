package com.example.asus.login_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.login_screen.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Product> productList=new ArrayList<>();
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public void addData(List<Product> data)
    {
        int initSize=productList.size();
        productList.addAll(data);
        notifyItemRangeChanged(initSize,data.size());
    }

    public String getLastItemId()
    {
        return productList.get(productList.size()-1).getId();
    }
    public ProductAdapter(Context mContext,List<Product> products, RecyclerView recyclerView) {
        this.mContext = mContext;
        productList = products;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return productList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public ProductAdapter( List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_ITEM) {
            View view=inflater.inflate(R.layout.row_search_product,viewGroup,false);
            holder= new ProductViewHolder(view);
        } else {
            View view=inflater.inflate(R.layout.load_more,viewGroup,false);
            holder = new ProgressViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ProductViewHolder) {
            Product product=productList.get(i);
            ((ProductViewHolder)viewHolder).tv_name.setText(product.getName());
            ((ProductViewHolder)viewHolder).tv_price.setText(String.format("%1$,.0f",product.getSalePrice()));
            ((ProductViewHolder)viewHolder).tv_id.setText(product.getId());
            ((ProductViewHolder)viewHolder).tv_quantity.setText(String.valueOf(product.getQuantity()));
            if(product.getListImage().size()!=0)
            {
                Glide.with(mContext).load(product.getListImage().get(0)).into( ((ProductViewHolder)viewHolder).img);
            }
            else
            {
                ((ProductViewHolder)viewHolder).img.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        }else {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
   /* @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        Product product=productList.get(i);
        productViewHolder.tv_name.setText(product.getName());
        productViewHolder.tv_price.setText(String.format("%1$,.0f",product.getSalePrice()));
        productViewHolder.tv_id.setText(product.getId());
        productViewHolder.tv_quantity.setText(String.valueOf(product.getQuantity()));
        if(product.getListImage().size()!=0)
        {
            Glide.with(mContext).load(product.getListImage().get(0)).into(productViewHolder.img);
        }
        else
        {
            productViewHolder.img.setImageResource(android.R.drawable.ic_menu_report_image);
        }


    }*/



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

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}

