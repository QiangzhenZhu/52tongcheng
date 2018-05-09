package cn.xcom.banjing.bean;

import java.util.List;

/**
 * Created by 10835 on 2018/3/21.
 */

public class ScoreList {

    /**
     * status : success
     * data : {"score_list":[{"id":"4","userid":"2899","keyword":"service_order_buy","score":"50","type":"1","title":"服务下单","icon_url":"uploads/system_icon/ic_weixin.png","description":"服务下单","create_time":"1521613970"},{"id":"3","userid":"2899","keyword":"goods_share","score":"80","type":"1","title":"分享商品","icon_url":"uploads/system_icon/ic_weixin.png","description":"分享商品","create_time":"1521613953"},{"id":"2","userid":"2899","keyword":"goods_share","score":"80","type":"1","title":"分享商品","icon_url":"uploads/system_icon/ic_weixin.png","description":"分享商品","create_time":"1521613905"},{"id":"1","userid":"2899","keyword":"goods_share","score":"80","type":"1","title":"分享商品","icon_url":"uploads/system_icon/ic_weixin.png","description":"分享商品","create_time":"1521613587"}],"overview":{"today_score":290,"today_task":4,"total_score":290}}
     */

    private String status;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * score_list : [{"id":"4","userid":"2899","keyword":"service_order_buy","score":"50","type":"1","title":"服务下单","icon_url":"uploads/system_icon/ic_weixin.png","description":"服务下单","create_time":"1521613970"},{"id":"3","userid":"2899","keyword":"goods_share","score":"80","type":"1","title":"分享商品","icon_url":"uploads/system_icon/ic_weixin.png","description":"分享商品","create_time":"1521613953"},{"id":"2","userid":"2899","keyword":"goods_share","score":"80","type":"1","title":"分享商品","icon_url":"uploads/system_icon/ic_weixin.png","description":"分享商品","create_time":"1521613905"},{"id":"1","userid":"2899","keyword":"goods_share","score":"80","type":"1","title":"分享商品","icon_url":"uploads/system_icon/ic_weixin.png","description":"分享商品","create_time":"1521613587"}]
         * overview : {"today_score":290,"today_task":4,"total_score":290}
         */

        private OverviewBean overview;
        private List<ScoreListBean> score_list;

        public OverviewBean getOverview() {
            return overview;
        }

        public void setOverview(OverviewBean overview) {
            this.overview = overview;
        }

        public List<ScoreListBean> getScore_list() {
            return score_list;
        }

        public void setScore_list(List<ScoreListBean> score_list) {
            this.score_list = score_list;
        }

        public static class OverviewBean {
            /**
             * today_score : 290
             * today_task : 4
             * total_score : 290
             */

            private float today_score;
            private int today_task;
            private float total_score;

            public float getToday_score() {
                return today_score;
            }

            public void setToday_score(int today_score) {
                this.today_score = today_score;
            }

            public int getToday_task() {
                return today_task;
            }

            public void setToday_task(int today_task) {
                this.today_task = today_task;
            }

            public float getTotal_score() {
                return total_score;
            }

            public void setTotal_score(int total_score) {
                this.total_score = total_score;
            }
        }

        public static class ScoreListBean {
            /**
             * id : 4
             * userid : 2899
             * keyword : service_order_buy
             * score : 50
             * type : 1
             * title : 服务下单
             * icon_url : uploads/system_icon/ic_weixin.png
             * description : 服务下单
             * create_time : 1521613970
             */

            private String id;
            private String userid;
            private String keyword;
            private String score;
            private String type;
            private String title;
            private String icon_url;
            private String description;
            private String create_time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIcon_url() {
                return icon_url;
            }

            public void setIcon_url(String icon_url) {
                this.icon_url = icon_url;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }
        }
    }
}
