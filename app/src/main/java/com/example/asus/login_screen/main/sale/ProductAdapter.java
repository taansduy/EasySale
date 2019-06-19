package com.example.asus.login_screen.main.sale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.asus.login_screen.model.Product;
import com.example.asus.login_screen.R;

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
    private ItemClickListener itemClickListener;
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void addData(List<Product> data)
    {
        int initSize=productList.size();
        productList.addAll(data);
        notifyItemRangeChanged(initSize,data.size());
    }

    public void updateData(List<Product> data)
    {
        productList=new ArrayList<>();
        productList.addAll(data);
        notifyDataSetChanged();
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

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof ProductViewHolder) {
            final Product product=productList.get(i);
            if(product.getQuantity()>0) {
                final ProductViewHolder temp = (ProductViewHolder) viewHolder;

                ((ProductViewHolder) viewHolder).tv_name.setText(product.getName());
                ((ProductViewHolder) viewHolder).tv_price.setText(String.format("%,d", product.getCostPrice()));
                if (product.getListImage().size() != 0) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .transforms(new CenterCrop(), new RoundedCorners(8))
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.image);

                    Glide.with(mContext).load(product.getListImage().values().toArray()[0]).apply(options).into(((ProductViewHolder) viewHolder).img);
                } else {
                    ((ProductViewHolder) viewHolder).img.setImageResource(R.drawable.image);
                }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null)
                            itemClickListener.onClick(v, i, product.getName());
                    }
                });
            }
            else ((ProductViewHolder) viewHolder).container.setVisibility(View.INVISIBLE);
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



    class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tv_name,tv_price,tv_id,tv_quantity;
        RelativeLayout container;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_product);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_price=itemView.findViewById(R.id.tv_price);
            container=itemView.findViewById(R.id.container);


        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
//    public void updateData( List<Product> tmp)
//    {
//        productList=tmp;
//    }

}

