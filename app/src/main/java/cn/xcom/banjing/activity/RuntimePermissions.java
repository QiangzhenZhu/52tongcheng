package cn.xcom.banjing.activity;

/**
 * Created by mac on 2017/10/3.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register an <code>Activity</code> or <code>Fragment</code> to handle permissions.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RuntimePermissions {
}