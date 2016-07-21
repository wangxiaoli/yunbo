package com.yunbo.myyunbo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**/
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoViewBuffer extends Activity implements OnInfoListener,
		OnBufferingUpdateListener, OnErrorListener, OnCompletionListener {

	/**
	 * TODO: Set the path variable to a streaming video URL or a local media
	 * file path.
	 */

	private String path = "http://gdl.lixian.vip.xunlei.com/download?fid=f2DY/mkuCFD5GUyNz63T0UK3lV0tYJAcAAAAANUajmIGCFQIN1wah36auVVQM5UT&mid=666&threshold=150&tid=D304B9A1B18BC06F497296362BFA6B24&srcid=4&verno=1&g=D51A8E6206085408375C1A877E9AB95550339513&scn=t20&dt=17&ui=340538966&s=479223853&n=0E&it=1416791230&cc=8089487440268664372&p=0&specid=225536";
	private String name = "";
	private Uri uri;
	private VideoView mVideoView;
	private ProgressBar pb;
	private TextView downloadRateView, loadRateView;
	private TextView fileNameTV, ratetv, down, copytv;
	private MediaController controller;
	private LinearLayout Scroll;
	private TextView textScroll;
	private List<String> files = new ArrayList<String>();
	private Map<String, String> headers = new HashMap<String, String>();
	private boolean isLive = false;
	private boolean isavi = false;
	private boolean iscopy = false;
	private ClipboardManager myClipboard;

	// private MediaPlayer _mediaPlayer=null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Vitamio.isInitialized(getApplicationContext());
		isLive = DyUtil.isLive;
		path = DyUtil.playUrl;

		if (TextUtils.isEmpty(path))
			path = DyUtil.newPlayObj.getUrl();
		name = DyUtil.newPlayObj.getName() + "";
		if (name.contains(".")) {
			final String fiString = name.substring(name.lastIndexOf("."))
					.trim().toLowerCase();
			if (fiString.endsWith("avi")) {
				isavi = true;
			}
		}
		/**/

		HistoryUtil.init(this);
		if (HistoryUtil.seek != 0) {
			seek = HistoryUtil.seek;
			HistoryUtil.seek = 0L;
		}

		// headers.put("Referer", path);
		// headers.put("user-agent",DyUtil.userAgent2);
		// headers.put("cookies", "FTN5K=" + DyUtil.newPlayObj.getCookie());
		if (!TextUtils.isEmpty(DyUtil.newPlayObj.getCookie()))
			headers.put(
					"headers", // "Connection: Keep-Alive\r\n"+
					"Accept-Encoding: identity\r\n"
							+
							// "Cache-Control: no-cache\r\n"+
							"Cookie:" + " FTN5K="
							+ DyUtil.newPlayObj.getCookie() + "\r\n"
							+ "User-Agent: " + DyUtil.userAgent3 + "\r\n");
		else {
			iscopy = true;
		}
		if (iscopy) {
			iscopy=DyUtil.isCanCopy;
			DyUtil.isCanCopy= true;
		}
		// files.add(path);
		files = DyUtil.fileList;
		if (files==null) {
			files = new ArrayList<String>();
		}
		// System.out.println("ok");
		setContentView(R.layout.videobuffer);
		mVideoView = (VideoView) findViewById(R.id.buffer);
		pb = (ProgressBar) findViewById(R.id.probar);

		downloadRateView = (TextView) findViewById(R.id.download_rate);
		loadRateView = (TextView) findViewById(R.id.load_rate);

		fileNameTV = (TextView) findViewById(R.id.nametv);
		ratetv = (TextView) findViewById(R.id.ratetv);
		Scroll = (LinearLayout) findViewById(R.id.video_Scroll);
		textScroll = (TextView) findViewById(R.id.text_Scroll);
		Scroll.setVisibility(View.GONE);

		down = (TextView) findViewById(R.id.download);
		if (isLive || !DyUtil.isCanDownload) {
			down.setVisibility(View.GONE);
		} else {
			down.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Bundle bundle = new Bundle();
					bundle.putBoolean("add", true);
					Intent intent = new Intent();
					intent.setClass(VideoViewBuffer.this,
							DownListActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}
			});
		}
		copytv = (TextView) findViewById(R.id.copytv);
		if (!iscopy) {
			copytv.setVisibility(View.GONE);
		} else {
			myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			copytv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (myClipboard != null) {
						// myClipboard.setText(path);

						ClipData myClip = ClipData.newPlainText("text", path);
						myClipboard.setPrimaryClip(myClip);
						Toast.makeText(getApplicationContext(), "地址已经复制到剪贴板",
								Toast.LENGTH_SHORT).show();
					}

				}
			});
		}
		if (AbStrUtil.isEmpty(path)) {
			// Tell the user to provide a media file URL/path.
			Toast.makeText(VideoViewBuffer.this, "视频地址为空！", Toast.LENGTH_LONG)
					.show();
			return;
		} else {
			// Alternatively,for streaming media you can use
			// mVideoView.setVideoURI(Uri.parse(URLstring));
			// AbToastUtil.showToast(this, "正在准备数据" +
			// DyUtil.newPlayObj.getName());
			mVideoView
					.setMediaController(controller = new MediaController(this));
			mVideoView.requestFocus();
			mVideoView.setOnInfoListener(this);
			mVideoView.setOnBufferingUpdateListener(this);
			mVideoView
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mediaPlayer) {
							// optional need Vitamio 4.0
							mediaPlayer.setPlaybackSpeed(1.0f);
							loadRateView.setText("流媒体打开成功");
							// _mediaPlayer=mediaPlayer;
							isOpen = true;
							/**/
							if (seek != 0L && seek < mediaPlayer.getDuration()
									&& seek != mediaPlayer.getCurrentPosition()) {
								mediaPlayer.seekTo(seek);
								seek = 0L;
							}
							if (mediaPlayer.getDuration() == 0) {
								isLive = true;
							} else {
								
								if (name.endsWith(".mp4")||name.endsWith(".rm")||
										name.endsWith(".flv")||name.endsWith(".mkv")||
										name.endsWith(".wmv")||name.endsWith(".rmvb")||
										name.endsWith(".mpg")||name.endsWith(".3gp")
										) {
									DyUtil.isCanDownload = true;
								}else {
									DyUtil.isCanDownload = false;
								}
							}
							if (isLive) {
								Toast.makeText(VideoViewBuffer.this,
										"双击屏幕可以放大缩小视频。", Toast.LENGTH_LONG)
										.show();
							}
							controller.show(3000);
						}
					});
			mVideoView.setOnErrorListener(this);
			mVideoView.setOnCompletionListener(this);
			controller
					.setOnShownListener(new MediaController.OnShownListener() {

						@Override
						public void onShown() {
							// TODO Auto-generated method stub
							// else
							fileNameTV.setVisibility(View.VISIBLE);
							if (DyUtil.isCanDownload)
								down.setVisibility(View.VISIBLE);
							if (iscopy) {
								copytv.setVisibility(View.VISIBLE);
								if (isLive)
									controller.hide();

							}
						}
					});
			controller
					.setOnHiddenListener(new MediaController.OnHiddenListener() {

						@Override
						public void onHidden() {
							// TODO Auto-generated method stub
							if (!isLive) {
								fileNameTV.setVisibility(View.GONE);
								copytv.setVisibility(View.GONE);
							} else {
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub

										fileNameTV.setVisibility(View.GONE);
										copytv.setVisibility(View.GONE);
									}
								}, 3000);
							}
							down.setVisibility(View.GONE);
						}
					});

			mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
			mOperationBg = (ImageView) findViewById(R.id.operation_bg);
			mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

			mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			mMaxVolume = mAudioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

			mGestureDetector = new GestureDetector(this,
					new MyGestureListener());
			/**/
			if (path.startsWith("/")) {
				mVideoView.setVideoPath(path);
			} else {

				uri = Uri.parse(path);
				if (TextUtils.isEmpty(DyUtil.newPlayObj.getCookie())) {
					mVideoView.setVideoURI(uri);
				} else
					mVideoView.setVideoURI(uri, headers);
			}

			// controller.setFileName(name);
			if (TextUtils.isEmpty(name)) {
				name="未命名";
			}
			if (DyUtil.fileList.size()>1&&TextUtils.isEmpty(DyUtil.newPlayObj.getCookie())) {
				Toast.makeText(VideoViewBuffer.this,
						"分段视频，有"+DyUtil.fileList.size()+"段。一段播完放下一段。", Toast.LENGTH_LONG)
						.show();
				if (path.equals(files.get(0))) {
					fileindex=1;
				}
			}
		}

	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_FILE_OPEN_OK:
			loadRateView.setText("流媒体打开成功");
			// controller.setFileName(name);
			fileNameTV.setVisibility(View.GONE);
			copytv.setVisibility(View.GONE);
			down.setVisibility(View.GONE);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
 if (!mVideoView.isPlaying()) {
	mVideoView.start();
}
				}
			}, 10000);
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				pb.setVisibility(View.VISIBLE);
				downloadRateView.setText("");
				loadRateView.setText("正在努力加载视频数据...");// +extra
				downloadRateView.setVisibility(View.VISIBLE);
				loadRateView.setVisibility(View.VISIBLE);
				fileNameTV.setVisibility(View.VISIBLE);
				if (!isLive)
					controller.show(30 * 1000);
			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			mVideoView.start();
			pb.setVisibility(View.GONE);
			downloadRateView.setVisibility(View.GONE);
			loadRateView.setVisibility(View.GONE);
			fileNameTV.setVisibility(View.GONE);
			down.setVisibility(View.GONE);
			copytv.setVisibility(View.GONE);
			controller.hide();
			controller.setFileName("正在播放：" + name);
			fileNameTV.setText(name);
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			// downloadRateView.setText("" + extra + "kb/s" + "  ");
			ratetv.setText("" + extra + "kb/s" + "  ");
			break;
		}
		return true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		
		if (percent==0) {
			loadRateView.setText("正在缓冲中，请耐心等待。");
		}else {
			loadRateView.setText("正在缓冲：" + percent + "%");
		}
		// controller.setFileName(percent + "%");
	}

	private boolean isOpen = false;

	@Override
	protected void onResume() {
		super.onResume();
		// isOpen=false;
		if (loadRateView != null) {
			pb.setVisibility(View.VISIBLE);
			fileNameTV.setText(DyUtil.newPlayObj.getName());
			ratetv.setText("" + 0 + "kb/s" + "  ");
			loadRateView.setText("正在连接播放服务器，请耐心等待。");
			loadRateView.setVisibility(View.VISIBLE);
			if (seek != 0L && !isLive) {
				loadRateView.setText("正在恢复播放进度");
				if (controller != null)
					controller.setFileName(DyUtil.newPlayObj.getName());
				if (mVideoView != null && isOpen && !mVideoView.isPlaying()) {
					mVideoView.start();
				}
			}
			if (isOpen) {
				pb.setVisibility(View.GONE);
				downloadRateView.setVisibility(View.GONE);
				loadRateView.setVisibility(View.GONE);
				fileNameTV.setVisibility(View.GONE);
				controller.hide();
			}
		}
		if (mVideoView != null) {
			if (isPause) {
				try {
					mVideoView.resume();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}
		if (controller != null) {
			if (!isLive)
				controller.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if (mVideoView != null)
				mVideoView.stopPlayback();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	int fileindex = 0;

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		/*
		 * if (files.size()==0) { //files=DyUtil.readFiles(path,
		 * getFilesDir()+"/"); files.add(path); }
		 */
		fileindex++;
		if (!TextUtils.isEmpty(DyUtil.newPlayObj.getCookie()))
		if (files.size() != 0 && fileindex < files.size()) {
			loadRateView.setText("正在连接播放服务器，请耐心等待。" + "连接进度"
					+ (fileindex * 100 / files.size()) + "%");
			uri = Uri.parse(path = files.get(fileindex));
			if (!TextUtils.isEmpty(DyUtil.newPlayObj.getCookie()))
				mVideoView.setVideoURI(uri, headers);
			else {
				mVideoView.setVideoURI(uri);
			}
			DyUtil.newPlayObj.setUrl(path);
			return true;
		}/*
		 * if (what == 0) {
		 * 
		 * Toast.makeText(this, "未知错误。", Toast.LENGTH_LONG).show();
		 * 
		 * } else { Toast.makeText(this, "视频类型不支持或者文件路径错误。",
		 * Toast.LENGTH_LONG).show(); }
		 */
		String msg = "";
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			msg = "发生未知错误";
			break;
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			msg = "媒体服务器死机";
			break;
		default:
			msg = "错误:" + what;
			break;
		}
		switch (extra) {
		case MediaPlayer.MEDIA_ERROR_IO:
			// io读写错误
			msg = msg + ",文件或网络相关的IO操作错误";
			break;
		case MediaPlayer.MEDIA_ERROR_MALFORMED:
			// 文件格式不支持
			msg = msg + ",比特流编码标准或文件不符合相关规范";
			break;
		case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
			// 一些操作需要太长时间来完成,通常超过3 - 5秒。
			msg = msg + ",操作超时";
			break;
		case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
			// 比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
			msg = msg + ",比特流编码标准或文件符合相关规范,但媒体框架不支持该功能";
			break;
		default:
			msg = msg + " ," + extra;
			break;
		}
		// Toast.makeText(this, "错误代码:"+what+"  "+extra,
		// Toast.LENGTH_LONG).show();
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		finish();
		return false;
	}

	long seek = 0L;
	private boolean isPause = false;

	@Override
	protected void onPause() {
		seek = mVideoView.getCurrentPosition();
		if (seek != 0L || !isavi)
			if (HistoryUtil.isRecord) {
				HistoryUtil.init(this);
				/**/HistoryUtil.data.setSeek(seek);
				// HistoryUtil.data.setUrl(path);
				HistoryUtil.save(this);
			}
		super.onPause();
		try {
			if (mVideoView != null) {
				mVideoView.pause();
				isPause = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

		if(TextUtils.isEmpty(DyUtil.newPlayObj.getCookie()))
		if (files.size() != 0  ) {
			if (fileindex < files.size()) {
				uri = Uri.parse(files.get(fileindex));
				mVideoView.setVideoURI(uri);
				fileindex++;

				pb.setVisibility(View.VISIBLE);
				loadRateView.setText("正在加载下一个视频(" + fileindex + "/"
						+ files.size() + ")");
				loadRateView.setVisibility(View.VISIBLE);
				return;
			}
		}

		AbToastUtil.showToast(this, "播放完成。");
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;

		// 处理手势结束
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}

		return super.onTouchEvent(event);
	}

	private int scrollVolume = 0;

	// /** 手势结束
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;
		Scroll.setVisibility(View.GONE);
		fileNameTV.setVisibility(View.VISIBLE);
		if (iscopy) {
			copytv.setVisibility(View.VISIBLE);
		}
		if (Math.abs(scrollVolume) > 10000) {
			long cp = mVideoView.getCurrentPosition() + scrollVolume;
			if (cp < 0) {
				cp = 0;
			}
			if (cp > mVideoView.getDuration()) {
				cp = mVideoView.getDuration() - 1;
			}
			mVideoView.seekTo(cp);
			if (!mVideoView.isPlaying()) {
				mVideoView.start();
			}
		}

		scrollVolume = 0;
		// 隐藏
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 2000);
		if (isLive && controller != null)
			controller.show(50);
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		// 双击
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
				mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
			else
				mLayout++;
			if (mVideoView != null)
				mVideoView.setVideoLayout(mLayout, 0);
			return true;
		}

		// 滑动
		@SuppressWarnings("deprecation")
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getRawY();
			int x = (int) e2.getRawX();
			Display disp = getWindowManager().getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();

			if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
				onVolumeSlide((mOldY - y) / windowHeight);
			else if (mOldX < windowWidth / 5.0)// 左边滑动
				onBrightnessSlide((mOldY - y) / windowHeight);
			else {
				if (!isLive)
					onScrollSlide((x - mOldX) / windowWidth);
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	// 定时隐藏
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mVolumeBrightnessLayout.setVisibility(View.GONE);
			fileNameTV.setVisibility(View.GONE);
			copytv.setVisibility(View.GONE);
		}
	};

	private void onScrollSlide(float percent) {
		// TODO Auto-generated method stub
		if (isOpen && mVideoView != null) {
			Scroll.setVisibility(View.VISIBLE);
			scrollVolume = (int) (percent * 1000 * 100);
			textScroll.setText((scrollVolume < 0 ? "-" : "+")
					+ GetTime(Math.abs(scrollVolume)) + "["
					+ GetTime(mVideoView.getCurrentPosition()) + "/"
					+ GetTime(mVideoView.getDuration()) + "]");

		}
	}

	// **
	// * 滑动改变声音大小
	// *
	// * @param percent
	//
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// 变更进度条
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = findViewById(R.id.operation_full).getLayoutParams().width
				* index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}

	// **
	// * 滑动改变亮度
	// *
	// * @param percent
	//
	private void onBrightnessSlide(float percent) {
		if (mBrightness < 0) {
			mBrightness = getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}
		WindowManager.LayoutParams lpa = getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		getWindow().setAttributes(lpa);

		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
		mOperationPercent.setLayoutParams(lp);
	}

	private View mVolumeBrightnessLayout;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	private AudioManager mAudioManager;
	// /** 最大声音
	private int mMaxVolume;
	// /** 当前声音
	private int mVolume = -1;
	// /** 当前亮度
	private float mBrightness = -1f;
	// /** 当前缩放模式
	private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
	private GestureDetector mGestureDetector;

	private String GetTime(long whs) {
		String time = "";
		long h = whs / 3600000;
		long m = (whs / 60000) % 60;
		long s = (whs / 1000) % 60;
		time += String.valueOf(h);
		if (String.valueOf(m).length() < 2)
			time += ":0" + String.valueOf(m);
		else
			time += ":" + String.valueOf(m);

		if (String.valueOf(s).length() < 2)
			time += ":0" + String.valueOf(s);
		else
			time += ":" + String.valueOf(s);
		return time;
	}

}
