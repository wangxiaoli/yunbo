package com.yunbo.myyunbo;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.ab.util.AbDialogUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.control.RandomUtil;
import com.yunbo.mode.Dydata;
import com.yunbo.mode.Movie;
import com.yunbo.mode.NameEnc;
import com.yunbo.mode.Node;
import com.yunbo.mode.PageContent;
import com.yunbo.mode.Video; 

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Mp4BaActivity extends AbActivity {

	private ListView mListView = null;
	private LayoutInflater mInflater;
	private ArrayList<Video> allList = new ArrayList<Video>();

	private BaseAdapter myListViewAdapter;
	private ImageButton moreButton;
	private EditText word;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null;
	private int page = 0;
	private View show;
	private Button jx115  ;
	private Button cili ;
	private WebView webview;  
	private ProgressBar progressbar;
	private TextView textView;
	private boolean isad=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_mp4ba);

		mListView = (ListView) this.findViewById(R.id.listView1);
		textView = (TextView) this.findViewById(R.id.textView1);
		textView.setText("高清MP4");
		word = (EditText) findViewById(R.id.editText1);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		show= findViewById(R.id.show);
		show.setVisibility(View.GONE);
		findViewById(R.id.ssLinearLayout1).setVisibility(View.GONE);
		 
		jx115 = (Button)  findViewById(R.id.button1);
		cili = (Button)  findViewById(R.id.button2);

		final GridView gridView = (GridView)  findViewById(R.id.mGridView);
		gridView.setVisibility(View.GONE);

		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);

		mAbImageDownloader = new AbImageLoader(this);
		mAbImageDownloader.setMaxWidth(180);
		mAbImageDownloader.setMaxHeight(240);
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setEmptyImage(R.drawable.image_empty);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);
		/**/
		mListView.setAdapter(myListViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					// 使用自定义的list_items作为Layout
					convertView = mInflater.inflate(R.layout.text_item, parent,
							false);
				}
				TextView itemsText = AbViewHolder
						.get(convertView, R.id.tx_item); 
				itemsText.setGravity(Gravity.CENTER);
				itemsText.setText(getItem(position).getTitle()//+"-"+getItem(position).getId()
						); 
				if ("N".equals(getItem(position).getIntro())) {
					itemsText.setBackgroundColor(Color.WHITE);
				}
				else {
					itemsText.setBackgroundColor(Color.GREEN);
				}
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Video getItem(int position) {
				// TODO Auto-generated method stub
				return allList.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return allList.size();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DyUtil.showVideo = allList.get(position);

				if (!isad&&"N".equals(DyUtil.showVideo.getIntro())) {
					//DyUtil.jiexiIndex=4;
					DyUtil.toGetPlayUrl(DyUtil.showVideo.getVid(),Mp4BaActivity.this);
					return;
				}
				show.setVisibility(View.VISIBLE);
				webview.loadUrl("http://www.meiyouad.com/torrent/mp4ba/"+DyUtil.showVideo.getId());
				if ("N".equals(DyUtil.showVideo.getIntro())) {
					gridView.setVisibility(View.GONE);
					jx115.setVisibility(View.GONE);
				}
				else {
					gridView.setVisibility(View.VISIBLE);
					jx115.setVisibility(View.VISIBLE);
				}
			}
		});
		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isad) 
					moreLoading();
				else
					moreLoading1();
			}

		});
		(findViewById(R.id.imagess))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						// Intent intent = new Intent();
						// intent.setClass(HomeActivity.this,
						// LanunyActivity.class);
						// startActivity(intent);
						search();
					}
				});
		(findViewById(R.id.imageView1)).setVisibility(View.GONE);
		if (LanunyActivity.totalKeys != null
				&& LanunyActivity.totalKeys.length > 0) {
			String[] ss = LanunyActivity.totalKeys;
			word.setText(ss[new Random().nextInt(ss.length)]);
		}
		
		webview = (WebView) findViewById(R.id.webview);
		progressbar = (ProgressBar) findViewById(R.id.progressBar1); 
		final int sum=4;
		final BaseAdapter myGridViewAdapter=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null){
					convertView=new TextView(Mp4BaActivity.this);
				}
				TextView tv=(TextView)convertView;
				if (index==position) {
					tv.setBackgroundColor(Color.GREEN);
				}else {
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
				String showtt="480P";
				if (position==1) {
					showtt="720P";
				}
				if (position==2) {
					showtt="1080P";
				}
				if (position==3) {
					showtt="2K";
				}
				return showtt;
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
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				index=position;
				myGridViewAdapter.notifyDataSetChanged();
			}
		});
		
		 webview.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return true;
			}});   
			// 设置setWebChromeClient对象
			webview.setWebChromeClient(new  WebChromeClient(){
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
			});
			cili.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DyUtil.toGetPlayUrl(DyUtil.showVideo.getVid(),Mp4BaActivity.this);
					 
				}
			}); 
			jx115.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					final AbActivity abActivity = Mp4BaActivity.this;
					AbDialogUtil.showProgressDialog(abActivity,
							R.drawable.progress_circular, "正在获取播放链接...");
					AbTask mAbTask = new AbTask();
					final AbTaskItem item = new AbTaskItem();
					item.setListener(new AbTaskObjectListener() {
						String  showtext="";
						@Override
						public void update(Object obj) {
							// TODO Auto-generated method stub
							// if ( isBack)return;
							AbDialogUtil.removeDialog(abActivity);
							if (obj == null) {
								AbToastUtil.showToast(abActivity,
										"没有播放链接，多试试或者请看其它影片！");
							} else {
								DyUtil.play(Mp4BaActivity.this, ""+obj, DyUtil.showVideo.getTitle() ,false);
							}
						}

						@Override
						public Object getObject() {
							// TODO Auto-generated method stub
							String url = "http://www.meiyouad.com/torrent/play/"+DyUtil.showVideo.getId();
							try {
								Document doc = Jsoup.connect(url)
										.userAgent(DyUtil.userAgent1)
										 .timeout(10000).post();
								System.out.println(url);
								System.out.println(doc.text()); 
								url="http://sproxy.meiyouad.com:81/proxy.php?vid="+doc.text()+""+index;
								
								System.out.println(url);
								  doc = Jsoup.connect(url)
										.userAgent(DyUtil.userAgent1)
										 .timeout(10000).get();
								String html=  doc.html();
								System.out.println(html);
								int start=html.indexOf("http");
								int end=html.indexOf("}", start);
								url=html.substring(start, end);
								System.out.println(url);
							     return url;
								 
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
			
			
			
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				 moreLoading();
			}
		}, 100); 

        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
	}

	@Override 
    //设置回退  
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {  
if (        	show.getVisibility()==View.VISIBLE) {
	show.setVisibility(View.GONE);
} else {
finish(); 
}
            return true;  
        }  
        
        return false;  
    }  
	int index=2;
	private void moreLoading1() {
		page++;
		moreButton.setVisibility(View.GONE);
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取电影列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Video> data = new ArrayList<Video>();
				String url="http://www.mp4ba.com/index.php?&page="+page;
				try {

					Document doc = Jsoup.connect(url)
							.userAgent(DyUtil.userAgent1).timeout(10000).get();

					Elements trs = doc.select("tbody#data_list > tr");
					for (Element tr : trs) {
						Elements tds = tr.select("td");
							Element td = tds.get(3);
						if (td != null) {
							//if (!"N".equals(td.text())) 
							{
								Video video = new Video();
								
								Element a = tds.get(2).select("a").first();
								video.setId(a.attr("href").replace("show.php?hash=", "")); 
								video.setTitle(String.format("【%s】", td.text())+a.text().split("\\.X264")[0]);
								video.setIntro("N");  
								video.setVid(video.getId());  
									data.add(video); 
								
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				moreButton.setVisibility(View.VISIBLE);
				AbDialogUtil.removeDialog(Mp4BaActivity.this);
				if (paramList == null || paramList.size() == 0) {
					page--;
					AbToastUtil.showToast(Mp4BaActivity.this, "查询结果为空！多试试");
					return;
				}
				List<Video> list = (List<Video>) paramList;
				allList.addAll(list);
				myListViewAdapter.notifyDataSetChanged();
				if (page < 2)
					closeInputMethod();
			}
		});
		mAbTask.execute(item);

	}
	private void moreLoading() {
		page++;
		moreButton.setVisibility(View.GONE);
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取电影列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Video> data = new ArrayList<Video>();
				String url="http://www.meiyouad.com/torrent";
				if (page>1) {
					url="http://www.meiyouad.com/torrent?page="+((page-1)*50);
				}
				try {

					Document doc = Jsoup.connect(url)
							.userAgent(DyUtil.userAgent2).timeout(10000).get();

					Elements trs = doc.select("tbody > tr");
					for (Element tr : trs) {
						Elements tds = tr.select("td");
							Element td = tds.get(4);
						if (td != null) {
							//if (!"N".equals(td.text())) 
							{
								Video video = new Video();
								
								Element a = tds.get(1).select("a").first();
								video.setIntro(td.text());
								video.setId(a.attr("href").replace("/torrent/mp4ba/", "")); 
								video.setTitle(a.text().split("\\.X264")[0]); 
								a = tds.get(3).select("a").first();
								video.setVid(a.attr("href").replace("magnet:?xt=urn:btih:", "")); 
								if(tds.get(0).text().contains("电影")||!"N".equals(td.text()))
								{
									data.add(video);
								}
								
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				moreButton.setVisibility(View.VISIBLE);
				AbDialogUtil.removeDialog(Mp4BaActivity.this);
				if (paramList == null || paramList.size() == 0) {
					page--;
					if (page==0) {
						isad=false;
						moreLoading1();
						return;
					}
					AbToastUtil.showToast(Mp4BaActivity.this, "查询结果为空！多试试");
					return;
				}

				findViewById(R.id.ssLinearLayout1).setVisibility(View.VISIBLE);

				findViewById(R.id.mGridView).setVisibility(View.VISIBLE);
				List<Video> list = (List<Video>) paramList;
				allList.addAll(list);
				myListViewAdapter.notifyDataSetChanged();
				if (page < 2)
					closeInputMethod();
			}
		});
		mAbTask.execute(item);

	}

	private void search() {
		// TODO Auto-generated method stub
		moreButton.setVisibility(View.GONE);
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取电影列表...");
		AbTask mAbTask = new AbTask();

		String wd = word.getText().toString().trim();

		textView.setText("搜索："+wd);
		try {
			wd = URLEncoder.encode(wd, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		final String url = String.format(
				"http://www.meiyouad.com/torrent?kw=%s", wd);
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stubList<Video> data=new
				List<Video> data = new ArrayList<Video>(); 
				try {

					Document doc = Jsoup.connect(url)
							.userAgent(DyUtil.userAgent1).timeout(10000).get();

					Elements trs = doc.select("tbody > tr");
					for (Element tr : trs) {
						Elements tds = tr.select("td");
							Element td = tds.get(4);
						if (td != null) {
							//if (!"N".equals(td.text())) 
							{
								Video video = new Video();
								
								Element a = tds.get(1).select("a").first();
								video.setIntro(td.text());
								video.setId(a.attr("href").replace("/torrent/mp4ba/", "")); 
								video.setTitle(a.text().split("\\.X264")[0]); 
								a = tds.get(3).select("a").first();
								video.setVid(a.attr("href").replace("magnet:?xt=urn:btih:", "")); 
								//if(tds.get(0).text().contains("电影")||!"N".equals(td.text()))
								data.add(video);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				// moreButton.setVisibility(View.VISIBLE);
				AbDialogUtil.removeDialog(Mp4BaActivity.this);
				if (paramList == null || paramList.size() == 0) {
					AbToastUtil.showToast(Mp4BaActivity.this, "查询结果为空！多试试");
					return;
				}
				List<Video> list = (List<Video>) paramList;
				allList.clear();
				allList.addAll(list);
				myListViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item);
	}

	private void closeInputMethod() {
		InputMethodManager imm = (InputMethodManager) abApplication
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen) {
			// imm.toggleSoftInput(0,
			// InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
			imm.hideSoftInputFromWindow(word.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
