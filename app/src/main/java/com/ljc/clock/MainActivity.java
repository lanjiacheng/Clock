package com.ljc.clock;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.ljc.clock.adapter.MyFragmentAdapter;
import com.ljc.clock.fragment.MyFragment;
import com.ljc.clock.myview.ClockView;
import com.ljc.clock.myview.CountTimeView;
import com.ljc.clock.myview.SecondTimeView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private List<MyFragment> fragmentList;
    private ViewPager viewPage;
    public static MainActivity mainActivity;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        tabLayout =(TabLayout)findViewById(R.id.tab_layout);
        viewPage = (ViewPager)findViewById(R.id.viewPage);
        fragmentList = new ArrayList();
        fragmentList.add(new MyFragment().setLayout(R.layout.clockadapter));
        fragmentList.add(new MyFragment().setLayout(R.layout.secondtimeadapter));
        fragmentList.add(new MyFragment().setLayout(R.layout.counttimeadapter));
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),fragmentList);
        viewPage.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPage);
        initTab();
    }
    /*
    初始化tab栏的tab子项，设置图标
     */
    private void initTab() {
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        tab1.setIcon(R.drawable.top_clock);
        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        tab2.setIcon(R.drawable.top_second);
        TabLayout.Tab tab3 = tabLayout.getTabAt(2);
        tab3.setIcon(R.drawable.top_wheel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //活动被销毁的时候，调用下面这几个方法，结束开启的所有子线程
        ClockView.onExit();
        SecondTimeView.onExit();
        CountTimeView.onExit();
    }
}
/*
TabLayout与ViewPage关联的方法有两个：
一是在xml文件中进行关联，即将TabLayout的标签放到ViewPage的标签内即可
二是在代码中分别创建两个视图的实例，然后调用TabLayout的setupWithViewPager(viewPage)方法即可进行关联
    （前提是将两个视图都预先放到主活动的布局中，才可以同时显示）
用代码关联的优点就是，关联之后，还可以通过TabLayout的getTanAt()方法来获得由于关联ViewPage而创建的Tab
从而可以独立的改变这些tab

关于ViewPage的一些总结：
ViewPage就是多个fragment（碎片）的集合，在这个集合中的相邻碎片之间，可以通过滑动来切换
因为是fragment，那么就会有生命周期，当切换到另一个不连续的碎片时（比如1碎片切换到3碎片），
那么这个碎片（1碎片）的视图就会被销毁（onViewDestroy方法），但是视图被销毁，在视图中创建过的
子线程会继续执行！
当切换到与某碎片相邻的碎片时（或则直接切换到某碎片），那么该碎片再次会创建视图，并调用onResum方法
等，同时因为重新创建视图，那些之前创建的视图的实例就会变为无效的，需要重新获得新的视图的实例
 */