package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.retrofit.ApiInstance;
import com.sam_chordas.android.stockhawk.retrofit.GraphStock;
import com.sam_chordas.android.stockhawk.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Semmiverian on 6/20/16.
 */
public class StockGraphActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {
    private BarChart mChart;
    private String symbol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        mChart = (BarChart) findViewById(R.id.chart);


        if (getIntent().getExtras() != null) {
            symbol = getIntent().getExtras().getString("symbol");
        }
        setChart();
        setChartData();
//        BarDataSet set1;
//        ArrayList<String> xVals = new ArrayList<String>();
//        for (int i = 0; i < 10; i++) {
//            xVals.add("asd");
//        }
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//
//        for (int i = 0; i < 10; i++) {
//            float day = i+10;
//            yVals1.add(new BarEntry(day, i));
//        }
//
//        set1 = new BarDataSet(yVals1, "DataSet");
//        set1.setBarSpacePercent(35f);
//        set1.setColors(ColorTemplate.MATERIAL_COLORS);
//
//        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//        dataSets.add(set1);
//
//        BarData data = new BarData(xVals, dataSets);
//        data.setValueTextSize(10f);
//
//        mChart.setData(data);


    }

    private void setChart() {
        assert mChart != null;
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(20);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
    }

    private void setChartData() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ApiInstance.API_INSTANCE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        RetrofitInterface retrofitInterface = retrofit2.create(RetrofitInterface.class);
        Call<GraphStock> call = retrofitInterface.checkGraphSymbol("select * from yahoo.finance.historicaldata where symbol ='" + symbol + "' and startDate ='2016-06-10' and endDate ='2016-06-20'", "json", "diagnostics", "store://datatables.org/alltableswithkeys");

        call.enqueue(new Callback<GraphStock>() {
            @Override
            public void onResponse(Call<GraphStock> call, Response<GraphStock> response) {
                if (response.isSuccessful()) {
                    mChart.invalidate();
                    GraphStock stock = response.body();
                    GraphStock.Query query = stock.getQuery();
                    GraphStock.Result result = query.getResult();
                    List<GraphStock.Quote> quotes = result.getQuotes();
                    BarDataSet set1;
                    ArrayList<String> xVals = new ArrayList<String>();
                    for(GraphStock.Quote quote : quotes){
                        xVals.add(quote.getDate());
                    }
                    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

                    for (int i = 0; i < quotes.size(); i++) {
                        float day = i+10;
                        yVals1.add(new BarEntry(quotes.get(i).getOpen(),i));
                    }


                    set1 = new BarDataSet(yVals1, "DataSet");
                    set1.setBarSpacePercent(35f);
                    set1.setColors(ColorTemplate.MATERIAL_COLORS);

                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);

                    BarData data = new BarData(xVals, dataSets);
                    data.setValueTextSize(10f);

                    mChart.setData(data);



                }
            }

            @Override
            public void onFailure(Call<GraphStock> call, Throwable t) {
                Log.e("error", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
