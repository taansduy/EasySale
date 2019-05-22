package com.example.asus.login_screen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus.login_screen.Model.TypeOfProduct;

import java.util.ArrayList;
import java.util.List;

public class BottomSheet_TypeSort extends BottomSheetDialogFragment implements ItemClickListener {
    private TypeSelectListener mListener;

    public BottomSheet_TypeSort() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.bottom_sheet_type_sort,container,false);
        RecyclerView recyclerView=(RecyclerView) v.findViewById(R.id.rv_type);
        List<String> typeList=new ArrayList<>();
        typeList.add("Tất cả sản phẩm");
        for (TypeOfProduct type: Local_Cache_Store.getListOfProductType().values()) {
            typeList.add(type.getType());
        }
        LinearLayoutManager linearLayout=new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayout);
        BottomSheetAdapter myAdapter=new BottomSheetAdapter(this.getActivity(),typeList);
        myAdapter.setClickListener(this);
        recyclerView.setAdapter(myAdapter);
        return v;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        setStyle( R.style.BottomSheetDialog,BottomSheetDialogFragment.STYLE_NORMAL);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {

    }

    @Override
    public void onClick(View view, int position, String type) {
        //Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        mListener.TypeSelect(type);
        dismiss();
    }

    public interface TypeSelectListener{
        void TypeSelect(String text);
    }

    public void setOnListItemSelectedListener(TypeSelectListener listener) {
        this.mListener = listener;
    }
}
