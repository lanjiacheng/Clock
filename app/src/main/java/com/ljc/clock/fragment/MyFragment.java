package com.ljc.clock.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljc.clock.R;
import com.ljc.clock.myview.ClockView;
import com.ljc.clock.myview.CountTimeView;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class MyFragment extends Fragment {
    private int mLayoutId;

    public MyFragment() {
        super();
    }
    public MyFragment setLayout(int layoutId){
        mLayoutId = layoutId;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(mLayoutId, container, false);
        return view;
    }
}
