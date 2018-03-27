package cn.xcom.banjing.chat;

import com.example.mylibrary.Chat.ChatType;

import java.io.Serializable;

/**
 * Created by mac on 2017/10/3.
 */


public class ChatDataItemBean implements Serializable {
    private ChatType chatType;
    private String myPhoto;
    private String hisPhoto;
    private String PicturePath;
    private String text;
    private String durtation;
    private String hisname;
    public ChatDataItemBean(ChatType chatType, String myPhoto, String hisPhoto, String picturePath, String text, String durtation,String name) {
        this.hisname = name;
        this.chatType = chatType;
        this.myPhoto = myPhoto;
        this.hisPhoto = hisPhoto;
        PicturePath = picturePath;
        this.text = text;
        this.durtation = durtation;
    }

    public String getHisname() {
        return hisname;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public String getMyPhoto() {
        return myPhoto;
    }

    public String getHisPhoto() {
        return hisPhoto;
    }

    public String getPicturePath() {
        return PicturePath;
    }

    public String getText() {
        return text;
    }

    public String getDurtation() {
        return durtation;
    }
}