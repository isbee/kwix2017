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

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hardcopy.btctemplate.MainActivity;
import com.hardcopy.btctemplate.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DrowsinessFragment extends Fragment {

    public static TextView text, IndexText;
    public static ImageView emoji, setting;
    public static LineChart DrowsinessChart;
    public static LineData DrowData;
    public static LineDataSet set;
    EditText editText;
    RadioGroup radioGroup;
    Button checkButton;
    myDialogFragment editNameDialog = new myDialogFragment();
    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;


//	public DrowsinessFragment(Context c, IFragmentListener l) {
//		mContext = c;
//		mFragmentListener = l;
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View rootView = inflater.inflate(R.layout.fragment_drowsiness, container, false);

        DrowsinessChart = (LineChart) rootView.findViewById(R.id.DrowsinessChart);

        DrowSinessMarkerView DrowMV = new DrowSinessMarkerView(mContext, R.layout.drowchart_marker);
        DrowsinessChart.setMarker(DrowMV);

        text = (TextView) rootView.findViewById(R.id.Time);
        text.setVisibility(View.INVISIBLE);
        IndexText = (TextView) rootView.findViewById(R.id.Index2);
        emoji = (ImageView) rootView.findViewById(R.id.DrowImage);
        setting = (ImageView) rootView.findViewById(R.id.setting);


        setting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                editNameDialog.show(getFragmentManager(), "Set Drowsiness Reference Value");

            }
        });


        set = new LineDataSet(null, "");

        DrowData = new LineData();
        DrowData.addDataSet(set);
        DrowData.setDrawValues(false);


        //	set.disableDashedHighlightLine();		// ???? ????????? ????????? ???
        set.setHighLightColor(Color.parseColor("#00000000"));
        set.setDrawCircles(false);
        set.setColor(Color.parseColor("#45be54"));
        set.setLineWidth(2.5f);


        //DrowsinessChart.setVisibleYRangeMaximum(100, AxisDependency.LEFT);

        LimitLine Drowsinessline = new LimitLine(38, "Wake up!!!");
        Drowsinessline.setTextSize(10);
        Drowsinessline.setLineColor(Color.parseColor("#ff4444"));
        Drowsinessline.setLineWidth(3.0f);

        DrowsinessChart.setScaleYEnabled(false);
        //	DrowsinessChart.setScaleMinima(2f, 5f);
        DrowsinessChart.getAxisLeft().addLimitLine(Drowsinessline);
        DrowsinessChart.getXAxis().setDrawAxisLine(false);
        DrowsinessChart.getXAxis().setGranularity(10000f);    // ??? index?????? ??? ??????? ???? ??? ?ð? ???????? ????


        //       DrowsinessChart.getXAxis().setDrawLabels(false);
        DrowsinessChart.getAxisLeft().setDrawAxisLine(false);
        DrowsinessChart.getAxisRight().setDrawAxisLine(false);
        DrowsinessChart.setDescription(null);
        DrowsinessChart.getLegend().setEnabled(false);
        DrowsinessChart.setData(DrowData);

        return rootView;
    }

    public class DrowSinessMarkerView extends MarkerView {

        private TextView tvContent, tvContent2;
        private MPPointF mOffset;

        public DrowSinessMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);

            // find your layout components
            tvContent = (TextView) findViewById(R.id.tvContent);
            tvContent.setTextColor(Color.parseColor("#064293"));
            tvContent2 = (TextView) findViewById(R.id.tvContent2);
            tvContent2.setTextColor(Color.parseColor("#064293"));
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            int intVal = (int) e.getY();

            long now = MainActivity.TimeData.get((int) e.getX());
            Date date = new Date(now);

            SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String strNow = sdfNow.format(date);

            //  tvContent.setText("???: " + intVal+", ?ð?: " + "" +strNow);
            tvContent.setText("Figure: " + intVal);
            tvContent2.setText("Time: " + strNow);

            // this will perform necessary layouting
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {

            if (mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = new MPPointF(-(getWidth() / 2) * 0.6f, -getHeight() * 1.2f);
            }

            return mOffset;
        }
    }

    public class myDialogFragment extends DialogFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_dialog, container);
            getDialog().setTitle("Drowsiness Limiting Value");
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            radioGroup = (RadioGroup) view.findViewById(R.id.whatCase);

            editText = (EditText) view.findViewById(R.id.setReference);
            editText.setEnabled(false);

            radioGroup.check(R.id.tabEEGs);

            radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // TODO Auto-generated method stub
                    if (checkedId == R.id.tabEEGs) editText.setEnabled(false);
                    else {
                        editText.setEnabled(true);
                        /*
						editText.setOnKeyListener(new OnKeyListener() {
							
							@Override
							public boolean onKey(View v, int keyCode, KeyEvent event) {
								// TODO Auto-generated method stub
								 if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
								 {
									 int val = Integer.parseInt(editText.getText().toString());

								 }
								return false;
							}
						});   */
                    }

                }
            });

            checkButton = (Button) view.findViewById(R.id.check);
            checkButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (editText.isEnabled() == true) {
                        if (!editText.getText().toString().isEmpty()) {
                            int val = Integer.parseInt(editText.getText().toString());
                            MainActivity.RefVal = val;

                            DrowsinessChart.getAxisLeft().removeAllLimitLines();

                            LimitLine Drowsinessline = new LimitLine(val, "Wake up!!!");
                            Drowsinessline.setTextSize(10);
                            Drowsinessline.setLineColor(Color.parseColor("#fd0202"));
                            Drowsinessline.setLineWidth(3.0f);

                            DrowsinessChart.getAxisLeft().addLimitLine(Drowsinessline);

                        }
                    } else {
                        // ?????? ?????? ????? ??? ???
                    }
                    editNameDialog.dismiss();
                }
            });

            return view;
        }

    }
}
