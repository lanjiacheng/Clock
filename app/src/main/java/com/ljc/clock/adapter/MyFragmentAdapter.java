package com.ljc.clock.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ljc.clock.fragment.MyFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private List fragmentList;
    public MyFragmentAdapter(FragmentManager fm,List fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        MyFragment fragment = (MyFragment)fragmentList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
/*
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "时钟";
            case 1:
                return "秒表";
            case 2:
                return "计时器";
        }
        return "未知Tab";
    }
    */
}
