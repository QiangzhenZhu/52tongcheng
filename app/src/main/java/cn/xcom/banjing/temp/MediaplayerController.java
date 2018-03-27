package cn.xcom.banjing.temp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

import cn.xcom.banjing.R;

/** 自定义视频播放器控制器 */
public class MediaplayerController extends FrameLayout {
	// private static final String LOG_TAG =
	// MediaplayerController.class.getName();

	private static final int FADE_OUT = 1;
	private static final int DEFTIMEOUT = 10000;
	private static final int SHOW_PROGRESS = 2;

	private ImageButton mBtnPause;
	private ImageButton mBtnFullscreen;
	private Handler mHandler = new MsgHandler(this);

	private ControlOper mPlayerCtrl;
	private Context mContext;
	private ViewGroup mAnchorVGroup;
	private View mRootView;
	private ProgressBar mProgress;
	private TextView mEndTime, mCurTime;
	private boolean mIsShowing;
	private boolean mIsDragging;
	StringBuilder mStrBuilder;
	Formatter mFormatter;

	public MediaplayerController(Context context, boolean useFastForward) {
		super(context);
		mContext = context;
	}

	public MediaplayerController(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRootView = null;
		mContext = context;
	}

	public MediaplayerController(Context context) {
		this(context, true);
	}

	public void removeHandlerCallback() {
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
	}

	@Override
	public void onFinishInflate() {
		if (mRootView != null) {
			initCtrlView(mRootView);
		}
	}

	public void setMediaPlayer(ControlOper player) {
		mPlayerCtrl = player;
		updatePausePlay();
		updateFullScreen();
	}

	public void setAnchorView(ViewGroup view) {

		mAnchorVGroup = view;
		LayoutParams frameParams = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		removeAllViews();

		View v = createCtrlView();
		addView(v, frameParams);
	}

	protected View createCtrlView() {

		LayoutInflater inflate = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mRootView = inflate.inflate(R.layout.controller, null);
		initCtrlView(mRootView);

		return mRootView;
	}

	private void initCtrlView(View v) {

		mBtnPause = (ImageButton) v.findViewById(R.id.pause);
		if (mBtnPause != null) {
			mBtnPause.requestFocus();
			mBtnPause.setOnClickListener(mPauseListener);
		}

		mBtnFullscreen = (ImageButton) v.findViewById(R.id.fullscreen);
		if (mBtnFullscreen != null) {
			mBtnFullscreen.requestFocus();
			mBtnFullscreen.setOnClickListener(mFullscreenListener);
		}
		// By default these are hidden. They will be enabled when
		// setPrevNextListeners() is called
		mProgress = (ProgressBar) v.findViewById(R.id.mediacontroller_progress);
		if (mProgress != null) {
			if (mProgress instanceof SeekBar) {
				SeekBar seeker = (SeekBar) mProgress;
				seeker.setOnSeekBarChangeListener(mSeekListener);
			}
			mProgress.setMax(1000);
		}

		mEndTime = (TextView) v.findViewById(R.id.time);
		mCurTime = (TextView) v.findViewById(R.id.time_current);
		mStrBuilder = new StringBuilder();
		mFormatter = new Formatter(mStrBuilder, Locale.getDefault());
	}

	/**
	 * Show the controller on screen. It will go away automatically after 3
	 * seconds of inactivity.
	 */
	public void show() {
		show(DEFTIMEOUT);
	}

	/**
	 * Disable pause or seek buttons if the stream cannot be paused or seeked.
	 * This requires the control interface to be a MediaPlayerControlExt
	 */
	private void disableUnsupportedButtons() {
		if (mPlayerCtrl == null) {
			return;
		}

		try {
			if (mBtnPause != null && !mPlayerCtrl.canPause()) {
				mBtnPause.setEnabled(false);
			}
		} catch (IncompatibleClassChangeError ex) {

		}
	}

	// TODO
	public void show(int timeout) {
		if (!mIsShowing && mAnchorVGroup != null) {
			setProgress();
			if (mBtnPause != null) {
				mBtnPause.requestFocus();
			}
			disableUnsupportedButtons();

			LayoutParams tlp = new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

			mAnchorVGroup.addView(this, tlp);
			mIsShowing = true;
		}
		updatePausePlay();
		updateFullScreen();

		mHandler.sendEmptyMessage(SHOW_PROGRESS);

		Message msg = mHandler.obtainMessage(FADE_OUT);
		if (timeout != 0) {
			mHandler.removeMessages(FADE_OUT);
			mHandler.sendMessageDelayed(msg, timeout);
		}
	}

	public boolean isShowing() {
		return mIsShowing;
	}

