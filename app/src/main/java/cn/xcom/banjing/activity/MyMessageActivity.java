package cn.xcom.banjing.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.lzy.widget.tab.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.fragment.mymessage.IsReleaseFragment;
import cn.xcom.banjing.fragment.mymessage.NoReleaseFragment;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class MyMessageActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout back;
    //FrameLayout tabcontent;
    //FragmentTabHost tabhost;
    //FrameLayout dataRealtabcontent;
    LinearLayout activityNewDetial;
    private LayoutInflater layoutInflater;
    private Class fragmentArray[] = {NoReleaseFragment.class, IsReleaseFragment.class};
    private int colorsArray[] = {R.color.white, R.color.colorPrimary};
    private String mTextviewArray[] = {"待发布", "已发布"};
    private int viewVisbility[] = {View.VISIBLE, View.GONE};
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mPagerSliderTabStrip;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_msg);
        fragments = new ArrayList<>();
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_my_message);
        mPagerSliderTabStrip = (PagerSlidingTabStrip) findViewById(R.id.message_title);
        back = (RelativeLayout) findViewById(R.id.rl_msg_back);
        back.setOnClickListener(this);
        /*tabcontent = (FrameLayout) findViewById(android.R.id.tabcontent);
        tabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        dataRealtabcontent = (FrameLayout) findViewById(R.id.data_realtabcontent);*/
        activityNewDetial = (LinearLayout) findViewById(R.id.activity_my_msg);
        layoutInflater = LayoutInflater.from(this);
        //tabhost.setup(this, getSupportFragmentManager(), R.id.data_realtabcontent);
        int count = fragmentArray.length;
        Fragment mNoReleaseFragment = new NoReleaseFragment();
        Fragment mIsReleaseFragment = new IsReleaseFragment();
        fragments.add(mNoReleaseFragment);
        fragments.add(mIsReleaseFragment);
        MymessageFragmentAdapter adapter = new MymessageFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mPagerSliderTabStrip.setViewPager(mViewPager);
        /*for (int i = 0; i < count; i++) {

            TabHost.TabSpec tabSpec = tabhost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));

            tabhost.addTab(tabSpec, fragmentArray[i], null);
//            tabhost.getTabWidget().getChildAt(i).setBackgrounde(R.drawable.selector_tab_background);
        }*/
    }

    /*private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.fragment_msg_release_item, null);
        TextView textView = (TextView) view.findViewById(R.id.data_item_title);
        textView.setText(mTextviewArray[index]);
        textView.setTextSize(18);
        textView.setTextColor(Color.BLUE);
        textView.setHintTextColor(Color.RED);


//        textView.setTextColor(colorsArray[index]);
//        textView.setBackgroundResource(mImageViewArray[index]);

        return view;
    }*/
 class MymessageFragmentAdapter extends FragmentPagerAdapter{
        private String titles[] = {"待发布", "已发布"};
        public MymessageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_msg_back:
                finish();
                break;
        }
    }
}
