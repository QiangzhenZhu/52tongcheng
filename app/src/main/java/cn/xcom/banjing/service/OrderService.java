package cn.xcom.banjing.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class OrderService extends Service {

    public OrderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timer timer = new Timer();
        timer.schedule(new Work(),0, 1000);
    }



    class Work extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            message.what=1;
            handler.sendMessage(message);
        }
    }

    Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what==1)
            {
//                final UserInfo userInfo;
//                userInfo = new UserInfo();
//                userInfo.readData(getApplicationContext());
//                RequestParams requestParams = new RequestParams();
//                requestParams.put("userid", userInfo.getUserId());
//
//                HelperAsyncHttpClient.get(NetConstant.GET_MY_NOAPPLY_HIRE_TAST_COUNT, requestParams,
//                        new JsonHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                super.onSuccess(statusCode, headers, response);
//                                if (response != null) {
//                                    Log.d("===response", response.toString() + userInfo.getUserId());
//                                    try {
//                                        String state = response.getString("status");
//                                        String data = response.getString("data");
//                                        if (state.equals("success") && (!data.equals("0"))) {
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                                    builder.setMessage("您有雇佣订单未处理，请前往处理");
//                                    builder.setTitle("提示");
//                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            getApplicationContext().startActivity(new Intent(getApplicationContext(), OrderTakingActivity.class));
//                                            Timer timer = new Timer();
//                                            timer.schedule(new Work(),0, 1000*60*5);
//                                            dialog.dismiss();
//
//                                        }
//                                    });
////
//                                    builder.show();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//
//                            }
//                        });
                Log.d("===response", "eee");
            }
        }

    };
}
