package com.example.asus.login_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.TypeViewHolder> {
    private Context mContext;
    private List<String> typeList=new ArrayList<>();
    private ItemClickListener itemClickListener;

    public BottomSheetAdapter(Context mContext, List<String> typeList) {
        this.mContext = mContext;
        this.typeList = typeList;
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public BottomSheetAdapter.TypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.row_type_sort,viewGroup,false);
        BottomSheetAdapter.TypeViewHolder holder= new BottomSheetAdapter.TypeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder typeViewHolder, int i) {
        String type=typeList.get(i);
        typeViewHolder.tv_type.setText(type);
    }



    @Override
    public int getItemCount() {
        return typeList.size();
    }

    class TypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_type;

        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type=(TextView) itemView.findViewById(R.id.tv_type);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            if (itemClickListener != null) itemClickListener.onClick(v,getAdapterPosition(),tv_type.getText().toString());
        }

    }
}
