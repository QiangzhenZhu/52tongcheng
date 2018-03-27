package cn.xcom.banjing.temp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.xcom.banjing.R;

/**
 * 广告图片自动轮播控件</br>
 * 
 */
public class ImageCycleView extends LinearLayout {
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 图片轮播视图
	 */
	private ViewPager mAdvPager = null;
	/**
	 * 滚动图片视图适配
	 */
	private ImageCycleAdapter mAdvAdapter;
	/**
	 * 图片轮播指示器控件
	 */
	private ViewGroup mGroup;

	/**
	 * 图片轮播指示个图
	 */
	private ImageView mImageView = null;

	/**
	 * 滚动图片指示视图列表
	 */
	private ImageView[] mImageViews = null;
	/**
	 * 手机密度
	 */
	private float mScale;

	/**
	 * @param context
	 */
	public ImageCycleView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	@SuppressWarnings("deprecation")
	public ImageCycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mScale = context.getResources().getDisplayMetrics().density;
		LayoutInflater.from(context).inflate(R.layout.social_view_ad_cycle,
				this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
		// 滚动图片右下指示器视
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);
	}

	/**
	 * 装填图片数据
	 * 
	 * @param bImgsList
	 *
	 * @param imageCycleViewListener
	 * @param b
	 */
	public void setImageResources(ArrayList<String> bImgsList, int page,
			ImageCycleViewListener imageCycleViewListener, boolean b) {
		// 清除
		mGroup.removeAllViews();
		// 图片广告数量
		final int imageCount = bImgsList.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
			int imageParams = (int) (mScale * 10 + 0.5f);// XP与DP转换，适应应不同分辨率
			int imagePadding = (int) (mScale * 5 + 0.5f);
			LayoutParams params = new LayoutParams(imageParams, imageParams);
			params.leftMargin = 30;
			mImageView.setScaleType(ScaleType.CENTER_CROP);
			mImageView.setLayoutParams(params);
			mImageView.setPadding(imagePadding, imagePadding, imagePadding,
					imagePadding);

			mImageViews[i] = mImageView;
			if (i == 0) {
				mImageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_focus);
			} else {
				mImageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_blur);
			}
			mGroup.addView(mImageViews[i]);
		}

		mAdvAdapter = new ImageCycleAdapter(mContext, bImgsList, page,
				imageCycleViewListener, b);
		mAdvPager.setAdapter(mAdvAdapter);
		mAdvPager.setCurrentItem(page);
	}

	public void setItem(int p) {
		mAdvPager.setCurrentItem(p);
	}

	/**
	 * 轮播图片监听
	 * 
	 * 
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			index = index % mImageViews.length;
			// 设置图片滚动指示器背
			mImageViews[index]
					.setBackgroundResource(R.drawable.banner_dian_focus);
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					mImageViews[i]
							.setBackgroundResource(R.drawable.banner_dian_blur);
				}
			}
		}
	}

	private class ImageCycleAdapter extends PagerAdapter {

		/**
		 * 图片视图缓存列表
		 */
		private ArrayList<ImageView> mImageViewCacheList;

		/**
		 * 图片资源列表
		 */
		ArrayList<String> imageArray;
		/**
		 * 广告图片点击监听
		 */
		private ImageCycleViewListener mImageCycleViewListener;

		private Context mContext;
		private Boolean c;

		public ImageCycleAdapter(Context context, ArrayList<String> bImgsList,
				int page, ImageCycleViewListener imageCycleViewListener,
				boolean b) {
			this.mContext = context;
			this.imageArray = bImgsList;
			this.c = b;
			mImageCycleViewListener = imageCycleViewListener;
			mImageViewCacheList = new ArrayList<ImageView>();
		}

		@Override
		public int getCount() {
			return imageArray.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			String imageUrl = imageArray.get(position % imageArray.size());
			ImageView imageView = null;
			if (mImageViewCacheList.isEmpty()) {
				imageView = new ImageView(mContext);
				if (c) {
					imageView.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					imageView.setScaleType(ScaleType.CENTER_INSIDE);
				} else {
					imageView.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					imageView.setScaleType(ScaleType.FIT_CENTER);
				}
				// 设置图片点击监听
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mImageCycleViewListener.onImageClick(position
								% imageArray.size(), v);
					}
				});
				imageView.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						mImageCycleViewListener.onImageLongClick(position
								% imageArray.size(), v);
						return false;
					}
				});
			} else {
				imageView = mImageViewCacheList.remove(0);
			}
			imageView.setTag(imageUrl);
			container.addView(imageView);
			mImageCycleViewListener.displayImage(position % imageArray.size(),
					imageUrl, imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ImageView view = (ImageView) object;
			mAdvPager.removeView(view);
			mImageViewCacheList.add(view);

		}

	}

	/**
	 * 轮播控件的监听事件
	 * 
	 * @author minking
	 */
	public static interface ImageCycleViewListener {

		/**
		 * 加载图片资源
		 * 
		 * @param imageURL
		 * @param imageView
		 */
		public void displayImage(int position, String imageURL,
                                 ImageView imageView);

		/**
		 * 单击图片事件
		 * 
		 * @param position
		 * @param imageView
		 */
		public void onImageClick(int position, View imageView);

		/**
		 * 长按图片
		 * 
		 * @param position
		 * @param imageView
		 */
		public void onImageLongClick(int position, View imageView);
	}

}
