package cn.xcom.banjing.net;

/**
 * Created by mac on 2017/9/25.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

//import okhttp3.Response;

/**
 * Created by hzh on 17/2/9.
 * 网络请求数据的帮助类
 */

public class ResponseHelper {
    JSONObject jsonObject;

    public ResponseHelper(String response) throws JSONException {
        Log.d("response", response);
        jsonObject = new JSONObject(response);
    }
//    public ResponseHelper(Response response) throws JSONException {
////        response = Response.body().string();
////        Log.i("response", response);
//        jsonObject = new JSONObject(response.toString());
//    }
    public boolean isSuccess() throws JSONException {
        String status = jsonObject.getString("status");
        if ("success".equals(status)) {
            return true;
        } else {
            return false;
        }
    }

    public String getData() throws JSONException {
        String data = jsonObject.getString("data");
        return data;
    }
}
