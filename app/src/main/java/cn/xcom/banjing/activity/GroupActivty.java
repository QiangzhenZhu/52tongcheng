package cn.xcom.banjing.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.FriendBean;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.MyImageLoader;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;

/**
 * Created by zhuchongkun on 2017/12/15.
 */

public class GroupActivty extends BaseActivity implements View.OnClickListener {
    private UserInfo userInfo;
    private RelativeLayout rl_back, rl_submit;
    private EditText et_group_name;
    private GridView noScrollgridview;
    private Context mContext;
    private GridAdapter gridAdapter;
    private ArrayList<FriendBean> friendListInfos = new ArrayList<FriendBean>();
    private KProgressHUD submit_hub;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_social_publish);
        mContext = this;
        userInfo = new UserInfo(mContext);
        initView();

    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_social_publish_back);
        rl_back.setOnClickListener(this);
        rl_submit = (RelativeLayout) findViewById(R.id.rl_social_publish_submit);
        rl_submit.setOnClickListener(this);
        et_group_name = (EditText) findViewById(R.id.et_social_publish_content);
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview_social_publish_img);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridAdapter = new GridAdapter(this);
        gridAdapter.notifyDataSetChanged();
        noScrollgridview.setAdapter(gridAdapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == friendListInfos.size()) {
                    Intent intent=new Intent(mContext, SelectGroupMemberActivity.class);
                    intent.putExtra("select",friendListInfos);
                    startActivityForResult( intent, 2);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            friendListInfos.clear();
            friendListInfos= (ArrayList<FriendBean>) data.getSerializableExtra("select");
            gridAdapter.notifyDataSetChanged();
            if (et_group_name.getText().toString().isEmpty()){
                String name="";
                for (int i = 0; i < friendListInfos.size(); i++) {
                    if (i==0){
                        name= friendListInfos.get(i).getName();
                    }else{
                        name=name+","+friendListInfos.get(i).getName();
                    }
                }
            }
            LogUtils.e("TGB","---?"+data.toString());
            LogUtils.e("TGB","---?"+friendListInfos.toString());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_social_publish_back:
                finish();
                break;
            case R.id.rl_social_publish_submit:
                if (friendListInfos.size()>0){
                    toCreatGroup();
                }else {
                    Toast.makeText(getApplication(), "选择邀请成员！", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
    private void toCreatGroup() {
        submit_hub = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        submit_hub.show();
        String url;

        url = NetConstant.NET_ADD_GROUP;//http://banjing.xiaocool.net/index.php?g=apps&m=index&a=CreateGroupChat&send_uid=123&title=测试群
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (submit_hub != null) {
                    submit_hub.dismiss();
                }
                if (s != null) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String state = object.getString("status");
                        LogUtils.e("TGB","-object--?"+object.toString());
//
                        if (state.equals("success")) {
                            JSONObject jsonObject=object.getJSONObject("data");

                            String groupid = jsonObject.getString("groupid");
                            toInviteGroup(groupid);
                        } else {
                            String error = object.getString("data");
                            Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();
//                            HelperApplication.getInstance().getTaskTypes().clear();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (submit_hub != null) {
                    submit_hub.dismiss();
                }
                HelperApplication.getInstance().getTaskTypes().clear();
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("send_uid", userInfo.getUserId());
        request.putValue("title", et_group_name.getText().toString());
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }
    private void toInviteGroup(String groupid) {
        submit_hub = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        submit_hub.show();
        String url;

        url = NetConstant.NET_ADD_GROUP_MEMBER;//g=apps&m=index&a=xcAddChatLinkMan&groupid=66&userid=124,125,126&username=找找,赵赵,张张&inviterid=123
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (submit_hub != null) {
                    submit_hub.dismiss();
                }
                if (s != null) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String state = object.getString("status");
                        LogUtils.e("TGB","-toInviteGroup  ---object--?"+object.toString());
                        if (state.equals("success")) {
                            String data = object.getString("data");
                            HelperApplication.getInstance().getTaskTypes().clear();

//                            homeActivity.checkToSecond(2);
//                            Intent intent = new Intent(mContext, HomeActivity.class);
//                            startActivity(intent);
                            Toast.makeText(getApplication(), "创建群组已提交", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(context, NearbyActivity.class);
//
//                            startActivity(intent);
                            finish();
                        } else {
                            String error = object.getString("data");
                            Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();
//                            HelperApplication.getInstance().getTaskTypes().clear();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (submit_hub != null) {
                    submit_hub.dismiss();
                }
                HelperApplication.getInstance().getTaskTypes().clear();
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("groupid", groupid);
        String userid="";
        String username="";
        for (int i = 0; i < friendListInfos.size(); i++) {
            if (i==0){
                username= friendListInfos.get(i).getName();
                userid= friendListInfos.get(i).getId();
            }else{
                userid=userid+","+friendListInfos.get(i).getId();
                username=username+","+friendListInfos.get(i).getName();
            }
        }
        request.putValue("userid",userid);
        request.putValue("username", username);
        request.putValue("inviterid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return (friendListInfos.size()+ 1);
        }


        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            GridAdapter.ViewHolder holder = null;
//            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new GridAdapter.ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
//            } else {
//                holder = (GridAdapter.ViewHolder) convertView.getTag();
//            }

            if (position == friendListInfos.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.ic_cheat_add));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + friendListInfos.get(position).getPhoto(), holder.image);
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }
    }
}
