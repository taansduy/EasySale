package com.example.asus.login_screen.main.overview;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.asus.login_screen.model.Local_Cache_Store;
import com.example.asus.login_screen.model.Bill;
import com.example.asus.login_screen.model.Product;
import com.example.asus.login_screen.model.Store;
import com.example.asus.login_screen.model.saleAnalyst;
import com.example.asus.login_screen.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;


public class Overview extends Fragment {

    private LineChart mChart;
    private SwipeRefreshLayout swipeContainer;
    private TextView tv_typeAnalyse,tv_Dates,tv_noChart,tv_totalSale,tv_OrderCount,tv_noHotProduct;
    private LinearLayout container_noChart,container_noHotProduct;
    private ListView lv_hotProduct;
    private CardView container_analyse_options;
    int option=1;
    public Overview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_overview, container, false);
        swipeContainer= v.findViewById(R.id.swipeContainer);
        mChart = v.findViewById(R.id.chart);
        tv_typeAnalyse= v.findViewById(R.id.tv_typeAnalyse);
        tv_Dates= v.findViewById(R.id.tv_Date);
        tv_noChart= v.findViewById(R.id.tv_noChart);
        tv_noHotProduct= v.findViewById(R.id.tv_noHotProduct);
        tv_totalSale= v.findViewById(R.id.tv_totalSale);
        tv_OrderCount= v.findViewById(R.id.tv_OrderCount);

        container_noChart= v.findViewById(R.id.container_noChart);
        container_noHotProduct= v.findViewById(R.id.container_noHotProduct);
        container_analyse_options=v.findViewById(R.id.container_option_analyse);

        lv_hotProduct=(ListView) v.findViewById(R.id.lv_hotProduct);

        IMarker marker = new MyMarkerView(getActivity().getBaseContext(), R.layout.custom_marker_view);
        MyMarkerView mv = new MyMarkerView(getActivity().getBaseContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(marker);
        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.getDescription().setEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDrawGridBackground(false);
        synchronized (this)
        {
            createWeekAnalyse(Local_Cache_Store.getShopName());
        }
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createWeekAnalyse(Local_Cache_Store.getShopName());
            }
        });
        container_analyse_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog options_dialog = new Dialog(getContext());
                options_dialog.setContentView(R.layout.layout_analyse_option);
                final RadioGroup option_gr=options_dialog.findViewById(R.id.radioGroup);
                if(option==1)
                {
                    option_gr.check(R.id.option1);
                }
                else if(option==2)
                {
                    option_gr.check(R.id.option2);
                }
                else
                {
                    option_gr.check(R.id.option3);
                }
                ImageView savebutton= options_dialog.findViewById(R.id.check_img);
                savebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch( option_gr.getCheckedRadioButtonId())
                        {
                            case R.id.option1:
                            {
                                if(option!=1)
                                {
                                    option=1;
                                    createWeekAnalyse(Local_Cache_Store.getShopName());

                                }
                                options_dialog.dismiss();
                                break;
                            }
                            case R.id.option2:
                            {
                                if(option!=2) {
                                    option = 2;
                                    createMonthAnalyse(Local_Cache_Store.getShopName());

                                }
                                options_dialog.dismiss();
                                break;
                            }
                            case R.id.option3:
                            {
                                if(option!=3) {
                                    option = 3;
                                    createHalfYearAnalyse(Local_Cache_Store.getShopName());

                                }
                                options_dialog.dismiss();
                                break;
                            }
                        };
                    }
                });
                options_dialog.show();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
    synchronized public void createWeekAnalyse(String storeName)
    {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query ref=mDatabase.child("saleAnalyst").child(storeName);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        tv_typeAnalyse.setText("7 ngày qua");
        synchronized (this) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<Entry> entries = new ArrayList<>();
                    ArrayList<Double> saleData = new ArrayList<>();
                    Double totalWeekSale=0d;
                    final ArrayList<String> Dates = new ArrayList<String>();
                    Calendar now = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
                    String showDate=df.format(now.getTime());
                    now.add(Calendar.DAY_OF_YEAR,-6);
                    tv_noChart.setText("Từ "+df.format(now.getTime())+ " đến "+showDate+" chưa có đơn hàng nào." );
                    tv_totalSale.setText("0");
                    showDate=df.format(now.getTime())+" - "+showDate;
                    tv_Dates.setText(showDate);
                    now.add(Calendar.DAY_OF_YEAR,7);
                    df = new SimpleDateFormat("dd/MM");
                    Dates.add(df.format(now.getTime()));
                    if(snapshot.exists())
                    {
                        saleAnalyst sales = snapshot.getValue(saleAnalyst.class);
                        int Year, Month, Day;
                        //Create data
                        for (int i = 0; i < 7; i++) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -i);
                            Date newDate = cal.getTime();
                            SimpleDateFormat df1 = new SimpleDateFormat("dd/MM");
                            SimpleDateFormat df2 = new SimpleDateFormat("MM");
                            SimpleDateFormat df3 = new SimpleDateFormat("YYYY");
                            cal.setTime(newDate);
                            Year = Integer.parseInt(df3.format(cal.getTime()));
                            Month = Integer.parseInt(df2.format(cal.getTime()));
                            Day = cal.get(Calendar.DAY_OF_MONTH);
                            Double DaySale;
                            try {
                                DaySale = sales.getYearSales().get(df3.format(cal.getTime())+"_").getListOfMSale().get(df2.format(cal.getTime()) +"_").getListOfDSale().get(cal.get(Calendar.DAY_OF_MONTH) +"_").getTotalSale();
                            } catch (Exception e) {
                                DaySale = 0d;
                            }
                            totalWeekSale+=DaySale;
                            saleData.add(DaySale);
                            Dates.add(df1.format(cal.getTime()));
                        }
                        tv_totalSale.setText(String.format("%1$,.0f",totalWeekSale));

                        //Styling the line chart
                        //XAxis
                        XAxis xAxis;
                        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis = mChart.getXAxis();
                        xAxis.setAxisLineColor(Color.parseColor("#D3D3D3"));
                        xAxis.setDrawGridLines(false);
                        xAxis.setTextColor(Color.BLACK);
                        xAxis.enableGridDashedLine(10f, 10f, 0f);
                        //Styling XAxis with string dates
                        IAxisValueFormatter Xformatter = new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                if(7-(int) value>7)
                                    return "";
                                else return Dates.get(7-(int) value);
                            }
                        };
                        xAxis.setAxisMinimum(0);
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(Xformatter);

                        //YAxis
                        YAxis yAxis;
                        yAxis = mChart.getAxisLeft();
                        mChart.getAxisRight().setEnabled(false);
                        yAxis.setAxisLineColor(Color.parseColor("#D3D3D3"));
                        yAxis.setTextColor(Color.BLACK);
                        yAxis.setGranularityEnabled(true);
                        Double max = Collections.max(saleData);
                        if(max==0) max=10000d;
                        final Double jumpStep = (max / 5);
                        yAxis.setLabelCount(5,true);
                        yAxis.setAxisMinimum(0);
                        yAxis.setAxisMaximum(max.floatValue()+jumpStep.floatValue());
                        mChart.setExtraOffsets(0,10,0,0);
                        yAxis.setValueFormatter(new LargeValueFormatter());

                        //create antries
                        for (int i = 0; i < saleData.size(); i++) {
                            if(saleData.get(6-i).floatValue()!=0 )
                                entries.add(new Entry(i, saleData.get(6-i).floatValue()));
                        }

                        //create chart
                        if(entries.size()!=0)
                        {
                            mChart.setVisibility(View.VISIBLE);
                            tv_noChart.setVisibility(View.INVISIBLE);
                            container_noChart.setVisibility(View.INVISIBLE);
                            LineDataSet dataSet = new LineDataSet(entries, "saleAnalyst");
                            dataSet.setColor(Color.parseColor("#3399ff"));
                            dataSet.setDrawCircleHole(false);
                            dataSet.setCircleColor(Color.parseColor("#3399ff"));
                            dataSet.setDrawValues(false);
                            dataSet.setDrawFilled(true);

                            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                            dataSet.setFillDrawable(drawable);
                            LineData data = new LineData(dataSet);
                            mChart.setData(data);
                            mChart.getLegend().setEnabled(false);
                            mChart.animateY(3000, Easing.EasingOption.EaseOutBack);
                            mChart.invalidate();
                        }
                        else{
                            mChart.setVisibility(View.INVISIBLE);
                            tv_noChart.setVisibility(View.VISIBLE);
                            container_noChart.setVisibility(View.VISIBLE);

                        }

                    }
                    else
                    {
                        mChart.setVisibility(View.INVISIBLE);
                        tv_noChart.setVisibility(View.VISIBLE);
                        container_noChart.setVisibility(View.VISIBLE);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });
        }
        ref=mDatabase.child("stores").child(storeName);
        synchronized (this) {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Integer orderCount=0;
                        Store store= dataSnapshot.getValue(Store.class);
                        Calendar now = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
                        String showDate=df.format(now.getTime());
                        now.add(Calendar.DAY_OF_YEAR,-6);
                        tv_noHotProduct.setText("Từ "+df.format(now.getTime())+ " đến "+showDate+" chưa bán mặt hàng nào." );
                        showDate=df.format(now.getTime())+" - "+showDate;
                        HashMap<String,Integer> AnalyseProduct=new HashMap<String,Integer>();
                        df = new SimpleDateFormat("YYYYMMdd");
                        for (int i = 0; i < 7; i++) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -i);
                            String idBill=df.format(cal.getTime());
                            for (Map.Entry<String, Bill> e : store.getListOrders().entrySet()) {
                                if (e.getKey().startsWith(idBill)) {
                                    orderCount++;
                                    List<Product> temp=e.getValue().getProductList();
                                    for(Product product : e.getValue().getProductList())
                                    {
                                        if(product==null) continue;
                                        if(AnalyseProduct.get(product.getName()+"_"+product.getIdType()+"_"+product.getId())==null)
                                        {
                                            AnalyseProduct.put(product.getName()+"_"+product.getIdType()+"_"+product.getId(),product.getQuantity());
                                        }
                                        else
                                        {
                                            AnalyseProduct.put(product.getName()+"_"+product.getIdType()+"_"+product.getId(),
                                                                AnalyseProduct.get(product.getName()+"_"+product.getIdType()+"_"+product.getId()) +product.getQuantity() );
                                        }
                                    }
                                }
                                if (e.getKey().substring(0,7).compareTo(idBill)>0){
                                    break;
                                }
                            }
                        }
                        if(orderCount==0)
                        {
                            lv_hotProduct.setVisibility(View.INVISIBLE);
                            container_noHotProduct.setVisibility(View.VISIBLE);
                        }
                        else {
                            Set<Map.Entry<String, Integer>> set = AnalyseProduct.entrySet();

                            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
                            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1,
                                                   Map.Entry<String, Integer> o2) {
                                    return o2.getValue().compareTo(o1.getValue());
                                }
                            });
                            ArrayList<Product> products=new ArrayList<Product>();
                            int count=0;
                            for (Map.Entry<String, Integer> e : list) {
                                int idx1=e.getKey().indexOf('_');
                                int idx2=e.getKey().indexOf('_',idx1+1);
                                products.add(new Product(e.getKey().substring(idx2+1),e.getKey().substring(idx1+1,idx2), e.getValue(),e.getKey().substring(0,idx1)));
                                count++;
                                if(count==4) break;
                            }
                            HotProduct_Adapter adapter= new HotProduct_Adapter(products,getActivity());
                            lv_hotProduct.setAdapter(adapter);
                            lv_hotProduct.setVisibility(View.VISIBLE);
                            container_noHotProduct.setVisibility(View.INVISIBLE);


                        }
                        tv_OrderCount.setText(String.valueOf(orderCount));

                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();

                }
            });
        }

        swipeContainer.setRefreshing(false);
    }
    synchronized public void createMonthAnalyse(String storeName)
    {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query ref=mDatabase.child("saleAnalyst").child(storeName);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        tv_typeAnalyse.setText("30 ngày qua");
        synchronized (this) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<Entry> entries = new ArrayList<>();
                    ArrayList<Double> saleData = new ArrayList<>();
                    Double totalWeekSale=0d;
                    final ArrayList<String> Dates = new ArrayList<String>();
                    Calendar now = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
                    String showDate=df.format(now.getTime());
                    now.add(Calendar.DAY_OF_YEAR,-29);
                    tv_noChart.setText("Từ "+df.format(now.getTime())+ " đến "+showDate+" chưa có đơn hàng nào." );
                    tv_totalSale.setText("0");
                    showDate=df.format(now.getTime())+" - "+showDate;
                    tv_Dates.setText(showDate);
                    now.add(Calendar.DAY_OF_YEAR,30);
                    df = new SimpleDateFormat("dd/MM");
                    Dates.add(df.format(now.getTime()));
                    if(snapshot.exists())
                    {
                        saleAnalyst sales = snapshot.getValue(saleAnalyst.class);
                        int Year, Month, Day;
                        //Create data
                        for (int i = 0; i < 30; i++) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -i);
                            Date newDate = cal.getTime();
                            SimpleDateFormat df1 = new SimpleDateFormat("dd/MM");
                            SimpleDateFormat df2 = new SimpleDateFormat("MM");
                            SimpleDateFormat df3 = new SimpleDateFormat("YYYY");
                            cal.setTime(newDate);
                            Year = Integer.parseInt(df3.format(cal.getTime()));
                            Month = Integer.parseInt(df2.format(cal.getTime()));
                            Day = cal.get(Calendar.DAY_OF_MONTH);
                            Double DaySale;
                            try {
                                DaySale = sales.getYearSales().get(df3.format(cal.getTime())+"_").getListOfMSale().get(df2.format(cal.getTime()) +"_").getListOfDSale().get(cal.get(Calendar.DAY_OF_MONTH) +"_").getTotalSale();
                            } catch (Exception e) {
                                DaySale = 0d;
                            }
                            totalWeekSale+=DaySale;
                            saleData.add(DaySale);
                            Dates.add(df1.format(cal.getTime()));
                        }
                        tv_totalSale.setText(String.format("%1$,.0f",totalWeekSale));

                        //Styling the line chart
                        //XAxis
                        XAxis xAxis;
                        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis = mChart.getXAxis();
                        xAxis.setAxisLineColor(Color.parseColor("#D3D3D3"));
                        xAxis.setDrawGridLines(false);
                        xAxis.setTextColor(Color.BLACK);
                        xAxis.enableGridDashedLine(10f, 10f, 0f);
                        //Styling XAxis with string dates
                        IAxisValueFormatter Xformatter = new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                if(30-(int) value>30)
                                    return "";
                                else return Dates.get(30-(int) value);
                            }
                        };
                        xAxis.setAxisMinimum(0);
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(Xformatter);

                        //YAxis
                        YAxis yAxis;
                        yAxis = mChart.getAxisLeft();
                        mChart.getAxisRight().setEnabled(false);
                        yAxis.setAxisLineColor(Color.parseColor("#D3D3D3"));
                        yAxis.setTextColor(Color.BLACK);
                        yAxis.setGranularityEnabled(true);
                        Double max = Collections.max(saleData);
                        if(max==0) max=10000d;
                        final Double jumpStep = (max / 5);
                        yAxis.setLabelCount(5,true);
                        yAxis.setAxisMinimum(0);
                        yAxis.setAxisMaximum(max.floatValue()+jumpStep.floatValue());
                        mChart.setExtraOffsets(0,10,0,0);
                        yAxis.setValueFormatter(new LargeValueFormatter());

                        //create antries
                        for (int i = 0; i < saleData.size(); i++) {
                            if(saleData.get(29-i).floatValue()!=0 )
                                entries.add(new Entry(i, saleData.get(29-i).floatValue()));
                        }

                        //create chart
                        if(entries.size()!=0)
                        {
                            mChart.setVisibility(View.VISIBLE);
                            tv_noChart.setVisibility(View.INVISIBLE);
                            container_noChart.setVisibility(View.INVISIBLE);
                            LineDataSet dataSet = new LineDataSet(entries, "saleAnalyst");
                            dataSet.setColor(Color.parseColor("#3399ff"));
                            dataSet.setDrawCircleHole(false);
                            dataSet.setCircleColor(Color.parseColor("#3399ff"));
                            dataSet.setDrawValues(false);
                            dataSet.setDrawFilled(true);

                            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                            dataSet.setFillDrawable(drawable);
                            LineData data = new LineData(dataSet);
                            mChart.setData(data);
                            mChart.getLegend().setEnabled(false);
                            mChart.animateY(3000, Easing.EasingOption.EaseOutBack);
                            mChart.invalidate();
                        }
                        else{
                            mChart.setVisibility(View.INVISIBLE);
                            tv_noChart.setVisibility(View.VISIBLE);
                            container_noChart.setVisibility(View.VISIBLE);

                        }

                    }
                    else
                    {
                        mChart.setVisibility(View.INVISIBLE);
                        tv_noChart.setVisibility(View.VISIBLE);
                        container_noChart.setVisibility(View.VISIBLE);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });
        }
        ref=mDatabase.child("stores").child(storeName);
        synchronized (this) {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Integer orderCount=0;
                        Store store= dataSnapshot.getValue(Store.class);
                        Calendar now = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
                        String showDate=df.format(now.getTime());
                        now.add(Calendar.DAY_OF_YEAR,-29);
                        tv_noHotProduct.setText("Từ "+df.format(now.getTime())+ " đến "+showDate+" chưa bán mặt hàng nào." );
                        showDate=df.format(now.getTime())+" - "+showDate;
                        HashMap<String,Integer> AnalyseProduct=new HashMap<String,Integer>();
                        df = new SimpleDateFormat("YYYYMMdd");
                        for (int i = 0; i < 30; i++) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -i);
                            String idBill=df.format(cal.getTime());
                            for (Map.Entry<String, Bill> e : store.getListOrders().entrySet()) {
                                if (e.getKey().startsWith(idBill)) {
                                    orderCount++;
                                    List<Product> temp=e.getValue().getProductList();
                                    for(Product product : e.getValue().getProductList())
                                    {
                                        if(product==null) continue;
                                        if(AnalyseProduct.get(product.getName()+"_"+product.getIdType()+"_"+product.getId())==null)
                                        {
                                            AnalyseProduct.put(product.getName()+"_"+product.getIdType()+"_"+product.getId(),product.getQuantity());
                                        }
                                        else
                                        {
                                            AnalyseProduct.put(product.getName()+"_"+product.getIdType()+"_"+product.getId(),
                                                    AnalyseProduct.get(product.getName()+"_"+product.getIdType()+"_"+product.getId()) +product.getQuantity() );
                                        }
                                    }
                                }
                                if (e.getKey().substring(0,7).compareTo(idBill)>0){
                                    break;
                                }
                            }
                        }
                        if(orderCount==0)
                        {
                            lv_hotProduct.setVisibility(View.INVISIBLE);
                            container_noHotProduct.setVisibility(View.VISIBLE);
                        }
                        else {
                            Set<Map.Entry<String, Integer>> set = AnalyseProduct.entrySet();

                            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
                            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1,
                                                   Map.Entry<String, Integer> o2) {
                                    return o2.getValue().compareTo(o1.getValue());
                                }
                            });
                            ArrayList<Product> products=new ArrayList<Product>();
                            int count=0;
                            for (Map.Entry<String, Integer> e : list) {
                                int idx1=e.getKey().indexOf('_');
                                int idx2=e.getKey().indexOf('_',idx1+1);
                                products.add(new Product(e.getKey().substring(idx2+1),e.getKey().substring(idx1+1,idx2), e.getValue(),e.getKey().substring(0,idx1)));
                                count++;
                                if(count==4) break;
                            }
                            HotProduct_Adapter adapter= new HotProduct_Adapter(products,getActivity());
                            lv_hotProduct.setAdapter(adapter);
                            lv_hotProduct.setVisibility(View.VISIBLE);
                            container_noHotProduct.setVisibility(View.INVISIBLE);


                        }
                        tv_OrderCount.setText(String.valueOf(orderCount));

                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();

                }
            });
        }

        swipeContainer.setRefreshing(false);
    }
    synchronized public void createHalfYearAnalyse(String storeName)
    {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query ref=mDatabase.child("saleAnalyst").child(storeName);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        final int[] temp = {0};
        tv_typeAnalyse.setText("6 tháng qua");
        synchronized (this) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<Entry> entries = new ArrayList<>();
                    ArrayList<Double> saleData = new ArrayList<>();
                    Double totalWeekSale=0d;
                    final ArrayList<String> Dates = new ArrayList<String>();
                    Calendar now = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("MM/YYYY");
                    String showDate=df.format(now.getTime());
                    now.add(Calendar.MONTH,-5);
                    tv_noChart.setText("Từ tháng"+df.format(now.getTime())+ " đến tháng "+showDate+" chưa có đơn hàng nào." );
                    tv_totalSale.setText("0");
                    showDate=df.format(now.getTime())+" - "+showDate;
                    tv_Dates.setText(showDate);
                    now.add(Calendar.MONTH,6);
                    df = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
                    Dates.add(df.format(now.getTime()));
                    if(snapshot.exists())
                    {
                        saleAnalyst sales = snapshot.getValue(saleAnalyst.class);
                        int Year, Month, Day;
                        //Create data
                        for (int i = 0; i < 6; i++) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.MONTH, -i);
                            Date newDate = cal.getTime();
                            SimpleDateFormat df1 = new SimpleDateFormat("MMM",Locale.ENGLISH);
                            SimpleDateFormat df2 = new SimpleDateFormat("MM");
                            SimpleDateFormat df3 = new SimpleDateFormat("YYYY");
                            SimpleDateFormat df4 = new SimpleDateFormat("dd");
                            cal.setTime(newDate);
                            Year = Integer.parseInt(df3.format(cal.getTime()));
                            Month = Integer.parseInt(df2.format(cal.getTime()));
                            if(i==0)
                            {
                                temp[0] = temp[0] + Integer.parseInt(df4.format(cal.getTime()));
                            }
                            else
                            {
                                switch(Month)
                                {
                                    case 1:
                                    case 3:
                                    case 5:
                                    case 7:
                                    case 8:
                                    case 10:
                                    case 12:
                                    {
                                        temp[0]+=31;
                                        break;
                                    }
                                    case 4:
                                    case 6:
                                    case 9:
                                    case 11:
                                    {
                                        temp[0]+=30;
                                        break;
                                    }
                                    case 2:
                                    {
                                        if(Year%4==0 && Year%100!=0 || Year%400==0)
                                        {
                                            temp[0]+=29;
                                        }
                                        else
                                        {
                                            temp[0]+=28;
                                        }
                                        break;
                                    }

                                }
                            }
                            Double DaySale;
                            try {
                                DaySale = sales.getYearSales().get(df3.format(cal.getTime())+"_").getListOfMSale().get(df2.format(cal.getTime()) +"_").getTotalSale();
                            } catch (Exception e) {
                                DaySale = 0d;
                            }
                            totalWeekSale+=DaySale;
                            saleData.add(DaySale);
                            Dates.add(df1.format(cal.getTime()));
                        }
                        tv_totalSale.setText(String.format("%1$,.0f",totalWeekSale));

                        //Styling the line chart
                        //XAxis
                        XAxis xAxis;
                        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis = mChart.getXAxis();
                        xAxis.setAxisLineColor(Color.parseColor("#D3D3D3"));
                        xAxis.setDrawGridLines(false);
                        xAxis.setTextColor(Color.BLACK);
                        xAxis.enableGridDashedLine(10f, 10f, 0f);
                        //Styling XAxis with string dates
                        IAxisValueFormatter Xformatter = new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                if(6-(int) value>6)
                                    return "";
                                else return Dates.get(6-(int) value);
                            }
                        };
                        xAxis.setAxisMinimum(0);
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(Xformatter);

                        //YAxis
                        YAxis yAxis;
                        yAxis = mChart.getAxisLeft();
                        mChart.getAxisRight().setEnabled(false);
                        yAxis.setAxisLineColor(Color.parseColor("#D3D3D3"));
                        yAxis.setTextColor(Color.BLACK);
                        yAxis.setGranularityEnabled(true);
                        Double max = Collections.max(saleData);
                        if(max==0) max=10000d;
                        final Double jumpStep = (max / 5);
                        yAxis.setLabelCount(5,true);
                        yAxis.setAxisMinimum(0);
                        yAxis.setAxisMaximum(max.floatValue()+jumpStep.floatValue());
                        mChart.setExtraOffsets(0,10,0,0);
                        yAxis.setValueFormatter(new LargeValueFormatter());

                        //create antries
                        for (int i = 0; i < saleData.size(); i++) {
                            if(saleData.get(5-i).floatValue()!=0 )
                                entries.add(new Entry(i, saleData.get(5-i).floatValue()));
                        }

                        //create chart
                        if(entries.size()!=0)
                        {
                            mChart.setVisibility(View.VISIBLE);
                            tv_noChart.setVisibility(View.INVISIBLE);
                            container_noChart.setVisibility(View.INVISIBLE);
                            LineDataSet dataSet = new LineDataSet(entries, "saleAnalyst");
                            dataSet.setColor(Color.parseColor("#3399ff"));
                            dataSet.setDrawCircleHole(false);
                            dataSet.setCircleColor(Color.parseColor("#3399ff"));
                            dataSet.setDrawValues(false);
                            dataSet.setDrawFilled(true);

                            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                            dataSet.setFillDrawable(drawable);
                            LineData data = new LineData(dataSet);
                            mChart.setData(data);
                            mChart.getLegend().setEnabled(false);
                            mChart.animateY(3000, Easing.EasingOption.EaseOutBack);
                            mChart.invalidate();
                        }
                        else{
                            mChart.setVisibility(View.INVISIBLE);
                            tv_noChart.setVisibility(View.VISIBLE);
                            container_noChart.setVisibility(View.VISIBLE);

                        }

                    }
                    else
                    {
                        mChart.setVisibility(View.INVISIBLE);
                        tv_noChart.setVisibility(View.VISIBLE);
                        container_noChart.setVisibility(View.VISIBLE);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });
        }
        ref=mDatabase.child("stores").child(storeName);
        synchronized (this) {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Integer orderCount=0;
                        Store store= dataSnapshot.getValue(Store.class);
                        Calendar now = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
                        String showDate=df.format(now.getTime());
                        now.add(Calendar.DAY_OF_YEAR,-temp[0]-1);
                        tv_noHotProduct.setText("Từ "+df.format(now.getTime())+ " đến "+showDate+" chưa bán mặt hàng nào." );
                        showDate=df.format(now.getTime())+" - "+showDate;
                        HashMap<String,Integer> AnalyseProduct=new HashMap<String,Integer>();
                        df = new SimpleDateFormat("YYYYMMdd");
                        for (int i = 0; i < temp[0]; i++) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -i);
                            String idBill=df.format(cal.getTime());
                            for (Map.Entry<String, Bill> e : store.getListOrders().entrySet()) {
                                if (e.getKey().startsWith(idBill)) {
                                    orderCount++;
                                    List<Product> temp=e.getValue().getProductList();
                                    for(Product product : e.getValue().getProductList())
                                    {
                                        if(product==null) continue;
                                        if(AnalyseProduct.get(product.getName()+"_"+product.getIdType()+"_"+product.getId())==null)
                                        {
                                            AnalyseProduct.put(product.getName()+"_"+product.getIdType()+"_"+product.getId(),product.getQuantity());
                                        }
                                        else
                                        {
                                            AnalyseProduct.put(product.getName()+"_"+product.getIdType()+"_"+product.getId(),
                                                    AnalyseProduct.get(product.getName()+"_"+product.getIdType()+"_"+product.getId()) +product.getQuantity() );
                                        }
                                    }
                                }
                                if (e.getKey().substring(0,7).compareTo(idBill)>0){
                                    break;
                                }
                            }
                        }
                        if(orderCount==0)
                        {
                            lv_hotProduct.setVisibility(View.INVISIBLE);
                            container_noHotProduct.setVisibility(View.VISIBLE);
                        }
                        else {
                            Set<Map.Entry<String, Integer>> set = AnalyseProduct.entrySet();

                            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
                            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1,
                                                   Map.Entry<String, Integer> o2) {
                                    return o2.getValue().compareTo(o1.getValue());
                                }
                            });
                            ArrayList<Product> products=new ArrayList<Product>();
                            int count=0;
                            for (Map.Entry<String, Integer> e : list) {
                                int idx1=e.getKey().indexOf('_');
                                int idx2=e.getKey().indexOf('_',idx1+1);
                                products.add(new Product(e.getKey().substring(idx2+1),e.getKey().substring(idx1+1,idx2), e.getValue(),e.getKey().substring(0,idx1)));
                                count++;
                                if(count==4) break;
                            }
                            HotProduct_Adapter adapter= new HotProduct_Adapter(products,getActivity());
                            lv_hotProduct.setAdapter(adapter);
                            lv_hotProduct.setVisibility(View.VISIBLE);
                            container_noHotProduct.setVisibility(View.INVISIBLE);


                        }
                        tv_OrderCount.setText(String.valueOf(orderCount));

                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();

                }
            });
        }

        swipeContainer.setRefreshing(false);
    }


}
