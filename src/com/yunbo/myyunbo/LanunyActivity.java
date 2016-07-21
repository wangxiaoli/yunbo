package com.yunbo.myyunbo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbDateUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.yunbo.control.DyUtil;
import com.yunbo.frame.KeywordsView;
import com.yunbo.mode.Urldata;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LanunyActivity extends AbActivity implements View.OnClickListener {

	public static String[] totalKeys = null;
	private String[] key_words = new String[15];

	private KeywordsView showKeywords = null;
	private LinearLayout searchLayout = null;
	private EditText keywordEdit;
	private GestureDetector mggd;
	private ImageButton search;
	private boolean isOutter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DyUtil.PATH = getFilesDir() + "/";

		setAbContentView(R.layout.activity_lanuny);

		searchLayout = (LinearLayout) this.findViewById(R.id.searchContent1);

		showKeywords = (KeywordsView) this.findViewById(R.id.word);
		search = (ImageButton) this.findViewById(R.id.imageButton1);
		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String key = keywordEdit.getText().toString();

				if (!AbStrUtil.isEmpty(key)) {
					key = key.trim();
					// if (key.equals("最新电影")) {
					// Intent intent = new
					// Intent(LanunyActivity.this,NewMovieActivity.class);
					// startActivity(intent);
					// }
					// else
					if (key.startsWith("thunder://")
							|| key.startsWith("ftp://")
							|| key.startsWith("http://")) {
						DyUtil.urlToPlay = key;
						Intent intent = new Intent();
						intent.setClass(LanunyActivity.this,
								PlayUrlActivity.class);
						startActivity(intent);
					} else {
						Bundle bundle = new Bundle();
						bundle.putString("key", key);
						Intent intent = new Intent();
						intent.setClass(LanunyActivity.this,
								SearchActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			}
		});
		keywordEdit = (EditText) this.findViewById(R.id.search_Keywords);
		// keywordEdit.setText("最新电影");
		showKeywords.setDuration(6000);
		showKeywords.setOnClickListener(this);
		this.mggd = new GestureDetector(new Mygdlinseter());
		showKeywords.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mggd.onTouchEvent(event); // 注册点击事件
			}
		});
		isOutter = true;

		((ImageButton) this.findViewById(R.id.homeButton))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Intent intent = new
						// Intent(LanunyActivity.this,HomeActivity.class);
						// startActivity(intent);
						finish();
					}
				});
		;

		handler.sendEmptyMessage(Msg_Start_Load);
	}

	private String[] getRandomArray() {
		if (totalKeys != null && totalKeys.length > 0) {
			String[] keys = new String[15];
			List<String> ks = new ArrayList<String>();
			for (int i = 0; i < totalKeys.length; i++) {
				ks.add(totalKeys[i]);
			}
			for (int i = 0; i < keys.length; i++) {
				int k = (int) (ks.size() * Math.random());
				keys[i] = ks.remove(k);
				// if(keys[i] == null) System.out.println("nulnulnulnulnul");
			}
			// System.out.println("result's length = "+keys.length);
			return keys;
		}
		return new String[] { "QQ", "单机", "联网", "游戏", "美女", "冒险", "uc", "安卓",
				"app", "谷歌", "多多米", "财迷", "快播", "YY", "MSN" };
	}

	private static final int Msg_Start_Load = 0x0102;
	private static final int Msg_Load_End = 0x0203;

	private LoadKeywordsTask task = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Msg_Start_Load:

				task = new LoadKeywordsTask();
				new Thread(task).start();

				break;
			case Msg_Load_End:
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				handler.sendEmptyMessageDelayed(0, 5000);
				break;

			case 0:
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);

				handler.sendEmptyMessageDelayed(0, 5000);
				break;
			}

		}
	};

	public static void readkeys() {
		String PATH = DyUtil.PATH + "/";
		totalKeys = null;
		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "keys.obj");
		if (file.exists()) {

			ObjectInputStream in;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				String[] tKeys = (String[]) in.readObject();

				in.close();
				// System.out.println(tKeys[0]+"    "+AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMD));
				if (!tKeys[0].equals(AbDateUtil
						.getCurrentDate(AbDateUtil.dateFormatYMD))) {
					load.start();
				} else {
					totalKeys = new String[tKeys.length - 1];
					for (int i = 0; i < totalKeys.length; i++) {
						totalKeys[i] = tKeys[i + 1];
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			load.start();
	}

	public static Thread load = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				ArrayList<String> keys = new ArrayList<String>();
				String[] urls = new String[] {
						"http://top.baidu.com/buzz?b=26&c=1"// 电影
						//, "http://top.baidu.com/buzz?b=4&c=2"// 电视剧
						//, "http://top.baidu.com/buzz?b=19&c=3"// 综艺
						//, "http://top.baidu.com/buzz?b=23&c=5"// 动漫卡通
				};
				for (String url : urls) {

					Document doc = Jsoup.connect(url).get();
					Elements as = doc.select("a[class=list-title]");
					for (Element ele : as) {
						keys.add(ele.text());
					}
				}
				totalKeys = new String[keys.size()];
				String[] tKeys = new String[keys.size() + 1];
				tKeys[0] = AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMD);
				for (int i = 0; i < totalKeys.length; i++) {
					totalKeys[i] = keys.get(i);
					tKeys[i + 1] = keys.get(i);
				}

				String PATH = DyUtil.PATH + "/";

				File dir = new File(PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File file = new File(dir, "keys.obj");
				file.createNewFile();
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(file));
				out.writeObject(tKeys);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});

	private class LoadKeywordsTask implements Runnable {
		@Override
		public void run() {
			try {
				if (totalKeys == null)
					readkeys();
				if (totalKeys == null)
					totalKeys = new String[] { "露水红颜", "心花路放", "一个人的武林", "游戏",
							"美女", "冒险", "uc", "安卓", "app", "谷歌", "多多米", "财迷",
							"快播", "YY", "MSN", };
				key_words = getRandomArray();
				if (key_words.length > 0)
					handler.sendEmptyMessage(Msg_Load_End);

			} catch (Exception e) {
			}
		}
	}

	private void feedKeywordsFlow(KeywordsView keyworldFlow, String[] arr) {
		for (int i = 0; i < KeywordsView.MAX; i++) {
			String tmp = arr[i];
			keyworldFlow.feedKeyword(tmp);
		}
	}

	@Override
	protected void onPause() {
		handler.removeMessages(0);
		super.onPause();
	}

	@Override
	protected void onResume() {
		handler.removeMessages(0);
		handler.sendEmptyMessageDelayed(0, 5000);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		handler.removeMessages(0);
		super.onDestroy();
	}

	class Mygdlinseter implements OnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e2.getX() - e1.getX() > 100) { // 右滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
				return true;
			}
			if (e2.getX() - e1.getX() < -100) {// 左滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				return true;
			}
			if (e2.getY() - e1.getY() < -100) {// 上滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				return true;
			}
			if (e2.getY() - e1.getY() > 100) {// 下滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
				return true;
			}
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		// System.out.println("V"+v);
		// TODO Auto-generated method stub
		if (isOutter) {
			// isOutter = false;

			String kw = ((TextView) v).getText().toString();
			System.out.println("V" + kw);
			if (!kw.trim().equals("")) {
				// searchLayout.removeAllViews();
				// handler.sendEmptyMessage(Msg_Start_Load);
				keywordEdit.setText(kw);
			}
			// Toast.makeText(this, "选中的内容是：" + ((TextView)
			// v).getText().toString(), 1).show();
		}

	}

	/**
	 * 处理返回按键事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			if (!isOutter) {
				isOutter = true;
				searchLayout.removeAllViews();
				searchLayout.addView(showKeywords);
				/**
				 * 将自身设为不可动作
				 */

				/**
				 * 将搜索栏清空
				 */

			} else {
				this.finish();
				/**
				 * 执行返回按键操作
				 */

			}

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
