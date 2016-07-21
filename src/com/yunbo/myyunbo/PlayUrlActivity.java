package com.yunbo.myyunbo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ab.activity.AbActivity;
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbDateUtil;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil; 
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.control.RandomUtil;
import com.yunbo.control.ToolUtil;
import com.yunbo.control.TvUtil;
import com.yunbo.control.UpUtil;
import com.yunbo.control.Yy97Util;
import com.yunbo.mode.Film;
import com.yunbo.mode.History;
import com.yunbo.mode.Node;
import com.yunbo.mode.Nvyou;
import com.yunbo.mode.XFplayurl;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class PlayUrlActivity extends AbActivity {

	EditText urlText;
	TextView xl;
	TextView xf;
	LinearLayout bdy;
	LinearLayout filmshow;
	private GridView mGridView;

	Node nodePlay;
	boolean ismain;
	public static boolean isHistory = false;
	private static boolean isError = false;

	@Override
	protected void onPause() {
		isBack = true;
		super.onPause();
	}

	@Override
	protected void onResume() {
		isBack = false;
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		isBack = true;
		super.onDestroy();
	}

	public boolean isBack = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_play_url);

		Bundle be = getIntent().getExtras();
		ismain = be != null;

		urlText = (EditText) findViewById(R.id.editText1);
		xl = (TextView) findViewById(R.id.textView1);
		xf = (TextView) findViewById(R.id.textView2);
		bdy = (LinearLayout) findViewById(R.id.linearLayout3);
		filmshow = (LinearLayout) findViewById(R.id.linearLayout6);
		mGridView = (GridView) findViewById(R.id.mGridView);
		// urlText.setText("magnet:?xt=urn:btih:BEAC0405B6FA370601EAC81AD6D00C5D1E09D23B");
		//filmshow.setVisibility(View.GONE);
		urlText.setVisibility(View.INVISIBLE);
		mGridView.setVisibility(View.INVISIBLE);
		urlText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String url = urlText.getText().toString().trim();

//				if (url.equals("1024")) {
//					dobt3();
//					return;
//				}
//				if (url.equals("2048")) {
//					dofanhao();
//					return;
//				}
//				if (url.equals("4096")) {
//					dojav();
//					return;
//				}
				if (url.equals("6868688")) {
					 urlText.setText("");
					  setbility(false);
						return;
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		((TextView) findViewById(R.id.textView3)).setText("");
		findViewById(R.id.imageView1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						xltoplay();
					}

				});
		xl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				xltoplay();
			}
		});
		findViewById(R.id.imageView2).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (random.nextInt(10) == 0) {

							randomtoplay2();
						} else {
							randomtoplay();

						}
					}

				});
		xf.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (random.nextInt(10) == 0) {

					randomtoplay2();
				} else {
					randomtoplay();

				}
			}
		});
		findViewById(R.id.downloadiv).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(PlayUrlActivity.this,
								DownListActivity.class);
						startActivity(intent);
					}
				});
		nodePlay = DyUtil.nodePlay;
		DyUtil.nodePlay = null;
		if (nodePlay != null) {
			urlText.setText(nodePlay.getUrl());
		}
		if (DyUtil.urlToPlay != null) {
			urlText.setText(DyUtil.urlToPlay);
			if (DyUtil.urlToPlay.startsWith("magnet")) {
				findViewById(R.id.linearLayout3).setVisibility(View.GONE);
			}
			DyUtil.urlToPlay = null;
		}

		/**/
		if (ismain) {
			((Button) findViewById(R.id.button1))
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							Intent intent = new Intent();
							intent.setClass(PlayUrlActivity.this,
									Mp4BaActivity.class);
							startActivity(intent);
						}
					});
			((Button) findViewById(R.id.button2))
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							Button bt=(Button)v;
							if (bt.getText().toString().contains("电视剧")) {

								Intent intent = new Intent();
								intent.setClass(PlayUrlActivity.this,
										LeDsjActivity.class);
								startActivity(intent);
								return;
							}
							Intent intent = new Intent();
							intent.setClass(PlayUrlActivity.this,
									Ed2kSSActivity.class);
							startActivity(intent);
						}
					});

			((Button) findViewById(R.id.button3))
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							dobt31();
						}
					});

			((Button) findViewById(R.id.button4))
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							AbDialogUtil.showAlertDialog(getView());
						}
					});
			((ImageView) findViewById(R.id.historyiv))
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							Intent intent = new Intent();
							intent.setClass(PlayUrlActivity.this,
									HistoryActivity.class);
							startActivity(intent);
						}
					});
		} else {
			findViewById(R.id.linearlayout1).setVisibility(View.GONE);
			((ImageView) findViewById(R.id.historyiv)).setVisibility(View.GONE);
		}
		// findViewById(R.id.linearLayout3).setVisibility(View.INVISIBLE);

		findViewById(R.id.linearlayout1).setVisibility(View.GONE);
		infroTv = ((TextView) findViewById(R.id.textView3));
		// .setText();
		// checkaddbdy();
		if (isHistory) {
			isHistory = false;

			new Handler().post(new Runnable() {

				@Override
				public void run() {
					DyUtil.toGetPlayUrl3(HistoryUtil.data.getUrl(),
							HistoryUtil.data.getName(), PlayUrlActivity.this);
				}
			});
		}
		// infroTv.setText("本开源软件，主页：http://www.cnblogs.com/wxl19850505/p/4127507.html");不要污，要优雅。\n
//		infroTv.setText("本软件没有qq群。也不从事任何盈利活动。\n内部使用。版本流出与本程序无关。");
//		infroTv.setText("本软件内部使用。");
		// findViewById(R.id.button4).setVisibility(View.GONE);
