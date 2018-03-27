package cn.xcom.banjing.adapter;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.ChatActivity;
import cn.xcom.banjing.bean.FriendListInfo;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.MyImageLoader;
import cn.xcom.banjing.utils.RoundImageView;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;

/**
 * Created by mac on 2017/9/28.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder>  {
    private List<FriendListInfo> list;
    private List<String> addList;
    private Context context;
    private Map<Integer, Boolean> states;
    private UserInfo userInfo;
    private OnFriendDeleteClick deleteClick;
    public FriendListAdapter(List<FriendListInfo> friendLists, Context context,OnFriendDeleteClick click) {
        this.list = friendLists;
        this.context = context;
        states = new HashMap<>();
        deleteClick = click;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_info, parent, false);
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(FriendListAdapter.ViewHolder holder, int position) {
        final FriendListInfo friend = list.get(position);
        if(friend.getFriend_photo()!=null){
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + friend.getFriend_photo(), holder.iv_avatar);
        }

        holder.tv_name.setText(friend.getFriend_name());
        holder.btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id", friend.getFriend_userid());
                intent.putExtra("name", friend.getFriend_name());
                context.startActivity(intent);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertView mAlertView = new AlertView("提示", "你确定删除改好友吗？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, final int position) {
                        if (position == 0) {
                            String url = NetConstant.DELETE_FRIEND;
                            StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String status = jsonObject.getString("status");
                                        if (status.equals("success")) {
                                            ToastUtil.showShort(context, "删除成功");
                                            deleteClick.onClick(position);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    ToastUtil.Toast(context, "网络错误，请检查");
                                }
                            });
                            request.putValue("userid", userInfo.getUserId());
                            request.putValue("friend_userid", friend.getFriend_userid());
                            SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
                        }else {

                        }
                    }
                }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(Object o) {

                    }
                });
                mAlertView.show();
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private RoundImageView iv_avatar;
        private TextView tv_name;
        private ImageView btn_chat;
        private ImageView btn_delete;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_avatar = (RoundImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            btn_chat =(ImageView)itemView.findViewById(R.id.btn_chat);
            btn_delete = (ImageView) itemView.findViewById(R.id.btn_delete);

        }


    }

    public interface OnFriendDeleteClick{
        void onClick(int position);
    }

}
