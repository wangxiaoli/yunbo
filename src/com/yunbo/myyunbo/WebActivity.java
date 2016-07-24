package com.yunbo.myyunbo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.control.Yy97Util;
import com.yunbo.mode.PageContent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("JavascriptInterface")
public class WebActivity extends AbActivity {

	private WebView webview;
	private ProgressBar progressbar;

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_web);
		((Button) findViewById(R.id.button1))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putBoolean("ismain", true);
						intent.setClass(WebActivity.this, PlayUrlActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
				});
		findViewById(R.id.button1).setVisibility(View.GONE);
		webview = (WebView) findViewById(R.id.webview);
		progressbar = (ProgressBar) findViewById(R.id.progressBar1);

		GridView gridView = (GridView) findViewById(R.id.mGridView);
		final int sum = 7;
		final BaseAdapter myGridViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = new TextView(WebActivity.this);
				}
				TextView tv = (TextView) convertView;
				if (tryqiyi == position) {
					tv.setBackgroundColor(Color.GREEN);
				} else {
					tv.setBackgroundColor(Color.WHITE);
				}
				tv.setText(getItem(position).toString());
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				if (position == 0) {
					return "爱奇艺";
				}
				if (1 == position) {
					return "搜狐";
				}
				if (2 == position) {
					return "通用";
				}
				if (3 == position) {
					return "优酷";
				}
				if (4 == position) {
					return "乐视";
				}

				if (5 == position) {
					return "芒果";
				}
				if (6 == position) {
					return "腾讯";
				}
				return "解析" + (position + 1);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return sum;
			}
		};
		gridView.setAdapter(myGridViewAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				tryqiyi = position;
				myGridViewAdapter.notifyDataSetChanged();
			}
		});
		WebSettings webSettings = webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setAllowFileAccessFromFileURLs(true);
		webSettings.setAllowContentAccess(true);
		webSettings.setAppCacheEnabled(true);

		webview.addJavascriptInterface(new MyJavascriptInterface(), "alistner");
		// 设置Web视图
		webview.setWebViewClient(new MyWebViewClient());
		// 设置setWebChromeClient对象
		webview.setWebChromeClient(new MyWebViewChromeClient());
		// 加载需要显示的网页
		webview.loadUrl("http://m.v.360.cn/android/index.html");
		// getYunflvurl();
	}

	String webtitle = "";

	private class MyWebViewChromeClient extends WebChromeClient {
		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			webtitle = title;
			if (!TextUtils.isEmpty(title) && title.contains("-")) {
				webtitle = title.split("-")[0];
			}
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(View.GONE);
			} else {
				if (progressbar.getVisibility() == View.GONE)
					progressbar.setVisibility(View.VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	};

	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		finish();// 结束退出程序
		return false;
	}

	private int tryqiyi = 2;

	// Web视图
	private class MyWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("http://www.iqiyi.com/v_")
					|| url.startsWith("http://www.letv.com/ptv/vplay/")
					|| url.startsWith("http://www.le.com/ptv/vplay/")
					|| url.startsWith("http://v.youku.com/v_show/id_")
					|| url.startsWith("http://tv.sohu.com/")
					|| url.startsWith("http://film.sohu.com/album/")
					|| url.startsWith("http://www.1905.com/vod/play/")
					|| url.startsWith("http://www.xiankan.com/")
					|| url.startsWith("http://v.pptv.com/show/")
					|| url.startsWith("http://v.qq.com/")
					|| url.startsWith("http://film.qq.com/")
					|| url.startsWith("http://www.hunantv.com/v/")
					|| url.startsWith("http://www.mgtv.com/v/")
					|| url.startsWith("http://www.wasu.cn/Play/show/")
					|| url.startsWith("http://vod.kankan.com/v/")
					|| url.startsWith("http://g.hd.baofeng.com/play/")
					|| url.startsWith("http://www.tudou.com/albumplay/")
					|| url.startsWith("http://tv.cztv.com/vplay/")
					|| url.startsWith("http://tv.cntv.cn/video/")
					|| url.startsWith("http://d.ifengimg.com/")
					|| url.startsWith("http://www.fun.tv/vplay/")
					|| url.startsWith("http://www.kumi.cn/donghua/")
					|| url.startsWith("http://61.iqiyi.com/comic-play/")
			// ||url.startsWith("")
			) {
				if (url.contains("?")
						&&!url.startsWith("http://v.qq.com/")
						&&!url.startsWith("http://film.qq.com/")) {
					url = url.split("\\?")[0];
				}
				if (url.startsWith("http://v.qq.com/")
						|| url.startsWith("http://film.qq.com/")) {
					url = url.replace("&ptag=360kan.tv","");
				}
				// tryqiyi++;
				// tryqiyi=tryqiyi%4;
				if (tryqiyi == 0) {
					////proxy.dy208.com/iqy/492675900
					if (url.startsWith("http://www.iqiyi.com/v_")) {
						jxhand = "http://www.cloudparse.com/ckflv/?url=";
						jxhand = "http://vip.4410yy.com/iqiyi.php?url=";
						jxiqiyi1_1(url);
						//url="http://www.wx6080.com/api/d4qiyi.php?id="+url;
						//url="http://api.addzsw.com/iqiyim3u8?url="+url;
					} else {
						AbToastUtil.showToast(WebActivity.this,
								"爱奇艺专用解析，请选择其他的。");

					}
					return true;
				}
				if (tryqiyi == 1) {
					// url="http://aqw894774.gotoip2.com/qyvip.php?id="+url;
					// jxhand="http://www.2341.tv/ckflv/api.php?url=";

					// url="http://14.29.50.27:8022/demo.php?url="+url;
					// http://proxy.dy208.com/sohu/index.php?vid=2784321
					if (url.startsWith("http://tv.sohu.com/")
							|| url.startsWith("http://film.sohu.com/album/")) { 
					url = String.format(Yy97Util.httpjxurl, url);
					} else {
						AbToastUtil.showToast(WebActivity.this,
								"搜狐专用解析，请选择其他的。");
					return true;
					}
				}
				if (tryqiyi == 2) {
					// jxhand="http://www.ysyunbo.com/ckflv/api.php?url=";
					// jxiqiyi( url );
					// return true;
					jxhand = "http://www.xgysxx.com/ckflv/api.php?url=";
					//jxhand = "http://www.cloudparse.com/ckflv/?url=";
					//jxhand = "http://v.1gnk.com/ckflv/index.php?url=";
					if (url.startsWith("http://v.qq.com/")
							|| url.startsWith("http://film.qq.com/")) {
						url = url.replace("http://v.qq.com/","http://film.qq.com/");
						//http://v.qq.com/cover/4/494f3g046v94bbs.html?vid=d0020mxf3xa
						url = url.replace(".html?vid=","/")+".html";
						jiexiurl(url);
						return true;
					}
					jxiqiyi(url);
//					xmlhand="www.kuaikyy.com/ydisk/api.php";
//					jxhand="http://www.kuaikyy.com/ydisk/index.php?url=";
//					jxiqiyi4(url);
					return true;
				}
				if (tryqiyi == 3) {
					if (url.startsWith("http://v.youku.com/v_show/id_")) { 
						jiexiurl(url); 
					} else {
						AbToastUtil.showToast(WebActivity.this,
								"优酷专用解析，请选择其他的。");
					}
					return true;
				}
				if (tryqiyi == 4) {
					// jxhand="http://v.1gnk.com/ckflv/api.php?url=";
					// jxhand="http://mp4tv.gotoip11.com/ckflv/api.php?url=";
					// jxiqiyi( url );

					if (url.startsWith("http://www.letv.com/ptv/vplay/")
							|| url.startsWith("http://www.le.com/ptv/vplay/")) {
						try {

//							url = "http://tx.ismdy.com/levip.php?v="
//									+ url.substring(url.lastIndexOf("/") + 1,
//											url.lastIndexOf("."))
//									+ "_1000_m3u8";

							  //http://proxy.dy208.com/levip/25892509
							
							url="http://jiexi.tepian.com/le.php?url="+url;
						} catch (Exception e) {
							// TODO: handle exception
							AbToastUtil.showToast(WebActivity.this, "格式错误。");
							return true;
						}
					} else {
						AbToastUtil.showToast(WebActivity.this,
								"乐视专用解析，请选择其他的。");
						return true;
					}
				}
				if (tryqiyi == 5) {
					// jxhand="http://v.1gnk.com/ckflv/api.php?url=";
					// jxhand="http://mp4tv.gotoip11.com/ckflv/api.php?url=";
					// jxiqiyi( url );
					jxhand="http://www.mtkan.cc/mgtv.php?url=";
					if (url.startsWith("http://www.mgtv.com/v/")) {
						  jxmgtv(url );
						//url = "http://www.ivlook.com/ivlook-api/mgtv.php?url="+ url;
					} else {
						AbToastUtil.showToast(WebActivity.this,
								"芒果专用解析，请选择其他的。");
					}
					return true;
				}
				if (tryqiyi == 6) {
					// http://www.yichongwu.com/cq/v/2638.php?v=n0020kiklv0
					if (url.startsWith("http://v.qq.com/")||url.startsWith("http://film.qq.com/")) {
						url=url.replace("http://v.qq.com/","http://film.qq.com/");
						jxhand = "http://www.yichongwu.com/cq/v/2638.php?v=";
						jxQQ(url);
					} else {
						AbToastUtil.showToast(WebActivity.this,
								"腾讯专用解析，请选择其他的。");
					}
					return true;
				}
				url = "" + url;

				final String playurl = url;
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						DyUtil.play(WebActivity.this, playurl, webtitle + "_"
								+ innerText, false);
					}
				}, 300);
			} else {

				view.loadUrl(url);
			}
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub

			super.onPageFinished(view, url);
			addAClickListner();
		}

	}

	private String jxhand = "http://v.1gnk.com/ckflv/api.php?url=";

	// http://v.1gnk.com/ckflv/api.php?url=

	private void jxiqiyi(final String qiyiurl) {
		// TODO Auto-generated method stub
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在解析,接口" + (tryqiyi + 1));
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(WebActivity.this);
				if (qiyiurl.equals(obj)) {
					obj=null;
				}
				if (obj == null) {
					// AbToastUtil.showToast(WebActivity.this, "解析失败。请多试试。");
					//jiexiurl(qiyiurl);

					xmlhand="www.kuaikyy.com/ydisk/api.php";
					jxhand="http://www.kuaikyy.com/ydisk/index.php?url=";
					jxiqiyi4(qiyiurl);
				} else {
					DyUtil.play(WebActivity.this, obj.toString(), webtitle
							+ "_" + innerText, false);
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				try {
 
					if (qiyiurl.startsWith("http://v.youku.com/v_show/id_")) {
						String urlString = String.format(Yy97Util.httpjxurl,
								qiyiurl);
						String okurl = getRedirects(urlString);
						if (!okurl.equals(urlString)) {
							return okurl;
						}
					}

					if (qiyiurl.startsWith("http://www.iqiyi.com/v_")) {
						if (tryint==0) {
							tryint++;
							return "http://player.bceapp.com/qymp4vip/local/qy_jx5_mpc.php?vid="+qiyiurl;
						}
						if (tryint==1) {
							tryint++;
							return "http://wx6080.com/8g.php?url="+qiyiurl;
						}
						tryint=0;
						return "http://14.29.50.27:8022/demo.php?url="+qiyiurl;
					}
					Document doc = Jsoup.connect(jxhand + qiyiurl)
							.userAgent(DyUtil.userAgent1).timeout(20000)
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("video=\\['(.*?)'\\]");
					if (html.contains("70775091")) {
						return null;
					}

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						String ts = mat.group(1);
						if (!TextUtils.isEmpty(ts)) {

							ts = URLDecoder.decode(ts, "utf-8");
							if (ts.startsWith("http")) {
								return ts;
							}
							String hand = jxhand.replace("http://", "").split(
									"\\?")[0];
							hand = hand.substring(0, hand.indexOf("/"));

							return "http://" + hand + ts;
						}
					}
					pat = Pattern.compile("a:'(.*?)'");

					mat = pat.matcher(html);
					if (mat.find()) {
						String ts = mat.group(1);

						if (!TextUtils.isEmpty(ts)) {

							ts = URLDecoder.decode(ts, "utf-8");
							if (ts.startsWith("http")) {
								return ts;
							}
							String hand = jxhand.replace("http://", "").split(
									"\\?")[0];
							hand = hand.substring(0, hand.indexOf("/"));

							return "http://" + hand + ts;
						}
					}
					pat = Pattern.compile("f:'(.*?)'");

					mat = pat.matcher(html);
					if (mat.find()) {
						String ts = mat.group(1);
						if (!TextUtils.isEmpty(ts)) {

							if (ts.endsWith(".swf")) {
								return null;
							}
							ts = URLDecoder.decode(ts, "utf-8");
							if (ts.startsWith("http")) {
								return ts;
							}
							String hand = jxhand.replace("http://", "").split(
									"\\?")[0];
							hand = hand.substring(0, hand.indexOf("/"));

							return "http://" + hand + ts;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}

	private void jxiqiyi1(final String qiyiurl) {
		// TODO Auto-generated method stub
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在解析,接口" + (tryqiyi + 1));
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(WebActivity.this);
				if (obj == null) {
					AbToastUtil.showToast(WebActivity.this, "解析失败。请多试试。");
					// jiexiurl(qiyiurl);
					obj="http://14.29.50.27:8022/demo.php?url="+qiyiurl;
					DyUtil.play(WebActivity.this, obj.toString(), webtitle
							+ "_" + innerText, false);
				} else {
					DyUtil.play(WebActivity.this, obj.toString(), webtitle
							+ "_" + innerText, false);
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				try {

					Document doc = Jsoup.connect(jxhand + qiyiurl)
							.userAgent(DyUtil.userAgent1).timeout(20000)
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("source:\\s*'(.+)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						String ts = mat.group(1);
						if (!TextUtils.isEmpty(ts)) {

							ts = URLDecoder.decode(ts, "utf-8");
							if (ts.startsWith("http")) {
								return ts;
							}
							if (ts.startsWith("./")) {

								String hand = jxhand.replace("http://", "")
										.split("\\?")[0];
								hand = hand.substring(0, hand.indexOf("/"));

								return "http://" + hand + ts.substring(1);
							}
						}
					}else {
						doc = Jsoup.connect( qiyiurl)
								.userAgent(DyUtil.userAgent1).timeout(20000)
								.header("Accept-Language", "zh-CN").get();
						  html = doc.html();
						  html=html.replace("\r", "").replace("\n", "|");
						for (String  str : html.split("\\|")) {
							str=(""+str).trim();
							if (str.startsWith("tvId:")) {
								str=str.substring(5, 5+9);
								return "http://api.addzsw.com:8080/vipiqiyimp4?tvid="+str;
							}
						}
					}
					

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}

	private void jxmgtv(final String qiyiurl) {
		// TODO Auto-generated method stub
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在解析,接口" + (tryqiyi + 1));
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(WebActivity.this);
				if (obj == null) {
					AbToastUtil.showToast(WebActivity.this, "解析失败。请多试试。"); 
				} else {
					DyUtil.play(WebActivity.this, obj.toString(), webtitle
							+ "_" + innerText, false);
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				try {

					Document doc = Jsoup.connect(jxhand + qiyiurl)
							.userAgent(DyUtil.userAgent1).timeout(20000)
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("source:\\s*'(.+)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						String ts = mat.group(1);
						if (!TextUtils.isEmpty(ts)) {

							ts = URLDecoder.decode(ts, "utf-8");
							if (ts.startsWith("http")) {
								return ts;
							}
							if (ts.startsWith("./")) {

								String hand = jxhand.replace("http://", "")
										.split("\\?")[0];
								hand = hand.substring(0, hand.indexOf("/"));

								return "http://" + hand + ts.substring(1);
							}
						}
					} 
					

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}
	int tryintqy=0;
	private void jxiqiyi1_1(final String qiyiurl) {
		// TODO Auto-generated method stub
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在解析,接口" + (tryqiyi + 1));
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(WebActivity.this);
				if (obj == null) {
					AbToastUtil.showToast(WebActivity.this, "解析失败。请多试试。");
					// jiexiurl(qiyiurl);
					obj="http://14.29.50.27:8022/demo.php?url="+qiyiurl;
					if (tryint==0) {
						tryint++;
						obj= "http://player.bceapp.com/qymp4vip/local/qy_jx5_mpc.php?vid="+qiyiurl;
					}else{
					tryint=0;
					obj="http://www.wx6080.com/api/d4qiyi.php?id="+qiyiurl;}
					DyUtil.play(WebActivity.this, obj.toString(), webtitle
							+ "_" + innerText, false);
				} else {
					DyUtil.play(WebActivity.this, obj.toString(), webtitle
							+ "_" + innerText, false);
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				try {

					String playurl=getm3u8(qiyiurl);
					if (!TextUtils.isEmpty(playurl)) {
						return playurl;
					}
					if (tryintqy==0&&!TextUtils.isEmpty(qiyiurl)) {tryintqy++;
						String urlString="http://wx6080.com/8g.php?url="+qiyiurl;
						  String okurl=getRedirects(urlString);
						  if (!okurl.equals(urlString)) {
						  return okurl; 
						  }
					}//tryintqy=0;
					 //http://m3u8.cc/api?apikey=2395d95223232995&url=http://www.iqiyi.com/v_19rrl4tz10.html&mode=iphone
					Document	doc = Jsoup.connect( qiyiurl)
								.userAgent(DyUtil.userAgent1).timeout(20000)
								.header("Accept-Language", "zh-CN").get();
					String	  html = doc.html();
						  html=html.replace("\r", "").replace("\n", "|");
						for (String  str : html.split("\\|")) {
							str=(""+str).trim();
							if (str.startsWith("tvId:")) {
								str=str.substring(5, 5+9);
								//return "http://api.addzsw.com:8080/vipiqiyimp4?tvid="+str;
								
								//String urlString="http://proxy.dy208.com/iqy/iqy4138.php?v="+str;
								//String urlString="http://proxy.dy208.com/iqy/"+str;
								String urlString="http://www.bage01.com/js/playdy/cc/qy.php?id="+str;
								tryintqy++;
								if (tryintqy==2) {
									urlString="http://proxy.dy208.com/iqy/"+str;
									tryintqy=0;
								}
								
								  String okurl=getRedirects(urlString);
								  if (!okurl.equals(urlString)) {
								  return okurl;
								  }
								return null;
							}
						}
					

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}
	//private String 

	private void jxiqiyi3(final String qiyiurl) {
		// TODO Auto-generated method stub
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在解析,接口" + (tryqiyi + 1));
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(WebActivity.this);
				if (obj == null||!obj.toString().startsWith("http")) {
					AbToastUtil.showToast(WebActivity.this, "解析失败。请多试试。");
					return;
				}
				if (obj == null) {
					// AbToastUtil.showToast(WebActivity.this, "解析失败。请多试试。");
					jiexiurl(qiyiurl);
				} else {
					List<String>list=new ArrayList<String>();
					for (String string : obj.toString().split("\\|")) {
						list.add(string);
					}
					
					if (list.size()==1) {
						
					DyUtil.play(WebActivity.this,list.get(0) , webtitle
							+ "_" + innerText, false);
					}else {
						DyUtil.play(WebActivity.this,list,list.get(0) , webtitle
								+ "_" + innerText, false);
					}
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				try {

					// if (qiyiurl.startsWith("http://www.mgtv.com/v/")) {
					// String
					// urlString="http://www.ivlook.com/ivlook-api/mgtv.php?url="+qiyiurl;
					// String okurl=getRedirects(urlString);
					// if (!okurl.equals(urlString)) {
					// return okurl;
					// }
					// }
					// if (qiyiurl.startsWith("http://v.youku.com/v_show/id_"))
					// {
					// String urlString=String.format(Yy97Util.httpjxurl,
					// qiyiurl);
					// String okurl=getRedirects(urlString);
					// if (!okurl.equals(urlString)) {
					// return okurl;
					// }
					// }
					Document doc = Jsoup.connect(jxhand + qiyiurl)
							.userAgent(DyUtil.userAgent1).timeout(20000)
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					// System.out.println(html);
					Pattern pat = Pattern
							.compile("\"time\"\\s*:\\s*\"(.*?)\".*?\"key\"\\s*:\\s*\"(.*?)\"");
					// if (html.contains("70775091")) {
					// return null;
					// }

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						String key = mat.group(2);
						String time = mat.group(1);
						String hand = jxhand.replace("http://", "")
								.split("\\?")[0];
						hand = hand.substring(0, hand.indexOf("/"));
						if (!TextUtils.isEmpty(key)) {

							String apiurl = "http://" + hand
									+ "/ckflv/api.php?key=" + key + "&time="
									+ time + "&url=" + qiyiurl + "&type=&xml=1";
							doc = Jsoup.connect(apiurl)
									.userAgent(DyUtil.userAgent1)
									.timeout(20000)
									.header("Accept-Language", "zh-CN").get();
							html = doc.html();
							System.out.println(html);
							// int  index=html.indexOf("<file><![CDATA[");//<file><![CDATA[
							// int end=html.indexOf("]", index);
							// String urls=html.substring(index+15, end);
							// if (index==-1) {
							// index=html.indexOf("<file>");
							// end=html.indexOf("</file>", index);
							// urls=html.substring(index+6, end).trim();
							// }
							// System.out.println(urls);

							// return urls;
							String urls="";
							int count=0;
							for (Element e : doc.select("file")) {
								count++;
								if (count==1) {
									urls = e.text().trim();
									if ("http://vip.4410yy.com/".equals(urls)) {
										return null;
									}
								}else {
									urls = urls+"|"+e.text().trim();
								}
								 
								//return urls;
							}
								System.out.println(urls);
							if (TextUtils.isEmpty(urls)) {
								return null;
							}
							return urls;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}
	private String xmlhand="";
	int tryint=0;
	private void jxiqiyi4(final String qiyiurl) {
		// TODO Auto-generated method stub
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在解析,接口" + (tryqiyi + 1));
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(WebActivity.this);
				if (obj == null||!obj.toString().startsWith("http")) {
					jiexiurl(qiyiurl);
					//AbToastUtil.showToast(WebActivity.this, "解析失败。请多试试。");
					return;
				}
				if (obj == null) {
					// AbToastUtil.showToast(WebActivity.this, "解析失败。请多试试。");
					jiexiurl(qiyiurl);
				} else {
					List<String>list=new ArrayList<String>();
					for (String string : obj.toString().split("\\|")) {
						list.add(string);
					}
					
					if (list.size()==1) {
						
					DyUtil.play(WebActivity.this,list.get(0) , webtitle
							+ "_" + innerText, false);
					}else {
						DyUtil.play(WebActivity.this,list,list.get(0) , webtitle
								+ "_" + innerText, false);
					}
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				try {

					// if (qiyiurl.startsWith("http://www.mgtv.com/v/")) {
					// String
					// urlString="http://www.ivlook.com/ivlook-api/mgtv.php?url="+qiyiurl;
					// String okurl=getRedirects(urlString);
					// if (!okurl.equals(urlString)) {
					// return okurl;
					// }
					// }
					// if (qiyiurl.startsWith("http://v.youku.com/v_show/id_"))
					// {
					// String urlString=String.format(Yy97Util.httpjxurl,
					// qiyiurl);
					// String okurl=getRedirects(urlString);
					// if (!okurl.equals(urlString)) {
					// return okurl;
					// }
					// }
					if (qiyiurl.startsWith("http://www.iqiyi.com/v_")) {
						if (tryint==0) {
							tryint++;
							return "http://player.bceapp.com/qymp4vip/local/qy_jx5_mpc.php?vid="+qiyiurl;
						}
						tryint=0;
						return "http://14.29.50.27:8022/demo.php?url="+qiyiurl;
					}
					Document doc = Jsoup.connect(jxhand + qiyiurl)
							.userAgent(DyUtil.userAgent1).timeout(20000)
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					// System.out.println(html);
					Pattern pat = Pattern
							.compile("\"time\"\\s*:\\s*\"(.*?)\".*?\"key\"\\s*:\\s*\"(.*?)\"");
					// if (html.contains("70775091")) {
					// return null;
					// }

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						String key = mat.group(2);
						String time = mat.group(1);
						String hand = jxhand.replace("http://", "")
								.split("\\?")[0];
						hand = hand.substring(0, hand.indexOf("/"));
						if (!TextUtils.isEmpty(key)) {

							String apiurl = "http://" +xmlhand+"?key=" + key + "&time="
									+ time + "&url=" + qiyiurl + "&type=&xml=1";
							doc = Jsoup.connect(apiurl)
									.userAgent(DyUtil.userAgent1)
									.timeout(20000)
									.header("Accept-Language", "zh-CN").get();
							html = doc.html();
							System.out.println(html);
							// int  index=html.indexOf("<file><![CDATA[");//<file><![CDATA[
							// int end=html.indexOf("]", index);
							// String urls=html.substring(index+15, end);
							// if (index==-1) {
							// index=html.indexOf("<file>");
							// end=html.indexOf("</file>", index);
							// urls=html.substring(index+6, end).trim();
							// }
							// System.out.println(urls);

							// return urls;
							String urls="";
							int count=0;
							for (Element e : doc.select("file")) {
								count++;
								if (count==1) {
									urls = e.text().trim();
									if ("http://vip.4410yy.com/".equals(urls)) {
										return null;
									}
								}else {
									urls = urls+"|"+e.text().trim();
								}
								 
								//return urls;
							}
								System.out.println(urls);
							if (TextUtils.isEmpty(urls)) {
								return null;
							}
							return urls;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}

	private void jxQQ(final String qqurl) {
		// TODO Auto-generated method stub
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在解析,接口" + (tryqiyi + 1));
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(WebActivity.this);
				if (obj == null) {
					AbToastUtil.showToast(WebActivity.this, "解析失败。请多试试。");
					// jiexiurl(qiyiurl);
				} else {
					DyUtil.play(WebActivity.this, obj.toString(), webtitle
							+ "_" + innerText, false);
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				try {

					String urlString = qqurl;
					if (qqurl.startsWith("http://film.qq.com/")) {

//						Document doc = Jsoup.connect(qqurl)
//								.userAgent(DyUtil.userAgent1).timeout(20000)
//								.header("Accept-Language", "zh-CN").get();
//						String okurl = doc.html();
						String okurl = urlString;
						//Pattern pat = Pattern.compile("vid:\"([0-9a-z]+)\"");
						Pattern pat = Pattern.compile("vid=([0-9a-z]+)");
						Matcher mat = pat.matcher(okurl);
						if (mat.find()) {
							String ts = mat.group(1);

							urlString = jxhand + ts;
							okurl = getRedirects(urlString);
							if (!okurl.equals(urlString)) {
								System.out.println(okurl);
								okurl = okurl.replace("\\/", "/");
								okurl = okurl.substring(okurl.lastIndexOf("//"));
								okurl = okurl.replace("//", "").replace("/",
										"//");
								System.out.println(okurl);
								return "http://" + okurl;
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}

	private void jiexiurl(final String url) {
		// TODO Auto-generated method stub
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在解析,接口" + (tryqiyi + 1));
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(WebActivity.this);
				if (obj == null) {
					if (url.startsWith("http://www.iqiyi.com/v_")) {
						// tryqiyi=2;
						// url="http://mp4tv.net/qyvip.php?id="+url;
						// http://14.29.50.27:8022/demo.php?url=http://www.iqiyi.com/v_19rrlh01mo.html
						String playurl = "http://14.29.50.27:8022/demo.php?url="
								+ url;
						DyUtil.play(WebActivity.this, playurl, webtitle + "_"
								+ innerText, false);
					} else if (url.startsWith("http://www.mgtv.com/v/")
					// ||url.startsWith("http://v.qq.com/")
					) {
						String playurl = "http://www.xgysxx.com/api.php?url="+ url+ "&ctype=phone";
						int start=url.lastIndexOf("/")+1;
						int end=url.lastIndexOf(".");
						if (end>start) {
							playurl="http://api.pronvod.com/dy/mg.php?v="+url.substring(start, end);
						}
						DyUtil.play(WebActivity.this, playurl, webtitle + "_"
								+ innerText, false);
					} else {
						// AbToastUtil.showToast(WebActivity.this,
						// "解析失败。请多试试。");
						String playurl = String.format(Yy97Util.httpjxurl, url);
						DyUtil.play(WebActivity.this, playurl, webtitle + "_"
								+ innerText, false);
					}

				} else {
					DyUtil.play(WebActivity.this, obj.toString(), webtitle
							+ "_" + innerText, false);
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				try {

					Document doc = Jsoup
							.connect(
									String.format(//
											"http://m3u8.cc:8020/api?apikey=55bfaf1b2821826c&url=%s&mode=iphone",
											url)).userAgent(DyUtil.userAgent1)
							.timeout(20000).header("Accept-Language", "zh-CN")
							.get();
					String html = doc.html();
					Pattern pat = Pattern.compile("'files:'(.*?)'".replace("'",
							"\""));

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						String ts = mat.group(1);
						ts = URLDecoder.decode(ts, "utf-8").replace("\\/", "/");
						if (ts.startsWith("http")) {
							return ts;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}

	String Yunflvurl = "/player/Yunflv/api/fk.php?url=";

	private static String getRedirects(String urlString) {

		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setFollowRedirects(true);
			// connection.connect();
			connection.getResponseCode();
			urlString = connection.getURL().toString();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urlString;
	}

	private void getYunflvurl() {
		// TODO Auto-generated method stub
		Yy97Util.httpjxurl = Yunflvurl;
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				String objString = (String) obj;
				if (!TextUtils.isEmpty(objString)) {
					Yunflvurl = objString;
					Yy97Util.httpjxurl = Yunflvurl;
					AbToastUtil.showToast(WebActivity.this, "解析地址更新成功。");
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub

				try {

					Document doc = Jsoup
							.connect(
									"http://v.1gnk.com/player/Yunflv/yunflv.html")
							.userAgent(
									"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
							.get();

					String html = doc.html();
					Pattern pat = Pattern.compile("video=\\['(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						String hand = mat.group(1);
						return hand;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return null;
			}
		});

		mAbTask.execute(item);
	}
	public String getm3u8(String urls) {
		// TODO Auto-generated method stub
		try {

			 
			URL	urlx = new URL(String.format("http://m3u8.cc：8020/api?apikey=55bfaf1b2821826c&url=%s&mode=iphone",urls))	;
			HttpURLConnection connection = (HttpURLConnection) urlx
					.openConnection();

			connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
					
			connection.setRequestProperty("Connection", "Keep-Alive"); 
			connection.setReadTimeout(30000);
			connection.setFollowRedirects(true);
			connection.connect(); 
			String html =""; 
			InputStream inStrm = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					inStrm,"utf-8"));

			String temp = "";
			while ((temp = br.readLine()) != null) {
				html += (temp + '\n');
			}
			br.close();
			connection.disconnect(); 
				String url=html.replace("\\/", "/");
				
				System.out.println(html); try {
					if (null != url) {
						JSONObject newsObject = new JSONObject(url);
						if (200!=newsObject.getInt("code")) {
							return null;
						}
						JSONObject jsonObject = newsObject.getJSONObject("result");
						String files = jsonObject.getString("files"); 
						 return files;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		return null;
	}
	private String innerText = "";

	// js通信接口
	final private class MyJavascriptInterface {

		@JavascriptInterface
		public void openLink(String str) {
			// Toast.makeText(WebActivity.this, str, Toast.LENGTH_LONG).show();
			innerText = str;
		}
	}

	// 注入js函数监听
	private void addAClickListner() {
		webview.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"a\"); "
				+ "for(var i=0;i<objs.length;i++)  " + "{"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.alistner.openLink(this.innerText);  "
				+ "    }  " + "}" + "})()");
	}
}
