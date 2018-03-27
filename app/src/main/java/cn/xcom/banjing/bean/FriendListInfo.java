package cn.xcom.banjing.bean;

/**
 * Created by mac on 2017/9/27.
 * 好友列表信息
 */

public class FriendListInfo {
    private String  id;
    private String friend_userid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriend_userid() {
        return friend_userid;
    }

    public void setFriend_userid(String friend_userid) {
        this.friend_userid = friend_userid;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }



    public String getFriend_photo() {
        return friend_photo;
    }

    public void setFriend_photo(String friend_photo) {
        this.friend_photo = friend_photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String friend_name;
    private String friend_photo;
    private String status;

}
