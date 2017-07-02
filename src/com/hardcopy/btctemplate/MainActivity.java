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

package com.hardcopy.btctemplate;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hardcopy.btctemplate.fragments.DrowsinessFragment;
import com.hardcopy.btctemplate.fragments.ExampleFragment;
import com.hardcopy.btctemplate.fragments.FFTfragment;
import com.hardcopy.btctemplate.fragments.FragmentAdapter;
import com.hardcopy.btctemplate.fragments.IFragmentListener;
import com.hardcopy.btctemplate.service.BTCTemplateService;
import com.hardcopy.btctemplate.utils.AppSettings;
import com.hardcopy.btctemplate.utils.Constants;
import com.hardcopy.btctemplate.utils.Logs;
import com.hardcopy.btctemplate.utils.RecycleUtils;

import org.achartengine.model.XYSeries;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, IFragmentListener {

    public final static int FFTsampleNum = 256;
    // Debugging
    private static final String TAG = "RetroWatchActivity";
    public static ArrayList<Entry> FFTEntry = new ArrayList<>();
    public static List<Long> TimeData = new ArrayList<Long>();

    // Global
    public static LineData data;
    public static LineDataSet dataSet;
    public static BarChart FFTchart;
    public static BarData data2;
    public static ArrayList<BarEntry> BarEntry = new ArrayList<>();
    public static BarDataSet dataSet2;
    public static int Scale = 4000;
    public static boolean DataExist = false;
    public static int RefVal = 38;
    public static int axisTime;
    public static double[] fftData = new double[FFTsampleNum * 2];
    public static int sampleCount = 0;
    public static int sec = 1;
    public static int min = 60;
    public static int hour = 3600;
    public static boolean secType = false;
    public static boolean minType = false;
    public static boolean hourType = false;
    // Variable
    int x = 0;

    //	public static XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
//	public static XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
//	public static XYSeries mCurrentSeries;
//	public static XYSeriesRenderer mCurrentRenderer;
//	public static GraphicalView mChartView;
    int x2 = 0;
    int i = 0;
    double seta = 0;
    double alpha = 0;
    double beta = 0;
    double sav, aav, bav;
    double sleepNum;
    int ii = 0;
    int sampling;
    AudioManager manager;
    Vibrator vibe;
    Uri notification;
    Ringtone r;
    // Context, System
    private Context mContext;
    private BTCTemplateService mService;
    private ActivityHandler mActivityHandler;
    // UI stuff
    private FragmentManager mFragmentManager;
    private FragmentAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ImageView mImageBT = null;
    private ImageView DrowImg;
    private TextView mTextStatus = null;
    private TextView DrowVal;
    private Button btButton = null;
    // Refresh timer
    private Timer mRefreshTimer = null;
    private TextToSpeech myTTS;


    // graph

	
	/*
	BarChart chart;
	ArrayList<BarEntry> BarEntry;
	BarDataSet dataSet;
	ArrayList<String> labels;
	BarData data;    */

    // ???? back??? ?????? ??????? ??? ??? ??????b ??????? ?????? onsaveinstance?? ???????!
    /**
     * Service connection
     */
    // bindService?? ???? ???
    private ServiceConnection mServiceConn = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, "Activity - Service connected");

            mService = ((BTCTemplateService.ServiceBinder) binder).getService();

            // Activity couldn't work with mService until connections are made
            // So initialize parameters and settings here. Do not initialize while running onCreate()
            initialize();
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "Activity - ???? ??????!");
            mService = null;
        }
    };

    /*****************************************************
     * Overrided methods
     ******************************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //----- System, Context
        mContext = this;    //.getApplicationContext();
        mActivityHandler = new ActivityHandler();
        AppSettings.initializeAppSettings(mContext);

        setContentView(R.layout.activity_main);


        //	 Window window = getWindow();
        //   window.addFlags(WindowManager.LayoutParams.beFLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //	 window.addFlags(WindowManager.LayoutParams.);
        //    window.setTitleColor(Color.parseColor("#363b74"));

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b9dc3")));


        // Create the adapter that will return a fragment for each of the primary sections of the app.
        mFragmentManager = getSupportFragmentManager();
        mSectionsPagerAdapter = new FragmentAdapter(mFragmentManager, mContext, this, mActivityHandler);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);    // ?? ???????? ????? ??????? ????


        // When swiping between different sections, select the corresponding tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);

            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        // Setup views
        mImageBT = (ImageView) findViewById(R.id.status_title);
        mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
        mTextStatus = (TextView) findViewById(R.id.status_text);
        mTextStatus.setText(getResources().getString(R.string.bt_state_init));

        DrowImg = (ImageView) findViewById(R.id.DrowEMOTICON);
        DrowVal = (TextView) findViewById(R.id.DrowValue);

        manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        myTTS = new TextToSpeech(this, new OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                myTTS.setLanguage(Locale.US);
            }
        });


        // Do data initialization after service started and binded
        doStartService();
    }

    @Override
    public synchronized void onStart() {
        super.onStart();
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        // Stop the timer
        if (mRefreshTimer != null) {
            mRefreshTimer.cancel();
            mRefreshTimer = null;
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finalizeActivity();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // onDestroy is not always called when applications are finished by Android system.
        finalizeActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                // Launch the DeviceListActivity to see devices and do scan
                doScan();
                return true;
            case R.id.action_discoverable:
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();		// TODO: Disable this line to run below code
        //moveTaskToBack(true);

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setMessage("Do you want to Quit Application?").setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (AppSettings.getBgService())
                    doStopService();    // run in background ???? ???? ????
                else ;

                finish();

                return;
            }
        });
		/*
		.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_BACK)
					dialog.dismiss();
				return false;
			}
		}); */                    //setCancelable ?? ?????? ???????.


        AlertDialog CloseAlert = alert.create();
        CloseAlert.show();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // This prevents reload after configuration changes
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Implements TabListener
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        Log.e("?????", String.valueOf(tab.getPosition()));
        if (tab.getPosition() == FragmentAdapter.FRAGMENT_POS_WAVE) {
            String sendingMessage = new String();
            sendingMessage = "a";
            //sendMessage(sendingMessage);
            byte[] send = new String(sendingMessage).getBytes();
            mService.mBtManager.write(send);        // BTCtemplate.mBtManager?? private >>> public???? ????????
            //		mService.mBtManager.write(send);
            //		mService.mBtManager.write(send);
        } else if (tab.getPosition() == FragmentAdapter.FRAGMENT_FFT) {
            String sendingMessage = new String();
            sendingMessage = "b";
            //sendMessage(sendingMessage);
            byte[] send = new String(sendingMessage).getBytes();
            mService.mBtManager.write(send);
            //		mService.mBtManager.write(send);
            //		mService.mBtManager.write(send);
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.e("????????", String.valueOf(tab.getPosition()));

        if (tab.getPosition() == FragmentAdapter.FRAGMENT_POS_WAVE) {
            String sendingMessage = new String();
            sendingMessage = "b";
            //sendMessage(sendingMessage);
            byte[] send = new String(sendingMessage).getBytes();
            mService.mBtManager.write(send);        // BTCtemplate.mBtManager?? private >>> public???? ????????
            //		mService.mBtManager.write(send);
            //		mService.mBtManager.write(send);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.e("????", String.valueOf(tab.getPosition()));


        if (tab.getPosition() == FragmentAdapter.FRAGMENT_POS_WAVE) {
            String sendingMessage = new String();
            sendingMessage = "a";
            //sendMessage(sendingMessage);
            byte[] send = new String(sendingMessage).getBytes();
            mService.mBtManager.write(send);        // BTCtemplate.mBtManager?? private >>> public???? ????????
            //	mService.mBtManager.write(send);
            //	mService.mBtManager.write(send);
        } else //(tab.getPosition() == FragmentAdapter.FRAGMENT_FFT)
        {
            String sendingMessage = new String();
            sendingMessage = "b";
            //sendMessage(sendingMessage);
            byte[] send = new String(sendingMessage).getBytes();
            mService.mBtManager.write(send);
            //	mService.mBtManager.write(send);
            //	mService.mBtManager.write(send);
        }

    }


    /*****************************************************
     * Private methods
     ******************************************************/

    @Override
    public void OnFragmentCallback(int msgType, int arg0, int arg1, String arg2, String arg3, Object arg4) {
        switch (msgType) {
            case IFragmentListener.CALLBACK_RUN_IN_BACKGROUND:
                if (mService != null)
                    mService.startServiceMonitoring();
                break;

            default:
                break;
        }
    }

    /**
     * Start service if it's not running
     */
    private void doStartService() {
        Log.d(TAG, "# Activity - doStartService()");
        //	startService(new Intent(this, BTCTemplateService.class));	//???? ????. Activity?? ?????? service?? ??????? ?????? ????? ??
        bindService(new Intent(this, BTCTemplateService.class), mServiceConn, Context.BIND_AUTO_CREATE);
    }

    /**
     * Stop the service
     */
    private void doStopService() {
        Log.d(TAG, "# Activity - doStopService()");


        unbindService(mServiceConn);    // Template???? ???? ???? ?鰲�?. ???? unbind?? ???? ??夷�??
        // LogCat?? ????? leaked ???? ??? ?????? ?????.
        // ???????? ????????? ??????? ??? ??? ???? ?姨�????

        mService.finalizeService();

        //	stopService(new Intent(this, BTCTemplateService.class));
    }

    /**
     * Initialization / Finalization
     */
    private void initialize() {
        Logs.d(TAG, "# Activity - initialize()");
        mService.setupService(mActivityHandler);

        // If BT is not on, request that it be enabled.
        // RetroWatchService.setupBT() will then be called during onActivityResult
        if (!mService.isBluetoothEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
        }

        // Load activity reports and display
        if (mRefreshTimer != null) {
            mRefreshTimer.cancel();
        }

        // Use below timer if you want scheduled job
        //mRefreshTimer = new Timer();
        //mRefreshTimer.schedule(new RefreshTimerTask(), 5*1000);
    }

    private void finalizeActivity() {
        Logs.d(TAG, "# Activity - finalizeActivity()");

        if (!AppSettings.getBgService()) {
            doStopService();
        } else {
        }

        // Clean used resources
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    /**
     * Launch the DeviceListActivity to see devices and do scan
     */
    private void doScan() {
        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CONNECT_DEVICE);
    }

    /**
     * Ensure this device is discoverable by others
     */
    private void ensureDiscoverable() {
        if (mService.getBluetoothScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
    }


    /*****************************************************
     *	Public classes
     ******************************************************/

    /**
     * Receives result from external activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logs.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {
            case Constants.REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Attempt to connect to the device
                    if (address != null && mService != null)
                        mService.connectDevice(address);
                }
                break;

            case Constants.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a BT session
                    mService.setupBT();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Logs.e(TAG, "BT is not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                }
                break;
        }    // End of switch(requestCode)
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        // set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        // set.setCircleRadius(4f);
        //set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        // set.setValueTextColor(Color.WHITE);
        // set.setValueTextSize(9f);
        //set.setDrawValues(false);
        return set;
    }

    /*****************************************************
     * Handler, Callback, Sub-classes
     ******************************************************/

    public class ActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // Receives BT state messages from service
                // and updates BT state UI
                case Constants.MESSAGE_BT_STATE_INITIALIZED:
                    mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " +
                            getResources().getString(R.string.bt_state_init));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));

                    break;
                case Constants.MESSAGE_BT_STATE_LISTENING:
                    mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " +
                            getResources().getString(R.string.bt_state_wait));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
                    break;
                case Constants.MESSAGE_BT_STATE_CONNECTING:
                    mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " +
                            getResources().getString(R.string.bt_state_connect));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_away));
                    break;
                case Constants.MESSAGE_BT_STATE_CONNECTED:
                    if (mService != null) {
                        String deviceName = mService.getDeviceName();
                        if (deviceName != null) {
                            mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " +
                                    getResources().getString(R.string.bt_state_connected) + " " + deviceName);
                            mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
                        }
                    }
                    break;
                case Constants.MESSAGE_BT_STATE_ERROR:
                    mTextStatus.setText(getResources().getString(R.string.bt_state_error));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));

                    break;

                // BT Command status
                case Constants.MESSAGE_CMD_ERROR_NOT_CONNECTED:
                    mTextStatus.setText(getResources().getString(R.string.bt_cmd_sending_error));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));

                    break;

                case Constants.MESSAGE_BT_READ:
                    // ??????!!!  ??? fragment ??? ???? ??? ??????
                    // ???? ??????? dataSet???? ??????? ???? ????? ??姨�? ??? ?????


                    byte[] readBuf = (byte[]) msg.obj;

                    //if(readBuf.length==5)
                    //{

                    //		try {
                    //			Log.e(TAG,new String(readBuf, "UTF-8"));
                    //		} catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    //			e1.printStackTrace();
                    //		}
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //String[] r_message = readMessage.split("\r");
                    //String mes = r_message.toString();
                    //if(readMessage.startsWith("a") )
                    //{

                    if (mViewPager.getCurrentItem() == FragmentAdapter.FRAGMENT_POS_WAVE) // Wave ???? ???
                    {
                        double y;

                        try {
                            y = Double.parseDouble(readMessage);//num;
                            y = (int) y;    // wave Value
                        } catch (NumberFormatException e) {

                            return;
                        }

                        Log.e("???泥�?", String.valueOf(sampling++));

                        if (y > 400)    // FFT ??????? ??? ????? ?????
                        {
                            //if(ExampleFragment.mCurrentSeries != null)
                            //	{
                            ExampleFragment.mCurrentSeries.add(x, y);
                            //	}
                            ExampleFragment.mChartView.repaint();//????? ???
                            x = x + 1;

                        } else    // wave?? ????? ????? ?? ????? ??? ??????
                        {
	  		    		/*
	  		    		String sendingMessage = new String();
	  		            sendingMessage="a";
	  		            //sendMessage(sendingMessage);
	  		            byte[] send = new String(sendingMessage).getBytes();
	  					mService.mBtManager.write(send);
	  					//mService.mBtManager.write(send);
	  					//mService.mBtManager.write(send);
	  					Log.e("wave????", "????"); */
                        }
                        //mCurrentSeries.add(x, y);	// ???? ???? view?? ????? ??? ??????? ???? ??????


                        if (x > Scale) {
                            //mCurrentSeries.remove()


                            ExampleFragment.mCurrentSeries.clear();//??? ???
                            ExampleFragment.mDataset.clear();//??? ???

                            //mCurrentSeries.clear();//??? ???
                            //mDataset.clear();//??? ???
                            x = 0;

                            String seriesTitle = "";
                            // create a new series of data
                            XYSeries series = new XYSeries(seriesTitle);
                            ExampleFragment.mDataset.addSeries(series);
                            ExampleFragment.mCurrentSeries = series;
                            // mDataset.addSeries(series);
                            // mCurrentSeries = series;
                            // create a new renderer for the new series
                            //        XYSeriesRenderer renderer = new XYSeriesRenderer();
                            //       	ExampleFragment.mRenderer.addSeriesRenderer(renderer);

                            //  mRenderer.addSeriesRenderer(renderer);
	  	                /*
	  	                // set some renderer properties
	  	                renderer.setPointStyle(PointStyle.CIRCLE);
	  	                renderer.setFillPoints(true);
	  	                renderer.setDisplayChartValues(true);
	  	                renderer.setDisplayChartValuesDistance(1);
	  	                mCurrentRenderer = renderer;
	  	                //mChartView.repaint(); */
                            //???? ????? ????? ??

                        }
                    } else {
                        float y2;

//	            	if(sampleCount == FFTsampleNum){
//	            		DoubleFFT_1D fft = new DoubleFFT_1D(FFTsampleNum);
//	            		fft.complexForward(fftData);
//
//	            		double max_i = -1;
//	                    double max_fftval = -1;
//	                    for (int i = 0; i < fftData.length/2; i += 2) { // we are only looking at the half of the spectrum
//	                        double hz = (i / 2);
//
//
//	                        // complex numbers -> vectors, so we compute the length of the vector, which is sqrt(realpart^2+imaginarypart^2)
//	                        double vlen = Math.sqrt(fftData[i] * fftData[i] + fftData[i + 1] * fftData[i + 1]);
//
//	                        if(i<FFTsampleNum/2){
//	                        	if(i%3!=0) 	System.out.print("?而�? ???????: " + vlen+", "+i+"  ");
//	                        	else System.out.println("?而�? ???????: " + vlen+", "+i);
//	                        }
//
//	                        if (max_fftval < vlen) {
//	                            // if this length is bigger than our stored biggest length
//	                            max_fftval = vlen;
//	                            max_i = i;
//	                        }
//
//	                    }
//	                    double dominantFreq = ((max_i) / 2);
//	                    System.out.println("\nDominant frequency: " + dominantFreq + "hz (output.txt line no. " + max_i + ")");
//	            	}
//


                        //Log.e("dd", readMessage);
                        if (readMessage.endsWith("c")) {

                            if (BarEntry.isEmpty() == false) {
                                if (BarEntry.size() < 90)    // FFT ????? ????? ?? ???? ??? ????
                                {
                                    BarDataSet BardataSet = new BarDataSet(BarEntry, "Frequency(Hz)");
                                    BardataSet.setDrawValues(false);
                                    //	BardataSet.setColor(Color.parseColor("#ceeaff"));
                                    BardataSet.setColor(Color.parseColor("#fc4877"));
                                    FFTfragment.BardataSet = BardataSet;
                                    FFTfragment.data = new BarData(BardataSet);
                                    FFTfragment.data.setBarWidth(0.9f);


                                    FFTfragment.FFTchart.getAxisRight().setDrawGridLines(false);
                                    FFTfragment.FFTchart.getAxisLeft().setDrawGridLines(false);
                                    FFTfragment.FFTchart.getXAxis().setDrawGridLines(false);

                                    FFTfragment.FFTchart.setData(FFTfragment.data);
                                    FFTfragment.FFTchart.invalidate();


                                    float seta = 0, alpha = 0, beta = 0;
                                    float sav, aav, bav, result;

                                    for (int i = 7; i < 17; i++)    // seta?? ????
                                        seta += BarEntry.get(i).getY();

                                    for (int i = 17; i < 28; i++)
                                        alpha += BarEntry.get(i).getY();

                                    for (int i = 28; i < BarEntry.size(); i++)
                                        beta += BarEntry.get(i).getY();

                                    sav = seta / 10.0f;
                                    aav = alpha / 11.0f;
                                    bav = beta / (float) BarEntry.size();

                                    String tmp1 = Float.toString(sav);
                                    String tmp2 = Float.toString(aav);
                                    String tmp3 = Float.toString(bav);

                                    BigDecimal bd1 = new BigDecimal(tmp1);
                                    BigDecimal bd2 = new BigDecimal(tmp2);
                                    BigDecimal bd3 = new BigDecimal(tmp3);

                                    //	BigDecimal add = bd1.add(bd2);
                                    //	BigDecimal sleepNum = add.divide(bd3, 4, BigDecimal.ROUND_UP);

                                    BigDecimal multiple = bd2.multiply(bd3);
                                    BigDecimal sleepNum = bd1.divide(multiple, 4, BigDecimal.ROUND_UP);

                                    float coefficient = 3.0f;
                                    String coef = Float.toString(coefficient);
                                    BigDecimal cf = new BigDecimal(coef);
                                    sleepNum = sleepNum.multiply(cf);

                                    data = DrowsinessFragment.DrowsinessChart.getData();


                                    if (data != null) {

                                        LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
                                        // set.addEntry(...); // can be called as well

                                        data.addEntry(new Entry(set.getEntryCount(), sleepNum.floatValue()), 0);
                                        //dataSet.addEntry(new Entry(set.getEntryCount(),y));


                                        long now = System.currentTimeMillis();
                                        TimeData.add(set.getEntryCount() - 1, now);


                                        axisTime = (int) (TimeData.get(set.getEntryCount() - 1) - TimeData.get(0)) / 1000;

                                        // sec ??? ??????
                                        if (axisTime >= min - 1 && axisTime <= min + 1)    // index 1???? 2?? ???? ??????
                                        {
                                            DrowsinessFragment.DrowsinessChart.getXAxis().setGranularity(set.getEntryCount() - 1);
                                            secType = false;
                                            minType = true;
                                            hourType = false;
                                        }


                                        DrowsinessFragment.DrowsinessChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {

                                            @Override
                                            public String getFormattedValue(float arg0, AxisBase arg1) {
                                                // TODO Auto-generated method stub
                                                int axistime = (int) (TimeData.get((int) arg0) - TimeData.get(0)) / 1000;

                                                String naming = " min";
                                                if (secType == true) {
                                                    naming = " sec";
                                                    return String.valueOf(axistime) + naming;
                                                } else if (minType == true) {
                                                    naming = " min";
                                                    return String.valueOf(axistime / 60) + naming;
                                                } else if (hourType == true) {
                                                    naming = " hour";
                                                    return String.valueOf(axistime / 3600) + naming;
                                                }

                                                return String.valueOf(axistime) + naming;
                                            }
                                        });


                                        DrowsinessFragment.DrowData = data;
                                        // data.notifyDataChanged();
                                        //FFTfragment.data.notifyDataChanged();

                                        // let the chart know it's data has changed
                                        DrowsinessFragment.DrowsinessChart.notifyDataSetChanged();

                                        // limit the number of visible entries
                                        if (set.getEntryCount() < 10) {
                                            DrowsinessFragment.DrowsinessChart.setVisibleXRangeMaximum(10);
                                            DrowsinessFragment.DrowsinessChart.setVisibleXRangeMinimum(10);
                                        } else {
                                            ii++;
                                            DrowsinessFragment.DrowsinessChart.setVisibleXRangeMaximum(10 + ii);
                                            DrowsinessFragment.DrowsinessChart.setVisibleXRangeMinimum(10);
                                        }

                                        DrowsinessFragment.DrowsinessChart.getAxis(AxisDependency.LEFT).setAxisMinimum(0);
                                        DrowsinessFragment.DrowsinessChart.getAxis(AxisDependency.LEFT).setAxisMaximum(50);
                                        //      DrowsinessFragment.DrowsinessChart.getAxisLeft().setDrawGridLines(false);
                                        DrowsinessFragment.DrowsinessChart.getAxisRight().setDrawGridLines(false);
                                        DrowsinessFragment.DrowsinessChart.getAxisRight().setDrawLabels(false);
                                        DrowsinessFragment.DrowsinessChart.getXAxis().setDrawGridLines(false);
                                        DrowsinessFragment.DrowsinessChart.getXAxis().setPosition(XAxisPosition.BOTTOM);

                                        // mChart.setVisibleYRange(30, AxisDependency.LEFT);
                                        Log.e("????", String.valueOf(DrowsinessFragment.DrowsinessChart.getVisibleXRange()));
                                        // move to the latest entry
                                        if (DrowsinessFragment.DrowsinessChart.getVisibleXRange() < DrowsinessFragment.DrowsinessChart.getHighestVisibleX()) {
                                            DrowsinessFragment.DrowsinessChart.invalidate();
                                            Log.e("ddddd", "Ddd");
                                        } else {
                                            DrowsinessFragment.DrowsinessChart.moveViewToX(data.getEntryCount() - (11 + ii));
                                            Log.e("d", "Ddd");
                                        }
                                        //  if(DrowsinessFragment.DrowsinessChart.getLowestVisibleX()== 10+ii)
                                        //          	DrowsinessFragment.DrowsinessChart.moveViewToX(data.getEntryCount()-(11+ii));
                                        //           else
                                        //           	DrowsinessFragment.DrowsinessChart.invalidate();
                                        //            DrowsinessFragment.DrowsinessChart.animateX(100);

                                        DrowsinessFragment.text.setVisibility(View.VISIBLE);
                                        DrowsinessFragment.IndexText.setText(sleepNum.toString());


                                        BigDecimal pp = new BigDecimal(String.valueOf((float) RefVal));
                                        BigDecimal getPercent = sleepNum.divide(pp, 4, BigDecimal.ROUND_CEILING);

                                        BigDecimal multiple100 = new BigDecimal("100");
                                        BigDecimal percent = getPercent.multiply(multiple100);


                                        //       int percent = (getPercent.intValue()*100);
                                        //       int percent = (int)((sleepNum.floatValue()/10.0f)*100);


                                        DrowVal.setText("Drowsiness: " + percent.intValue() + "%");


                                        if (percent.intValue() >= 100) {

                                            //	manager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, 1, AudioManager.FLAG_PLAY_SOUND);
                                            // 	vibe.vibrate(1000);

                                            if (!r.isPlaying()) r.play();

                                            myTTS.speak("Wake Up!", TextToSpeech.QUEUE_ADD, null);

                                            DrowVal.setTextColor(Color.parseColor("#fc0000"));

                                            LimitLine Drowsinessline = new LimitLine(data.getEntryCount() - 1, "");
                                            Drowsinessline.setLineColor(Color.parseColor("#ffa27f"));
                                            Drowsinessline.setLineWidth(20.0f);

                                            DrowsinessFragment.DrowsinessChart.getXAxis().setDrawLimitLinesBehindData(true);
                                            DrowsinessFragment.DrowsinessChart.getXAxis().addLimitLine(Drowsinessline);

                                        } else if (percent.intValue() >= 70) {
                                            DrowVal.setTextColor(Color.parseColor("#e93f3b"));

                                            if (r.isPlaying()) r.stop();
                                            if (myTTS.isSpeaking()) myTTS.stop();
                                        } else if (percent.intValue() >= 40) {
                                            DrowVal.setTextColor(Color.parseColor("#fef646"));

                                            if (r.isPlaying()) r.stop();
                                            if (myTTS.isSpeaking()) myTTS.stop();
                                        } else {
                                            DrowVal.setTextColor(Color.parseColor("#00d42e"));

                                            if (r.isPlaying()) r.stop();
                                            if (myTTS.isSpeaking()) myTTS.stop();
                                        }

                                    }
                                }

                            }


                            // if(BarEntry != null)
                            //{

                            //BarEntry.clear();
                            BarEntry = new ArrayList<>();

                            //}
                            x2 = 0;

                        } else {
                            //Log.e("dd2", String.valueOf(BarEntry.size()));
                            //	Log.e("x2", String.valueOf(x2));

                            try {
                                y2 = Float.parseFloat(readMessage);//num;

                            } catch (NumberFormatException e) {

                                return;
                            }
                            //	float y2 =  Float.parseFloat(readMessage);	// FFT value


                            // data2 = FFTfragment.FFTchart.getData();


                            //    if (data2 != null) {

                            // BarDataSet set = (BarDataSet) data2.getDataSetByIndex(0);
                            // set.addEntry(...); // can be called as well

                            //        if (set == null) {
                            //            set = new BarDataSet(null, "Dynamic Data");		// ???? ????!! (?�뀭?? ?? code ???? ??????? ????? ?????? ??)
                            //Log.e("dd", String.valueOf(y2));
                            //            data2.addDataSet(set);

                            //        }
                            // BarDataSet set = (BarDataSet) data2.getDataSetByIndex(0);


                            //  data2.addEntry(new Entry(set.getEntryCount(),y2), 0);
                            //  FFTfragment.data = data2;
                            //  FFTfragment.data.notifyDataChanged();
                            //  FFTfragment.FFTchart.notifyDataSetChanged();
                            //  FFTfragment.FFTchart.invalidate();
                            //
                            if (y2 > 500) {
		        	   /*
		        	   String sendingMessage = new String();
	                    sendingMessage="b";
	                    //sendMessage(sendingMessage);
	                    byte[] send = new String(sendingMessage).getBytes();
	        			mService.mBtManager.write(send);
	        		//	mService.mBtManager.write(send);
	        		//	mService.mBtManager.write(send);
	        			//Log.e("FFT????", String.valueOf(BarEntry.size()));  */
                            } else {
                                BarEntry.add(new BarEntry(x2, y2));
                                //Log.e("dd", String.valueOf(BarEntry.get(x2)));
                                x2++;

                            }

                        }


                        //    }
                    }

                    //mViewPager.getCurrentItem()
                    //data2.addEntry(new Entry(x2, 10f), arg1);
			/*
	  		     mChartView = ExampleFragment.mChartView;
	  		     mCurrentRenderer = ExampleFragment.mCurrentRenderer;
	  		   mCurrentSeries = ExampleFragment.mCurrentSeries;
	  		 mDataset = ExampleFragment.mDataset;
	  		mRenderer = ExampleFragment.mRenderer;
				 */


				 /*
				 byte[] readBuf = (byte[]) msg.obj;
				 String readMessage = new String(readBuf, 0, msg.arg1);


				 float y =  Float.parseFloat(readMessage);


				 FFTEntry.add(new Entry(y,x));
				 dataSet = new LineDataSet(FFTEntry, "project");

				 ArrayList<String> labels = new ArrayList<>();
				 for(int i=0; i<x+1; i++)
				 {
					 labels.add(String.valueOf(i));
				 }

				 data = new LineData(labels, dataSet);
				 FFTfragment.data = data;
				 FFTfragment.data.notifyDataChanged();
				 FFTfragment.FFTchart.notifyDataSetChanged();
				// FFTfragment.FFTchart.moveViewToX(data.getXValCount());
				 FFTfragment.FFTchart.invalidate();

         		 x++;
         		 if(x>100)
         		 {
         			 x=0;
         		 } */

                    //Log.e("test", readMessage);


				 /*
				 data = FFTfragment.FFTchart.getData();



			        if (data != null) {

			            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
			            // set.addEntry(...); // can be called as well

			            if (set == null) {
			                set = createSet();
			                data.addDataSet(set);
			            }
			            	//dataSet = (LineDataSet) data.getDataSets();
			            	//Log.e("test", String.valueOf(data.getDataSetCount()));
			            	//Log.e("test", String.valueOf(data.getMaxEntryCountSet()));

				            if(x>99)
				            {
				            //	data.clearValues();



				            	LineDataSet lastDataSet = (LineDataSet) data.getDataSetByIndex(data.getDataSetCount()-1);
				            	Entry lastEntry = lastDataSet.getEntryForIndex(lastDataSet.getEntryCount()-1);
				            	data.removeEntry(lastEntry, data.getDataSetCount()-1);
				            	//Log.e("test", String.valueOf(lastDataSet.getEntryCount()));
				            	//Log.e("test", String.valueOf(data.getDataSetCount()));


				            	LineDataSet latestDataSet = (LineDataSet) data.getDataSets();
				            	Entry newEntry = new Entry();

				            	for(int i=0; i<100; i++)
				            	{
				            		//Entry FirstEntry = (Entry) latestDataSet.getEntriesForXValue(i);
				            		Entry SecondEntry = (Entry) latestDataSet.getEntriesForXValue(i+1);

				            		//FirstEntry = SecondEntry;
				            		newEntry.

				            	}


				            	//LineDataSet FirstDataSet = (LineDataSet) data.getDataSetByIndex(i);
				            	//Entry FirstEntry = FirstDataSet.getEntryForIndex(i);
				            	//data.removeEntry(FirstEntry, i);

				            	//dataSet.removeEntry(i);
				            	//Log.e("test", String.valueOf(FirstDataSet.getEntryCount()));


				            	//i++;



				            	x--;
				            	//x=0;
				            }
				            data.addEntry(new Entry(set.getEntryCount(),y), 0);
				            //dataSet.addEntry(new Entry(set.getEntryCount(),y));


				            x++;

				            FFTfragment.data = data;
				           // data.notifyDataChanged();
				            //FFTfragment.data.notifyDataChanged();

				            // let the chart know it's data has changed
				            FFTfragment.FFTchart.notifyDataSetChanged();

				            // limit the number of visible entries
				            //FFTfragment.FFTchart.setVisibleXRangeMaximum(100);
				            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

				            // move to the latest entry
				            FFTfragment.FFTchart.moveViewToX(data.getEntryCount());

				            // this automatically refreshes the chart (calls invalidate())
				            // mChart.moveViewTo(data.getXValCount()-7, 55f,
				            // AxisDependency.LEFT);

				           // Log.e("test", String.valueOf(data.getDataSetCount()));

				            // dataSet?? 1?? ???? ?????? ?????? ?????? ?? ??
				            // ??????? ????????? ???????? ?�닞?????

			        } */


				 /*
				 	ArrayList<Entry> LineEntry2 = new ArrayList<>();

			        for(int e=0; e<100; e++){
			        	LineEntry2.add(new Entry(e, (float) (Math.random() * 40) + 300f));
			    	}


					LineDataSet dataSet2 = new LineDataSet(LineEntry2, "Projects");

					ArrayList<String> labels2 = new ArrayList<>();
					for(int e=0; e<100; e++){
			        	labels2.add(String.valueOf(e));
			    	}


					//dataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

					data = new LineData(dataSet2);
					//FFTfragment.data = data;
					FFTfragment.FFTchart.notifyDataSetChanged();
					//FFTfragment.FFTchart.invalidate(); */

				/*
				 data = FFTfragment.FFTchart.getData();
				 dataSet = data.getDataSetByIndex(0);
				 x = dataSet.getEntryCount();

				 if(data.getXValCount()<=100){
					 data.getXVals().add(""+x);
					 data.getXVals().remove(0);

					 for(int i=0; i<x; i++){
						 Entry e = dataSet.getEntryForXIndex(i);
						 if (e==null) continue;

						 e.setXIndex(e.getXIndex() - 1);
					 }
					 x = 100;
				 }
				 data.addEntry(new Entry(y, x), 0);
				 //FFTfragment.FFTchart.notifyDataSetChanged();
				 //FFTfragment.FFTchart.invalidate();
				 //FFTfragment.FFTchart.setData(data); */


                    break;


                ///////////////////////////////////////////////
                // When there's incoming packets on bluetooth
                // do the UI works like below
                ///////////////////////////////////////////////
//			case Constants.MESSAGE_READ_ACCEL_REPORT:
//				ActivityReport ar = (ActivityReport)msg.obj;
//				if(ar != null) {
//					TimelineFragment frg = (TimelineFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_TIMELINE);
//					frg.showActivityReport(ar);
//				}
//				break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }    // End of class ActivityHandler

    /**
     * Auto-refresh Timer
     */
    private class RefreshTimerTask extends TimerTask {
        public RefreshTimerTask() {
        }

        public void run() {
            mActivityHandler.post(new Runnable() {
                public void run() {
                    // TODO:
                    mRefreshTimer = null;
                }
            });
        }
    }


}
