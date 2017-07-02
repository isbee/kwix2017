/*
 * Copyright (C) 2014 Bluetooth Connection Template
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hardcopy.btctemplate.fragments;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hardcopy.btctemplate.MainActivity;
import com.hardcopy.btctemplate.R;
import com.xw.repo.BubbleSeekBar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class ExampleFragment extends Fragment {

    public static XYMultipleSeriesDataset mDataset;
    public static XYMultipleSeriesRenderer mRenderer;
    public static XYSeries mCurrentSeries;
    public static XYSeriesRenderer mCurrentRenderer;
    public static GraphicalView mChartView;
    public static BubbleSeekBar seekBar;
    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;
    private Handler mActivityHandler = null;

    //public static Button

//	public ExampleFragment(Context c, IFragmentListener l, Handler h) {
//		mContext = c;
//		mFragmentListener = l;
//		mActivityHandler = h;
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);

        mContext = getContext();

        Log.d("짜잔", "생성");
        mDataset = new XYMultipleSeriesDataset();
        mRenderer = new XYMultipleSeriesRenderer();

        String seriesTitle = "EEG Analyzer";
        XYSeries series = new XYSeries(seriesTitle);

        mDataset.addSeries(series);
        mCurrentSeries = series;
        // create a new renderer for the new series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);


        // set some renderer properties
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        //renderer.setDisplayChartValues(true);
        //renderer.setDisplayChartValuesDistance(1);
        mCurrentRenderer = renderer;
        mCurrentRenderer.setLineWidth(4);
        mCurrentRenderer.setColor(Color.parseColor("#ff7466"));


        mRenderer.setApplyBackgroundColor(true);//배경색 사용 여부
        mRenderer.setBackgroundColor(Color.TRANSPARENT);//배경색 설정
        mRenderer.setAxisTitleTextSize(30);//축 글씨 크기
//		 mRenderer.setChartTitleTextSize(20);//차트 제목 글씨 크기
        mRenderer.setLabelsTextSize(30);//라벨 글씨 크기

        //mRenderer.setLegendTextSize(40);//하단 그래프 이름 글씨 크기
        mRenderer.setMargins(new int[]{80, 30, 30, 30});//위아래 여백
        //	 mRenderer.setZoomButtonsVisible(false);//줌 버튼 표시 여부

        mRenderer.setPointSize(1);//그래프 포인트 크기

        // if (mChartView == null) {
        RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.waveLayout);
        //      RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(
        //              LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mChartView = ChartFactory.getLineChartView(mContext, mDataset, mRenderer);
        // enable the chart click events
        mRenderer.setClickEnabled(false);
        //mRenderer.setSelectableBuffer(10);
        //mRenderer.setApplyBackgroundColor(true);
        //mRenderer.setYLabelsColor(0, Color.DKGRAY);
        mRenderer.setShowLegend(false);
        //     mRenderer.setFitLegend(true);
        //mRenderer.setLegendHeight(400);
        mRenderer.setMarginsColor(color.white);
        mRenderer.setYAxisMax(1100);//Y축 최대값
        mRenderer.setYAxisMin(500);//Y축 최소값
        mRenderer.setXAxisMin(0);//X축 최소값
        mRenderer.setXAxisMax(MainActivity.Scale);//X축 최대값
        mRenderer.setZoomEnabled(false, false);
        //     mRenderer.setExternalZoomEnabled(false);//확대 방지
        //mRenderer.setZoomLimits(new double[]{10,10,0,0});
        mRenderer.setPanEnabled(false);//그래프 뷰 이동 방지
        //mRenderer.setPanLimits(new double[] {10,10,10,10});
        mRenderer.setShowGrid(false);

        mRenderer.setGridColor(Color.parseColor("#c9c9c9"));
        //mRenderer.setXLabelsColor(color.white);
        mRenderer.setXTitle("Time(sec)");
        mRenderer.setChartTitle("Brain Wave");
        //mRenderer.setAxesColor(Color.parseColor("#00c9c9"));
        mRenderer.setChartTitleTextSize(50);
        mRenderer.setXLabels(0);
        // mRenderer.setXLabelsColor(color.white);
        // mRenderer.setInScroll(true);
        mRenderer.setShowLabels(true, false);
        mRenderer.addXTextLabel(0, "0.0");
        //  mRenderer.addXTextLabel(25, "0.025");
        //  mRenderer.addXTextLabel(50, "0.050");
        //  mRenderer.addXTextLabel(75, "0.075");
        //  mRenderer.addXTextLabel(100, "0.1");
        mRenderer.addXTextLabel(MainActivity.Scale, "5.0");
        mRenderer.setAxisTitleTextSize(40);
        mRenderer.setLabelsTextSize(40);
        mRenderer.setLabelsColor(Color.parseColor("#0066ff"));
        mRenderer.setAxesColor(Color.parseColor("#322254"));
        mRenderer.setXLabelsColor(Color.parseColor("#322254"));


        layout.addView(mChartView, RelativeLayout.LayoutParams.MATCH_PARENT,
                1200);


        seekBar = (BubbleSeekBar) rootView.findViewById(R.id.seekBar);
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                Log.e("change", String.valueOf(progress));
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                Log.e("actionUp", String.valueOf(progress));
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {
                Log.e("final", String.valueOf(progress));

                if (progress == 1) {
                    mRenderer.clearXTextLabels();
                    MainActivity.Scale = 4000;
                    mRenderer.setXAxisMax(MainActivity.Scale);
                    mRenderer.addXTextLabel(0, "0.0");
                    mRenderer.addXTextLabel(MainActivity.Scale, "5.0");
                } else if (progress == 2) {
                    mRenderer.clearXTextLabels();
                    MainActivity.Scale = 800;        // 1 hz
                    mRenderer.setXAxisMax(MainActivity.Scale);
                    mRenderer.addXTextLabel(0, "0.0");
                    mRenderer.addXTextLabel(MainActivity.Scale, "1.0");
                } else if (progress == 3) {
                    mRenderer.clearXTextLabels();
                    MainActivity.Scale = 160;        // 5 hz
                    mRenderer.setXAxisMax(MainActivity.Scale);
                    mRenderer.addXTextLabel(0, "0.0");
                    mRenderer.addXTextLabel(MainActivity.Scale, "0.2");
                } else if (progress == 4) {
                    mRenderer.clearXTextLabels();
                    MainActivity.Scale = 80;        // 10 hz
                    mRenderer.setXAxisMax(MainActivity.Scale);
                    mRenderer.addXTextLabel(0, "0.0");
                    mRenderer.addXTextLabel(MainActivity.Scale, "0.1");
                } else if (progress == 5) {
                    mRenderer.clearXTextLabels();
                    MainActivity.Scale = 40;        // 20hz
                    mRenderer.setXAxisMax(MainActivity.Scale);
                    mRenderer.addXTextLabel(0, "0.0");
                    mRenderer.addXTextLabel(MainActivity.Scale, "0.05");
                } else if (progress == 6) {
                    mRenderer.clearXTextLabels();
                    MainActivity.Scale = 30;        // 30hz
                    mRenderer.setXAxisMax(MainActivity.Scale);
                    mRenderer.addXTextLabel(0, "0.0");
                    mRenderer.addXTextLabel(MainActivity.Scale, "0.0333");
                }

            }
        });

        return rootView;
    }


}
