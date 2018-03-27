package cn.xcom.banjing.constant;

/**
 * Created by zhuchongkun on 16/5/27.
 */
public interface HelperConstant {
    /**
     *  是否打印log
     *  true 打印
     */
    boolean isLog=false;
    /**
     *
     */
    String DEVICE_STATE="2";
    String SHOW = "http://banjing.xiaocool.net/";
    /**
     * 请求地址
     */
    String NET_HOST="http://banjing.xiaocool.net";
    /**
     * 接口前缀
     */
    String NET_HOST_PREFIX = "http://banjing.xiaocool.net/index.php?g=apps&m=index&";


    /**
     * 是否实名认证（1是,2否）
     */
    String IS_HAD_AUTHENTICATION = "IS_HAD_AUTHENTICATION";

    /**
     * 是否保险认证（1-已认证， －1认证中， 0 未认证或认证失败）
     */
    String IS_INSURANCE = "IS_INSURANCE";

    /**
     * 邀请码
     */
    String MY_REFERAL = "MY_REFERAL";

}
