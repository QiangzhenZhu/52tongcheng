package cn.xcom.banjing.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mylibrary.Chat.ChatItemView;
import com.example.mylibrary.Chat.OnChatViewClickListener;
import com.example.mylibrary.Chat.photoType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.ImageDetailActivity;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;

import static cn.xcom.banjing.chat.DateUtils.dateToStamp;
import static com.example.mylibrary.Chat.photoType.*;

/**
 * Created by mac on 2017/10/3.
 */


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.viewHolder> {
    private List<ChatItemBean> list;
    private Context context;
    private int LayoutId;
    UserInfo userInfo;
    private Intent intent;
    private ArrayList<String> listPicturePath;
    String s;
    public PlayerManager playerManager;
    public ChatListAdapter(List<ChatItemBean> list, Context context, int layoutId, ArrayList<String> strings) {
        this.list = list;
        this.context = context;
        this.listPicturePath = strings;
        intent = new Intent();
        LayoutId = layoutId;
        userInfo = new UserInfo(context);
        playerManager = PlayerManager.getManager();
    }

    //    AudioManager audioManager;
    public void setListPicturePath(ArrayList<String> listPicturePath) {
        this.listPicturePath = listPicturePath;

    }
    public void setList(List<ChatItemBean> list) {
        this.list = list;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(LayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        ChatItemBean chatItemView = list.get(position);
        holder.rightNickName.setVisibility(View.VISIBLE);
        if (chatItemView.getSend_uid().equals(userInfo.getUserId())) {
            if (position ==0){

                try {
                    holder.chatItemView.setRightTimeShow(dateToStamp(list.get(position).getCreate_time()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if (position != 0 &&Long.parseLong(list.get(position).getCreate_time())- Long.parseLong(list.get(position-1).getCreate_time()) >=1000 * 60 * 2){
                try {
                    holder.chatItemView.setRightTimeShow(dateToStamp(list.get(position).getCreate_time()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                holder.chatItemView.setTimeGone();
            }
            switch (chatItemView.getContent_type()) {
                case "0":
                    holder.chatItemView.setRightTextType(chatItemView.getSend_nickname(), chatItemView.getContent(), NetConstant.SHOW_IMAGE + chatItemView.getSend_face());
                    break;
                case "1":
                    holder.chatItemView.setRightPictureType(chatItemView.getSend_nickname(), NetConstant.SHOW + chatItemView.getPicInfo().getThumb_url(), NetConstant.SHOW_IMAGE + chatItemView.getSend_face());
                    break;
                case "2":
                    holder.chatItemView.setRightVoiceType(chatItemView.getSend_nickname(), chatItemView.getAudioInfo().getDuration(), NetConstant.SHOW_IMAGE + chatItemView.getSend_face());
                    break;
            }
        } else {
            if (position ==0){
                try {
                    holder.chatItemView.setLeftTimeShow(dateToStamp(list.get(position).getCreate_time()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if (position != 0 &&Long.parseLong(list.get(position).getCreate_time())- Long.parseLong(list.get(position-1).getCreate_time()) >=1000 * 60 * 2){
                try {
                    holder.chatItemView.setLeftTimeShow(dateToStamp(list.get(position).getCreate_time()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                holder.chatItemView.setTimeGone();
            }
            switch (chatItemView.getContent_type()) {
                case "0":
                    holder.chatItemView.setLeftTextType(chatItemView.getSend_nickname(), chatItemView.getContent(), NetConstant.SHOW_IMAGE + chatItemView.getSend_face());
                    break;
                case "1":
                    holder.chatItemView.setLeftPictureType(chatItemView.getSend_nickname(), NetConstant.SHOW + chatItemView.getPicInfo().getThumb_url(), NetConstant.SHOW_IMAGE + chatItemView.getSend_face());
                    break;
                case "2":
                    holder.chatItemView.setLeftVoiceType(chatItemView.getSend_nickname(), chatItemView.getAudioInfo().getDuration(), NetConstant.SHOW_IMAGE + chatItemView.getSend_face());
                    break;
            }


        }
//        switch (chatItemView.getChatType()){
//            case textLeft:
//                Log.i("看左边文字",""+chatItemView.getText());
//                holder.chatItemView.setLeftTextType(chatItemView.getHisname(),chatItemView.getText(),chatItemView.getHisPhoto());
//                break;
//            case textRight:
//                Log.i("看右边文字",""+chatItemView.getText());
//                holder.chatItemView.setRightTextType("我",chatItemView.getText(),chatItemView.getMyPhoto());
//                break;
//            case pictureLeft:
//                holder.chatItemView.setLeftPictureType(chatItemView.getHisname(),chatItemView.getPicturePath(),chatItemView.getHisPhoto());
//                break;
//            case pictureRight:
//                holder.chatItemView.setRightPictureType("我",chatItemView.getPicturePath(),chatItemView.getMyPhoto());
//                break;
//            case voiceLeft:
//                holder.chatItemView.setLeftVoiceType(chatItemView.getHisname(),chatItemView.getDurtation(),chatItemView.getHisPhoto());
//                break;
//            case voiceRight:
//                holder.chatItemView.setRightVoiceType("我",chatItemView.getDurtation(),chatItemView.getMyPhoto());
//                break;
//        }
        holder.chatItemView.setOnChatViewClickListener(new OnChatViewClickListener() {
            /*头像的点事件监听*/
            @Override
            public void OnPhotoClicked(View view, photoType photoType) {
                switch (photoType) {
                    case left:
                        Log.i("看", "看" + "    " + position + "条数据：" + "左头像被点击");
                        break;
                    case right:
                        Log.i("看", "看" + "    " + position + "条数据：" + "右头像被点击");
                        break;
                }
            }

            @Override
            public void OnTextViewLongClicked(View view) {
                Log.i("看", "看" + "    " + position + "条数据：" + "文本被长按");
            }

            @Override
            public void OnPlayButtonClickedListener(View view,final photoType playtype) {
                Log.i("看", "看" + "    " + position + "条数据：" + "播放按钮被点击"+list.get(position).getAudioInfo().getAudio_url());
                if (playerManager.isPlaying()){
                    playerManager.stop();
                }
                final ImageView playbutton;
                if(playtype==left){
                    playbutton=holder.ivAudio;
                }else{
                    playbutton=holder.ivAudio_r;
                }
                PlayerManager.PlayCallback callback = new PlayerManager.PlayCallback() {

                    @Override
                    public void onPrepared() {
//                         addHint("音乐准备完毕,开始播放");
                            if (playbutton != null && playbutton.getBackground() instanceof AnimationDrawable) {
                                AnimationDrawable animation = (AnimationDrawable) playbutton.getBackground();
                                animation.start();
                            }
                    }
                    @Override
                    public void onComplete() {
//                                    addHint("音乐播放完毕");
                        if (playbutton != null && playbutton.getBackground() instanceof AnimationDrawable) {
                            AnimationDrawable animation = (AnimationDrawable) playbutton.getBackground();
                            animation.stop();
                            animation.selectDrawable(0);
                        }
                    }

                    @Override
                    public void onStop() {
                        if (playbutton != null && playbutton.getBackground() instanceof AnimationDrawable) {
                            AnimationDrawable animation = (AnimationDrawable) playbutton.getBackground();
                            animation.stop();
                            animation.selectDrawable(0);
                        }
                    }
                };
                playerManager.play(NetConstant.SHOW + list.get(position).getAudioInfo().getAudio_url(), callback);
            }

            @Override
            public void OnPictureClickedListener(View view) {
                ChatItemBean chatItemView = list.get(position);
//                intent.putExtra(FunctionConfig.EXTRA_PREVIEW_SELECT_LIST, (Serializable) listPicturePath);
//                intent.putExtra(FunctionConfig.EXTRA_POSITION, position);
//                context.startActivity(intent);

//                s = communicateModel.getContent();
                s = chatItemView.getPicInfo().getOriginal_url();
                int selectposition=0;
                for (int i = 0; i < listPicturePath.size(); i++) {
                    Log.i("看", "看" + "    " + listPicturePath.get(i));
                    if (s.equals(listPicturePath.get(i))) {
                        selectposition=i;

                        intent.setClass(context, ImageDetailActivity.class);
                        intent.putStringArrayListExtra("Imgs", listPicturePath);
                        intent.putExtra("fromtype", "chat");
                        intent.putExtra("position",selectposition);
                        intent.putExtra("type", "4");
                        context.startActivity(intent);

                        break;
                    }
                }
                Log.i("看", "看" + "    " + position + "条数据：" + "照片被点击");
            }

            @Override
            public void OnPictureLongClickedListener(View view) {
                Log.i("看", "看" + "    " + position + "条数据：" + "照片被长按");
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    final class viewHolder extends RecyclerView.ViewHolder {
        private ChatItemView chatItemView;
        private TextView time;
        private ImageButton play;
        private ImageView ivAudio, ivAudio_r;
        private TextView leftNickName;
        private TextView rightNickName;
        private RelativeLayout rlAudio;
        private RelativeLayout rlAudio_r;
        public viewHolder(View itemView) {
            super(itemView);
            chatItemView = (ChatItemView) itemView.findViewById(R.id.c);
            time = (TextView) itemView.findViewById(R.id.time);
            ivAudio = (ImageView) itemView.findViewById(R.id.playLeft);
            ivAudio_r = (ImageView) itemView.findViewById(R.id.playRight);
            leftNickName= (TextView) itemView.findViewById(R.id.nicknameLeft );
            rightNickName= (TextView) itemView.findViewById(R.id.nicknameRight);
            rlAudio= (RelativeLayout) itemView.findViewById(R.id.chatVoiceLeft);
            rlAudio_r= (RelativeLayout) itemView.findViewById(R.id.chatVoiceRight);
        }
    }
}
