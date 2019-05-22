package com.example.asus.login_screen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class cart_dialog  extends Dialog implements
        android.view.View.OnClickListener{
    Button btn_check_out;
    TextView tv_Total;
    RecyclerView rv_orderedProduct;
    public cart_dialog(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onClick(View v) {

    }
}
