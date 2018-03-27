package cn.xcom.banjing.utils;

/**
 * Created by 10835 on 2018/1/15.
 */
import android.os.Environment;

public class Config {
    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DEFAULT_CACHE_DIR = SDCARD_DIR + "/PLDroidPlayer";
}