package com.yunbo.myyunbo;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ab.util.AbBase64;
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
import android.text.TextUtils;
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

public class LongFeng4Activity extends AbActivity {

	private ListView mListView = null;
	private LayoutInflater mInflater;
	private ArrayList<Video> allList = new ArrayList<Video>();
	private ArrayList<Video> showList = new ArrayList<Video>();

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_mp4ba); 
		mListView = (ListView) this.findViewById(R.id.listView1);
		textView = (TextView) this.findViewById(R.id.textView1);
		textView.setText("最新精品av");
		word = (EditText) findViewById(R.id.editText1);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		show= findViewById(R.id.show);
		show.setVisibility(View.GONE);
		 
		jx115 = (Button)  findViewById(R.id.button1);
		cili = (Button)  findViewById(R.id.button2);
		cili.setText("播放");
		final GridView gridView = (GridView)  findViewById(R.id.mGridView);
		gridView.setVisibility(View.GONE);
		//findViewById(R.id.ssLinearLayout1).setVisibility(View.GONE);
		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);

		mAbImageDownloader = new AbImageLoader(this);
//		mAbImageDownloader.setMaxWidth(180);
//		mAbImageDownloader.setMaxHeight(240);
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
					convertView = mInflater.inflate(R.layout.item_film, parent,
							false);
				}
				String imageUrl = getItem(position).getImg();
				TextView itemsText = AbViewHolder
						.get(convertView, R.id.itemsTitle);
				TextView itemsTop = AbViewHolder
						.get(convertView, R.id.itemsTop);
				ImageView itemsIcon = AbViewHolder
						.get(convertView, R.id.itemsIcon);
				itemsText.setText(showList.get(position).getTitle());
				itemsTop.setText(showList.get(position).getVid());
				// 设置加载中的View
				mAbImageDownloader.setLoadingView(convertView
						.findViewById(R.id.progressBar));
				// 图片的下载
				mAbImageDownloader.display( itemsIcon, imageUrl);
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
				return showList.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return showList.size();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DyUtil.showVideo = showList.get(position);
				show.setVisibility(View.VISIBLE);
				if (TextUtils.isEmpty(DyUtil.showVideo.getIntro().trim())) {
					//DyUtil.toGetPlayUrl3(DyUtil.showVideo.getId(),DyUtil.showVideo.getVid(),LongFeng4Activity.this);
					//return;
					webview.loadUrl("http://javtotal.com/"+DyUtil.showVideo.getRating().toLowerCase()+"/");
				}else {
					
				webview.loadDataWithBaseURL(null,
						"<HTML><body bgcolor='#f3f3f3'><div align=center>"
								+AbBase64.decode(DyUtil.showVideo.getIntro() , "utf-8") + "</div></body></html>", "text/html",
						"UTF-8", null); 
				}
//				if ("N".equals(DyUtil.showVideo.getIntro())) {
					gridView.setVisibility(View.GONE);
					jx115.setVisibility(View.VISIBLE);
					jx115.setText("搜索："+DyUtil.showVideo.getRating());
//				}
//				else {
//					gridView.setVisibility(View.VISIBLE);
//					jx115.setVisibility(View.VISIBLE);
//				}
			}
		});
		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreLoading();
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
//		if (LanunyActivity.totalKeys != null
//				&& LanunyActivity.totalKeys.length > 0) {
//			String[] ss = LanunyActivity.totalKeys;
//			word.setText(ss[new Random().nextInt(ss.length)]);
//		}
		
		webview = (WebView) findViewById(R.id.webview);
		progressbar = (ProgressBar) findViewById(R.id.progressBar1); 
		final int sum=4;
		final BaseAdapter myGridViewAdapter=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null){
					convertView=new TextView(LongFeng4Activity.this);
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
				if (url.startsWith("magnet:?xt=urn:btih:")) {

					String hash=DyUtil.changeED2K(url); 
						DyUtil.toGetPlayUrl(hash, LongFeng4Activity.this);
				}
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
					DyUtil.toGetPlayUrl3(DyUtil.showVideo.getId(),DyUtil.showVideo.getVid(),LongFeng4Activity.this);
					 
				}
			}); 
			jx115.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Bundle bundle = new Bundle();
					bundle.putString("key", DyUtil.showVideo.getRating());
					Intent intent = new Intent();
					intent.setClass(LongFeng4Activity.this, Ed2kSSActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
					
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
        webview.getSettings().setUserAgentString(DyUtil.userAgent4);
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

	private void moreLoading() {
		page++;
		moreButton.setVisibility(View.GONE);
//		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
//				"正在获取电影列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Video> data = new ArrayList<Video>();
				 Map<String, String>map=new HashMap<String, String>();

					try {
						InputStreamReader inputReader = new InputStreamReader(
								getResources().getAssets().open("youzhi.txt"), "utf-8");
						BufferedReader bufReader = new BufferedReader(inputReader);
						String line;
						while ((line = bufReader.readLine()) != null) {
							if (!AbStrUtil.isEmpty(line) && line.contains("|")) {
								String strs[]=line.split("\\|");
								try {
									if (map.containsKey(strs[0])) {
										continue;
									}
	Video video = new Video();
								 
								video.setIntro(strs[5]);
								video.setId(strs[2]); 
								video.setTitle(strs[3]); 
								video.setVid(strs[1]); 
								video.setImg(strs[4]); 
								video.setRating(strs[0]);
								map.put(strs[0], strs[2]);
								data.add(video);  
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
						
						bufReader.close();
						// UpUtil.upgif(getApplicationContext(), list);
					} catch (Exception e) {
						e.printStackTrace();
					} 
							
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				//moreButton.setVisibility(View.VISIBLE);
//				AbDialogUtil.removeDialog(LongFeng4Activity.this);
				if (paramList == null || paramList.size() == 0) {
					page--;
					AbToastUtil.showToast(LongFeng4Activity.this, "查询结果为空！多试试");
					return;
				}
				List<Video> list = (List<Video>) paramList;
				allList.addAll(list);
				showList.addAll(list);
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
		String wd = word.getText().toString().trim();

		textView.setText("搜索："+wd); 
		wd=(""+wd).toLowerCase();
		showList=new ArrayList<Video>();
		for (Video video : allList) {
			if ((""+video.getTitle()).toLowerCase().contains(wd)) {
				showList.add(video);
			}
		}
		myListViewAdapter.notifyDataSetChanged();
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
