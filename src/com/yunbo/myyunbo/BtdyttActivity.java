package com.yunbo.myyunbo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
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
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.control.Yy97Util;
import com.yunbo.mode.Nvyou;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class BtdyttActivity extends AbActivity {
 
	private AbActivity myContext=this;
	private GridView fGridView = null;
	private ArrayList<Nvyou> allList = new ArrayList<Nvyou>();
	private ArrayList<Nvyou> showList = new ArrayList<Nvyou>();  
	private BaseAdapter fGridViewAdapter;
	private ImageButton moreButton; 
	private AbImageLoader mAbImageDownloader = null; 
	private String[] types=new String[]{"国产","港台","欧美","日韩","海外","动画"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_btdy);
		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);
		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreLoading();
			}

		});findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				View view1 = mInflater.inflate(R.layout.dialog_edit_button, null);
				final Button one = (Button) view1.findViewById(R.id.one_btn);  
				final TextView textView = (TextView) view1
						.findViewById(R.id.title_choices); 
				final EditText urlText = (EditText) view1.findViewById(R.id.editTextzb);
				 view1
					.findViewById(R.id.two_btn_panel).setVisibility(View.GONE);
				 textView.setVisibility(View.GONE);
					 view1
						.findViewById(R.id.one_btn_panel).setVisibility(View.VISIBLE); 
						one.setText("播放"); urlText.setHint("40位哈希");
						one.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								String url = "" + urlText.getText().toString().trim();
								if ("福利".equals(url)) {
									Intent intent = new Intent();
									Bundle bundle = new Bundle();
									bundle.putBoolean("ismain", true);
									intent.setClass(BtdyttActivity.this, PlayUrlActivity.class);
									intent.putExtras(bundle);
									startActivity(intent);
								} else if(Pattern.matches("[0-9a-fA-F]{40}", url)){
									url=url.toLowerCase();
									DyUtil.toGetPlayUrl(url, myContext);
								}
							}

						});  AbDialogUtil.showAlertDialog(view1);
			}

		});
	mAbImageDownloader = new AbImageLoader(this);
	mAbImageDownloader.setMaxWidth(300);
	mAbImageDownloader.setMaxHeight(300);
	mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
	mAbImageDownloader.setEmptyImage(R.drawable.image_empty);
	mAbImageDownloader.setErrorImage(R.drawable.image_error);
		((Button) findViewById(R.id.button1))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putBoolean("ismain", true);
						intent.setClass(BtdyttActivity.this, PlayUrlActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
						//finish();
					}
				});
		 findViewById(R.id.button1).setVisibility(View.GONE); 

		GridView gridView = (GridView) findViewById(R.id.mGridView);
		 
		final BaseAdapter myGridViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = new TextView(BtdyttActivity.this);
				}
				TextView tv = (TextView) convertView;
				if (index == position) {
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
				return types[position ];
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return types.length;
			}
		};
		gridView.setAdapter(myGridViewAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				index = position;
				myGridViewAdapter.notifyDataSetChanged();
				page=0;
				showList = new ArrayList<Nvyou>(); 
				moreLoading();
			}
		});
GridView fGridView = (GridView) findViewById(R.id.fGridView);
		 
		fGridViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					// 使用自定义的list_items作为Layout
					convertView = mInflater.inflate(R.layout.item_xdy, parent,
							false);
				}
				String imageUrl = getItem(position).getImg();
				TextView itemsText = AbViewHolder
						.get(convertView, R.id.itemsTitle); 
				ImageView itemsIcon = AbViewHolder
						.get(convertView, R.id.itemsIcon);
				itemsIcon.setScaleType(ScaleType.FIT_CENTER);
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
		};
		fGridView.setAdapter(fGridViewAdapter);
		fGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DyUtil.play(myContext, showList.get(position).getUrl(), showList.get(position).getName(),false);
			}
		});
		moreLoading( );
	}
	private int index=0;
	private int page=0;

	public   void moreLoading( ){ 

		page++;
		moreButton.setVisibility(View.GONE); 
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取影片列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Nvyou> data = allList=new ArrayList<Nvyou>(); 
				try {

					Document doc = Jsoup
							.connect(
									"http://btmovie.wx.jaeapp.com/QueryMovie?page="+page
									+ "&pageSize=20&type="+URLEncoder.encode(types[index]+"电影", "utf-8"))
							.userAgent(
									"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
							.timeout(20000).header("Accept-Language", "zh-CN,zh;q=0.8")
							.header("Origin:", "http://btdytt.xyz")
							.header("Accept", "*/*")
							.header("Accept-Encoding", "gzip, deflate, sdch")
							.header("Cache-Control", "max-age=0")
							.referrer("http://btdytt.xyz/index.html")
							//.header("Cookie", cookie)
							.get();
					
					String html=doc.select("body") .first().html();
					html=html.replace("\\\"", "\"");
					html=html.replace("\"}\"", "\"}");
					html=html.replace("\"{\"", "{\"");
					System.out.println(html);
					 try {
							if (null != html) {
								JSONObject newsObject = new JSONObject(html);
								JSONArray body = newsObject.getJSONArray("body"); 
								for (int i = 0; i < body.length(); i++) {  
									JSONObject mov = body.getJSONObject(i);

												 

							Nvyou nvyou=new Nvyou(); 
								nvyou.setName(mov.getString("name").split("\\.")[0]);	
								nvyou.setUrl("http://btmovie.wx.jaeapp.com/MoviePlayWeb.m3u8?movieid="+
										mov.getString("objectId")+ "&user=nieiqmhj997038@sina.com");
								nvyou.setImg(mov.getString("mainImageUrl"));		
							allList.add(nvyou);
											 
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}  
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} 
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub 
				AbDialogUtil.removeDialog(myContext);
				moreButton.setVisibility(View.VISIBLE);
				if (paramList == null || paramList.size() == 0) {  
				page--;
						
					AbToastUtil.showToast(myContext, "查询结果为空！多试试");
					return;
				}  
		showList.addAll(allList);
		fGridViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item); 
	}
}
