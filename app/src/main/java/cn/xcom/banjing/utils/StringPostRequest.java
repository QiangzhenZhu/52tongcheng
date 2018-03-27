package cn.xcom.banjing.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2016-05-05.
 */
public class StringPostRequest extends StringRequest {
    private Map map;

    public StringPostRequest(String url,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(Method.POST,url, listener, errorListener);
        map=new HashMap();
    }


    public void putValue(String key,String value) {
        map.put(key, value);
    }

    public void putMap(Map<String,String> map){
        this.map.putAll(map);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
