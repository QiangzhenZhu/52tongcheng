package cn.xcom.banjing.bean;

import java.io.Serializable;
import java.util.List;

public class ConvenienceAd {


    /**
     * mid : 9241
     * type : 12
     * userid : 2999
     * title :
     * content : 💁🏻【一致草本国皂】 经常看手机、电脑，脸上爱出油 长痘的你‼️一定要做好深层清洁工作‼️ 国皂含有丰富的雪莲、水解蚕丝，深层清洁❗祛痘去黑头❗美白不紧绷❗❗一定是你的首选‼️
     * video : yyy65641516970183329.mp4
     * create_time : 1516970183
     * name : 皮囊
     * photo : yyy65131516971799648.jpg
     * pictureurl : yyy85851516970183298.jpg
     * likeInfo : {"liked":true,"count":1}
     * collectionInfo : {"collected":true,"count":1}
     * pictureList : ["yyy85851516970183298.jpg"]
     * comment : [{"userid":"2920","name":"我是朱振强","photo":"avatar20180324150626.png","content":"测试","add_time":"1525863659"},{"userid":"2920","name":"我是朱振强","photo":"avatar20180324150626.png","content":"看起来不错","add_time":"1525863673"}]
     * redpacket : {"packettime":15,"packet_id":"1660","red_packet":0.98,"red_balance":0.66,"money_info":[{"money":"0.01","create_time":"1525863650"}],"can_touch_packet":{"status":false,"reason":"您已抢过红包"}}
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
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private LikeInfoBean likeInfo;
    private CollectionInfoBean collectionInfo;
    private RedpacketBean redpacket;
    private List<String> pictureList;
    private List<CommentBean> comment;

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

    public CollectionInfoBean getCollectionInfo() {
        return collectionInfo;
    }

    public void setCollectionInfo(CollectionInfoBean collectionInfo) {
        this.collectionInfo = collectionInfo;
    }

    public RedpacketBean getRedpacket() {
        return redpacket;
    }

    public void setRedpacket(RedpacketBean redpacket) {
        this.redpacket = redpacket;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    public List<CommentBean> getComment() {
        return comment;
    }

    public void setComment(List<CommentBean> comment) {
        this.comment = comment;
    }

    public static class LikeInfoBean implements Serializable{
        /**
         * liked : true
         * count : 1
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

    public static class CollectionInfoBean implements Serializable {
        /**
         * collected : true
         * count : 1
         */

        private boolean collected;
        private int count;

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public static class RedpacketBean  implements  Serializable{
        /**
         * packettime : 15
         * packet_id : 1660
         * red_packet : 0.98
         * red_balance : 0.66
         * money_info : [{"money":"0.01","create_time":"1525863650"}]
         * can_touch_packet : {"status":false,"reason":"您已抢过红包"}
         */

        private int packettime;
        private String packet_id;
        private double red_packet;
        private double red_balance;
        private CanTouchPacketBean can_touch_packet;
        private List<MoneyInfoBean> money_info;

        public int getPackettime() {
            return packettime;
        }

        public void setPackettime(int packettime) {
            this.packettime = packettime;
        }

        public String getPacket_id() {
            return packet_id;
        }

        public void setPacket_id(String packet_id) {
            this.packet_id = packet_id;
        }

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

        public CanTouchPacketBean getCan_touch_packet() {
            return can_touch_packet;
        }

        public void setCan_touch_packet(CanTouchPacketBean can_touch_packet) {
            this.can_touch_packet = can_touch_packet;
        }

        public List<MoneyInfoBean> getMoney_info() {
            return money_info;
        }

        public void setMoney_info(List<MoneyInfoBean> money_info) {
            this.money_info = money_info;
        }

        public static class CanTouchPacketBean implements Serializable {
            /**
             * status : false
             * reason : 您已抢过红包
             */

            private boolean status;
            private String reason;

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }
        }

        public static class MoneyInfoBean implements Serializable {
            /**
             * money : 0.01
             * create_time : 1525863650
             */

            private String money;
            private String create_time;

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }
        }
    }

    public static class CommentBean implements Serializable {
        /**
         * userid : 2920
         * name : 我是朱振强
         * photo : avatar20180324150626.png
         * content : 测试
         * add_time : 1525863659
         */

        private String userid;
        private String name;
        private String photo;
        private String content;
        private String add_time;

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

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