	/**
	 * Remove the controller from the screen.
	 */
	public void hide() {
		if (mAnchorVGroup == null) {
			return;
		}

		try {
			mAnchorVGroup.removeView(this);
			if (mHandler != null) {
				mHandler.removeMessages(SHOW_PROGRESS);
			}
		} catch (IllegalArgumentException ex) {
			Log.w("MediaController", "already removed");
		}
		mIsShowing = false;
	}

	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mStrBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	private int setProgress() {
		if (mPlayerCtrl == null || mIsDragging) {
			return 0;
		}

		int position = mPlayerCtrl.getCurPosition();
		int duration = mPlayerCtrl.getDuration();
		if (mProgress != null) {
			if (duration > 0) {
				// use long to avoid overflow
				long pos = 1000L * position / duration;
				mProgress.setProgress((int) pos);
			}
			int percent = mPlayerCtrl.getBufPercent();
			mProgress.setSecondaryProgress(percent * 10);
		}

		if (mEndTime != null)
			mEndTime.setText(stringForTime(duration));
		if (mCurTime != null)
			mCurTime.setText(stringForTime(position));

		return position;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		show(DEFTIMEOUT);
		return true;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		show(DEFTIMEOUT);
		return false;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (mPlayerCtrl == null) {
			return true;
		}

		int keyCode = event.getKeyCode();
		final boolean uniqueDown = event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN;
		if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
				|| keyCode == KeyEvent.KEYCODE_SPACE) {
			if (uniqueDown) {
				doPauseResume();
				show(DEFTIMEOUT);
				if (mBtnPause != null) {
					mBtnPause.requestFocus();
				}
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
			if (uniqueDown && !mPlayerCtrl.isPlaying()) {
				mPlayerCtrl.start();
				updatePausePlay();
				show(DEFTIMEOUT);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
			if (uniqueDown && mPlayerCtrl.isPlaying()) {
				mPlayerCtrl.pause();
				updatePausePlay();
				show(DEFTIMEOUT);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
				|| keyCode == KeyEvent.KEYCODE_VOLUME_UP
				|| keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
			// don't show the controls for volume adjustment
			return super.dispatchKeyEvent(event);
		} else if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_MENU) {
			if (uniqueDown) {
				hide();
			}
			return true;
		}

		show(DEFTIMEOUT);
		return super.dispatchKeyEvent(event);
	}

	private OnClickListener mPauseListener = new OnClickListener() {
		public void onClick(View v) {
			doPauseResume();
			show(DEFTIMEOUT);
		}
	};

	// TODO
	private OnClickListener mFullscreenListener = new OnClickListener() {
		public void onClick(View v) {
			doToggleFullscreen();
			show(DEFTIMEOUT);
		}
	};

	public void updatePausePlay() {
		if (mRootView == null || mBtnPause == null || mPlayerCtrl == null) {
			return;
		}

		if (mPlayerCtrl.isPlaying()) {
			mBtnPause.setImageResource(R.drawable.pause);
		} else {
			mBtnPause.setImageResource(R.drawable.play);
		}
	}

	public void updateFullScreen() {
		if (mRootView == null || mBtnFullscreen == null || mPlayerCtrl == null) {
			return;
		}
		if (mPlayerCtrl.isFullScreen()) {
			mBtnFullscreen.setImageResource(R.drawable.nscale);
		} else {
			mBtnFullscreen.setImageResource(R.drawable.nscale);
		}
	}

	private void doPauseResume() {
		if (mPlayerCtrl == null) {
			return;
		}

		if (mPlayerCtrl.isPlaying()) {
			mPlayerCtrl.pause();
		} else {
			mPlayerCtrl.start();
		}
		updatePausePlay();
	}

	private void doToggleFullscreen() {
		if (mPlayerCtrl == null) {
			return;
		}
		if (mPlayerCtrl.screenOrient() == 0) {
			mPlayerCtrl.verticalScreen();
		} else {
			mPlayerCtrl.fullScreen();
		}
	}

	private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
		public void onStartTrackingTouch(SeekBar bar) {
			show(3600000);

			mIsDragging = true;
			mHandler.removeMessages(SHOW_PROGRESS);
		}

		public void onProgressChanged(SeekBar bar, int progress,
				boolean fromuser) {
			if (mPlayerCtrl == null) {
				return;
			}

			if (!fromuser) {
				// We're not interested in programmatically generated changes to
				// the progress bar's position.
				return;
			}

			long duration = mPlayerCtrl.getDuration();
			long newposition = (duration * progress) / 1000L;
			mPlayerCtrl.seekTo((int) newposition);
			if (mCurTime != null)
				mCurTime.setText(stringForTime((int) newposition));
		}

		public void onStopTrackingTouch(SeekBar bar) {
			mIsDragging = false;
			setProgress();
			updatePausePlay();
			show(DEFTIMEOUT);

			mHandler.sendEmptyMessage(SHOW_PROGRESS);
		}
	};

	@Override
	public void setEnabled(boolean enabled) {
		if (mBtnPause != null) {
			mBtnPause.setEnabled(enabled);
		}
		if (mProgress != null) {
			mProgress.setEnabled(enabled);
		}
		disableUnsupportedButtons();
		super.setEnabled(enabled);
	}

	@SuppressLint("NewApi")
	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(MediaplayerController.class.getName());
	}

	@SuppressLint("NewApi")
	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(MediaplayerController.class.getName());
	}

	public interface ControlOper {
		void start();

		void pause();

		int getDuration();

		int getCurPosition();

		void seekTo(int pos);

		boolean isPlaying();

		int getBufPercent();

		boolean canPause();

		boolean canSeekBackward();

		boolean canSeekForward();

		boolean isFullScreen();

		/** 横屏状态 */
		void verticalScreen();

		/** 竖屏状态 */
		void fullScreen();

		/** 判断当前状态是横屏还是竖屏 */
		int screenOrient();
	}

	private static class MsgHandler extends Handler {

		private final WeakReference<MediaplayerController> mView;

		MsgHandler(MediaplayerController view) {
			mView = new WeakReference<MediaplayerController>(view);
		}

		@Override
		public void handleMessage(Message msg) {

			MediaplayerController view = mView.get();
			if (view == null || view.mPlayerCtrl == null) {
				return;
			}

			int pos;
			switch (msg.what) {

			case FADE_OUT: {
				view.hide();
				break;
			}

			case SHOW_PROGRESS: {

				if (view.mPlayerCtrl.isPlaying()) {
					pos = view.setProgress();
				} else {
					return;
				}

				if (!view.mIsDragging && view.mIsShowing
						&& view.mPlayerCtrl.isPlaying()) {

					msg = obtainMessage(SHOW_PROGRESS);
					sendMessageDelayed(msg, 1000 - (pos % 1000));
				}
				break;
			}
			}
		}
	}

}
