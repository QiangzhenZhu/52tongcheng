package cn.xcom.banjing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.locationBase;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private List<locationBase.ChildlistBean> cityList;
    ItemCliclk itemCliclk;
    public CityAdapter(List<locationBase.ChildlistBean> cityList,ItemCliclk itemCliclk) {
        this.cityList = cityList;
        this.itemCliclk = itemCliclk;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_location);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_text,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        locationBase.ChildlistBean city = cityList.get(position);
        holder.textView.setText(city.getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCliclk.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (cityList!=null){
            return cityList.size();
        }else {
            return 0;
        }
    }
    public interface ItemCliclk{
        void onClick(int position);
    }
}
