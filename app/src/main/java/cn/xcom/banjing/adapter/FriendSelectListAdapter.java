package cn.xcom.banjing.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.ChatActivity;
import cn.xcom.banjing.bean.FriendBean;
import cn.xcom.banjing.bean.FriendListInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.MyImageLoader;

/**
 * Created by mac on 2017/9/28.
 */

public class FriendSelectListAdapter extends RecyclerView.Adapter<FriendSelectListAdapter.ViewHolder>  {
    private List<FriendBean> list;
    private List<String> addList;
    private Context context;
    private Map<Integer, Boolean> states;
    public FriendSelectListAdapter(List<FriendBean> friendLists, Context context) {
        this.list = friendLists;
        this.context = context;
        states = new HashMap<>();

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_friend, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(list.get(position).getPhoto()!=null){
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + list.get(position).getPhoto(), holder.iv_avatar);
        }

        holder.tv_name.setText(list.get(position).getName());
        holder.btn_chat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.e("TGB","---?"+isChecked);
                if (isChecked){
                    list.get(position).setSelectStatus("1");
                }else{
                    list.get(position).setSelectStatus("0");
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView iv_avatar;
        private TextView tv_name;
        private CheckBox btn_chat;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            btn_chat =(CheckBox)itemView.findViewById(R.id.btn_chat);

        }


    }
}
