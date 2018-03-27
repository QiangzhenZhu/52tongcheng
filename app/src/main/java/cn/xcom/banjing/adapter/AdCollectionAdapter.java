package cn.xcom.banjing.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.SaleDetailActivity;
import cn.xcom.banjing.bean.Collection;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.MyImageLoader;

/**
 * Created by 10835 on 2018/3/17.
 */

public class AdCollectionAdapter extends BaseAdapter {
    private List<Collection> addList;
    private Context context;

    public AdCollectionAdapter(List<Collection> addList, Context context) {
        this.addList = addList;
        this.context = context;
    }


    @Override
    public int getCount() {
        Log.e("=======shipei1qi", "" + addList.size());
        return addList.size();

    }

    @Override
    public Object getItem(int position) {
        return addList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e("========shipeiqi", "ZOUBUZOU");
        AdCollectionAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new AdCollectionAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.collec_layout, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgview);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.title);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.content);
            viewHolder.textView3 = (TextView) convertView.findViewById(R.id.price);
            viewHolder.button = (Button) convertView.findViewById(R.id.havesell);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AdCollectionAdapter.ViewHolder) convertView.getTag();
        }
        final Collection front = addList.get(position);
        Log.e("========shipeiqi", "" + addList.size());
        if(front.getGoodspicture().size()!=0){
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + front.getGoodspicture().get(0).getFile(), viewHolder.imageView);
        }
        viewHolder.textView1.setText(front.getTitle());
        viewHolder.textView2.setText(front.getDescription());
        viewHolder.textView3.setVisibility(View.GONE);
        viewHolder.button.setVisibility(View.GONE);

//        viewHolder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context,SaleDetailActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("item10", addList.get(position));
//                Log.i("---", addList.size() + "");
//                intent.putExtras(bundle);
//                // intent.putExtra("item",bundle);
//                Log.i("---", position + "");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SaleDetailActivity.class);
                intent.putExtra("id",front.getObject_id());
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    public class ViewHolder {
        private ImageView imageView;
        private TextView textView1, textView2, textView3, textView4, textView5;
        private Button button;

    }
}
