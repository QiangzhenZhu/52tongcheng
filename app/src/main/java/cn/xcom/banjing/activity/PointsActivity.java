package cn.xcom.banjing.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.PointsAdapter;
import cn.xcom.banjing.bean.ScoreList;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.OkHttpUtils;
import cn.xcom.banjing.utils.RoundImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PointsActivity extends BaseActivity {

    @BindView(R.id.tv_all_points)
    TextView tvAllPoints;
    @BindView(R.id.tv_today_points)
    TextView tvTodayPoints;
    @BindView(R.id.tv_task_points)
    TextView tvTaskPoints;
    @BindView(R.id.ri_points)
    RoundImageView riPoints;
    @BindView(R.id.tv_points_name)
    TextView tvPointsName;
    @BindView(R.id.rv_points)
    XRecyclerView rvPoints;
    UserInfo userInfo;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private Context mContext;
    private ScoreList scoreList;
    private String error;
    private PointsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        ButterKnife.bind(this);
        mContext = this;
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        rvPoints.setLoadingMoreEnabled(false);
        rvPoints.setPullRefreshEnabled(false);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Picasso.with(mContext).load(NetConstant.NET_DISPLAY_IMG + userInfo.getUserImg()).into(riPoints);
        tvPointsName.setText(userInfo.getUserName() + "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = Uri.parse(NetConstant.GET_SCORE_LIST)
                .buildUpon()
                .appendQueryParameter("userid", userInfo.getUserId())
                .build();
        final String errorTitle;
        OkHttpUtils.sendOkHttp(uri.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "网络加载错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                String status = "";

                try {
                    JSONObject object = new JSONObject(result);
                    status = object.getString("status");
                    error = object.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if ("success".equals(status)) {
                    scoreList = new Gson().fromJson(result, ScoreList.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setOrNotifyAdapter();
                            init();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public void init() {
        tvAllPoints.setText(scoreList.getData().getOverview().getTotal_score() + "");
        tvTodayPoints.setText(scoreList.getData().getOverview().getToday_score() + "");
        tvTaskPoints.setText(scoreList.getData().getOverview().getToday_task() + "");

    }

    public void setOrNotifyAdapter() {
        if (adapter == null) {
            adapter = new PointsAdapter(scoreList.getData().getScore_list());
            rvPoints.setLayoutManager(new LinearLayoutManager(mContext));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(mContext.getResources().getDrawable(R.drawable.item_divider_decoration));
            rvPoints.addItemDecoration(dividerItemDecoration);
            rvPoints.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }
}
