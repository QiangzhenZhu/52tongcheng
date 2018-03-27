package cn.xcom.banjing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class Convenience implements Serializable {

    /**
     * mid : 7
     * type : 1
     * userid : 607
     * name :: 13905930087
     * title  51郑
     * phone : 13905930087
     * content : 郑测试1609192253
     * sound :
     * create_time : 1474296846
     * photo : avatar20160920104210.png
     * pic : [{"pictureurl":"avatar2016:09:19:22:54:06:2540.png"}]
     * like : []
     * comment : []
     * packettime ：
     * redpacket ：
     * balance :
     */

    private String mid;
    private String type;
    private String userid;
    private String name;
    private String phone;
    private String title;
    private String content;
    private String sound;
    private long create_time;
    private String photo;
    private String soundtime;
    private String video;
    private String packettime;
    private String redpacket;
    private String packetstate;
    private String packetId;
    private String packetcount;
    private String touchcount;
    private String balance;
    private String favorites_count;

    public String getFavorites_count() {
        return favorites_count;
    }

    public void setFavorites_count(String favorites_count) {
        this.favorites_count = favorites_count;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPackettime() {
        return packettime;
    }

    public void setPackettime(String packettime) {
        this.packettime = packettime;
    }

    public String getRedpacket() {
        return redpacket;
    }

    public void setRedpacket(String redpacket) {
        this.redpacket = redpacket;
    }

    /**
     * pictureurl : avatar2016:09:19:22:54:06:2540.png
     */

    private List<PicBean> pic;
    private List<?> like;
    private List<CommentInfo> comment;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public List<?> getLike() {
        return like;
    }

    public void setLike(List<?> like) {
        this.like = like;
    }

    public List<CommentInfo> getComment() {
        return comment;
    }

    public void setComment(List<CommentInfo> comment) {
        this.comment = comment;
    }

    public String getSoundtime() {
        return soundtime;
    }

    public void setSoundtime(String soundtime) {
        this.soundtime = soundtime;
    }

    public static class PicBean implements Serializable{
        private String pictureurl;

        public String getPictureurl() {
            return pictureurl;
        }

        public void setPictureurl(String pictureurl) {
            this.pictureurl = pictureurl;
        }
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPacketstate() {
        return packetstate;
    }

    public void setPacketstate(String packetstate) {
        this.packetstate = packetstate;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getPacketcount() {
        return packetcount;
    }

    public void setPacketcount(String packetcount) {
        this.packetcount = packetcount;
    }

    public String getTouchcount() {
        return touchcount;
    }

    public void setTouchcount(String touchcount) {
        this.touchcount = touchcount;
    }
}
