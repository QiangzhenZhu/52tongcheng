package cn.xcom.banjing.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.DetailAuthenticatinActivity;
import cn.xcom.banjing.activity.DisplayAdViewPagerActivivty;
import cn.xcom.banjing.activity.DisplayAdvertisingActivity;
import cn.xcom.banjing.activity.DisplayVideoAdvertisingActivity;
import cn.xcom.banjing.bean.AuthenticationList;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.MyImageLoader;
import cn.xcom.banjing.utils.RoundImageView;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class ConvenienceAdapter extends RecyclerView.Adapter<ConvenienceAdapter.ViewHolder> {
    private List<Convenience> list;
    private List<String> addList;
    private Context context;
    private ImageView imageView;
    //private ListGridview listGridview;
    private UserInfo userInfo;
    private Map<Integer, Boolean> states;
    private String videoPic;
    List<AuthenticationList> authenticationLists;
    public int adPosition = 0;
    private int type;
    public ConvenienceAdapter(List<Convenience> list, Context context, @Nullable int type) {
        this.list = list;
        this.context = context;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        states = new HashMap<>();
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Convenience convenience = list.get(position);
        states.put(position, true);
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + convenience.getPhoto(), holder.convenience_photo);
        holder.convenience_name.setText(convenience.getName());
        if (convenience.getContent() != null) {
            holder.tv_content.setText(convenience.getContent());
        }

        holder.convenience_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailAuthenticatinActivity.class);
                intent.putExtra("haveid", true);
                intent.putExtra("userid", convenience.getUserid());
                context.startActivity(intent);
            }
        });
        addList = new ArrayList<>();
        authenticationLists = new ArrayList<>();
        addList.clear();
        if (convenience.getPic().size() > 0) {
            for (int i = 0; i < convenience.getPic().size(); i++) {
                addList.add(NetConstant.NET_DISPLAY_IMG + convenience.getPic().get(i).getPictureurl());
            }

            if (!convenience.getVideo().equals("")) {
                adPosition=position;
                holder.title_back_img.setVisibility(View.VISIBLE);
                holder.title_back_img.setImageResource(R.drawable.ic_bofang);
                MyImageLoader.display(addList.get(0), holder.title_img);




            } else {
                adPosition=position;
                holder.title_back_img.setVisibility(View.GONE);
                MyImageLoader.display(addList.get(0),holder.title_img);
            }
        }
        holder.title_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Bundle bundle = new Bundle();
//                bundle.putSerializable("adinfo", convenience);
////                Intent intent = new Intent(context, DisplayAdvertisingActivity.class);
////                Intent intent = new Intent(context, AdDetailActivity.class);
//                Intent intent = new Intent(context, DisplayVideoAdvertisingActivity.class);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
                if (!convenience.getVideo().equals("")) {
                    /*Bundle bundle = new Bundle();
                    bundle.putSerializable("adinfo", convenience);
                    Log.d("hellonihao", "1 ");
                    Intent intent = new Intent(context, DisplayVideoAdvertisingActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);*/
                    if (type  != 1) {//广告启动
                        Intent intent = DisplayAdViewPagerActivivty.newIntent(context, list, position, 1);
                        context.startActivity(intent);
                    }else {
                        Intent intent = DisplayAdViewPagerActivivty.newIntent(context, list, position, 2);
                        intent.putExtra("adinfo",convenience);
                        context.startActivity(intent);
                    }

                } else {
                    //图片启动
                    /*
                    Log.d("hellonihao", "2 ");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("adinfo", convenience);
                    Intent intent = new Intent(context, DisplayAdvertisingActivity.class);
                    intent.putExtra("title", convenience.getContent());
                    intent.putExtra("username", convenience.getName());
                    intent.putExtra("position", position);

                    intent.putExtras(bundle);
                    if( convenience.getPic().size()>0){
                        intent.putExtra("ImageList", convenience.getPic().get(0).getPictureurl());
                    }
                    //LogUtils.d("ImageList", convenience.getPic().get(0).getPictureurl() + position);
                    context.startActivity(intent);*/
                    if (type != 1) {
                        Intent intent = DisplayAdViewPagerActivivty.newIntent(context, list, position, 1);
                        context.startActivity(intent);
                    }else {
                        Intent intent = DisplayAdViewPagerActivivty.newIntent(context, list, position, 2);
                        intent.putExtra("adinfo",convenience);
                        context.startActivity(intent);
                    }
                }


            }
        });
        holder.iv_like.setVisibility(View.VISIBLE);
        //    holder.iv_shanchu.setVisibility(View.GONE);
        if (convenience.getLike()!=null) {
            int likeCount = convenience.getLike().size();
            holder.tv_likecount.setText(likeCount+"");
        }
        holder.tv_packet.setText(convenience.getRedpacket());
        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHadLikePost(convenience,convenience.getLike().size(),holder);
            }
        });
        if (convenience.getTouchcount() !=null) {
            holder.tv_redCount.setText(convenience.getTouchcount());
        }
        int soundTime = 0;
        if (!TextUtils.isEmpty(convenience.getSoundtime()))

        {
            soundTime = Integer.valueOf(convenience.getSoundtime());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoundImageView convenience_photo;
        private TextView convenience_name, tv_likecount, tv_packet, tv_content, tv_redCount;
        private ImageView convenience_image;
        private ImageView convenience_phone;//convenience_message
        private ImageView iv_like, title_img, title_back_img;


        public ViewHolder(View itemView) {
            super(itemView);
            convenience_photo = (RoundImageView) itemView.findViewById(R.id.convenience_photo);
            convenience_name = (TextView) itemView.findViewById(R.id.convenience_name);
            tv_likecount = (TextView) itemView.findViewById(R.id.tv_like_count);
            tv_packet = (TextView) itemView.findViewById(R.id.tv_packet_count);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
            title_img = (ImageView) itemView.findViewById(R.id.title_img);
            convenience_phone = (ImageView) itemView.findViewById(R.id.convenience_phone);
            tv_content = (TextView) itemView.findViewById(R.id.ev_ad_content);
            tv_redCount= (TextView) itemView.findViewById(R.id.touched_red_count);
            title_back_img= (ImageView) itemView.findViewById(R.id.back_img);

        }

    }

    /**
     * 点赞
     * @param convenience
     */
    public  void SetLikePost(Convenience convenience, final int likeCount, final ViewHolder holder){
        String url = NetConstant.SETLIKE_POST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String count=likeCount+1+"";
                        holder.tv_likecount.setText(count);
                        ToastUtil.showShort(context, "点赞成功");

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

        request.putValue("refid", convenience.getMid());
        request.putValue("type", "1");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 先判断受否点赞过，没有点过赞再调用SetLikePost（）进行点赞；
     */
    public  void setHadLikePost(final Convenience con, final int likeCount, final ViewHolder holder){
        final Convenience convenience =con;
        String url = NetConstant.CHECK_HAD_LIKE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String isLike=jsonObject.getString("data");
                        if (!"had".equals(isLike)){
                            SetLikePost(convenience,likeCount,holder);
                        }else {
                            Toast.makeText(context,"你已经点过赞了",Toast.LENGTH_SHORT).show();
                        }
                    }if (status.equals("error")&&"no".equals(jsonObject.getString("data"))){
                        SetLikePost(convenience,likeCount,holder);
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

        request.putValue("refid", convenience.getMid());
        request.putValue("type", "1");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }
}
