package cn.xcom.banjing.bean;

import java.util.List;

/**
 * Created by 10835 on 2018/3/22.
 */

public class AdCollection {

    /**
     * mid : 9839
     * type : 48
     * userid : 2920
     * title :
     * content : ces
     * sound :
     * soundtime :
     * video :
     * create_time : 1521681182
     * name : 我是朱振强
     * phone : 17853593228
     * photo : yyy12361516936803668.jpg
     * pic : [{"pictureurl":"yyy30511521681176450.jpg"}]
     * like : [{"userid":"2920","name":"我是朱振强"}]
     * favorites_count : 1
     * comment : []
     * packetstate : 0
     * packetId :
     * redpacket : 0
     */

    private String mid;
    private String type;
    private String userid;
    private String title;
    private String content;
    private String sound;
    private String soundtime;
    private String video;
    private String create_time;
    private String name;
    private String phone;
    private String photo;
    private String favorites_count;
    private String packetstate;
    private String packetId;
    private String redpacket;
    private List<PicBean> pic;
    private List<LikeBean> like;
    private List<?> comment;

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

    public String getSoundtime() {
        return soundtime;
    }

    public void setSoundtime(String soundtime) {
        this.soundtime = soundtime;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFavorites_count() {
        return favorites_count;
    }

    public void setFavorites_count(String favorites_count) {
        this.favorites_count = favorites_count;
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

    public String getRedpacket() {
        return redpacket;
    }

    public void setRedpacket(String redpacket) {
        this.redpacket = redpacket;
    }

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public List<LikeBean> getLike() {
        return like;
    }

    public void setLike(List<LikeBean> like) {
        this.like = like;
    }

    public List<?> getComment() {
        return comment;
    }

    public void setComment(List<?> comment) {
        this.comment = comment;
    }

    public static class PicBean {
        /**
         * pictureurl : yyy30511521681176450.jpg
         */

        private String pictureurl;

        public String getPictureurl() {
            return pictureurl;
        }

        public void setPictureurl(String pictureurl) {
            this.pictureurl = pictureurl;
        }
    }

    public static class LikeBean {
        /**
         * userid : 2920
         * name : 我是朱振强
         */

        private String userid;
        private String name;

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
    }
}
