package cn.xcom.banjing.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.xcom.banjing.R;
import cn.xcom.banjing.temp.ImageCycleView;

public class ImageCycleActivity extends Activity implements
		OnClickListener {
	private RelativeLayout rl_back;
	// private ArrayList<String> mImgsList;
	private ArrayList<String> bImgsList;
	private Context mContext;
	private Bitmap mBitmap;
	private String path;
	private ImageLoader bImageLoader = ImageLoader.getInstance();
	// private DisplayImageOptions mOptions;
	private DisplayImageOptions bOptions;
	// private ImageCycleView mAdView;
	private ImageCycleView bAdView;
	private ProgressDialog proDialog;
	private ProgressBar loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_social_image_cycle);
		mContext = this;
		mContext = this;
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 控件初始化
	 */
	private void initView() {
		// 显示图片的配置
		// mOptions = new
		// DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).build();
		bOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_square)
				.showImageOnFail(R.drawable.default_square).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();
		rl_back = (RelativeLayout) findViewById(R.id.rl_social_image_cycle_back);
		rl_back.setOnClickListener(this);
		// mImgsList = getIntent().getStringArrayListExtra("mImgs");
		bImgsList = getIntent().getStringArrayListExtra("bImgs");
		int ID = getIntent().getIntExtra("position", 0);
		loading = (ProgressBar) findViewById(R.id.pb_social_image_cycle);
		// mAdView = (ImageCycleView)
		// findViewById(R.id.imageCycleView_social_image_cycle_m);
		// mAdView.setImageResources(mImgsList, ID, mAdCycleViewListener,true);
		bAdView = (ImageCycleView) findViewById(R.id.imageCycleView_social_image_cycle_b);
		bAdView.setImageResources(bImgsList, ID, bAdCycleViewListener, false);
		proDialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	private Runnable connectNet = new Runnable() {
		@Override
		public void run() {
			try {
				// 以下是取得图片的两种方法
				// ////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap
				byte[] data = getImage(path);
				if (data != null) {
					mBitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);// bitmap
					saveImageToGallery(mContext, mBitmap);
				} else {
					connectHanlder.sendEmptyMessage(1);
				}
				// //////////////////////////////////////////////////////

				// ******** 方法2：取得的是InputStream，直接从InputStream生成bitmap
				// ***********/
				// mBitmap =
				// BitmapFactory.decodeStream(getImageStream(filePath));
				// ********************************************************************/

				// 发送消息，通知handler在主线程中更新UI
				connectHanlder.sendEmptyMessage(0);
			} catch (Exception e) {
				connectHanlder.sendEmptyMessage(1);
				e.printStackTrace();
			}
		}

	};
	@SuppressLint("HandlerLeak")
	private Handler connectHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				proDialog.setMessage("保存成功！");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						proDialog.dismiss();
					}
				}).start();
				break;
			case 1:
				proDialog.setMessage("保存失败，请检查网络！");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						proDialog.dismiss();
					}
				}).start();
				break;
			}

		}
	};

	public static void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(),
				"Caterin");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.fromFile(new File(file.getPath()))));
	}

	/**
	 * 点击事件的处理
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rl_social_image_cycle_back:
			finish();
			break;
		}
	}

	private ImageCycleView.ImageCycleViewListener bAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

		@Override
		public void onImageClick(final int position, View imageView) {

		}

		@Override
		public void displayImage(final int position, String imageURL,
				ImageView imageView) {
			bImageLoader.displayImage(imageURL, imageView, bOptions,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							loading.setVisibility(ProgressBar.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							loading.setVisibility(ProgressBar.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							loading.setVisibility(ProgressBar.GONE);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub
							loading.setVisibility(ProgressBar.GONE);
						}
					});
		}

		@Override
		public void onImageLongClick(final int position, View imageView) {
			AlertDialog dlg = new AlertDialog.Builder(mContext,
					AlertDialog.THEME_HOLO_LIGHT).setItems(
					R.array.OperationSocialImageCycle,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								// 上传图片和数据资料
								proDialog.setMessage("图片正在保存中，请稍等...");
								proDialog
										.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								proDialog.setCanceledOnTouchOutside(false);
								proDialog.show();
								path = bImgsList.get(position);
								new Thread(connectNet).start();
								break;
							}
						}
					}).create();
			dlg.show();
		}
	};
}
