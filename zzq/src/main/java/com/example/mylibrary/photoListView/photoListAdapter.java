package com.example.mylibrary.photoListView;

/**
 * Created by 赵自强 on 2017/8/26 8:31.
 * 这个类的用处
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.mylibrary.R;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 赵自强 on 2017/8/26 0:49.
 * 这个类的用处
 */
public class photoListAdapter extends RecyclerView.Adapter<photoListAdapter.ViewHolder> {
    private int LayoutId;
    private Context context;
    private List<PictureDataBean> seledtedList;
    private List<PictureDataBean> DataList;
    private OnPhotoSeletedListener listener;
    private int MaxSelectCount;
//    private ImageLoader imageLoader  ;
//    private DisplayImageOptions options = new DisplayImageOptions.Builder().
//            showImageOnLoading(R.drawable.wait).showImageOnFail(R.mipmap.ic_launcher)
//            .cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565) .build();
    public photoListAdapter(List<String> list1, Context ccontext, int layoutId, int maxCount) {
        this.MaxSelectCount = maxCount;
        Collections.reverse(list1);
//        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(ccontext)
//                .writeDebugLogs() //打印log信息
//                .build();
//        ImageLoader.getInstance().init(configuration);
//        imageLoader = ImageLoader.getInstance();
        seledtedList = new ArrayList<>();
        DataList = new ArrayList<>();
        for (String s :list1){
            DataList.add(new PictureDataBean(DataList.size(),s));
        }
        context = ccontext;
        LayoutId = layoutId;
    }
    public interface OnPhotoSeletedListener{
        void OnPhotoSeleted(int count);
    }
    public void setList(List<String> list) {
        DataList.clear();
        for (String s :list){
            DataList.add(new PictureDataBean(DataList.size(),s));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(LayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (DataList.get(position).isSelected()){
            holder.picCount.setVisibility(View.VISIBLE);
            holder.picCount.setText(""+(getPosition(DataList.get(position).getPath()) + 1));
        }else {
            holder.picCount.setVisibility(View.INVISIBLE);
        }
        Glide.with(context)
                .load(DataList.get(position).getPath())
                .into(holder.iamgeViewAdjustHeight);
//        imageLoader.displayImage(ImageDownloader.Scheme.FILE.wrap(DataList.get(position).getPath()),holder.iamgeViewAdjustHeight,options);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataList.get(position).isSelected()){
                   DataList.get(position).setSelected(false);
                    int o = getPosition(DataList.get(position).getPath());
                    seledtedList.remove(o);
                    notifyItemChanged(position);
                    upDataView(o);
                }else {
                    if (seledtedList.size() < MaxSelectCount){
                        DataList.get(position).setSelected(true);
                        seledtedList.add(new PictureDataBean(DataList.get(position).getPositon(),DataList.get(position).getPath()));
                        notifyItemChanged(position);
                    }
                }
                if (listener!= null){
                    listener.OnPhotoSeleted(seledtedList.size());
                }
            }
        });
    }
    public String getFirstStringSeletedPic(){
        if (seledtedList.size() != 0){
            return seledtedList.get(0).getPath();
        }
        return "nothing";
    }
    public void setListener(OnPhotoSeletedListener listener) {
        this.listener = listener;
    }

    public List<PictureDataBean> getSeledtedList() {
        return seledtedList;
    }

    private int getPosition(String path){
        int c = -1;
        int o = 0;
        for ( o = 0; o < seledtedList.size();o++){
            if (seledtedList.get(o).getPath().equals(path)){
                c = o;
                break;
            }
            c = o;
        }
        if (c <seledtedList.size() ){
            return c;
        }
        return c;
    }
    private void upDataView(int position) {
        for (int o = position; o < seledtedList.size(); o++){
            Log.i("看","刷新" + o);
            notifyItemChanged(seledtedList.get(o).getPositon());
        }
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {
        private IamgeViewAdjustHeight iamgeViewAdjustHeight;
        private TextView picCount;
        public ViewHolder(View itemView) {
            super(itemView);
            iamgeViewAdjustHeight = (IamgeViewAdjustHeight) itemView.findViewById(R.id.imageViewItem);
            picCount = (TextView) itemView.findViewById(R.id.picCount);
        }
    }


}
