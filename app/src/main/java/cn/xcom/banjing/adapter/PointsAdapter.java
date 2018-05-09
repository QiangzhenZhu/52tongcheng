package cn.xcom.banjing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.ScoreList;
import cn.xcom.banjing.constant.NetConstant;

/**
 * Created by 10835 on 2018/3/21.
 */

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {
    private List<ScoreList.DataBean.ScoreListBean> scoreListBeans;

    public PointsAdapter(List<ScoreList.DataBean.ScoreListBean> scoreListBeans) {
        this.scoreListBeans = scoreListBeans;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mPointText;
        private TextView mCompleteText;
        private ImageView mItemIcom;
        public ViewHolder(View itemView) {
            super(itemView);
            mPointText = (TextView) itemView.findViewById(R.id.tv_score);
            mCompleteText = (TextView) itemView.findViewById(R.id.tv_complete);
            mItemIcom = (ImageView) itemView.findViewById(R.id.iv_item_score);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_points_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScoreList.DataBean.ScoreListBean score = scoreListBeans.get(position);
        if ("1".equals(score.getType())) {
            holder.mPointText.setText(score.getTitle() + "   积分 +" + score.getScore());
        }else {
            holder.mPointText.setText(score.getTitle() + "   积分 -" + score.getScore());

        }
        Picasso.with(holder.itemView.getContext()).load(NetConstant.NET_HOST + "/" + score.getIcon_url()).into(holder.mItemIcom);

    }

    @Override
    public int getItemCount() {
        return scoreListBeans.size();
    }
}
