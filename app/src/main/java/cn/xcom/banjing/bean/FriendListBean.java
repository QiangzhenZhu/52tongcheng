package cn.xcom.banjing.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by mac on 2017/9/25.
 */

public class FriendListBean implements Comparable<FriendListBean>, Serializable {

    /**
     * id : 1
     * name : 糖友1
     * photo : yyy1497487820172.jpg
     * birthday : 0
     * time : 1495421454
     * morning_glucose :
     */

    private String id;
    private String name;
    private String photo;

    public FriendListBean(String id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }





    @Override
    public int compareTo(@NonNull FriendListBean o) {
//        char[] chars = getPinyin().toCharArray();
//        char[] anotherChars = o.getPinyin().toCharArray();
//
//        int length = chars.length > anotherChars.length ? anotherChars.length:chars.length;
//
//        for(int i = 0; i < length; i ++){
//            if(chars[i] < anotherChars[i]){
//                return -1;
//            }else if(chars[i] > anotherChars[i]){
//                return 1;
//            }
//        }
        return 0;
    }
}

