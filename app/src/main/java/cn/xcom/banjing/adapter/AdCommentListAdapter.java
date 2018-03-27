package cn.xcom.banjing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.CommentInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.MyImageLoader;
import cn.xcom.banjing.utils.RoundImageView;
import cn.xcom.banjing.utils.TimeUtils;


/**
 * Created by 10835 on 2017/12/12.
 */

public class AdCommentListAdapter extends RecyclerView.Adapter<AdCommentListAdapter.AdCommentViewHolder> {
        class AdCommentViewHolder extends RecyclerView.ViewHolder {
            private TextView userNameText;
            private TextView sendTimeText;
            private TextView contentText;
            RoundImageView profileRoundImageView;
            private CommentInfo info;
            public AdCommentViewHolder(View itemView) {
                super(itemView);
                userNameText= (TextView) itemView.findViewById(R.id.tv_chat_item_username);
                sendTimeText= (TextView) itemView.findViewById(R.id.tv_chat_item_send_time);
                contentText= (TextView) itemView.findViewById(R.id.tv_chat_item_chatcontent);
                profileRoundImageView= (RoundImageView) itemView.findViewById(R.id.riv_chat_item_profile);
            }
            public void bind(CommentInfo info){
                this.info=info;
                userNameText.setText(info.getName());
                Long longTime=Long.valueOf(info.getAdd_time());
                String time=TimeUtils.fromateTimeShowByRule(longTime);
                sendTimeText.setText(time);
                contentText.setText(info.getContent());
                String url=NetConstant.NET_DISPLAY_IMG+info.getPhoto();
                Glide.with(itemView.getContext()).load(url).placeholder(R.mipmap.bg_img_none).into(profileRoundImageView);
            }
        }



        List<CommentInfo> commentInfos;

       public AdCommentListAdapter(List<CommentInfo> commentInfos) {
           this.commentInfos = commentInfos;
       }

       @Override
       public AdCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           /*if (viewType == -1){
               return new AdCommentViewHolder(new View(parent.getContext()));
           } else {*/
               View view = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.item_ad_comment_rv, parent, false);
               return new AdCommentViewHolder(view);
           //}

       }

       @Override
       public void onBindViewHolder(AdCommentViewHolder holder, int position) {
            CommentInfo info=commentInfos.get(position);
            holder.bind(info);
       }

       @Override
       public int getItemCount() {
           return commentInfos.size();
       }

    @Override
    public int getItemViewType(int position) {
        /*if (position == 0 ){
            return  -1;
        }
        */
        return super.getItemViewType(position);
    }
}

