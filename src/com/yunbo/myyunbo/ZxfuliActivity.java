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
import com.ab.util.AbBase64;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class ZxfuliActivity extends AbActivity {
 
	private AbActivity myContext=this;
	//private GridView fGridView = null;
	private ArrayList<Nvyou> allList = new ArrayList<Nvyou>();
	private ArrayList<Nvyou> showList = new ArrayList<Nvyou>();  
	private BaseAdapter fGridViewAdapter;
	private BaseAdapter myGridViewAdapter;
	private ImageButton moreButton; 
	private AbImageLoader mAbImageDownloader = null; 
	private String[] types=new String[]{"1,电影","2,电视剧","3,福利","4,综艺","5,午夜","6,音乐","7,动漫","8,成人","9,恐怖"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_zxfl);
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
				 view1.findViewById(R.id.two_btn_panel).setVisibility(View.GONE);
				 textView.setText("搜索");;
					 view1
						.findViewById(R.id.one_btn_panel).setVisibility(View.VISIBLE); 
						one.setText("搜索"); urlText.setHint("关键字");
						one.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								
								String s=
										urlText.getText().toString().trim();
								if (TextUtils.isEmpty(s)) {
									AbToastUtil.showToast(myContext,"搜索关键字不能为空");
									return;
								}
								try {
									s=URLEncoder.encode(s, "utf-8");
								} catch (Exception e) {
									// TODO: handle exception
								}
								 urlString = "http://"+host+"/s/"+s+"/页码.html";
								showList = new ArrayList<Nvyou>(); 
								isSearch=true;
								page=0;
								index=-1;
								myGridViewAdapter.notifyDataSetChanged();
								moreLoading();
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
						intent.setClass(ZxfuliActivity.this, PlayUrlActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
						//finish();
					}
				});
		 //findViewById(R.id.button1).setVisibility(View.GONE); 

		GridView gridView = (GridView) findViewById(R.id.mGridView);
		 
		    myGridViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = new TextView(ZxfuliActivity.this);
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
				return types[position ].split(",")[1];
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
				isSearch=false;
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
					final int position, long id) {
				// TODO Auto-generated method stub
				AbDialogUtil.showProgressDialog(myContext,
						R.drawable.progress_circular, "正在获取播放列表链接...");
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {
					String showtext = showList.get(position).getName();
					String nameT = showList.get(position).getName();

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						AbDialogUtil.removeDialog(myContext);
						final List<String> alls = (List<String>) obj;
						if (obj == null || alls.size() == 0) {
							AbToastUtil.showToast(myContext,
									"没有播放链接，多试试或者请看其它影片！");
						} else {

							View mView = myContext.mInflater.inflate(
									R.layout.dia_list_text_pro, null);
							final View pb=  mView
									.findViewById(R.id.progressBar1);
							pb.setVisibility(View.GONE);
							ListView listView = (ListView) mView
									.findViewById(R.id.listView1);
							final TextView textView1 = (TextView) mView
									.findViewById(R.id.textView1);
							if (AbStrUtil.isEmpty(showtext)) {
								textView1.setVisibility(View.GONE);
							} else {
								textView1.setText(showtext);
							}
							String[] mStrings = new String[alls.size()];
							for (int i = 0; i < alls.size(); i++) {
								mStrings[i] = alls.get(i).split("\\|")[0];
							}
							ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
									myContext, R.layout.dialog_list_item_1,
									mStrings);
							listView.setAdapter(listViewAdapter);
							listView.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									pb.setVisibility(View.VISIBLE);

									String url = alls.get(position)
											.split("\\|")[1];

									String name = alls.get(position).split(
											"\\|")[0];
									textView1.setText("正在解析" + name);
									Yy97Util.jiexi(myContext, textView1, url,
											nameT + name+"\n"+showtext,pb);
								}
							});
							AbDialogUtil.showAlertDialog(mView);
						}
					}

					@Override
					public Object getObject() {
						// TODO Auto-generated method stub
						String url = showList.get(position).getUrl();
						System.out.println(url);
						List<String> list = new ArrayList<String>();
						try {
							Document doc = Jsoup.connect(url)
									.userAgent(DyUtil.userAgent1)
									.followRedirects(true).timeout(3000).get();

							Elements as = doc.select(".dslist-group-item > a");
							 for (Element a : as) {
								list.add(a.text()+"|zxfl$"+a.attr("abs:href")+"$zxfl");
							}
							 showtext=doc.select(".summary").text();
							return list;
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						return list;
					}
				});

				mAbTask.execute(item);
			}
		});
		moreLoading( );
	}
	private int homeindex=-2;
	private int index=homeindex;
	private int page=0;
	private String host="www.zxfuli.com";
	private String urlString="http://"+host;
	private boolean	isSearch=false;

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
					if(!isSearch&&index>-1)
					  urlString="http://"+host+"/type/"+types[index].split(",")[0]+"/页码.html";
					Document doc = Jsoup
							.connect( urlString.replace("页码",""+page))
							.userAgent(
									"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
							.timeout(20000).header("Accept-Language", "zh-CN,zh;q=0.8")
							.header("Origin:", "http://"+host)
							.header("Accept", "*/*")
							.header("Accept-Encoding", "gzip, deflate, sdch")
							.header("Cache-Control", "max-age=0")
							.referrer("http://"+host)
							//.header("Cookie", cookie)
							.get();
					
					Elements ms=doc.select(".movie-item") ;
								for (Element m : ms) {
									
								 
					 try { 
									 Element a=m.select("a").first();
									 Element i=a.select("img").first();
							Nvyou nvyou=new Nvyou(); 
								nvyou.setName(a.attr("title"));	 
								nvyou.setUrl(a.attr("abs:href"));
								nvyou.setImg(i.attr("abs:src"));		
							allList.add(nvyou);
											 
								
							 
						} catch (Exception e) {
							e.printStackTrace();
						}  }
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
					fGridViewAdapter.notifyDataSetChanged();
					return;
				}  if(index==homeindex)moreButton.setVisibility(View.GONE);
		showList.addAll(allList);
		fGridViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item); 
	}
}
