package cn.xcom.banjing.bean;

import java.util.List;

/**
 * Created by 10835 on 2018/4/19.
 */

public class test {

    /**
     * status : success
     * data : [{"mid":"8432","userid":"2948","title":"","content":"美及了","video":"yyy97811516438144927.mp4","create_time":"1516438145","name":"幸福","photo":"","pictureurl":"yyy26831516438144875.jpg","like_count":"0","packet_id":"883","packettime":9,"redpacket":"0.0098","balance":"0","money_info":[],"can_touch_packet":{"status":false,"reason":"红包已抢完"}},{"mid":"8924","userid":"3002","title":"","content":"全网最全颜色！L*V围巾/披肩款（实物手感顺滑舒服）绝非市场流通品。Monogram 披肩已是经久不衰、优雅大方的衣橱必备单品，无论哪个场合都可以配带出来，让你倍儿有面子！此款以柔软轻盈的羊绒和羊毛混织而成。精密的编织经典Monogram图案和路易威登标志！材质 百分百羊绒 尺寸：45*200cm","video":"","create_time":"1516780592","name":"兰若","photo":"yyy61281516805764416.jpg","pictureurl":"yyy14861516780587264.jpg","like_count":"1","packet_id":"1353","packettime":9,"redpacket":"0.25","balance":"0","money_info":[],"can_touch_packet":{"status":false,"reason":"红包已抢完"}},{"mid":"8912","userid":"2980","title":"","content":"🌀24X816-----55💰包邮     35-40 (标准码)\n💏 潮流休闲舒适健稳马丁靴 (单里)","video":"","create_time":"1516773161","name":"张","photo":"yyy86601516439497638.jpg","pictureurl":"yyy22201516773160913.jpg","like_count":"0","packet_id":"1341","packettime":9,"redpacket":"0.25","balance":"0","money_info":[],"can_touch_packet":{"status":false,"reason":"红包已抢完"}},{"mid":"8910","userid":"2980","title":"","content":"老顾客再回购一下拿了三双童鞋，还是位人民教师呦[鼓掌]","video":"","create_time":"1516772545","name":"张","photo":"yyy86601516439497638.jpg","pictureurl":"yyy621516772545179.jpg","like_count":"1","packet_id":"1339","packettime":9,"redpacket":"0.25","balance":"0","money_info":[],"can_touch_packet":{"status":false,"reason":"红包已抢完"}},{"mid":"8909","userid":"2980","title":"","content":"小黑裙洗护五件套装，[玫瑰][玫瑰][玫瑰]一洗1\u20e3 一护2\u20e3  还有3\u20e3 沐浴露外加浴花4\u20e3 一个。！另外还送5\u20e3 按摩洗脚盆。只要6\u20e3 8\u20e3 元超值又实惠。数量有限！","video":"","create_time":"1516771961","name":"张","photo":"yyy86601516439497638.jpg","pictureurl":"yyy29761516771959157.jpg","like_count":"1","packet_id":"1338","packettime":9,"redpacket":"0.25","balance":"0","money_info":[],"can_touch_packet":{"status":false,"reason":"红包已抢完"}},{"mid":"8892","userid":"2994","title":"","content":"孩子们背的多认真","video":"yyy4971516769022840.mp4","create_time":"1516769025","name":"绿","photo":"yyy88211516793214101.jpg","pictureurl":"yyy78161516769022454.jpg","like_count":"0","packet_id":"1322","packettime":9,"redpacket":"0.25","balance":"0","money_info":[],"can_touch_packet":{"status":false,"reason":"红包已抢完"}},{"mid":"8845","userid":"2988","title":"","content":"悦龙休闲商务酒店！客房特价198元含双早 免洗浴送足疗！自助餐39元！洗浴门票18元！洗浴套票300元10张！欢迎各界人士光临！","video":"","create_time":"1516758936","name":"52半径用户25639","photo":"","pictureurl":"yyy62481516758887847.jpg","like_count":"1","packet_id":"1275","packettime":9,"redpacket":"0.25","balance":"0","money_info":[],"can_touch_packet":{"status":false,"reason":"红包已抢完"}},{"mid":"8780","userid":"2980","title":"","content":"🌀23F18-----53💰包邮     35-39 (标准码)\n💏 经典时尚纯色个性休闲马丁靴 ( 二棉） 💓","video":"","create_time":"1516684178","name":"张","photo":"yyy86601516439497638.jpg","pictureurl":"yyy40391516684176120.jpg","like_count":"0","packet_id":"1210","packettime":9,"redpacket":"0.49","balance":"0","money_info":[],"can_touch_packet":{"status":false,"reason":"红包已抢完"}},{"mid":"8253","userid":"2904","title":"","content":"测试16","video":"","create_time":"1516258359","name":"面具人生","photo":"yyy33821506610898608.jpg","pictureurl":"yyy30351516258360186.jpg","like_count":"2","packet_id":"","redpacket":"0"},{"mid":"8961","userid":"2994","title":"","content":"有需要的来","video":"yyy98951516801824962.mp4","create_time":"1516801829","name":"绿","photo":"yyy88211516793214101.jpg","pictureurl":"yyy3911516801824863.jpg","like_count":"0","packet_id":"1388","packettime":9,"redpacket":"0.5","balance":"0.03","money_info":[],"can_touch_packet":{"status":true,"reason":"可以抢红包"}}]
     */

    private String status;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * mid : 8432
         * userid : 2948
         * title :
         * content : 美及了
         * video : yyy97811516438144927.mp4
         * create_time : 1516438145
         * name : 幸福
         * photo :
         * pictureurl : yyy26831516438144875.jpg
         * like_count : 0
         * packet_id : 883
         * packettime : 9
         * redpacket : 0.0098
         * balance : 0
         * money_info : []
         * can_touch_packet : {"status":false,"reason":"红包已抢完"}
         */

        private String mid;
        private String userid;
        private String title;
        private String content;
        private String video;
        private String create_time;
        private String name;
        private String photo;
        private String pictureurl;
        private String like_count;
        private String packet_id;
        private int packettime;
        private String redpacket;
        private String balance;
        private CanTouchPacketBean can_touch_packet;
        private List<?> money_info;

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
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

        public String getLike_count() {
            return like_count;
        }

        public void setLike_count(String like_count) {
            this.like_count = like_count;
        }

        public String getPacket_id() {
            return packet_id;
        }

        public void setPacket_id(String packet_id) {
            this.packet_id = packet_id;
        }

        public int getPackettime() {
            return packettime;
        }

        public void setPackettime(int packettime) {
            this.packettime = packettime;
        }

        public String getRedpacket() {
            return redpacket;
        }

        public void setRedpacket(String redpacket) {
            this.redpacket = redpacket;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public CanTouchPacketBean getCan_touch_packet() {
            return can_touch_packet;
        }

        public void setCan_touch_packet(CanTouchPacketBean can_touch_packet) {
            this.can_touch_packet = can_touch_packet;
        }

        public List<?> getMoney_info() {
            return money_info;
        }

        public void setMoney_info(List<?> money_info) {
            this.money_info = money_info;
        }

        public static class CanTouchPacketBean {
            /**
             * status : false
             * reason : 红包已抢完
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
    }
}
