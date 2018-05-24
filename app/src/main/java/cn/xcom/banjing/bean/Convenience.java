package cn.xcom.banjing.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class Convenience implements Serializable {
    /**
     * mid : 10055
     * type : 12
     * userid : 2987
     * title :
     * content : 爱无价，月月爱负离子卫生巾，一款男人女人都可以用的卫生巾，欢迎咨询
     * video :
     * create_time : 1524840398
     * name : 小甜甜
     * photo : yyy1001516602171080.jpg
     * pictureurl : yyy51671524840397515.jpg
     * likeInfo : {"liked":false,"count":0}
     * redpacket : {"red_packet":0.98,"red_balance":0.41}
     */

    private String mid;
    private String type;
    private String userid;
    private String title;
    private String content;
    private String video;
    private String create_time;
    private String name;
    private String photo;
    private String pictureurl;
    private LikeInfoBean likeInfo;
    private RedpacketBean redpacket;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPictureurl() {
        return pictureurl;
    }

    public void setPictureurl(String pictureurl) {
        this.pictureurl = pictureurl;
    }

    public LikeInfoBean getLikeInfo() {
        return likeInfo;
    }

    public void setLikeInfo(LikeInfoBean likeInfo) {
        this.likeInfo = likeInfo;
    }

    public RedpacketBean getRedpacket() {
        return redpacket;
    }

    public void setRedpacket(RedpacketBean redpacket) {
        this.redpacket = redpacket;
    }

    public static class LikeInfoBean implements Serializable {
        /**
         * liked : false
         * count : 0
         */

        private boolean liked;
        private int count;

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public static class RedpacketBean implements  Serializable {
        /**
         * red_packet : 0.98
         * red_balance : 0.41
         */

        private double red_packet;
        private double red_balance;

        public double getRed_packet() {
            return red_packet;
        }

        public void setRed_packet(double red_packet) {
            this.red_packet = red_packet;
        }

        public double getRed_balance() {
            return red_balance;
        }

        public void setRed_balance(double red_balance) {
            this.red_balance = red_balance;
        }
    }


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
}
