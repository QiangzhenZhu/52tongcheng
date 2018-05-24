package cn.xcom.banjing.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.utils.MyImageLoader;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class ListGridview extends BaseAdapter{
    private List <String>list;
    private Context context;
    private List<Convenience>convenienceList;
    private int pposition;
    Drawable drawable;
    ImageView img;


    public ListGridview(Context context, List <String>list,List<Convenience> convenienceList,int pposition) {
        this.context = context;
        this.list = list;
        this.convenienceList = convenienceList;
        this.pposition = pposition;
    }

    @Override
    public int getCount() {
        return  list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        Convenience convenience = convenienceList.get(pposition);
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_gridview,null);
            viewHolder.imageView1= (ImageView) convertView.findViewById(R.id.add_image);
            img = viewHolder.imageView1;
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if (!convenience.getVideo().equals("")) {
            // TODO: 2018/5/9   
            /*if (position == convenience.getPic().size() - 1) {
                img.setImageResource(R.drawable.ic_bofang);
                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                MyImageLoader.displayBg(list.get(position),viewHolder.imageView1);

            }else {
                MyImageLoader.display(list.get(position),viewHolder.imageView1);

            }*/
        }else {
            MyImageLoader.display(list.get(position),viewHolder.imageView1);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView1,imageView2;
    }
}
