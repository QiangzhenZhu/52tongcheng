package com.example.mylibrary.photoListView;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.mylibrary.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵自强 on 2017/8/26 8:28.
 * 这个类的用处
 */

public class QPhotoView extends LinearLayout {
    private Context mContext;
    private RecyclerView recyclerView;
    private List<String> list ;
    private photoListAdapter adapter;
    private TextView tv_empty;
    private int maxSeleteCount = 1;
    private boolean showCrop = true;
    private int sendBtnBackground = R.drawable.send_photo_btn;
    private int albumTextColor  = Color.parseColor("#7D7DFF");
    private int  cropEnabledTextColor = Color.parseColor("#7D7DFF");
    private  int  cropNoUnEnableTextColor = Color.parseColor("#bbbbbb");
    private  int sendEnabledTextColor =  Color.parseColor("#ffffff");
    private   int sendUnenabledTextColor =Color.parseColor("#ffffff");
    private TextView album;
    private TextView crop;
    private TextView send_photo;
    private QPhotoListener listener;
    private LinearLayout photoListView;
    public QPhotoView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.photo_list_view,this);
        recyclerView = (RecyclerView) findViewById(R.id.photoList);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        init();
    }

    public QPhotoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.photo_list_view,this);
        recyclerView = (RecyclerView) findViewById(R.id.photoList);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        album= (TextView) findViewById(R.id.album);
        crop= (TextView) findViewById(R.id.crop);
        send_photo= (TextView) findViewById(R.id.send_photo);
        photoListView = (LinearLayout) findViewById(R.id.photoListView);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.QPhotoView));

    }

    public void setQPhotoListener(QPhotoListener listener) {
        this.listener = listener;
        init();
    }

    public QPhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.photo_list_view,this);
        recyclerView = (RecyclerView) findViewById(R.id.photoList);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        album= (TextView) findViewById(R.id.album);
        crop= (TextView) findViewById(R.id.crop);
        send_photo= (TextView) findViewById(R.id.send_photo);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.QPhotoView));

    }

    private void parseAttributes(TypedArray array) {
        sendBtnBackground = array.getResourceId(R.styleable.QPhotoView_sendBtnBackground,sendBtnBackground);
        showCrop = array.getBoolean(R.styleable.QPhotoView_showCrop,showCrop);
        albumTextColor  =array.getColor(R.styleable.QPhotoView_albumTextColor,albumTextColor);
        cropEnabledTextColor = array.getColor(R.styleable.QPhotoView_cropEnabledTextColor,cropEnabledTextColor);
        cropNoUnEnableTextColor = array.getColor(R.styleable.QPhotoView_cropNoUnEnableTextColor,cropNoUnEnableTextColor);
        sendEnabledTextColor = array.getColor(R.styleable.QPhotoView_sendEnabledTextColor,sendEnabledTextColor);
        sendUnenabledTextColor =array.getColor(R.styleable.QPhotoView_sendNoUnEnableTextColor,sendUnenabledTextColor);
        maxSeleteCount =array.getInteger(R.styleable.QPhotoView_maxSeleteCount,maxSeleteCount);
        Log.i("看maxSeleteCount",""+maxSeleteCount);
        array.recycle();
        initsetting();
    }

    private void initsetting() {
        if (showCrop){
            crop.setVisibility(VISIBLE);
            crop.setEnabled(false);
            crop.setTextColor(cropNoUnEnableTextColor);
        }else {
            crop.setVisibility(GONE);
        }
        album.setTextColor(albumTextColor);
        send_photo.setBackgroundResource(sendBtnBackground);
        send_photo.setEnabled(false);

        init();
    }
    private void init() {
//        list = new ArrayList<>();
        tv_empty.setText("你可以使用相机拍摄照片或视频");
        list = getSystemPhotoList(mContext);
        if (list == null) {
            tv_empty.setVisibility(VISIBLE);
            photoListView.setVisibility(GONE);
        }else {
            tv_empty.setVisibility(GONE);
            photoListView.setVisibility(VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter = new photoListAdapter( list,mContext, R.layout.photo_list_item,maxSeleteCount));
            adapter.setListener(new photoListAdapter.OnPhotoSeletedListener() {
                @Override
                public void OnPhotoSeleted(int count) {
                    if (count == 0){
                        send_photo.setEnabled(false);
                        send_photo.setText("发送");
                        send_photo.setTextColor(sendUnenabledTextColor);
                        if (showCrop ){
                            crop.setTextColor(cropNoUnEnableTextColor);
                            crop.setEnabled(false);
                        }
                    }else {
                        send_photo.setText("发送 ("+count+" )");
                        send_photo.setEnabled(true);
                        send_photo.setTextColor(sendUnenabledTextColor);
                        if (showCrop){
                            if (count == 1){
                                crop.setEnabled(true);
                                crop.setTextColor(cropEnabledTextColor);
                            }else {
                                crop.setEnabled(false);
                                crop.setTextColor(cropNoUnEnableTextColor);
                            }
                        }
                    }
                }
            });
            if (listener != null){
                send_photo.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.OnSendBtnClicked(adapter.getSeledtedList().size(),adapter.getSeledtedList());
                    }
                });
                album.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onAlbumClicked();
                    }
                });
                crop.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (adapter.getFirstStringSeletedPic().equals("nothing")){

                        }else {
                            listener.OnCropClicked(adapter.getFirstStringSeletedPic());
                        }
                    }
                });
            }
        }

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE){
            init();
        }
    }


    public static List<String> getSystemPhotoList(Context context)
    {
        List<String> result = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
        while (cursor.moveToNext())
        {
            int index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // 文件地址
            File file = new File(path);
            if (file.exists())
            {
                result.add(path);
            }
        }

        return result ;
    }
}
