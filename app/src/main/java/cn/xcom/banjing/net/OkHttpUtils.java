package cn.xcom.banjing.net;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 10835 on 2018/3/21.
 */

public class OkHttpUtils {
    public static final void sendOkHttp(String url,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
