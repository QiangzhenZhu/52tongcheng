package cn.xcom.banjing.bean;

import org.w3c.dom.ProcessingInstruction;

import java.io.Serializable;

/**
 * Created by 10835 on 2017/12/12.
 */

public class CommentInfo  implements Serializable{
    private String name;
    private String add_time;
    private String content;
    private String userid;
    private String photo;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
