package com.yunbo.myyunbo;

import com.ab.util.AbToastUtil;  
import com.yunbo.control.DyUtil;

import android.R.integer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayActivity extends Activity {
/**//*
	APlayerAndroid aPlayerAndroid;
	Surface sur;
	String strPath;
	Boolean isPause;
	SeekBar seekBar;
	ProgressBar progressBar;
	TextView testView;
	int nOrientation = 1;
	int playfrom = 0;
	SurfaceView surView; 

	LinearLayout operation;
	LinearLayout loading;
	LinearLayout Scroll;
	TextView textScroll;
	TextView textTimer;
	TextView name;
	Button pbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_play);
		strPath = DyUtil.playUrl;
		aPlayerAndroid = new APlayerAndroid(); 
		seekBar = ((SeekBar) findViewById(R.id.seekBar));
		progressBar = ((ProgressBar) findViewById(R.id.progressBar1));
		surView = (SurfaceView) findViewById(R.id.surfaceview);
		operation = (LinearLayout) findViewById(R.id.video_operation);
		loading = (LinearLayout) findViewById(R.id.video_loading);
		Scroll = (LinearLayout) findViewById(R.id.video_Scroll);
		textScroll = (TextView) findViewById(R.id.text_Scroll);
		textTimer = (TextView) findViewById(R.id.text_time);
		name = (TextView) findViewById(R.id.name);
		aPlayerAndroid.SetView(surView);

		((Button) findViewById(R.id.startbtn))
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View view) {
		aPlayerAndroid.Close();
		aPlayerAndroid.SetConfig(1105, "FTN5K=" + DyUtil.newPlayObj.getCookie());
		aPlayerAndroid.SetConfig(1108,DyUtil.userAgent1);
						  aPlayerAndroid.Open(strPath);
						//
						//outSysremPlay();
					}

				});

		// aPlayerAndroid.Open(strPath);

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// aPlayerAndroid.SetVideoOrientation(APlayerAndroid.Orientation.VIDEO_ORIENTARION_LEFT90);
		// aPlayerAndroid.UpdateWindow();
		name.setText(DyUtil.newPlayObj.getName()+ DyUtil.newPlayObj.getCookie());
		isPause = false;
		(pbtn = (Button) findViewById(R.id.pausebtn))
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View view) {
						Button btn = (Button) view;
						if (!isPause) {
							// btn.setText("播放");
							btn.setBackgroundResource(R.drawable.play);
							isPause = true;
							aPlayerAndroid.Pause();
						} else {
							// btn.setText("暂停");
							btn.setBackgroundResource(R.drawable.pause);
							isPause = false;
							aPlayerAndroid.Play();
							//mDismissHandler.removeMessages(0);
							//mDismissHandler.sendEmptyMessageDelayed(0, 3000);
						}
					}

				});

		((Button) findViewById(R.id.stopbtn))
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View view) {
						aPlayerAndroid.Close();
						finish();
					}

				});*/

		/*
		 * ((Button) findViewById(R.id.turnbtn)).setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * public void onClick(View view) {
		 * aPlayerAndroid.SetVideoOrientation(APlayerAndroid
		 * .Orientation.VIDEO_ORIENTARION_LEFT90);
		 * aPlayerAndroid.UpdateWindow(); }
		 * 
		 * });
		 */

		/*seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				operation.setVisibility(View.GONE);
				loading.setVisibility(View.VISIBLE);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					aPlayerAndroid.SetPosition(progress);
					playfrom = progress;
					if (isPause) {
						// pbtn.setText("暂停");
						pbtn.setBackgroundResource(R.drawable.pause);
						aPlayerAndroid.Play();
						isPause = false;
					}
				}
			}
		});*/
/*
		aPlayerAndroid
				.setOnGetPositionListener(new APlayerAndroid.() {

					@Override
					public void onGetPosition(int position) {
						// TODO Auto-generated method stub
						seekBar.setProgress(position);
						//loading.setVisibility(View.GONE);
						if (operation.getVisibility() == View.VISIBLE)
							textTimer.setText(GetTime(seekBar.getProgress())
									+ " / " + GetTime(seekBar.getMax()));
						if (progressBar.getProgress() - seekBar.getProgress()<1000*10) {
							loading.setVisibility(View.VISIBLE);
						}else {
							loading.setVisibility(View.GONE);
						}
						if ((progressBar.getProgress() - seekBar.getProgress()) > 1000 * 60 * 3
								|| seekBar.getProgress() - playfrom > 1000 * 60 * 3) {
							// loading.setVisibility(View.VISIBLE);
							aPlayerAndroid.SetPosition(position - 1);
							playfrom = position - 1;
						}

					}
				});

		aPlayerAndroid
				.setOnGetReadPositionListener(new APlayerAndroid.OnGetReadPositionListener() {

					@Override
					public void onGetReadPosition(int position) {
						// TODO Auto-generated method stub
						progressBar.setProgress(position);
						seekBar.setSecondaryProgress(position);
					}
				});
 

		aPlayerAndroid
				.setOnPlayCompleteListener(new APlayerAndroid.OnPlayCompleteListener() {

					@Override
					public void onPlayComplete(APlayerAndroid mp) {
						aPlayerAndroid.Close();
						finish();
					}
				});*/
		/*
		aPlayerAndroid.setOnBufferListenerr(new APlayerAndroid.OnBufferListener() {
			
			@Override
			public void onBuffer(int progress) {
				// TODO Auto-generated method stub

				name.setText(DyUtil.newPlayObj.getName()+"正在缓冲"+progress+"%"); 
			}
		});
		final String[] state=new String[]{"准备就绪",
				"正在打开",
		       "正在暂停",
		        "暂停中",
		        "正在开始播放",
		       "播放中",//5
		        "正在开始关闭"
};
		aPlayerAndroid.setOnPlayStateChangeListener(new APlayerAndroid.OnPlayStateChangeListener() {
			
			@Override
			public void onPlayStateChange(int nCurrentState, int nPreState) {
				// TODO Auto-generated method stub
				if (nCurrentState==5) {

					operation.setVisibility(View.GONE);
					Scroll.setVisibility(View.GONE);
					loading.setVisibility(View.GONE);

					pbtn.setBackgroundResource(R.drawable.pause);
					isPause = false;
				}else {
					if (nCurrentState==1) {

						loading.setVisibility(View.VISIBLE);
					}else {

						loading.setVisibility(View.GONE);
					}
					Scroll.setVisibility(View.VISIBLE);
				}

				name.setText(DyUtil.newPlayObj.getName()+state[nCurrentState]);
				if (nCurrentState==0&&nPreState==1) {
					name.setText("播放失败，地址无效");
				}
				if (nCurrentState==0) {

					pbtn.setBackgroundResource(R.drawable.pause);
					aPlayerAndroid.Play();
					isPause = false;

					seekBar.setMax(aPlayerAndroid.GetDuration());
					progressBar.setMax(aPlayerAndroid.GetDuration());
				}
			}
		});
		aPlayerAndroid.setOnOpenSuccessListener(new APlayerAndroid.OnOpenSuccessListener() {
			
			@Override
			public void onOpenSuccess() {
				// TODO Auto-generated method stub 
					aPlayerAndroid.Play();
					seekBar.setMax(aPlayerAndroid.GetDuration());
					progressBar.setMax(aPlayerAndroid.GetDuration());
					//aPlayerAndroid.SetPosition(pauseint);
					operation.setVisibility(View.GONE);
					Scroll.setVisibility(View.GONE);
					playfrom = pauseint; 
					name.setText("打开成功");
				
			}
		});
		aPlayerAndroid.SetConfig(1105, "FTN5K=" + DyUtil.newPlayObj.getCookie());
		aPlayerAndroid.SetConfig(1108,DyUtil.userAgent1);
						  aPlayerAndroid.Open(strPath);*/
		/*
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
					}
					//某些视频的duration可能会延迟得到，所以对未能取得duration的视频要不断去获取
					if(seekBar.getMax() == 0){
						seekBar.setMax(aPlayerAndroid.GetDuration());
					}
					seekBar.setProgress(aPlayerAndroid.GetPosition());
				}
			}
		}.start();*/
/*		mGestureDetector = new GestureDetector(this, new MyGestureListener());

		//
	}

	private void outSysremPlay() {
		Uri uri = Uri.parse(strPath);
		// 调用系统自带的播放器
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Log.v("URI:::::::::", uri.toString());
		intent.setDataAndType(uri, "video/mp4");
		startActivity(intent);
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		// 双击  
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (!isPause) {
				// pbtn.setText("播放");
				pbtn.setBackgroundResource(R.drawable.play);
				aPlayerAndroid.Pause();
				isPause = true;
			} else {
				// pbtn.setText("暂停");
				pbtn.setBackgroundResource(R.drawable.pause);
				aPlayerAndroid.Play();
				isPause = false;
			}
			// operation.setVisibility(View.VISIBLE);
			return true;
		}

		// 滑动 
		@SuppressWarnings("deprecation")
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) { 
				int dx = ((int) e2.getX() - (int) xdown) * 500;
				// System.out.println(seekBar.getProgress()
				// +"  "+(int)e2.getX()+"  "+(int)e1.getX());
				Scroll.setVisibility(View.VISIBLE);
				operation.setVisibility(View.VISIBLE);
				textScroll.setText((dx < 0 ? "-" : "+") + GetTime(Math.abs(dx))
						+ "[" + GetTime(seekBar.getProgress()) + "]");
				 
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	private GestureDetector mGestureDetector;

	int xdown = 0, ydown = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mGestureDetector == null)
			return false;
		if (mGestureDetector.onTouchEvent(event))
			return true;

		// 处理手势结束
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			Scroll.setVisibility(View.GONE);
			int dx = ((int) event.getX() - (int) xdown);
			if (Math.abs(dx) > 10) {
				dx = dx * 500;
				dx=seekBar.getProgress() + dx;
				if (dx<0) {
					dx=0;
				}
				if (dx>seekBar.getMax()) {
					dx=seekBar.getMax();
				}
				aPlayerAndroid.SetPosition(dx);
				playfrom = dx;
				if (isPause) {
					// pbtn.setText("暂停");
					pbtn.setBackgroundResource(R.drawable.pause);
					aPlayerAndroid.Play();
					isPause = false;
				}
				loading.setVisibility(View.VISIBLE);
			}
			endGesture();
			break;
		case MotionEvent.ACTION_DOWN:
			xdown = (int) event.getX();
			ydown = (int) event.getY();

			// endGesture();
			operation.setVisibility(View.VISIBLE);
			break;
		}

		return super.onTouchEvent(event);
	}

	private void endGesture() {
		// if(
		// operation.getVisibility()==View.VISIBLE)loading.setVisibility(View.VISIBLE);
		if (!isPause) {
			// 隐藏
			mDismissHandler.removeMessages(0);
			mDismissHandler.sendEmptyMessageDelayed(0, 3000);
		}
	}

	// 定时隐藏
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			operation.setVisibility(View.GONE);
		}
	};
	int pauseint = 0;
*/
		/*
	@Override
	protected void onPause() {
		pauseint = seekBar.getProgress();
		if (aPlayerAndroid != null)
			aPlayerAndroid.Close();
		super.onPause();
	}*/
/*
	@Override
	protected void onResume() {
		super.onResume();
		
		loading.setVisibility(View.VISIBLE);
		try {
			if (aPlayerAndroid != null)
				aPlayerAndroid.Open(strPath);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			outSysremPlay();
			AbToastUtil.showToast(this, "无法播放！");
		}
	}*/
/*
	@Override
	protected void onDestroy() {
		
		pauseint = seekBar.getProgress();
		if (aPlayerAndroid != null){
			aPlayerAndroid.Pause(); 
			aPlayerAndroid.Close();
		}
		super.onDestroy();
	}

	public String GetTime(int whs) {
		String time = "";
		int h = whs / 3600000;
		int m = (whs / 60000) % 60;
		int s = (whs / 1000) % 60;
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
	}*/

}
