package com.example.mylibrary.RecordAudioView;

import android.view.View;

/**
 * Created by 赵自强 on 2017/8/26 0:10.
 * 这个类的用处
 */

public interface RecordInterfaceListener {
    void  OnStartRecord(View ClickView);
    void  OnDeleteRecordSeleted();
    void  OnPlayRecordSeleted(int Duration, String filePath);
    void  OnRecordFinish(int Duration,String filepath,String RecordName);
}
