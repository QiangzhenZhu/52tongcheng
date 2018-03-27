package cn.xcom.banjing.fragment.collection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.CollectionAdapter;
import cn.xcom.banjing.bean.Collection;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;

/**
 * Created by 10835 on 2018/3/17.
 */

public class MarketFragment extends Fragment {

    private List<Collection> addList = new ArrayList<>();
    private ListView listView;
    private UserInfo userInfo;
    private CollectionAdapter collectionAdapter;
    private Context mContext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = container.getContext();
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_market,container,false);
        listView= (ListView) view. findViewById(R.id.collection_listView);
        collectionAdapter=new CollectionAdapter(addList,mContext,0);
        listView.setAdapter(collectionAdapter);
        userInfo=new UserInfo();
        userInfo.readData(mContext);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        collectionList();
    }

    public void collectionList(){
        String url= NetConstant.HAS_COLLECTION;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String data=jsonObject.getString("data");
                    if (data!=null){
                        Gson gson=new Gson();
                        List<Collection> fronts =gson.fromJson(data,
                                new TypeToken<ArrayList<Collection>>(){}.getType());
                        addList.clear();
                        addList.addAll(fronts);
                        collectionAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }
}
