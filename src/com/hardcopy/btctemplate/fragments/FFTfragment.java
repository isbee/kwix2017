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

    // Fragment는 default 생성자를 권장
    // 혹여 생성자에 parameter를 사용하고 싶다면 default를 하나 만들어 놓아도 작동할 듯
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


        //data = new BarData(BardataSet);	// 블루투스 연결 되기 전에는 그래프를 그리지 않기로 함
        data = new BarData();
        data.setDrawValues(false);
        FFTchart.setData(data);
//		FFTchart.setDrawValueAboveBar(false);
//		FFTchart.setBackgroundResource(drawable.chart_background2);

        // 원래 뇌파를 구분할 때 limitline을 사용했었다
       /* LimitLine setaline = new LimitLine(14, "Theta-wave");
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


		FFTchart.getXAxis().addLimitLine(setaline);
		FFTchart.getXAxis().addLimitLine(betaline);
		FFTchart.getXAxis().addLimitLine(alphaline);
		FFTchart.getXAxis().setDrawLimitLinesBehindData(true);*/

        FFTchart.setDescription(null);
        FFTchart.getLegend().setTextSize(14.0f);
        FFTchart.getAxisLeft().setDrawLabels(false);
        FFTchart.getAxisRight().setDrawLabels(false);
        FFTchart.getAxisLeft().setAxisMaximum(80.0f);

        FFTchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        FFTchart.getXAxis().setDrawLabels(false);
        FFTchart.setScaleEnabled(false);
        //	FFTchart.setPinchZoom(false);



        FFTchart.getXAxis().setDrawAxisLine(false);
        FFTchart.getAxisLeft().setDrawAxisLine(false);
        FFTchart.getAxisRight().setDrawAxisLine(false);


        return rootView;
    }

    // 라이브러리에서 제공하는 MarkerView라는 것이 있다. 이것은 bar나 line, 혹은 어떤 value값등의 어떤 구성요소를 클릭했을 때(highlighted)
    // 그곳의 위치를 받아와서 mark를 달아주는 것이다.
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
            // 최대 64개의 fft data가 들어오며, 아두이노 code 변경이 딱히 없다면 현재 0~30hz 주파수 대역 이므로, 단순하게 /2 로 주파수를 나눴다
            tvContent.setText((intVal + 8) / 2 + "Hz");

            // this will perform necessary layouting
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {

            if (mOffset == null) {
                // center the marker horizontally and vertically
                // 그래프의 어느 위치에 plot 할지 수치를 통해 변경 가능하다
                mOffset = new MPPointF(-(getWidth() / 2), -getHeight() * 0.8f);
            }

            return mOffset;
        }
    }
}

