package cn.xcom.banjing.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.ChatActivity;
import cn.xcom.banjing.bean.ChatListInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.MyImageLoader;
import cn.xcom.banjing.utils.RoundImageView;

/**
 * Created by mac on 2017/10/6.
 */

public class ChatListAdapter  extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>  {
    private List<ChatListInfo> chatLists;
    private List<String> addList;
    private Context context;
    public ChatListAdapter(List<ChatListInfo> chatlist, Context context) {
        this.chatLists = chatlist;
        this.context = context;

    }
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatListAdapter.ViewHolder holder, int position) {

        final ChatListInfo info = chatLists.get(position);
        holder.nameTv.setText(info.getOther_nickname());
        holder.timeTv.setText(info.getCreate_time());
        holder.contentTv.setText(info.getLast_content());
        if (Integer.parseInt(info.getNoreadcount()) < 1) {
            holder.msgCountTv.setVisibility(View.GONE);
        } else {
            holder.msgCountTv.setVisibility(View.VISIBLE);
            holder.msgCountTv.setText(info.getNoreadcount());
        }
        if (info.getOther_face() != null) {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                    info.getOther_face(), holder.iconIv);
        } else {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, holder.iconIv);
        }
        holder.showchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id", info.getChat_uid());
                intent.putExtra("name", info.getOther_nickname());
                intent.putExtra("receive_type", info.getReceive_type());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, contentTv, timeTv, msgCountTv;
        RelativeLayout showchat;
        RoundImageView iconIv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);
            contentTv = (TextView) itemView.findViewById(R.id.tv_content);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
            iconIv = (RoundImageView) itemView.findViewById(R.id.iv_icon);
            msgCountTv = (TextView) itemView.findViewById(R.id.tv_msgCount);
            showchat=(RelativeLayout) itemView.findViewById(R.id.rl_content);

        }
    }
}
