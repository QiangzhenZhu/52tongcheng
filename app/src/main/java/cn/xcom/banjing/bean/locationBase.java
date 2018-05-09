package cn.xcom.banjing.bean;

import java.util.List;

/**
 * Created by 10835 on 2018/4/13.
 */

public class locationBase {

    /**
     * cityid : 1
     * name : 北京市
     * status : 1
     * latitude : 116.402343
     * longitude : 39.914555
     * haschild : 1
     * childlist : [{"cityid":"42","name":"朝阳区","status":"1","latitude":"116.443499","longitude":"39.925898","haschild":0}]
     */

    private String cityid;
    private String name;
    private String status;
    private String latitude;
    private String longitude;
    private int haschild;
    private List<ChildlistBean> childlist;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getHaschild() {
        return haschild;
    }

    public void setHaschild(int haschild) {
        this.haschild = haschild;
    }

    public List<ChildlistBean> getChildlist() {
        return childlist;
    }

    public void setChildlist(List<ChildlistBean> childlist) {
        this.childlist = childlist;
    }

    public static class ChildlistBean {
        /**
         * cityid : 42
         * name : 朝阳区
         * status : 1
         * latitude : 116.443499
         * longitude : 39.925898
         * haschild : 0
         */

        private String cityid;
        private String name;
        private String status;
        private String latitude;
        private String longitude;
        private int haschild;

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public int getHaschild() {
            return haschild;
        }

        public void setHaschild(int haschild) {
            this.haschild = haschild;
        }
    }
}