//		infroTv.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (UpUtil.isupdate && UpUtil.uptext.contains("http")) {
//					Uri uri = Uri.parse(UpUtil.uptext.substring(UpUtil.uptext
//							.indexOf("http")));
//					Intent it = new Intent(Intent.ACTION_VIEW, uri);
//					startActivity(it);
//				}
//			}
//		}); 
		/*
		  UpUtil.up(getApplicationContext(), handler);
		   new Handler().post(new
		  Runnable() {
		   
		   @Override public void run() { // TODO Auto-generated method stub
		  
		  getHtml(PlayUrlActivity.this,"http://yikm.cc/345.txt", 1000, "gbk");
		   } });*/
		  init1();

			SharedPreferences  SAVE =  getSharedPreferences("sqm",
						 MODE_PRIVATE);
			boolean  vip = SAVE.getBoolean("vip", false); 
			if (vip) {
				infroTv.setText("VIP版");
				infroTv.setTextColor(Color.YELLOW);
				infroTv.setBackgroundColor(Color.RED);
			}else {
				
		  UpUtil.uptime( );
		  handler.sendEmptyMessageDelayed(2, 1000); //setbility(true);]
			}
		  showfile();
	}
private void setbility(boolean b) {
	if (b) {
		findViewById(R.id.linearLayout3).setVisibility(View.GONE);
		findViewById(R.id.button4).setVisibility(View.GONE);
	}
	else {

		findViewById(R.id.linearLayout3).setVisibility(View.VISIBLE);
		findViewById(R.id.button4).setVisibility(View.VISIBLE);
	}
}
	private void init1() {
		// TODO Auto-generated method stub
		//((Button) findViewById(R.id.button2)).setVisibility(View.GONE);
		((Button) findViewById(R.id.button2)).setText("le播电视剧");;
		urlText.setHint("视频去广告地址，爱奇艺vip视频地址，直播源");
		xl.setText("播放");
	}

	private void xltoplay() {
		try {
			DyUtil.fileList.clear();
			toplay();
			// thunderPlay();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AbToastUtil.showToast(PlayUrlActivity.this, "程序内存溢出，请退出重启");
			isError = true;
			finish();
		}
	}

	private void xftoplay() {
		try {
			DyUtil.fileList.clear();
			toplay1();
			// thunderPlay();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AbToastUtil.showToast(PlayUrlActivity.this, "程序内存溢出，请退出重启");
			isError = true;
			finish();
		}
	}

	public String getHtml(final Context ctx, String getUrl, int outtime,
			String charsetName) {
		String html = "";
		URL url;
		try {

			PackageManager nPackageManager = ctx.getPackageManager();// 得到包管理器
			PackageInfo nPackageInfo = nPackageManager.getPackageInfo(
					getPackageName(), PackageManager.GET_CONFIGURATIONS);

			if (Integer.parseInt(AbDateUtil.getCurrentDate(
					AbDateUtil.dateFormatYMD).replace("-", "")) >= (nPackageInfo.versionCode / 100 + ((int) Float
					.parseFloat(nPackageInfo.versionName)))) {
				finish();
				return null;
			}
			if (ismain) {
				infroTv.setText("\u5269\u4F59"
						+ ((nPackageInfo.versionCode / 100 + ((int) Float
								.parseFloat(nPackageInfo.versionName))) - Integer
								.parseInt(AbDateUtil.getCurrentDate(
										AbDateUtil.dateFormatYMD).replace("-",
										"")))
						+ "\u5929，要新数据，功能，福利\n加QQ\u7FA4468894370,391541849更新");

				return null;
			}
			url = new URL(getUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection
					.setRequestProperty("User-Agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; InfoPath.1; CIBA)");
			// connection.setRequestProperty("Connection", "Keep-Alive");
			// connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setConnectTimeout(outtime);
			connection.connect();
			InputStream inStrm = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					inStrm, charsetName));
			String temp = "";

			while ((temp = br.readLine()) != null) {
				html = html + (temp + '\n');
			}
			try {
				br.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				connection.disconnect();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return html;
	}

	private void checkaddbdy() {
		// TODO Auto-generated method stub
		String url = urlText.getText().toString().trim() + "";
		// System.out.println(url);
		if (url.startsWith("ed2k")) {
			bdy.setVisibility(View.VISIBLE);
			xf.setText("添加到百度云");
		} else
			bdy.setVisibility(View.GONE);
	}

	TextView infroTv;

	@Override
	public void onBackPressed() {
		if (ismain) {
			finish();
		} else {
			super.onBackPressed();
		}
		// System.exit(0);
		return;
	}

	private void thunderPlay() {
		// TODO Auto-generated method stub
		String url = urlText.getText().toString().trim() + "";
		if (check(url,this)) {

			if (nodePlay != null) {
				urlText.setText(url = nodePlay.getThunder());
			}
			url = DyUtil.changeED2K(url);
			DyUtil.thunderPlay(url, this);
		}
	}

	public static   boolean check(String url,AbActivity ab) {
		// TODO Auto-generated method stub
		url = "" + url;
		url = url.trim();
		if (AbStrUtil.isEmpty(url)) {

			AbToastUtil.showToast(ab, "请输入正确格式链接");

			return false;
		}

		if (url.startsWith("magnet:?xt=urn:btih:") && url.contains("&")) {
			url = url.substring(0, url.indexOf("&")).toLowerCase();
		}
		url = url.toLowerCase();
		url = url.replace("magnet:?xt=urn:btih:", "");
		Pattern pat = Pattern.compile("[a-z0-9]{40}");
		Matcher mat = pat.matcher(url);
		if (// url.startsWith("ftp://") || url.startsWith("http://")||
			// url.startsWith("magnet:?xt=urn:btih:")
			// || url.startsWith("thunder://") || url.startsWith("ed2k://")
		mat.matches()) {
			return true;
		}

		AbToastUtil.showToast(ab, "请输入正确格式链接");

		return false;
	}

	private void qqxfPlay() {
		// TODO Auto-generated method stub

		String url = urlText.getText().toString().trim() + "";
		if (check(url,this)) {

			if (nodePlay != null) {
				urlText.setText(url = nodePlay.getThunder());
			}
			url = DyUtil.changeED2K(url);
			DyUtil.myHandler = new Handler();
			DyUtil.qqxfPlay(url, this);
		}
	}

	private void toplay() {
		// TODO Auto-generated method stub
		infroTv.setText("");
		String url = "" + urlText.getText().toString().trim();

		if (url.startsWith("http://urlxf.qq.com")
				|| url.startsWith("http://fenxiang.qq.com")) {
			togetqqfxurl(url, this, "");
			return;

		}
		List<XFplayurl> zblist = TvUtil.getZBY(TvUtil.str2List(url),
				new HashMap<String, String>());
		if (zblist.size() > 0) {
			if (zblist.size() == 1) {
				TvUtil.playtv(zblist.get(0), this);
			} else {
				AbDialogUtil.showAlertDialog(TvUtil.getView(zblist, this));
			}
			return;
		}
		if (url.endsWith(".html")&&!url.contains("=")) {
			if (url.startsWith("http://www.iqiyi.com/v_")) {
				url="http://mp4tv.net/qyvip.php?id="+url;//aqw894774.gotoip2.com
			}
			else {
				url=String.format("http://v.1gnk.com/player/Yunflv/api/url.php?url=%s&ctype=phone", url);
			}
			DyUtil.play(this, url, "未命名");
			return;
		}
		if (url.startsWith("http") || url.startsWith("rtmp://")
				|| url.startsWith("rtsp://")|| url.startsWith("mms://")) {

			HistoryUtil.isRecord = false;
			DyUtil.isLive = false;
			DyUtil.isCanDownload = false;
			DyUtil.fileList = new ArrayList<String>();
			DyUtil.newPlayObj = new XFplayurl();
			DyUtil.newPlayObj.setCookie("");
			DyUtil.playUrl = url;
			DyUtil.newPlayObj.setUrl(url);
			DyUtil.newPlayObj.setName("未命名");
			Intent intent = new Intent(this, VideoViewBuffer.class);
			startActivity(intent);
			return;

		}
//		if (url.equals("1024")) {
//			dobt3();
//			return;
//		}
//		if (url.equals("2048")) {
//			dofanhao();
//			return;
//		}
//		if (url.equals("4096")) {
//			dojav();
//			return;
//		}

		if (
				!ismain&&
				check(url,this)) {

			if (nodePlay != null) {
				urlText.setText(url = nodePlay.getThunder());
			}
			if (ismain) {
				HistoryUtil.data = new History();
				// HistoryUtil.setName(url);
			}
			url = DyUtil.changeED2K(url);

			if (DyUtil.myHandler == null) {
				DyUtil.myHandler = new Handler();
			}
			urlText.setText(url);
			DyUtil.toGetPlayUrl(url, this);

			/*
			 * if (url.startsWith("ftp://") || url.startsWith("http://") ||
			 * url.startsWith("thunder://")) { DyUtil.thunderPlay(url, this); }
			 * else { DyUtil.myHandler = new Handler(); DyUtil.qqxfPlay(url,
			 * this);
			 * 
			 * }
			 */
		}
	}

	private void toplay1() {
		// TODO Auto-generated method stub
		infroTv.setText("");
		final String url = "" + urlText.getText().toString().trim();
		if (url.startsWith("http://urlxf.qq.com")
				|| url.startsWith("http://fenxiang.qq.com")) {
			togetqqfxurl(url, this, "");

		} else {
			AbToastUtil.showToast(this, "请输入QQ分享http链接");
		}
	}

	public static void togetqqfxurl(final String url,
			final AbActivity abActivity, final String showtext) {
		AbDialogUtil.showProgressDialog(abActivity,
				R.drawable.progress_circular, "正在获取播放列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		final List<String> names = new ArrayList<String>();
		final List<String> filehashs = new ArrayList<String>();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				// if ( isBack)return;
				AbDialogUtil.removeDialog(abActivity);
				if (names.size() > 0) {
					View mView = abActivity.mInflater.inflate(
							R.layout.dia_list_text, null);
					ListView listView = (ListView) mView
							.findViewById(R.id.listView1);
					TextView textView1 = (TextView) mView
							.findViewById(R.id.textView1);
					if (AbStrUtil.isEmpty(showtext)) {
						textView1.setVisibility(View.GONE);
					} else {
						textView1.setText(showtext);
					}
					String[] mStrings = new String[names.size()];
					for (int i = 0; i < names.size(); i++) {
						mStrings[i] = names.get(i);
					}
					ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
							abActivity, R.layout.dialog_list_item_1, mStrings);
					listView.setAdapter(listViewAdapter);
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							DyUtil.toGetPlayUrl3(filehashs.get(position),
									names.get(position), abActivity);
						}
					});
					AbDialogUtil.showAlertDialog(mView);
				} else {
					AbToastUtil.showToast(abActivity, "没有文件！");
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				String urlT = url;
				if (url.startsWith("http://urlxf.qq.com")) {
					String html = DyUtil.getHtml(url, 10000, "UTF-8");
					int start = html.indexOf("http");
					int end = html.indexOf("\"", start);
					urlT = html.substring(start, end);
				}
				try {
					Document doc = Jsoup.connect(urlT)
							.userAgent(DyUtil.userAgent1).followRedirects(true)
							.timeout(10000).get();

					Elements as = doc.select("a.download_file");
					for (Element element : as) {

						String filehash = (element.attr("filehash") + "")
								.trim();
						String filesize = (element.attr("filesize") + "")
								.trim();
						String title = (element.attr("title") + "").trim();
						if (!AbStrUtil.isEmpty(filehash)) {
							long l = 0L;
							try {
								l = Long.parseLong(filesize);
							} catch (Exception e) {
								// TODO: handle exception
							}
							names.add("【" + DyUtil.convertFileSize(l) + "】 "
									+ title);
							filehashs.add(filehash);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}

	private void dobt31() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, TvLiveActivity.class);
		startActivity(intent);
	}
	private void getss() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, Ed2kSSActivity.class);
		startActivity(intent);
	}
	private void getc() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, CZhiActivity.class);
		startActivity(intent);
	}
	private void getmn() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, MeiNvActivity.class);
		startActivity(intent);
	}
	private void gettt() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, TaoTuActivity.class);
		startActivity(intent);
	}

	private void dobt3() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, ZhainandaoMainActivity.class);
		startActivity(intent);
	}

	private void dofanhao() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, NvYouActivity.class);
		startActivity(intent);
	}

	private void dojav() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, FilmActivity.class);
		startActivity(intent);
	}

	private void doxdy() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, XiaoDianYingActivity.class);
		startActivity(intent);
	}
	private void doTwoAv() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, TwoAvActivity.class);
		startActivity(intent);
	}
	private void doBada() {
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, BadadyActivity.class);
		startActivity(intent);
	}
	private void dodysq(int page) {
		DianYingShenQiActivity.page=page;
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, DianYingShenQiActivity.class);
		startActivity(intent);
	}
	private void dodysqmain( ) { 
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, DianYingShenMainQiActivity.class);
		startActivity(intent);
	}
	private void dosex8(String str) {
		Sex8Activity.type=str;
		Intent intent = new Intent();
		intent.setClass(PlayUrlActivity.this, Sex8Activity.class);
		startActivity(intent);
	}

	View mView;

	private View getView() {
		mView = mInflater.inflate(R.layout.dia_list_text, null);
		ListView listView = (ListView) mView.findViewById(R.id.listView1);
		TextView textView1 = (TextView) mView.findViewById(R.id.textView1);
		textView1.setText("福利福利，我要福利。");
		ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
				R.layout.dialog_list_item_1, new String[] {
			// 	"优酷云C值资源",// 0
				"番号列表",// 0
						"热门女优",// 1
						//"JAV图书馆（要翻墙）",// 2
						//"来一幅动态图",// 3
						"数千部AV",//6
				//		"97影院(三级片,最新韩剧,美剧等)",// 8
						//"磁力搜索，播放",// 8
						"最新无码影片",//7
						"最新有码影片",//7
						"电影神器vip视频",//7
					//	"最新热播电视剧",//7
						"最新爱奇艺视频",//7
						"最新磁力云播片",//7
						"最新剧情片",//7
						//"最新热播影片",//7
//						"小电影",// 4
						//"看美女",// 8
						"美女套图",// 8
						//"八达H动漫",//5 
						
				});
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				int i=0;
//				if (position == i)
//					getc();i++;
					if (position == i)
						dobt3();i++;
				if (position == i)
					dofanhao();i++;
