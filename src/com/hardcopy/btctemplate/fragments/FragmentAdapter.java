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
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hardcopy.btctemplate.R;

import java.util.Locale;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    public static final String TAG = "FragmentAdapter";

    // TODO: Total count
    public static final int FRAGMENT_COUNT = 4;

    // TODO: Fragment position
    public static final int FRAGMENT_DROWSINESS = 0;
    public static final int FRAGMENT_POS_WAVE = 1;
    public static final int FRAGMENT_FFT = 2;
    public static final int FRAGMENT_POS_SETTINGS = 3;


    // System
    private Context mContext = null;
    private Handler mHandler = null;
    private IFragmentListener mFragmentListener = null;

    // ??? ?? ??? fragment? ????(??? ??) ????? ???? ??
    private Fragment mExampleFragment = null;
    private Fragment mLLSettingsFragment = null;
    private Fragment mFFTfragment = null;
    private Fragment mDrowsinessFragment = null;

    public FragmentAdapter(FragmentManager fm, Context c, IFragmentListener l, Handler h) {
        super(fm);
        mContext = c;
        mFragmentListener = l;
        mHandler = h;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        Fragment fragment;
        //boolean needToSetArguments = false;

        if (position == FRAGMENT_POS_WAVE) {
            if (mExampleFragment == null) {
                //mExampleFragment = new ExampleFragment(mContext, mFragmentListener, mHandler);    // default ???? ?? ?? ??
                mExampleFragment = new ExampleFragment();
                //needToSetArguments = true;
            }
            fragment = mExampleFragment;

        } else if (position == FRAGMENT_POS_SETTINGS) {
            if (mLLSettingsFragment == null) {
                //mLLSettingsFragment = new LLSettingsFragment(mContext, mFragmentListener);
                mLLSettingsFragment = new LLSettingsFragment();
                //needToSetArguments = true;
            }
            fragment = mLLSettingsFragment;

        } else if (position == FRAGMENT_FFT) {
            if (mFFTfragment == null) {
                //mFFTfragment = new FFTfragment(mContext, mFragmentListener);
                mFFTfragment = new FFTfragment();
                //needToSetArguments = true;
            }
            fragment = mFFTfragment;

        } else if (position == FRAGMENT_DROWSINESS) {
            if (mDrowsinessFragment == null) {
                //mDrowsinessFragment = new DrowsinessFragment(mContext, mFragmentListener);
                mDrowsinessFragment = new DrowsinessFragment();
                //needToSetArguments = true;
            }
            fragment = mDrowsinessFragment;

        } else {
            fragment = null;
        }

        // TODO: If you have something to notify to the fragment.
        /*
		if(needToSetArguments) {
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
		}
		*/

        return fragment;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case FRAGMENT_POS_WAVE:
                return mContext.getString(R.string.title_wave).toUpperCase(l);
            case FRAGMENT_POS_SETTINGS:
                return mContext.getString(R.string.title_ll_settings).toUpperCase(l);
            case FRAGMENT_FFT:
                return mContext.getString(R.string.title_fft).toUpperCase(l);
            case FRAGMENT_DROWSINESS:
                return mContext.getString(R.string.title_drowsiness).toUpperCase(l);
        }
        return null;
    }


}
