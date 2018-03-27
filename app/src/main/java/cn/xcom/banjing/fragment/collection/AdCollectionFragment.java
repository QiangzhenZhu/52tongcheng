package cn.xcom.banjing.fragment.collection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.CollectionAdapter;
import cn.xcom.banjing.adapter.ConvenienceAdapter;
import cn.xcom.banjing.bean.AdCollection;
import cn.xcom.banjing.bean.Collection;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;

/**
 * Created by 10835 on 2018/3/17.
 */

public class AdCollectionFragment extends Fragment {
    private List<Convenience> addList = new ArrayList<>();
    private XRecyclerView listView;
    private UserInfo userInfo;
    private ConvenienceAdapter convenienceAdapter;
    private Context mContext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = container.getContext();
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_ad_collection,container,false);
        listView= (XRecyclerView) view. findViewById(R.id.collection_listView);
        convenienceAdapter = new ConvenienceAdapter(addList,mContext,1);
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setAdapter(convenienceAdapter);
        listView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                collectionList();
            }

            @Override
            public void onLoadMore() {

            }
        });
        listView.setLoadingMoreEnabled(false);
        userInfo=new UserInfo();
        userInfo.readData(mContext);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        collectionList();
    }

    /**
     * type 4为广告收藏列表
     */
    public void collectionList(){
        String url= NetConstant.AD_COLLECTION;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String data=jsonObject.getString("data");
                    if (data!=null){
                        listView.refreshComplete();
                        Gson gson=new Gson();
                        List<Convenience> fronts =gson.fromJson(data,
                                new TypeToken<ArrayList<Convenience>>(){}.getType());
                        addList.clear();
                        addList.addAll(fronts);
                        convenienceAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listView.refreshComplete();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listView.refreshComplete();


            }
        });
        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }

}