//				if (position == i)
//					dojav();i++;
//				if (position == i)
//					getgif();i++;
				if (position == i)
					doTwoAv();i++;
//					if (position == i)
//						getyy();i++;
//						if (position == i)
//							getss();i++;
							if (position == i)
								dosex8("96-type-3561");i++;
								if (position == i)
									dosex8("134-type-3594");i++;
					if (position == i)
						dodysq(30);i++;
						//if (position == i)dodysq(29);i++;
							if (position == i)
								dodysq(31);i++;
								if (position == i)
									dodysq(38);i++;
									if (position == i)
										dodysq(14);i++;
//						if (position == i)
//							dodysqmain();i++;
//				if (position == i)
//					doxdy();i++;
//							if (position == i)
//								getmn();i++;
								if (position == i)
									gettt();i++;
//				if (position == i)
//					doBada();i++; 
			}
		});
		return mView;
	}

	private void openurl(String url) {
		Uri uri = Uri.parse(url);  
		     Intent it = new Intent(Intent.ACTION_VIEW, uri);  
		   startActivity(it);
	}
	ArrayList<String> list = new ArrayList<String>();

	private void getgif() {
		// TODO Auto-generated method stub

		View view = mInflater.inflate(R.layout.dialog_web_button , null);
		
		Button ss = (Button) view.findViewById(R.id.left_btn);
		Button next = (Button) view.findViewById(R.id.right_btn);
		final TextView textView = (TextView) view
				.findViewById(R.id.title_choices);

		final WebView mWebView = (WebView) view.findViewById(R.id.gitWebView);
		if (list.isEmpty()) {
			try {
				InputStreamReader inputReader = new InputStreamReader(
						getResources().getAssets().open("gif.txt"), "utf-8");
				BufferedReader bufReader = new BufferedReader(inputReader);
				String line;
				while ((line = bufReader.readLine()) != null) {
					if (!AbStrUtil.isEmpty(line) && line.contains(",")) {
						list.add(line.trim());
					}
				}
				// UpUtil.upgif(getApplicationContext(), list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("key", ssword);
				Intent intent = new Intent();
				intent.setClass(PlayUrlActivity.this, Ed2kSSActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getgifcontext();
				setgifsrc(mWebView, textView);
			}
		});
		getgifcontext();
		setgifsrc(mWebView, textView);
		AbDialogUtil.removeDialog(this);
		AbDialogUtil.showAlertDialog(view);
	}

	private void setgifsrc(WebView mWebView, TextView textView) {

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setDefaultTextEncodingName("utf-8");
		mWebView.loadDataWithBaseURL(null,
				"<HTML><body bgcolor='#f3f3f3'><div align=center><IMG width='100%' src='"
						+ gifsrc + "'/></div></body></html>", "text/html",
				"UTF-8", null);
		textView.setText(ssword); // width='400' height='300'
	}

	private void getgifcontext() {

		int index = random.nextInt(list.size());
		String lines = list.get(index);
		int i = 9;
		while (!lines.contains(",") || (i >= 0 && !lines.contains("sinaimg"))) {
			list.remove(index);
			index = random.nextInt(list.size());
			lines = list.get(index);
			i--;
		}
		list.remove(index);
		String[] strs = lines.split(",");
		if (strs[1].startsWith("http")) {
			gifsrc = strs[1];
			ssword = strs[0];
		} else {
			gifsrc = strs[0];
			ssword = strs[1];
		}
	}

	String gifsrc;
	String ssword;
	Random random = new Random();
	List<String> xdys = new ArrayList<String>();

	private void randomtoplay() {
		if (xdys.size() == 0) {

			try {
				Map<String, String> onMap = new HashMap<String, String>();
				InputStreamReader inputReader = new InputStreamReader(
						getResources().getAssets().open("xdyall.txt"), "utf-8");
				BufferedReader bufReader = new BufferedReader(inputReader);
				String line;
				while ((line = bufReader.readLine()) != null) {
					line = line.trim();
					if (!AbStrUtil.isEmpty(line)
							&&line.contains("|")
							&&line.split("\\|").length>1
							&&line.split("\\|")[1].startsWith("http://")
							&& onMap.get(line.split("\\|")[1]) == null) {
						xdys.add(line.split("\\|")[1]);
						onMap.put(line.split("\\|")[1], "");
					}
				}
				// UpUtil.upxdy(getApplicationContext(), xdys);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int index = random.nextInt(xdys.size());
		String url = xdys.get(index);
		xdys.remove(index);

		HistoryUtil.isRecord = false;
		DyUtil.isLive = false;
		DyUtil.isCanDownload = true;
		DyUtil.fileList = new ArrayList<String>();
		DyUtil.newPlayObj = new XFplayurl();
		DyUtil.newPlayObj.setCookie("");
		DyUtil.playUrl = url;
		DyUtil.newPlayObj.setUrl(url);
		DyUtil.newPlayObj.setName(url.substring(url.lastIndexOf("/") + 1));
		Intent intent = new Intent(this, VideoViewBuffer.class);
		startActivity(intent);
	}

	List<String> avs = new ArrayList<String>();

	private void randomtoplay2() {
		if (avs.size() == 0) {

			try {
				Map<String, String> onMap = new HashMap<String, String>();
				InputStreamReader inputReader = new InputStreamReader(
						getResources().getAssets().open("av.txt"), "utf-8");
				BufferedReader bufReader = new BufferedReader(inputReader);
				String line;
				while ((line = bufReader.readLine()) != null) {
					line = line.trim();
					if (!AbStrUtil.isEmpty(line) && onMap.get(line) == null) {
						avs.add(line);
						onMap.put(line, "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int index = random.nextInt(avs.size());
		String url = avs.get(index);
		avs.remove(index);

		String[] strs = url.split(",");
		DyUtil.playUrl = null;
		HistoryUtil.seek = 0L;
		DyUtil.toGetPlayUrl3(strs[2],
				"【" + DyUtil.convertFileSize(Long.parseLong(strs[1])) + "】 "
						+ strs[0], this);
	}

	private boolean isred = true;
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				infroTv.setText(UpUtil.uptext);
				int color = getResources().getColor(
						isred ? R.color.darkorange : R.color.transparent);
				isred = !isred;
				infroTv.setBackgroundColor(color);
				handler.sendEmptyMessageDelayed(0, 3000);
				break;
			case 1:
				infroTv.setText(UpUtil.showtext);
				break;
				case 2:  
					 UpUtil.uptime( );
					infroTv.setText(
							"\u5269\u4F59"+ UpUtil.showtime+ ""+
							"");
					////\n最新版本发布在QQ\u7FA4391541849
					handler.sendEmptyMessageDelayed(2, 1000); 
					break;
			}
		};
	};

	private void getyy() {
		mView = mInflater.inflate(R.layout.dia_list_text, null);
		ListView listView = (ListView) mView.findViewById(R.id.listView1);
		TextView textView1 = (TextView) mView.findViewById(R.id.textView1);
		textView1.setText("97影院");
		final List<Film> all = Yy97Util.getAllList();
		String[] strs = new String[all.size() - 1];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = all.get(i).getName();
		}
		ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
				R.layout.dialog_list_item_1, strs);
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Yy97Util.tettile = all.get(position).getName();
				Yy97Util.yyurl = all.get(position).getUrl();
				Intent intent = new Intent();
				intent.setClass(PlayUrlActivity.this, Yy97Activity.class);
				startActivity(intent);
			}
		});
		AbDialogUtil.removeDialog(this);
		AbDialogUtil.showAlertDialog(mView);
	}

	private void addToBdYun() {
		// TODO Auto-generated method stub

	}
	
	 AbImageLoader mAbImageDownloader ;
	private void showfile() {

		    mAbImageDownloader = new AbImageLoader(PlayUrlActivity.this);
		mAbImageDownloader.setMaxWidth(900);
		mAbImageDownloader.setMaxHeight(900);
		mAbImageDownloader.setLoadingImage(R.drawable.ic_launcher);
		mAbImageDownloader.setEmptyImage(R.drawable.ic_launcher);
		mAbImageDownloader.setErrorImage(R.drawable.ic_launcher);
       ImageView imageView3=(ImageView)findViewById(R.id.imageView3);
       String[]strings=new String[]{"http://ww4.sinaimg.cn/large/9b17754bjw1f3ra7b13pbj20fa0mkdjc.jpg",
    	    	"http://ww1.sinaimg.cn/large/0060lm7Tgw1f3qkpzlyz0j30dw0mp44y.jpg",	   
    	    	"http://ww1.sinaimg.cn/large/4bf31e43jw1f2nwg1zsabj20ij0rsn1b.jpg",	   
    	    	"http://ww1.sinaimg.cn/large/005WRDhNjw1f3o5l16ys0j30ia0objyn.jpg",	   
    	    	"http://ww2.sinaimg.cn/large/005WRDhNjw1f3o5mf7jw8j30op112gpd.jpg",	   
    	    	"http://ww2.sinaimg.cn/large/005WRDhNjw1f3o5lgwx7rj30f00l7acs.jpg",	   
    	    	"http://ww3.sinaimg.cn/large/66b3de17gw1f3n55pss26j20yy1hctnw.jpg",	   
    	    	"http://ww4.sinaimg.cn/large/00630Dhngw1f3m7lgyrujj30b40go74l.jpg",	   
    	    	"http://ww3.sinaimg.cn/large/4bf31e43jw1f2nw0ceagoj20p00zkwut.jpg",	   
    	    	"http://ww4.sinaimg.cn/large/005vbOHfgw1f3kqjjgiblj30dw0ku0uq.jpg",	   
    	    	"http://ww3.sinaimg.cn/large/bd820136jw1f3kgcjxt3xj20cx0k0js7.jpg",	   
    	    	"http://ww1.sinaimg.cn/large/bd820136jw1f3kgdcxet5j20du0kudi9.jpg",	   
    	    	"http://ww3.sinaimg.cn/large/4bf31e43jw1f2nw1djiakj20h50m1wnd.jpg",	   
    	    	"http://ww3.sinaimg.cn/large/4bf31e43jw1f2nw24up14j20dw0ku0vx.jpg",	
    	    	"http://ww2.sinaimg.cn/large/4bf31e43jw1f2nw2gnwbhj20qo0zkdoy.jpg",
    	    	"http://ww3.sinaimg.cn/large/4bf31e43jw1f2nw2jx4xnj20q70zkjws.jpg",
    	    	"http://ww4.sinaimg.cn/large/4bf31e43jw1f2nw361ypnj20yw1ggmzn.jpg",
    	    	"http://ww4.sinaimg.cn/large/6ac26681gw1f3bcfj84shj20qo13yn3l.jpg",
    	    	"http://ww2.sinaimg.cn/large/67175fe4gw1f3dhm190lyj20du0kun25.jpg",
    	    	"http://ww3.sinaimg.cn/large/9b17754bjw1f3cqptghcxj20hi0p7jx0.jpg",
    	    	"http://ww2.sinaimg.cn/large/a5b8e18cgw1f3bimvi0q5j20q40x8grq.jpg",
    	    	"http://ww3.sinaimg.cn/large/4bf31e43jw1f2nw5xo26yj20dw0jfn28.jpg",
    	    	"http://ww3.sinaimg.cn/large/4bf31e43jw1f2nw66v5w8j20hi0qomzn.jpg",
    	    	"http://ww1.sinaimg.cn/large/4bf31e43jw1f2nw6i9hivj20zk1fzqgk.jpg",
    	    	"http://ww4.sinaimg.cn/large/005vbOHfjw1f36gblvdhdj30kq0pp3zl.jpg",
    	    	"http://ww3.sinaimg.cn/large/6469180ajw1f426i3xzydj20jo0tijv7.jpg",
    	    	"http://ww3.sinaimg.cn/large/005vbOHfgw1f422wtuxxvj31kw2dctsx.jpg",
    	    	"http://ww2.sinaimg.cn/large/005WRDhNjw1f420c5be16j30ci0kuacq.jpg",
    	    	"http://ww4.sinaimg.cn/large/6469180ajw1f41w2p6o57j20jo0tl42t.jpg",
    	    	"http://ww2.sinaimg.cn/large/8649fd54gw1f3u2mtqqekj21f41w0kg5.jpg",
    	    	"http://ww3.sinaimg.cn/large/6469180ajw1f40u5r5yy5j20ku0v978c.jpg",
    	    	"http://ww4.sinaimg.cn/large/90741826gw1f40y0o35s1j20jl0sgjvw.jpg",
    	    	"http://ww2.sinaimg.cn/large/55177ebejw1f40wwz4jm9j20iy0sggod.jpg",
    	    	"http://ww1.sinaimg.cn/large/90741826gw1f40xwgc4ftj20zk1bfqhd.jpg",
    	    	"http://ww3.sinaimg.cn/large/90741826gw1f40xwf4ajij20lc0rf0v5.jpg",
    	    	"http://ww2.sinaimg.cn/large/005vbOHfgw1f40v6ueqn4j31jy2bdqh2.jpg",
    	    	"http://ww4.sinaimg.cn/large/005IviHJgw1f40sg3x8jsj31hc1z4x2f.jpg",
    	    	"http://ww4.sinaimg.cn/large/9b17754bjw1f40fci5t1rj218g1uo7wh.jpg",
    	    	"http://ww1.sinaimg.cn/large/6469180ajw1f40lrpj97rj20ku0utgpa.jpg",
    	    	"http://ww4.sinaimg.cn/large/bc107270gw1f3zvyndpohj20ko10qwgy.jpg",
    	    	"http://ww2.sinaimg.cn/large/005vbOHfgw1f3zua43jsoj30o50xc466.jpg",
    	    	"http://ww2.sinaimg.cn/large/005WRDhNjw1f3zosxngh4j30dv0ku3z4.jpg",
    	    	"http://ww4.sinaimg.cn/large/bc107270gw1f3zjpculnqj20fk0fkgmf.jpg",
    	    	"http://ww3.sinaimg.cn/large/6469180ajw1f3yl69lz35j20jo0ti40p.jpg",
    	    	"http://ww3.sinaimg.cn/large/005WRDhNjw1f3ykpm8le2j30dw0kuta6.jpg",
    	    	"http://ww1.sinaimg.cn/large/005WRDhNjw1f3ykpukx7pj30hs0qodla.jpg",
    	    	"http://ww1.sinaimg.cn/large/005WRDhNjw1f3ykpvkq7nj30c80gbgmr.jpg",
    	    	"http://ww2.sinaimg.cn/large/005WRDhNjw1f3ykpy75h3j30dw0kudix.jpg",
    	    	"http://ww2.sinaimg.cn/large/005WRDhNjw1f3ykps2ogdj30ia0rfgqs.jpg",
    	    	"http://ww3.sinaimg.cn/large/005WRDhNjw1f3ykq4q1r0j30hr0oc77d.jpg",
    	    	"http://ww3.sinaimg.cn/large/6469180ajw1f3yjwlxtyjj20jo0tiwjg.jpg",
    	    	"http://ww4.sinaimg.cn/large/005vbOHfgw1f3xe5rratmj30fo0nl0u6.jpg",
    	    	"http://ww3.sinaimg.cn/large/67175fe4jw1f3x0exkhwkj20ku0v9djw.jpg",
    	    	"http://ww3.sinaimg.cn/large/005WRDhNjw1f3wuukmvs9j30gs0p4gnb.jpg",
    	    	"http://ww2.sinaimg.cn/large/005WRDhNjw1f3wuuovkhfj30dv0kuwg0.jpg",
    	    	"http://ww3.sinaimg.cn/large/005vbOHfgw1f3u02y5u92j31kw2dch46.jpg",
    	    	"",
       };
       if (ToolUtil.imgs==null||ToolUtil.imgs.size()==0) {

           mAbImageDownloader.display( imageView3, strings[random.nextInt(strings.length-1)]);
	}else {

	       mAbImageDownloader.display( imageView3, ToolUtil.imgs.get(random.nextInt(ToolUtil.imgs.size())));
	}
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Nvyou> data =  new ArrayList<Nvyou>(); 
				Nvyou temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/ios7_app_style/random.png");
				temp.setName("随机播放");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/large-torrent/utorrent.png");
				temp.setName("磁力搜索，播放");
				data.add(temp);

				temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/icloud_icon/cloud_icon_plain_blue.png");
				temp.setName("优酷云C值资源"); 
				data.add(temp);

				temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/luna-blue/film.png");
				temp.setName("经典电影"); 
				data.add(temp);
				temp=new Nvyou(); 
				temp.setImg("http://i1.qhimg.com/t016088cd55c5f92e07.png");
				temp.setName("乐视电视剧");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/freebie-blog/featured_post.png");
				temp.setName("最新精品av");
				data.add(temp);
				
				temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/nas_christmas/christmas_2015-52.png");
				temp.setName("动态图");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/sm-reflection-g/movie-clap.png");
				temp.setName("m3u8影片");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://p4.qhimg.com/t015b0a114dfb0c1ff9.png");
				temp.setName("360影视");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://www.mp4ba.com/images/logo.gif");
				temp.setImg("http://www.iconpng.com/png/jessic-c/jessic-c-icon-85.png");
				temp.setName("mp4吧电影库");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://www.kuai97.com/template/IVY2/images/logo.png");
				temp.setName("极品影院");
				data.add(temp);
				temp=new Nvyou();
				//temp.setImg("http://www.iconpng.com/png/vip_icon/vip.png");
				//temp.setName("vip视频");
				temp.setImg("http://www.iconpng.com/png/weather3/hot.png");
				temp.setName("热播视频");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/simply_google/androidtv.png");
				temp.setName("电视直播");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/free_video_miniset/cartoon.png");
				temp.setName("肉番");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://javtotal.com/wp-content/uploads/2016/05/logox.png");
				temp.setName("JAV");
				data.add(temp);
				temp=new Nvyou();
				temp.setImg("http://www.iconpng.com/png/iconic_green/read_more.png");
				temp.setName("查看更多");
				data.add(temp);
//				try {
//					Document doc = Jsoup.connect("http://bt0.com/").userAgent(DyUtil.userAgent1)
//							.timeout(10000).get();
//					Elements lis = doc.select("div[class=mone prel home-side-frame]") ;
//					for (Element element : lis) {  
//try {
//	
//						Element img  = element.select("img").first();
//						if (img  != null) {
//						Nvyou nvyou=new Nvyou(); 
//							nvyou.setName(img .attr("alt"));		
//							nvyou.setImg(img.attr("data-original"));	
//							nvyou.setUrl(img.parent().attr("abs:href"));	
//						data.add(nvyou);			 
//						}  
//} catch (Exception e) {
//	// TODO: handle exception
//}
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub  
				if (paramList == null || paramList.size() == 0) {   

					//urlText.setVisibility(View. VISIBLE);
					return;
				}   
				mGridView.setVisibility(View.VISIBLE);
				mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
				filmshow.setBackgroundColor(Color.TRANSPARENT);
			final List<Nvyou> showList =  (List<Nvyou>) paramList;			/**/
			mGridView.setAdapter( new BaseAdapter() {
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub

					if (convertView == null) {
						// 使用自定义的list_items作为Layout
						convertView = mInflater.inflate(R.layout.item_xdy, parent,
								false);
					}
					convertView.setBackgroundColor(Color.TRANSPARENT);
					TextView itemsText = AbViewHolder
							.get(convertView, R.id.itemsTitle);
					ImageView itemsIcon = AbViewHolder
							.get(convertView, R.id.itemsIcon); 
					itemsIcon.setBackgroundColor(Color.TRANSPARENT);
					String imageUrl = getItem(position).getImg();
					try {
						String wd=imageUrl.substring(imageUrl.lastIndexOf("/")+1);
						imageUrl =imageUrl.replace(wd,URLEncoder.encode(wd, "utf-8") );  
					} catch (Exception e) {
						// TODO: handle exception
					}if (ToolUtil.isWidth) {
						itemsIcon.setScaleType(ScaleType.FIT_CENTER);
					}else {
						itemsIcon.setScaleType(ScaleType.CENTER_INSIDE);
					}
					
					itemsText.setText(getItem(position).getName());
					mAbImageDownloader.setLoadingView(convertView
							.findViewById(R.id.progressBar));
					mAbImageDownloader.display( itemsIcon, imageUrl);
					return convertView;
				}
				
				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					return position;
				}
				
				@Override
				public Nvyou getItem(int position) {
					// TODO Auto-generated method stub
					return showList.get(position);
				}
				
				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return showList.size();
				}
			});
			mGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					int i=0;
					if (position==i) {

						if (RandomUtil.list==null||RandomUtil.list.size()==0)RandomUtil.init1(PlayUrlActivity.this);
						if (RandomUtil.list.size()>0) {
							DyUtil.toGetPlayUrl(RandomUtil.list.get(random.nextInt(RandomUtil.list.size())) ,PlayUrlActivity.this);
							return;
						}
						randomtoplay();
						return;
					}i++;
					if (position==i) { 
						getss();
						return;
					}i++;

					if (position==i) { 
						 getc(); 
						 return;
					}i++;
					if (position==i) {  
						Intent intent = new Intent();
						intent.setClass(PlayUrlActivity.this,
								FilmMainActivity.class);
						startActivity(intent);
						return;
					}i++;
					if (position==i) { 
						//getc();
						Intent intent = new Intent();
						intent.setClass(PlayUrlActivity.this,
								LeDsjActivity.class);
						startActivity(intent);
						return;
					}i++;
					if (position==i) { 

						Intent intent = new Intent();
						intent.setClass(PlayUrlActivity.this,
								LongFeng4Activity.class);
						startActivity(intent);
						return;
					}i++;
					if (position==i) { 
						getgif();
						return;
					}i++;
					if (position==i) { 
						doxdy();
						return;
					}i++;
					if (position==i) { 
						Intent intent = new Intent();
						intent.setClass(PlayUrlActivity.this,
								WebActivity.class);
						startActivity(intent);
						return;
					}i++;
					if (position==i) { 
						Intent intent = new Intent();
						intent.setClass(PlayUrlActivity.this,
								Mp4BaActivity.class);
						startActivity(intent);
						return;
					}i++;
					if (position==i) {
						getyy(); 
						return;
					}i++;
					if (position==i) {  
						//dodysq(30);
						dodysqmain();
						return;
					}i++;
					if (position==i) { 
						Intent intent = new Intent();
						intent.setClass(PlayUrlActivity.this,
								TvLiveActivity.class);
						startActivity(intent);
						return;
					}i++;
					if (position==i) {  
						doBada();
						return;
					}i++;
					if (position==i) {  
						dojav();
						return;
					}i++;
					if (position==i) { 
						AbDialogUtil.showAlertDialog(getView());
						return;
					}i++;
					
					
					
					
					Nvyou nvyou= showList.get (position) ;
					final String url = (nvyou).getUrl();  
					AbDialogUtil.showProgressDialog(PlayUrlActivity.this,
							R.drawable.progress_circular, "正在获取播放列表...");
					AbTask mAbTask = new AbTask();
					final AbTaskItem item = new AbTaskItem();
					final List<String> names = new ArrayList<String>();
					final List<String> filehashs = new ArrayList<String>();
					item.setListener(new AbTaskObjectListener() {

						@Override
						public void update(Object obj) {
							// TODO Auto-generated method stub
							// if ( isBack)return;
							AbDialogUtil.removeDialog(PlayUrlActivity.this);
							if (names.size() > 0) {
								View mView = PlayUrlActivity.this.mInflater.inflate(
										R.layout.dia_list_text, null);
								ListView listView = (ListView) mView
										.findViewById(R.id.listView1);
								TextView textView1 = (TextView) mView
										.findViewById(R.id.textView1); 
									textView1.setVisibility(View.GONE); 
								String[] mStrings = new String[names.size()];
								for (int i = 0; i < names.size(); i++) {
									mStrings[i] = names.get(i);
								}
								ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
										PlayUrlActivity.this, R.layout.dialog_list_item_1, mStrings);
								listView.setAdapter(listViewAdapter);
								listView.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> parent,
											View view, int position, long id) {
										DyUtil.toGetPlayUrl(filehashs.get(position) ,PlayUrlActivity.this);
									}
								});
								AbDialogUtil.showAlertDialog(mView);
							} else {
								AbToastUtil.showToast(PlayUrlActivity.this, "没有文件！");
							}
						}

						@Override
						public Object getObject() {
							// TODO Auto-generated method stub 
							try {
								Document doc = Jsoup.connect(url)
										.userAgent(DyUtil.userAgent1).followRedirects(true)
										.timeout(10000).get();

								Elements as = doc.select("tr.odd");
								for (Element element : as) {

									String  title= (element.select(".bt_title").text()+ "")
											.trim(); 
									String filehash = (element.select(".a-magnet").attr("href") + "").trim();
									filehash=DyUtil.changeED2K(filehash);
									if (!AbStrUtil.isEmpty(filehash)) {
										 
										names.add( title);
										filehashs.add(filehash);
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							return null;
						}
					});

					mAbTask.execute(item);
				}
			}); 
			
			
			}
		});
		mAbTask.execute(item); 
	}

}
