package com.yunbo.myyunbo;
 
import java.io.BufferedOutputStream;
import java.io.File; 
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List; 

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ab.activity.AbActivity;
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.util.AbDialogUtil; 
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil; 
import com.yunbo.mode.Film;  

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FilmActivity extends AbActivity {

	private ListView mListView = null;
	private LayoutInflater mInflater;
	private BaseAdapter myListViewAdapter;
	private ArrayList<Film> allList = new ArrayList<Film>();

	private LinearLayout loadingpro;
	private EditText textpage;
	private EditText sstext;

	private Button button3;

	private int page = 1;
	private String tempHtmlpath;
	private String webUrl="http://jav-library.com/";
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setAbContentView(R.layout.activity_film);
		webUrl="http://javtotal.com/";
		tempHtmlpath = getCacheDir() + File.pathSeparator + "temp.html";
		mListView = (ListView) this.findViewById(R.id.mListView);
		textpage = (EditText) this.findViewById(R.id.editText1);
		sstext = (EditText) this.findViewById(R.id.editText2);
		textpage.setText("1");
		loadingpro = (LinearLayout) this.findViewById(R.id.loadingpro);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAbImageDownloader = new AbImageLoader(this);
		//mAbImageDownloader.setMaxWidth(735);
		//mAbImageDownloader.setMaxHeight(550);
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setEmptyImage(R.drawable.image_empty);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);

		Button gobutton = (Button) this.findViewById(R.id.button1);
		mListView.setAdapter(myListViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					// 使用自定义的list_items作为Layout
					convertView = mInflater.inflate(R.layout.item_film1, parent,
							false);
				}
				String imageUrl = getItem(position).getImg();
				TextView itemsText = AbViewHolder
						.get(convertView, R.id.itemsTitle);
				TextView itemsTop = AbViewHolder
						.get(convertView, R.id.itemsTop);
				ImageView itemsIcon = AbViewHolder
						.get(convertView, R.id.itemsIcon);
				TextView itemsl = AbViewHolder
						.get(convertView, R.id.itemsl);
				TextView itemsr = AbViewHolder
						.get(convertView, R.id.itemsr);
				itemsText.setText(allList.get(position).getName());
				itemsTop.setText(allList.get(position).getInfro().split("\\|")[1]);
				itemsl.setText(allList.get(position).getInfro().split("\\|")[0].split("#")[0]);
				itemsr.setText(allList.get(position).getInfro().split("\\|")[0].split("#")[1]);
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
			public Film getItem(int position) {
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
				show(allList.get(position).getUrl(),allList.get(position).getTags() );
			}
		});
		gobutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				allList.clear();
				page = Integer.parseInt(textpage.getText().toString()) - 1;
				doGo();
			}

		});
		(button2 = (Button) findViewById(R.id.button2))
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doGo();
			}

		});
		(button3 = (Button) findViewById(R.id.button3))
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				page = 1;
				webUrl="http://javtotal.com/";
				try {
					_S="?s="+URLEncoder.encode(sstext.getText().toString().trim(), "UTF-8");//.toLowerCase()+"&searchsubmit=U";
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				doGo();
			}

		});
		page = 1;_S="";
		webUrl="http://javtotal.com/category/censored/tag/downloadable/";
		doGo();
	}

	Button button2;

	protected void show(final String url,final List<String> list) {
		// TODO Auto-generated method stub
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取内容...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {
			boolean iscontextok = true;

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub 
				try {

					Document doc = Jsoup.connect(url).timeout(30000).get();
					 
					try {
						Element temp23 = doc.select("article").first();
						//temp23.select("noindex").remove();
						temp23.select("footer").remove();
						temp23.select(".rpbt_shortcode").remove();
						temp23.select(".awac-wrapper").remove();
						//temp23.select("#comments").remove();
						String content = temp23.html(); 
						File file = new File(tempHtmlpath);
						if (!file.exists()) {
							file.createNewFile();
						}
						BufferedOutputStream bi = new BufferedOutputStream(
								new FileOutputStream(file));
						bi.write(content.getBytes("utf-8"));
						bi.flush();
						bi.close();

					} catch (Exception e) {
						// TODO: handle exception
						iscontextok = false;
						e.printStackTrace();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub

				AbDialogUtil.removeDialog(FilmActivity.this);
 
				if (!iscontextok) {
					AbToastUtil.showToast(FilmActivity.this, "查询结果为空！");
					return;
				} 
				View mView = mInflater.inflate(R.layout.dia_list, null);
				ListView listView = (ListView) mView
						.findViewById(R.id.listView1);
				final String[] mStrings =  new String[list.size()] ;
				for (int i = 0; i < list.size(); i++) {
					mStrings[i]=list.get(i);
				} 
				ArrayAdapter<String> listViewAdapter;
				listView.setAdapter(listViewAdapter = new ArrayAdapter<String>(
						FilmActivity.this, R.layout.dialog_list_item_1,
						mStrings));
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) { 
						String key = mStrings[position];
						if (position!=0) {
							page=1;_S="";//key=key.toLowerCase();
							try {//http://javtotal.com/tag/migd/
								webUrl="http://javtotal.com/tag/"+URLEncoder.encode(key, "UTF-8").toLowerCase()+"/";
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
							textpage.setText(""+page);
							doGo();
							AbDialogUtil.removeDialog(FilmActivity.this);
							return;
						}
						Bundle bundle = new Bundle();
						bundle.putString("key", key); 
						Intent intent = new Intent(FilmActivity.this,
								Ed2kSSActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
				WebView mWebView = (WebView) mView.findViewById(R.id.webView1);
				if (iscontextok) {
					WebSettings webSettings = mWebView.getSettings();
					webSettings.setDefaultTextEncodingName("utf-8");

					webSettings.setLoadWithOverviewMode(true);
					webSettings.setUseWideViewPort(true);
					mWebView.setWebViewClient(new WebViewClient(){

						@Override
						public boolean shouldOverrideUrlLoading(WebView view, String url) {
							// TODO Auto-generated method stub
							if (url.startsWith("magnet:?xt=urn:btih:")) {

								String hash=DyUtil.changeED2K(url); 
									DyUtil.toGetPlayUrl(hash, FilmActivity.this);
							}
							return true;
						}}); 
					mWebView.loadUrl("file://" + tempHtmlpath);
					
				} else {
					mWebView.setVisibility(View.GONE);
				}
				AbDialogUtil.showAlertDialog(mView);
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
			imm.hideSoftInputFromWindow(textpage.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	private String _S="";

	private void doGo() {
		// TODO Auto-generated method stub 
		
		loadingpro.setVisibility(View.VISIBLE);
		button2.setVisibility(View.GONE);
		handler.sendEmptyMessageDelayed(0, 500);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {
			boolean isnext=true;
			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				if (page<=0) {
					page=1;
				}
				String url = webUrl
						+ (page == 1 ? "" : "page/" + page + "/")+_S;
				try {

					Document doc = Jsoup.connect(url).timeout(30000).userAgent(DyUtil.userAgent1)
							.referrer("http://javtotal.com/").get();
					Elements articles = doc.select(".td-block-span4");
					Element next=doc.select(".td-icon-menu-right").first();
					isnext=next!=null;
					if (articles.size() <= 0) {
						return null;
					}
					List<Film> films = new ArrayList<Film>();
					for (Element element : articles) {
						Element a = element.select("a").first();
						Film film = new Film();
						String name=a.attr("title");
						film.setName(name);
						name=name.split(" ")[0];//.replace("[", "");
						film.getTags().add(name);
						film.setUrl(a.attr("abs:href"));
						a = element.select(".mR2 > .rDate").first();
						String infro=element.select(".seederC").first().text()
								+"#"+element.select(".leacherC").first().text();
						film.setInfro(infro+"|"+name+" "+a.text().trim());
						Element img = element.select("img").first();
						film.setImg(img.attr("abs:src"));
						Elements tags = element.select("a[href^=http://javtotal.com/tag/]");
						for (Element tag : tags) 
							if(!"Downloadable".equals(tag.text()))
							film.getTags().add(tag.text()); 
						films.add(film);
					}
					return films;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				loadingpro.setVisibility(View.GONE);
				button2.setVisibility(isnext?View.VISIBLE:View.GONE);
				closeInputMethod();
				if (paramList == null) {
					 
					return;
				}
				if (paramList.size() == 0) {
					AbToastUtil.showToast(FilmActivity.this, "查询结果为空！");
					return;
				}
				page++;
				
				textpage.setText("" + page);
				List<Film> list = (List<Film>) paramList;
				if (page==2)allList=new ArrayList<Film>();
				allList.addAll(list);
				myListViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				closeInputMethod();
				break;
			}
		}
	};

}
