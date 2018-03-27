package com.example.mylibrary.photoListView;

import java.util.List;

/**
 * Created by 赵自强 on 2017/8/26 17:09.
 * 这个类的用处
 */

public interface QPhotoListener {
    void onAlbumClicked();
    void OnSendBtnClicked(int picCount, List<PictureDataBean> selectedPicList);
    void OnCropClicked(String picPath);
}
