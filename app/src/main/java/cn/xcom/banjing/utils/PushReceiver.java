package cn.xcom.banjing.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.ui.PushActivity;
import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.activity.BillActivity;
import cn.xcom.banjing.activity.ChatActivity;
import cn.xcom.banjing.activity.HomeActivity;
import cn.xcom.banjing.activity.LoginActivity;
import cn.xcom.banjing.activity.MyOrderActivity;
import cn.xcom.banjing.activity.OrderTakingActivity;
import cn.xcom.banjing.activity.WalletActivity;
import cn.xcom.banjing.bean.OrderHelper;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.chat.ExampleUtil;
import cn.xcom.banjing.constant.HelperConstant;
import cn.xcom.banjing.fragment.AdFragment;
import cn.xcom.banjing.fragment.FriendFragment;
import uk.co.senab.photoview.log.LoggerDefault;

/**
 * Created by Administrator on 2016/9/8.
 */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static final String JPUSHMESSAGE = "JPUSHMESSAGE";
    private static final String JPUSHHOMEWORK = "JPUSHHOMEWORK";
    private static final String JPUSHTRUST = "JPUSHTRUST";
    private static final String JPUSHNOTICE = "JPUSHNOTICE";
    private static final String JPUSHDAIJIE = "JPUSHDAIJIE";
    private static final String JPUSHLEAVE = "JPUSHLEAVE";
    private static final String JPUSHACTIVITY = "JPUSHACTIVITY";
    private static final String JPUSHCOMMENT = "JPUSHCOMMENT";
    private MediaPlayer mediaPlayer;
    private boolean flag = true; //播放音乐标记

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String type = bundle.getString(JPushInterface.EXTRA_EXTRA);

        if (type == null) return;
        String key = "";
        String v = "";
        try {
            JSONObject jsonObject = new JSONObject(type);
            key = jsonObject.getString("key");
            v = jsonObject.getString("v");
            Log.d(TAG, "key and v "+key +"\n"+v);
        } catch (JSONException e) {
            Log.i(TAG, "JSONException" + type);
            e.printStackTrace();
        }
        Log.i(TAG, "[PushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.i(TAG, "[PushReceiver] 接收Registeration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: "+ JPushInterface.ACTION_CONNECTION_CHANGE);
            Log.i(TAG, "[PushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context,bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[PushReceiver] 接收到推送下来的通知");
            Log.d(TAG, "onReceive: "+JPushInterface.ACTION_NOTIFICATION_RECEIVED);
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.i(TAG, "[PushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            Log.d(TAG, "key and v 2  "+key+"\n"+v);
            switch (key) {
                case "certificationType":
                    if (v.equals("1")) {
                        SPUtils.put(context, HelperConstant.IS_HAD_AUTHENTICATION, "1");
                    }
                    break;
                case "loginFromOther":
                    String title = "您的账号在异地登录";
                    String message = "您需要重新登陆";
                    popLogOutDialog(title, message);
                    break;

                case "prohibitVisit":
                    title = "封号";
                    message = "您的账号因为言论已被封停";
                    popLogOutDialog(title, message);
                    break;

                case "deleteUser":
                    title = "删除";
                    message = "您的账号被系统删除，如有疑问请拨打4000608856!";
                    popLogOutDialog(title, message);
                    break;

                case "acceptTaskType":
                    if(v.equals("-1")){
                        playNotificationSound(context,"task_cancel");
                    }
                    break;
                case "sendTaskType":
                    if(v.equals("1")){
                        playNotificationSound(context,"task_taked");
                    }
                    break;
                case "businessOrderType":
                    if(v.equals("1")){
                        playNotificationSound(context,"buy_goods");
                    }
                    break;
                case "TaskTimeOut":
                    playNotificationSound(context,"timeout");
                    break;
                case "system":
                    playNotificationSound(context,"system");
                    break;
                case "newTask":
                    playNotificationSound(context,"task_new");
                    break;
                case "hireTaskType":
                    playNotificationSound(context,"hire_task");
                    break;
                case "bbspushwithcityname":
                    break;
                case "newMessage":
//                    if (ChatActivity.isForeground) {
//                        String chatmessage = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//                        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//                        Intent msgIntent = new Intent(ChatActivity.MESSAGE_RECEIVED_ACTION);
//                        msgIntent.putExtra(ChatActivity.KEY_MESSAGE, chatmessage);
//                        if (!ExampleUtil.isEmpty(extras)) {
//                            try {
//                                JSONObject extraJson = new JSONObject(extras);
//                                if (null != extraJson && extraJson.length() > 0) {
//                                    msgIntent.putExtra(ChatActivity.KEY_EXTRAS, extras);
//                                }
//                            } catch (JSONException e) {
//
//                            }
//
//                        }
//                        context.sendBroadcast(msgIntent);
//                    }else{
//
//                    }
//                    Log.i(TAG, "[newMessage] 收到新的消息::"+v);
//                    intent = new Intent(context,ChatActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("id", v);
//                    context.sendBroadcast(intent);
                    break;
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i(TAG, "[PushReceiver] 用户点击打开了通知");
            openNotification(context, key, v);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.i(TAG, "[PushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.i(TAG, "[PushReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.i(TAG, "[PushReceiver] Unhandled intent - " + intent.getAction());
        }
    }


    private void openNotification(Context context, String key, String value) {
        Intent intent;
        switch (key) {
            case "newTask":
                intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from", "push");
                context.startActivity(intent);
                break;
            case "newMessage":
                Log.d(TAG, "openNotification: 接收到新消息");
                intent = new Intent(context, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value);
                context.startActivity(intent);
                break;
            case "sendTaskType":
                intent = new Intent(context, BillActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case "acceptTaskType":
                intent = new Intent(context, OrderTakingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case "myWallet":
                intent = new Intent(context, WalletActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case "buyOrderType":
                intent = new Intent(context, MyOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("order_type", OrderHelper.BuyerOrder);
                context.startActivity(intent);
                break;
            case "businessOrderType":
                intent = new Intent(context, MyOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("order_type", OrderHelper.SellerOrder);
                context.startActivity(intent);
                break;
            case "loginFromOther":
                UserInfo userInfo = new UserInfo();
                userInfo.clearDataExceptPhone(context);
                SPUtils.clear(context);
                JPushInterface.stopPush(context);
                context.startActivity(new Intent(context, LoginActivity.class));
                HelperApplication.getInstance().onTerminate();
                break;
            case "certificationType":
                intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from", "push");
                context.startActivity(intent);
                break;
            case "prohibitVisit":
                UserInfo userInfo1 = new UserInfo();
                userInfo1.clearDataExceptPhone(context);
                SPUtils.clear(context);
                JPushInterface.stopPush(context);
                context.startActivity(new Intent(context, LoginActivity.class));
                HelperApplication.getInstance().onTerminate();
                break;
            case "hireTaskType":
                intent = new Intent(context, OrderTakingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from", "push");
                context.startActivity(intent);
                break;
            case "bbspushwithcityname":
                Log.d("hello", "抢了红包: ");
                intent = HomeActivity.newIntent(context,2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case "TaskTimeOut":
                Log.d(TAG, "TaskTimeOut ");
                intent = new Intent(context,BillActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;

        }


    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


    /**
     * 接受推送弹出提示框
     *
     * @param title
     * @param message
     */
    private void popLogOutDialog(String title, String message) {
        List<Activity> activities = HelperApplication.getInstance().getActivities();
        if (activities.size() == 0) {
            return;
        }
        final Activity activity = activities.get(activities.size() - 1);
        if (activity == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserInfo userInfo = new UserInfo();
                userInfo.clearDataExceptPhone(activity);
                SPUtils.clear(activity);
                JPushInterface.stopPush(activity);
                activity.startActivity(new Intent(activity, LoginActivity.class));
                HelperApplication.getInstance().onTerminate();
            }
        });
        builder.show();

    }
    private void processCustomMessage(Context context, Bundle bundle) {
        if (FriendFragment.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(FriendFragment.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(FriendFragment.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        Log.i("kan","有extras");
                        msgIntent.putExtra(FriendFragment.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
            NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manger.cancel(0);
        }else{

        }
    }

//
//    private void processCustomMessage(Context context, Bundle bundle,String key) {
//        boolean done=false;
//        switch (key){
//            case "newMessage":
//                if (ChatActivity.isForeground) {
//                    String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//                    String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//                    Intent msgIntent = new Intent(ChatActivity.MESSAGE_RECEIVED_ACTION);
//                    msgIntent.putExtra(ChatActivity.KEY_MESSAGE, message);
//                    if (!ExampleUtil.isEmpty(extras)) {
//                        try {
//                            JSONObject extraJson = new JSONObject(extras);
//                            if (null != extraJson && extraJson.length() > 0) {
//                                msgIntent.putExtra(ChatActivity.KEY_EXTRAS, extras);
//                            }
//                        } catch (JSONException e) {
//
//                        }
//
//                    }
//
//                    context.sendBroadcast(msgIntent);
//                    done=true;
//                }else{
//
//                }
//                break;
//        }
//        if(!done) {
//            NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            //为了版本兼容  选择V7包下的NotificationCompat进行构造
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//            //Ticker是状态栏显示的提示
//            builder.setTicker(bundle.getString(JPushInterface.EXTRA_TITLE));
//            //第一行内容  通常作为通知栏标题
//            builder.setContentTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
//            //第二行内容 通常是通知正文
//            builder.setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            //可以点击通知栏的删除按钮删除
//            builder.setAutoCancel(true);
//            //系统状态栏显示的小图标
//            builder.setSmallIcon(R.mipmap.ic_logo);
//            Notification notification = builder.build();
////        notification.sound = Uri.parse("android.resource://"
////                + context.getPackageName() + "/" + R.raw.timeout);
//            builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS);
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//            Intent clickIntent = new Intent(); //点击通知之后要发送的广播
//            int id = (int) (System.currentTimeMillis() / 1000);
//            clickIntent.addCategory(context.getPackageName());
//            clickIntent.setAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
//            clickIntent.putExtra(JPushInterface.EXTRA_EXTRA, bundle.getString(JPushInterface.EXTRA_EXTRA));
//            PendingIntent contentIntent = PendingIntent.getBroadcast(context, id, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            notification.contentIntent = contentIntent;
//            manger.notify(id, notification);
//        }
//    }


    private void playNotificationSound(Context context,String type){
        if (flag) {
            flag = false;
            mediaPlayer = new MediaPlayer();
//                if (mediaPlayer != null /*&& !mediaPlayer.isPlaying()*/) {
//                }
            playMusic(context,type);

        }
    }


    /**
     * 播放提示音
     * @param context
     */
    protected void playMusic(Context context,String type) {
        if(mediaPlayer == null){
            return;
        }
        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor fileDescriptor = null;
        try {
            switch (type){
                case "timeout":
                    fileDescriptor = assetManager.openFd("timeout.mp3");
                    break;
                case "task_cancel":
                    fileDescriptor = assetManager.openFd("task_cancel.mp3");
                    break;
                case "task_taked":
                    fileDescriptor = assetManager.openFd("task_taked.mp3");
                    break;
                case "buy_goods":
                    fileDescriptor = assetManager.openFd("buy_goods.mp3");
                    break;
                case "system":
                    fileDescriptor = assetManager.openFd("system_new.mp3");
                    break;
                case "task_new":
                    fileDescriptor = assetManager.openFd("task_new.mp3");
                break;
                case "hire_task":
                    fileDescriptor = assetManager.openFd("task_new.mp3");
                    break;
            }

            mediaPlayer
                    .setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer
                    .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//播出完毕事件
                        @Override
                        public void onCompletion(MediaPlayer arg0) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            flag = true;
                        }
                    });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
