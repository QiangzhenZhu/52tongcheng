package cn.xcom.banjing.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.DisplayVideoAdvertisingActivity;
import cn.xcom.banjing.activity.PaymentActivity;
import cn.xcom.banjing.activity.SpaceVideoDetialActivity;
import cn.xcom.banjing.bean.MyMessage;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.record.SoundView;
import cn.xcom.banjing.utils.MyImageLoader;
import cn.xcom.banjing.utils.NoScrollGridView;
import cn.xcom.banjing.utils.RoundImageView;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.TimeUtils;
import cn.xcom.banjing.utils.ToastUtil;
import cn.xcom.banjing.view.TextViewExpandableAnimation;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class NoReleaseAdapter extends RecyclerView.Adapter<NoReleaseAdapter.ViewHolder> {
    private List<MyMessage> list;
    private List<String> addList;
    private Context context;
    private ImageView imageView;
    private ListGridviews listGridview;
    private UserInfo userInfo;
    private Map<Integer, Boolean> states;
    private String payId;
    public NoReleaseAdapter(List<MyMessage> list, Context context) {
        this.list = list;
        this.context = context;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        states = new HashMap<>();

    }

    @Override
    public NoReleaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_msg_rec_item, parent, false);
        return new NoReleaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoReleaseAdapter.ViewHolder holder, final int position) {
        final MyMessage myMessage = list.get(position);
        states.put(position, true);

        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + myMessage.getPhoto(), holder.convenience_photo);
        holder.convenience_name.setText(myMessage.getName());
        holder.convenience_time.setText(TimeUtils.getDateToString(myMessage.getCreate_time() * 1000));
        holder.contentEtv.setText(myMessage.getContent());
        holder.contentEtv.setOnStateChangeListener(new TextViewExpandableAnimation.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                states.put(position, isShrink);
            }
        });
//        holder.tvExpand.setText(mLists.get(position).getText());
        //important! reset its state as where it left
        holder.contentEtv.resetState(states.get(position));
        holder.gopay.setVisibility(View.VISIBLE);
        holder.gopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payId=myMessage.getMid();
                getMessagePrice();
                Log.d("---payid",payId);
            }
        });
        addList = new ArrayList<>();
        if (myMessage.getPic().size() > 0) {
            for (int i = 0; i < myMessage.getPic().size(); i++) {
                addList.add(NetConstant.NET_DISPLAY_IMG + myMessage.getPic().get(i).getPictureurl());
            }
        }
        listGridview = new ListGridviews(context, addList);
        holder.noScrollGridView.setAdapter(listGridview);
        holder.noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!myMessage.getVideo() .equals("") ) {
                    if (position == myMessage.getPic().size() - 1) {
                        String videoUrl = NetConstant.NET_DISPLAY_IMG+myMessage.getVideo();
                        Intent intent = new Intent(context, SpaceVideoDetialActivity.class);
                        intent.putExtra("videoUrl",videoUrl);
                        context.startActivity(intent);

                    }else {
                        addList.clear();
                        if (myMessage.getPic().size() > 0) {
                            for (int i = 0; i < myMessage.getPic().size(); i++) {
                                addList.add(NetConstant.NET_DISPLAY_IMG + myMessage.getPic().get(i).getPictureurl());
                            }
                        }
                        Intent intent = new Intent(context, DisplayVideoAdvertisingActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("list111", (Serializable) addList);
                        intent.putExtra("position",position);
                        intent.putExtras(bundle);
                        int[] location = new int[2];
                        intent.putExtra("locationX", location[1]);//必须
                        intent.putExtra("locationY", location[0]);//必须
                        intent.putExtra("width",view.getWidth());//必须
                        intent.putExtra("height",view.getHeight());//必须
                        context.startActivity(intent);
                    }
                }else {

                    addList.clear();

                    if (myMessage.getPic().size() > 0) {
                        for (int i = 0; i < myMessage.getPic().size(); i++) {
                            addList.add(NetConstant.NET_DISPLAY_IMG + myMessage.getPic().get(i).getPictureurl());
                        }
                    }
                    Intent intent = new Intent(context, DisplayVideoAdvertisingActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("list111", (Serializable) addList);
                    intent.putExtra("position",position);
                    intent.putExtras(bundle);
                    int[] location = new int[2];
                    intent.putExtra("locationX", location[1]);//必须
                    intent.putExtra("locationY", location[0]);//必须
                    intent.putExtra("width",view.getWidth());//必须
                    intent.putExtra("height",view.getHeight());//必须
                    context.startActivity(intent);
                }
            }
        });
        int soundTime = 0;
        if (!TextUtils.isEmpty(myMessage.getSoundtime())) {
            soundTime = Integer.valueOf(myMessage.getSoundtime());
        }
        if (TextUtils.isEmpty(myMessage.getSound())) {
            holder.soundView.setVisibility(View.GONE);
        } else {
            holder.soundView.setVisibility(View.VISIBLE);
            holder.soundView.init(NetConstant.NET_DISPLAY_IMG + myMessage.getSound(), soundTime);
        }
        holder.ivshanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertView mAlertView = new AlertView("提示", "你确定删除自己的便民圈？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, final int position) {
                        if (position == 0) {
                            String url = NetConstant.DELETE_OWN_POST;
                            StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String status = jsonObject.getString("status");
                                        if (status.equals("success")) {
                                            ToastUtil.showShort(context, "删除成功");
                                            list.remove(position);
                                            notifyDataSetChanged();
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
                            request.putValue("id", myMessage.getMid());
                            SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
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
        private RoundImageView convenience_photo;
        private TextView convenience_name;
        private TextView convenience_time;
        private TextView gopay;
        private NoScrollGridView noScrollGridView;
        private SoundView soundView;
        private TextViewExpandableAnimation contentEtv;
        private ImageView ivshanchu;
        public ViewHolder(View itemView) {
            super(itemView);
            convenience_photo = (RoundImageView) itemView.findViewById(R.id.msg_convenience_photo);
            convenience_name = (TextView) itemView.findViewById(R.id.msg_convenience_name);
            convenience_time = (TextView) itemView.findViewById(R.id.msg_convenience_time);
            contentEtv = (TextViewExpandableAnimation) itemView.findViewById(R.id.msg_content_etv);
            noScrollGridView = (NoScrollGridView) itemView.findViewById(R.id.msg_gridview);
            soundView = (SoundView) itemView.findViewById(R.id.msg_sound_view);
            gopay = (TextView) itemView.findViewById(R.id.msg_go_pay);
            ivshanchu = (ImageView)itemView.findViewById(R.id.frg_iv_shanchu);
        }
    }
    private void getMessagePrice() {
        String url = NetConstant.GET_MESSAGE_PRICE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("---price", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.optString("status").equals("success")) {

//                        JSONObject publishresult = object.getJSONObject("data");
                        HelperApplication.getInstance().type="";
                        Intent payIntent = new Intent(context, PaymentActivity.class);
                        payIntent.putExtra("paytype", "ConveniencePay");
                        payIntent.putExtra("tradeNo", "M"+payId);
                        payIntent.putExtra("body","便民圈");
                        payIntent.putExtra("price", object.optString("data"));
                        payIntent.putExtra("type","4");
//                        payIntent.putExtra("payId",payId);
                        context.startActivity(payIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });

        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }
}
