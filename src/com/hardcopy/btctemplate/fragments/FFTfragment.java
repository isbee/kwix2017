package com.hardcopy.btctemplate.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hardcopy.btctemplate.R;

import java.util.ArrayList;


public class FFTfragment extends Fragment {

    public static TextView text;
    //	public static LineChart FFTchart;
//	public static LineData data = new LineData();
    public static BarChart FFTchart;
    public static BarData data;
    public static ArrayList<BarEntry> BarEntry = new ArrayList<>();
    public static BarDataSet BardataSet;
    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;
    private Handler mActivityHandler = null;


//	public FFTfragment(Context c, IFragmentListener l) {
//		mContext = c;
//		mFragmentListener = l;
//
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View rootView = inflater.inflate(R.layout.fragment_fft, container, false);

        FFTchart = (BarChart) rootView.findViewById(R.id.bar_chart2);
        text = (TextView) rootView.findViewById(R.id.Hz);
        text.setVisibility(View.INVISIBLE);

        FFTMarkerView mv = new FFTMarkerView(mContext, R.layout.fftchart_marker);
        //	FFTchart.setMarkerView(mv);
        FFTchart.setMarker(mv);

        //ArrayList<BarEntry> BarEntry2 = new ArrayList<>();
        /*
		BarEntry.add(new BarEntry(0, 10f));
		BarEntry.add(new BarEntry(1, 1f));
		BarEntry.add(new BarEntry(2, 2f));
		BarEntry.add(new BarEntry(3, 7f));
		BarEntry.add(new BarEntry(4, 6f));
		BarEntry.add(new BarEntry(5, 8f));
		
		BardataSet = new BarDataSet(BarEntry, "Roading...");
		
		ArrayList<String> labels2 = new ArrayList<>();
		
		labels2.add("January");
		labels2.add("February");
		labels2.add("March");
		labels2.add("April");
		labels2.add("May");
		labels2.add("June");
		
		ArrayList<String> labels = new ArrayList<>();
		
		labels.add(10, "5");
		labels.add(20, "10");
		labels.add(30, "15");
		labels.add(40, "20");
		labels.add(50, "25");
		labels.add(60, "30"); 
		
		BardataSet.setColors(ColorTemplate.COLORFUL_COLORS);   */

        //data = new BarData(BardataSet);	// 블루투스 연결 되기 전에는 그래프를 그리지 않기로 함
        data = new BarData();
        data.setDrawValues(false);
        FFTchart.setData(data);
//		FFTchart.setDrawValueAboveBar(false);
//		FFTchart.setBackgroundResource(drawable.chart_background2);

        LimitLine setaline = new LimitLine(14, "Theta-wave");
        LimitLine alphaline = new LimitLine(26, "\nAlpha-wave");
        LimitLine betaline = new LimitLine(54, "Beta-wave");

        setaline.setLabelPosition(LimitLabelPosition.LEFT_TOP);
        alphaline.setLabelPosition(LimitLabelPosition.LEFT_TOP);
        betaline.setLabelPosition(LimitLabelPosition.LEFT_TOP);

        setaline.setTextSize(10);
        setaline.setLineColor(Color.parseColor("#ff6f69"));
        setaline.setLineWidth(2.5f);
        betaline.setTextSize(10);
        betaline.setLineColor(Color.parseColor("#6f69ff"));
        betaline.setLineWidth(2.5f);
        alphaline.setTextSize(10);
        alphaline.setLineColor(Color.parseColor("#ffcc5c"));
        alphaline.setLineWidth(2.5f);


//		FFTchart.getXAxis().addLimitLine(setaline);
//		FFTchart.getXAxis().addLimitLine(betaline);
//		FFTchart.getXAxis().addLimitLine(alphaline);
//		FFTchart.getXAxis().setDrawLimitLinesBehindData(true);
        FFTchart.setDescription(null);
        FFTchart.getLegend().setTextSize(14.0f);
        FFTchart.getAxisLeft().setDrawLabels(false);
        FFTchart.getAxisRight().setDrawLabels(false);

        FFTchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        FFTchart.getXAxis().setDrawLabels(false);
        FFTchart.setScaleEnabled(false);
        //	FFTchart.setPinchZoom(false);

        //FFTchart.getXAxis()
		/*
		FFTchart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
			
			@Override
			public String getFormattedValue(float arg0, AxisBase arg1) {
				// TODO Auto-generated method stub
				return null;
			}
		}); */


        FFTchart.getXAxis().setDrawAxisLine(false);
        FFTchart.getAxisLeft().setDrawAxisLine(false);
        FFTchart.getAxisRight().setDrawAxisLine(false);
		
		/*
		FFTchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
		    @Override
		    public void onValueSelected(Entry e, Highlight h) {
		        FFTchart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), FFTchart.getXAxis());
		       // FFTchart.highlightValue(h);
		    }

		    @Override
		    public void onNothingSelected() {

		    }

		}); */        //OnChart~ 구현
		
		/*
		FFTchart.setOnChartGestureListener(new OnChartGestureListener() {
			
			@Override
			public void onChartTranslate(MotionEvent arg0, float arg1, float arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChartSingleTapped(MotionEvent arg0) {
				// TODO Auto-generated method stub
				float tappedX = arg0.getX();
			    float tappedY = arg0.getY();
			    Toast toast = Toast.makeText(mContext, ""+, duration)
			}
			
			@Override
			public void onChartScale(MotionEvent arg0, float arg1, float arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChartLongPressed(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChartGestureStart(MotionEvent arg0, ChartGesture arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChartGestureEnd(MotionEvent arg0, ChartGesture arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChartFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChartDoubleTapped(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		}); */


        //	FFTchart.animateXY(500, 500);
		
		
		
		
		
		
		
		
		/*
		FFTchart = (LineChart) rootView.findViewById(R.id.FFFchart);
		//FFTchart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);
		
		
		//FFTchart.buildLayer();
		//FFTchart.setVisibleXRangeMaximum(100);
		
		
		//XAxis xl = FFTchart.getXAxis();
		//xl.setAxisMaxValue(100f);
		//xl.setAvoidFirstLastClipping(true);
		
		//YAxis leftAxis = FFTchart.getAxisLeft();
		//leftAxis.setAxisMaxValue(1000f);
        //leftAxis.setAxisMinValue(0f);
        
        ArrayList<Entry> LineEntry2 = new ArrayList<>();
		
        for(int e=0; e<100; e++){
        	LineEntry2.add(new Entry(e,(float) (Math.random() * 40) + 100f));
    	}
		
		
		LineDataSet dataSet2 = new LineDataSet(LineEntry2, "Projects");
		
		
		
		ArrayList<String> labels2 = new ArrayList<>();
		for(int e=0; e<100; e++){
        	labels2.add(String.valueOf(e));
    	}
			
		
		//dataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
		
		data = new LineData();
		
		
		FFTchart.setData(data);
		//FFTchart.animateXY(2000, 2000);   */


        return rootView;
    }


    public class FFTMarkerView extends MarkerView {

        private TextView tvContent;
        private MPPointF mOffset;

        public FFTMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);

            // find your layout components
            tvContent = (TextView) findViewById(R.id.tvContent);
            tvContent.setTextColor(Color.parseColor("#064293"));
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            int intVal = (int) e.getX();
            tvContent.setText(intVal / 2 + "Hz");

            // this will perform necessary layouting
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {

            if (mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = new MPPointF(-(getWidth() / 2), -getHeight() * 0.8f);
            }

            return mOffset;
        }
    }
}

