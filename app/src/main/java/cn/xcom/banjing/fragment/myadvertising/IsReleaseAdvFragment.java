package cn.xcom.banjing.fragment.myadvertising;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.AdvIsReleaseAdapter;
import cn.xcom.banjing.bean.AdvReleaseBean;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class IsReleaseAdvFragment extends Fragment {
    AdvIsReleaseAdapter isReleaseAdapter;
    private XRecyclerView xRecyclerView;
    private List<AdvReleaseBean> addlist;
    UserInfo user;
    View view;
    Context context;
    private KProgressHUD hud;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_no_release_advertising, container, false);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        user = new UserInfo();
        user.readData(context);
        getNewDatas();
        initView();
    }

    private void initView() {

        addlist = new ArrayList<>();
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.fragment_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {
                getNewDatas();
            }

            @Override
            public void onLoadMore() {
                getMoreDatas();
            }
        });
        isReleaseAdapter = new AdvIsReleaseAdapter(addlist, context);
        xRecyclerView.setAdapter(isReleaseAdapter);
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
    }

    private void getNewDatas() {
        if (hud != null) {
            hud.show();
        }
        String url = NetConstant.GET_MY_ADLIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        hud.dismiss();
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<AdvReleaseBean> lists = gson.fromJson(data, new TypeToken<ArrayList<AdvReleaseBean>>() {
                        }.getType());
                        addlist.clear();
                        addlist.addAll(lists);
                        isReleaseAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hud.dismiss();
                }
                xRecyclerView.refreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hud.dismiss();
                ToastUtil.Toast(context, "网络错误，请检查");
                xRecyclerView.refreshComplete();

            }
        });
        request.putValue("beginid", "0");
        request.putValue("status", "1");
        request.putValue("userid", user.getUserId());

        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    private void getMoreDatas() {
        String url = NetConstant.GET_MY_BB_SPOSTLIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<AdvReleaseBean> lists = gson.fromJson(data, new TypeToken<ArrayList<AdvReleaseBean>>() {
                        }.getType());

                        addlist.addAll(lists);
                        isReleaseAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.loadMoreComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtil.Toast(context, "网络错误，请检查");
                xRecyclerView.loadMoreComplete();

            }
        });
        AdvReleaseBean lastMsgV = addlist.get(addlist.size() - 1);
        request.putValue("beginid", lastMsgV.getSlide_id());
        request.putValue("userid", user.getUserId());
        request.putValue("status", "1");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onPause() {
        super.onPause();

//        ToastUtil.showShort(getActivity(),"bbb");
    }

    @Override
    public void onResume() {
        getNewDatas();
        isReleaseAdapter.notifyDataSetChanged();
//        ToastUtil.showShort(getActivity(),"aaa");
        super.onResume();
    }
}

