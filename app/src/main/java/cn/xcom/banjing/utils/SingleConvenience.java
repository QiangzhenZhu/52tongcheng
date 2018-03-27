package cn.xcom.banjing.utils;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.banjing.bean.Convenience;

/**
 * Created by 10835 on 2018/1/11.
 */

public class SingleConvenience  {
    private static SingleConvenience singleConvenience =null ;
    private static List<Convenience> addlist =null;

    private SingleConvenience(){
        addlist =new ArrayList<>();
    }

    public static SingleConvenience get(){
        if (addlist == null){
            synchronized (SingleConvenience.class){
                if (addlist == null){
                    singleConvenience =new SingleConvenience();
                }
            }
        }
        return singleConvenience;
    }

    public static List<Convenience> getAddlist(){
        if (addlist != null) {
            return addlist;
        }
        return null;
    }
}
