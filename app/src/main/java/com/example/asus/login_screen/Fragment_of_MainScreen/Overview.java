package com.example.asus.login_screen.Fragment_of_MainScreen;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.login_screen.Model.Store;
import com.example.asus.login_screen.MyMarkerView;
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
import com.github.mikephil.charting.utils.ViewPortHandler;
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
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Overview extends Fragment {

    private LineChart mChart;
    private SwipeRefreshLayout swipeContainer;
    private TextView tv_typeAnalyse,tv_Dates,tv_noChart,tv_totalSale,tv_OrderCount;
    private LinearLayout container_noChart;
    public Overview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_overview, container, false);
        swipeContainer=(SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        mChart = (LineChart)v.findViewById(R.id.chart);
        tv_typeAnalyse=(TextView) v.findViewById(R.id.tv_typeAnalyse);
        tv_Dates=(TextView) v.findViewById(R.id.tv_Date);
        tv_noChart=(TextView) v.findViewById(R.id.tv_noChart);
        tv_totalSale=(TextView) v.findViewById(R.id.tv_totalSale);
        tv_OrderCount=(TextView) v.findViewById(R.id.tv_OrderCount);
        container_noChart=(LinearLayout) v.findViewById(R.id.container_noChart);
        IMarker marker = new MyMarkerView(getActivity().getBaseContext(), R.layout.custom_marker_view);
        MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
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
            createWeekChart("def");
        }
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createWeekChart("def");
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
    synchronized public void createWeekChart(String storeName)
    {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query ref=mDatabase.child("stores").orderByChild("shopName").equalTo(storeName);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        synchronized (this) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        ArrayList<Entry> entries = new ArrayList<>();
                        ArrayList<Double> Sale = new ArrayList<>();
                        ArrayList<Double> saleData = new ArrayList<>();
                        Double totalWeekSale=0d;
                        Integer orderCount=0;
                        final ArrayList<String> Dates = new ArrayList<String>();
                        Store store = postSnapshot.getValue(Store.class);
                        Calendar now = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
                        String showDate=df.format(now.getTime());
                        now.add(Calendar.DAY_OF_YEAR,-6);
                        tv_noChart.setText("Từ "+df.format(now.getTime())+ " đến "+showDate+" chưa có đơn hàng nào." );
                        showDate=df.format(now.getTime())+" - "+showDate;
                        tv_Dates.setText(showDate);
                        now.add(Calendar.DAY_OF_YEAR,7);
                        df = new SimpleDateFormat("dd/MM");
                        Dates.add(df.format(now.getTime()));
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
                                DaySale = store.getYearSales().get(Year+"_").getListOfMSale().get(Month +"_").getListOfDSale().get(Day +"_").getTotalSale();
                            } catch (Exception e) {
                                DaySale = 0d;
                            }
                            try{
                                orderCount+=store.getYearSales().get(Year+"_").getListOfMSale().get(Month +"_").getListOfDSale().get(Day +"_").getListOfOrder().size();
                            }catch (Exception e)
                            {
                                orderCount+=0;
                            }
                            totalWeekSale+=DaySale;
                            Sale.add(DaySale);
                            Dates.add(df1.format(cal.getTime()));
                        }
                        tv_totalSale.setText(String.format("%1$,.0f",totalWeekSale));
                        tv_OrderCount.setText(String.valueOf(orderCount));
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

                                return Dates.get(7-(int) value);
                            }


                        };
                        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                        xAxis.setValueFormatter(Xformatter);
                        //YAxis
                        YAxis yAxis;
                        yAxis = mChart.getAxisLeft();
                        mChart.getAxisRight().setEnabled(false);
                        yAxis.setAxisLineColor(Color.parseColor("#D3D3D3"));
                        yAxis.setTextColor(Color.BLACK);
                        yAxis.setGranularityEnabled(true);



                        Double max = Collections.max(Sale);
                        if(max==0) max=10000d;
                        final Double jumpStep = (max / 5);

                        yAxis.setLabelCount(5,true);
                        yAxis.setAxisMinimum(0);
                        yAxis.setAxisMaximum(max.floatValue()+jumpStep.floatValue());
                        mChart.setExtraOffsets(0,10,0,0);


                        yAxis.setValueFormatter(new LargeValueFormatter());
                        //create antries
                        for (int i = 0; i < Sale.size(); i++) {
                            if(Sale.get(6-i).floatValue()!=0 )
                                entries.add(new Entry(i, Sale.get(6-i).floatValue()));
                        }
                        progressDialog.dismiss();
                        if(entries.size()!=0)
                        {
                            mChart.setVisibility(View.VISIBLE);
                            tv_noChart.setVisibility(View.INVISIBLE);
                            container_noChart.setVisibility(View.INVISIBLE);
                            LineDataSet dataSet = new LineDataSet(entries, "Sale");
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
                        swipeContainer.setRefreshing(false);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });
        }
    }



}
