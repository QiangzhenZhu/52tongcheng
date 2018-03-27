package cn.xcom.banjing.utils;


import android.util.Log;

import cn.xcom.banjing.constant.HelperConstant;

/**
 * Created by zhuchongkun on 16/5/27.
 */
public class LogUtils{
    private static final String TAG = "banjing--->";
    public static void i(String tag,String message){
        if (HelperConstant.isLog){
            Log.i(TAG+tag,message);
        }
    }
    public static void d(String tag,String message){
        if (HelperConstant.isLog){
            Log.d(TAG+tag,message);
        }

    }
    public static void e(String tag,String message){
        if (HelperConstant.isLog){
            Log.e(TAG+tag,message);
        }
    }
    public static void v(String tag,String message){
        if (HelperConstant.isLog){
            Log.v(TAG+tag,message);
        }
    }
    public static void w(String tag,String message){
        if (HelperConstant.isLog){
            Log.w(TAG+tag,message);
        }
    }


}
