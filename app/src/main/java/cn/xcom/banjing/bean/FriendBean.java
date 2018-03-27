package cn.xcom.banjing.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by mac on 2017/9/25.
 */

public class FriendBean implements Serializable {

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
    private String status;
    private String selectStatus;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSelectStatus() {
        return selectStatus;
    }

    public void setSelectStatus(String selectStatus) {
        this.selectStatus = selectStatus;
    }

    @Override
    public String toString() {
        return "FriendBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", status='" + status + '\'' +
                ", selectStatus='" + selectStatus + '\'' +
                '}';
    }
}

