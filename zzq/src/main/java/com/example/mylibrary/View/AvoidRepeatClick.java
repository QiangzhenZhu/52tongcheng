package com.example.mylibrary.View;

import android.view.View;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public class AvoidRepeatClick {


    public static boolean avoidRepeatClick(View view){
        int MIN_CLICK_DELAY_TIME = 1000;
        long lastClickTime = 0;
        boolean flag = true;
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            flag=false;
        }
        return flag;
    }

}
