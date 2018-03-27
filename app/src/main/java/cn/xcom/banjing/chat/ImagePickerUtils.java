package cn.xcom.banjing.chat;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.banjing.R;

/**
 * Created by mac on 2017/10/3.
 */

public class ImagePickerUtils {
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private static FunctionConfig functionConfig;

    private int maxPic;//最大图片数

    public ImagePickerUtils(int maxPic) {
        this.maxPic = maxPic;
    }



    //多选 不可编辑
    private FunctionConfig getMultipleChoiceFc(List<PhotoInfo> selectedList) {
        FunctionConfig config = new FunctionConfig.Builder()
//                .setMutiSelect()//配置是否多选
                .setMutiSelectMaxSize(maxPic)//配置多选数量
                .setEnableEdit(false)//开启编辑功能
                .setEnableCrop(false)//开启裁剪功能
                .setEnableRotate(false)//开启旋转功能
                .setEnableCamera(true)//开启相机功能
                .setSelected(selectedList)//添加已选列表,只是在列表中默认呗选中不会过滤图片
                //.setCropWidth(int width)//裁剪宽度
                //.setCropHeight(int height)//裁剪高度
                //.setCropSquare(true)//裁剪正方形
                //.setFilter(List list)//添加图片过滤，也就是不在GalleryFinal中显示
                //setRotateReplaceSource(boolean)//配置选择图片时是否替换原始图片，默认不替换
                //setCropReplaceSource(boolean)//配置裁剪图片时是否替换原始图片，默认不替换
                //setForceCrop(boolean)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                //setForceCropEdit(boolean)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
                //setEnablePreview(boolean)//是否开启预览功能
                .build();
        return config;
    }


    public void init(Context context, List<PhotoInfo> mPhotoList){
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();

        functionConfigBuilder.setMutiSelectMaxSize(maxPic);
        functionConfigBuilder.setEnableEdit(true);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setEnablePreview(true);
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        functionConfig = functionConfigBuilder.build();

        //配置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarTextColor(Color.WHITE)//标题栏文本字体颜色
                .setTitleBarBgColor(Color.parseColor("#51a6ff"))//标题栏背景颜色
                .setTitleBarIconColor(Color.WHITE)//标题栏icon颜色，如果设置了标题栏icon，设置setTitleBarIconColor将无效
                .setCheckNornalColor(Color.GRAY)//选择框未选颜色
                .setCheckSelectedColor(Color.parseColor("#51a6ff"))//选择框选中颜色
                //setCropControlColor//设置裁剪控制点和裁剪框颜色
                .setFabNornalColor(Color.parseColor("#51a6ff"))//设置Floating按钮Nornal状态颜色
                .setFabPressedColor(Color.parseColor("#51a6ff"))//设置Floating按钮Pressed状态颜色
                .setIconBack(R.drawable.jc_back)//设置返回按钮icon
                //setIconCamera//设置相机icon
                //setIconCrop//设置裁剪icon
                //setIconRotate//设置选择icon
                //setIconClear//设置清楚选择按钮icon（标题栏清除选择按钮）
                //setIconFolderArrow//设置标题栏文件夹下拉arrow图标
                //setIconDelete//设置多选编辑页删除按钮icon
                //setIconCheck//设置checkbox和文件夹已选icon
                //setIconFab//设置Floating按钮icon
                //setEditPhotoBgTexture//设置图片编辑页面图片margin外背景
                //setIconPreview设置预览按钮icon
                //setPreviewBg设置预览页背景
                .build();
        ImageLoader imageLoader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, theme)
                .setNoAnimcation(true)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);

        GalleryFinal.init(coreConfig);

    }

    public  void openAblum(Context context, List<PhotoInfo> mPhotoList, GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback){
        init(context,mPhotoList);
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
    }

    public  void openCamera(Context context, List<PhotoInfo> mPhotoList, GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback){
        init(context, mPhotoList);
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
    }

    public void openSingleAblum(Context context,GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback){
        init(context,new ArrayList<PhotoInfo>());
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY,functionConfig,mOnHanlderResultCallback);
    }

    public void openSingleCamera(Context context,GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback){
        init(context,new ArrayList<PhotoInfo>());
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
    }

}
