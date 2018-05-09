package cn.xcom.banjing.bean;

/**
 * Created by 10835 on 2018/4/19.
 */

public class AdSetting {

    /**
     * packet_setting : {"count":2,"money":1}
     * can_publish : {"status":true,"reason":"可以发布广告"}
     */

    private PacketSettingBean packet_setting;
    private CanPublishBean can_publish;

    public PacketSettingBean getPacket_setting() {
        return packet_setting;
    }

    public void setPacket_setting(PacketSettingBean packet_setting) {
        this.packet_setting = packet_setting;
    }

    public CanPublishBean getCan_publish() {
        return can_publish;
    }

    public void setCan_publish(CanPublishBean can_publish) {
        this.can_publish = can_publish;
    }

    public static class PacketSettingBean {
        /**
         * count : 2
         * money : 1
         */

        private int count;
        private int money;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }
    }

    public static class CanPublishBean {
        /**
         * status : true
         * reason : 可以发布广告
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
