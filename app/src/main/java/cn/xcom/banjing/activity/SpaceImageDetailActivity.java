//package cn.xcom.banjing.activity;
///*
//  * 加载便民圈的图片，并且点击放大
// */
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.xcom.banjing.R;
//import cn.xcom.banjing.adapter.ViewPageAdapter;
//import cn.xcom.banjing.constant.NetConstant;
//import cn.xcom.banjing.utils.MyImageLoader;
//import cn.xcom.banjing.utils.SmoothImageView;
//
//public class SpaceImageDetailActivity extends BaseActivity {
//    private int mLocationX;
//    private int mLocationY;
//    private int mWidth;
//    private int mHeight;
//    private ImageView imageView1,redstate;
//    private SmoothImageView imageView;
//    private String convenience;
//    private Context context;
//    private List addViewList;//添加图片的list
//    private ViewPager viewPager;
//    private ViewPageAdapter viewPageAdapter;
//    private RelativeLayout rl_back;
//    private LinearLayout ll_down;
//    private TextView down_time,money,title,timetext;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //去除title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //去掉Activity上面的状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        setContentView(R.layout.activity_space_image_detail);
//        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
//        rl_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        //getSupportActionBar().hide();
//        context=this;
//        addViewList=new ArrayList();
////        convenience=new ArrayList<>();
//        Intent intent=getIntent();
//        int ID=getIntent().getIntExtra("position", 0);
//        ll_down = (LinearLayout) findViewById(R.id.ll_down_timer);
//        down_time = (TextView) findViewById(R.id.down_timer);
//        redstate = (ImageView) findViewById(R.id.red_image);
//        timetext = (TextView) findViewById(R.id.time_text);
//        title = (TextView) findViewById(R.id.title);
//        viewPager= (ViewPager) findViewById(R.id.view_pager);
//        convenience= intent.getStringExtra("ImageList");
//        mLocationX = getIntent().getIntExtra("locationX", 0);
//        mLocationY = getIntent().getIntExtra("locationY", 0);
//        mWidth = getIntent().getIntExtra("width", 0);
//        mHeight = getIntent().getIntExtra("height", 0);
//        imageView = new SmoothImageView(this);
//        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
//        imageView.transformIn();
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
////            for (int i=0;i<convenience.size();i++){
//                imageView1=new ImageView(context);
//                imageView.buildDrawingCache();
//                Bitmap bmap = imageView.getDrawingCache();
//                imageView1.setImageBitmap(bmap);
//        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + convenience, imageView1);
//                imageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                addViewList.add(imageView1);
////            }
//        viewPageAdapter=new ViewPageAdapter(addList, addViewList,context);
//        viewPager.setAdapter(viewPageAdapter);
////        viewPager.setCurrentItem(ID);
//        timer.start();
//
//    }
//    private CountDownTimer timer = new CountDownTimer(10000, 1000) {
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            down_time.setText(millisUntilFinished/1000+"''");
//        }
//
//        @Override
//        public void onFinish() {
//            timetext.setVisibility(View.GONE);
//            down_time.setText("+0.1");
//            redstate.setImageResource(R.mipmap.ic_red_open);
//
//        }
//    };
//}
