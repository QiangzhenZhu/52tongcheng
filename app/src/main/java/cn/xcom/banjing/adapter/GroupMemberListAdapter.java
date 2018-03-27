package cn.xcom.banjing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.FriendBean;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.temp.RecyclerViewOnItemLongClickListener;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.MyImageLoader;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;

/**
 * Created by mac on 2017/9/28.
 */

public class GroupMemberListAdapter extends RecyclerView.Adapter<GroupMemberListAdapter.ViewHolder> {
    private List<FriendBean> list;
    private List<String> addList;
    private Context context;
    private Map<Integer, Boolean> states;
    private RecyclerViewOnItemLongClickListener onItemLongClickListener;

    public GroupMemberListAdapter(List<FriendBean> friendLists, Context context) {
        this.list = friendLists;
        this.context = context;
        states = new HashMap<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_member_friend, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    onItemLongClickListener.onItemLongClickListener(v, (Object) v.getTag());
                }
                return false;
            }
        });
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(list.get(position).getPhoto()!=null){
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + list.get(position).getPhoto(), holder.iv_avatar);
        }

        holder.tv_name.setText(list.get(position).getName());

        holder.ll.setTag(list.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*设置点击事件*/
    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_avatar;
        private TextView tv_name;
        private LinearLayout ll;
        public ViewHolder(View itemView) {
            super(itemView);
            ll= (LinearLayout) itemView.findViewById(R.id.bottom_pop_ly);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);

        }


    }
}
