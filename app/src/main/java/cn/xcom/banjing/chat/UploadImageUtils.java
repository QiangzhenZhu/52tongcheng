package cn.xcom.banjing.chat;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.constant.NetConstant;
import okhttp3.Call;

/**
 * Created by mac on 2017/10/3.
 */

public class UploadImageUtils {
    public static  void pushImage(String filePath,String URL, final OnUploadListener onUploadListener) {
        final String fileName = "b" + System.currentTimeMillis() + ".jpg";
        String newPath = HelperApplication.DIRECTORY + fileName;
        //压缩图片
        ImageCompressUtils.compressPicture(filePath,newPath);

        OkHttpUtils.post().url(URL)
                .addFile("upfile", fileName, new File(newPath))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        onUploadListener.onError(e);
                        Log.d("uploadimg", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("看"," "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            onUploadListener.onSuccess(jsonObject.getString("image_Id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onUploadListener.onError(e);
                        }
                        Log.d("uploadimg", response);
                    }
                });
    }
    public static void uploadImg(String filePath, final OnUploadListener onUploadListener) {
        final String fileName = "cc" + System.currentTimeMillis() + ".jpg";
        String newPath = HelperApplication.DIRECTORY + fileName;
        //压缩图片
        ImageCompressUtils.compressPicture(filePath,newPath);

        OkHttpUtils.post().url(NetConstant.NET_UPLOAD_IMG)
                .addFile("upfile", fileName, new File(newPath))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        onUploadListener.onError(e);
                        Log.d("uploadimg", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("success")) {
                                onUploadListener.onSuccess(fileName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onUploadListener.onError(e);
                        }
                        Log.d("uploadimg", response);
                    }
                });
    }

    public interface OnUploadListener{
        void onSuccess(String imgName);
        void onError(Exception e);
    }
}
