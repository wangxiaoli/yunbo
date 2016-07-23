package com.yunbo.myyunbo;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; 
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ab.activity.AbActivity;
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener; 
import com.ab.util.AbCharacterParser; 
import com.ab.util.AbDialogUtil;  
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;  
import com.ab.util.AbViewHolder;  
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.frame.DoubleScaleImageView;
import com.yunbo.mode.Nvyou;
import com.yunbo.mode.Video;
import com.yunbo.mode.XFplayurl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView; 
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText; 
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView; 
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CaoPooActivity extends AbActivity {

	private GridView mGridView = null;
	private GridView fGridView = null;
	private ArrayList<Nvyou> allList = new ArrayList<Nvyou>();
	private ArrayList<Nvyou> showList = new ArrayList<Nvyou>();
	private BaseAdapter myGridViewAdapter;
	private ArrayList<String> imgList = new ArrayList<String>();
	private BaseAdapter fGridViewAdapter;
	private ImageButton moreButton;
	private EditText word;

	private Button up  ;
	private Button next ;
	private  TextView textView  ; 
	private View convertView ;
	private View show;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_caopoo);

		mGridView = (GridView) this.findViewById(R.id.mGridView);
		fGridView = (GridView) this.findViewById(R.id.mGridView2);
		word = (EditText) findViewById(R.id.editText1); 
		  up = (Button)  findViewById(R.id.left_btn);
		  next = (Button)  findViewById(R.id.right_btn);
		  textView = (TextView)  
				 findViewById(R.id.title_choices); 
			convertView= findViewById(R.id.probar);
			show= findViewById(R.id.show);
			show.setVisibility(View.GONE);
	//	((TextView) this.findViewById(R.id.textView1)).setText("美女套图");
//		(findViewById(R.id.imageView1)).setVisibility(View.GONE);

		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);
		//homeurl=String.format(homeurl, ""+page);
		 (findViewById(R.id.ssLinearLayout1)).setVisibility(View.GONE);
		mAbImageDownloader = new AbImageLoader(this);
		mAbImageDownloader.setMaxWidth(1800);
		mAbImageDownloader.setMaxHeight(1800);
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setEmptyImage(R.drawable.image_empty);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);
		/**/
		mGridView.setAdapter(myGridViewAdapter = new BaseAdapter() {
			
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
		});
		

		fGridView.setAdapter(fGridViewAdapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					// 使用自定义的list_items作为Layout
					convertView = mInflater.inflate(R.layout.item_xdy, parent,
							false);
				}
				String imageUrl = getItem(position);
				TextView itemsText = AbViewHolder
						.get(convertView, R.id.itemsTitle);
				itemsText.setVisibility(View.GONE);
				ImageView itemsIcon = AbViewHolder
						.get(convertView, R.id.itemsIcon);
				itemsIcon.setScaleType(ScaleType.FIT_CENTER); 
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
			public String getItem(int position) {
				// TODO Auto-generated method stub
				return imgList.get(position);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return imgList.size();
			}
		});
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				 nvyou=(Nvyou)myGridViewAdapter.getItem(position) ; 
 
show.setVisibility(View.VISIBLE);

convertView .setVisibility(View.GONE);
setpicsrc();
				 
			}
		}); 
		(findViewById(R.id.imagess))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
 
						search();
					}
				}); 
		//read( );
		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreLoading();
			}

		});
		up.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub  
				
			}
		});
		up.setVisibility(View.GONE);
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				DyUtil.play(CaoPooActivity.this, nvyou.getUrl(), nvyou.getName()+".mp4",true,true);
			}

		}); 
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {  
			//closeInputMethod(); 
				moreLoading( );
			}
		}, 100); 
		
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
 
	Nvyou nvyou; 
			private void setpicsrc(  ) {
				// TODO Auto-generated method stub
				 String first=nvyou.getImg();
				 imgList.clear();
				 imgList.add(first);
				 for (int i = 2; i < 5; i++) 
				 imgList.add(first.replace("/1.jpg", String.format("/%d.jpg", i))); 
				 textView.setText(nvyou.getName());
				 fGridViewAdapter.notifyDataSetChanged();
			}
	 

	private void search() {
		// TODO Auto-generated method stub  
		String str= word.getText().toString().trim(); 
		if (TextUtils.isEmpty(str)) {
			AbToastUtil.showToast(this, "关键字不能为空。");
			return;
		}
		showList.clear();
		String wd = word.getText().toString().trim();
		try {
			wd = URLEncoder.encode(wd, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		homeurl = "/ajax.aspx?fun=list&key="+wd+"&PageNow=";
		page=0;
		moreLoading();
	}
 
	
	
	private void closeInputMethod() {
		InputMethodManager imm = (InputMethodManager) abApplication
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen) { 
			imm.hideSoftInputFromWindow(word.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	private int page=0;
	private String homeurl = "videos?page=";

	private String hostServer="http://www.caopoo77.com/";
	//private String imgServer="http://98.126.146.154:8081";
	private   String cookie="";
	private   void getcookie() {
		try { 
			URL url = new URL(hostServer);
			HttpURLConnection connection = (HttpURLConnection) url 
					.openConnection();

			connection.setRequestMethod("GET"); 
			connection.setRequestProperty("Accept", "*/*"); 
			  connection.setRequestProperty("Accept-Language", "zh-CN");
			  connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			  connection.setRequestProperty("DNT", "1"); 
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
			connection.setRequestProperty("Connection", "Keep-Alive"); 
			connection.setReadTimeout(30000);
			connection.setFollowRedirects(true);
			connection.getResponseCode(); 
			String cookieskey = "Set-Cookie";
			Map<String, List<String>> maps = connection.getHeaderFields();
			List<String> coolist = maps.get(cookieskey);

			for (String string : coolist) {
				String cc = string.split(";")[0];
				cookie=cookie+" "+cc+";";
				System.out.println(cc);
			} 
			  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public   void moreLoading( ){ 

		page++;
		moreButton.setVisibility(View.GONE);
		if (page < 2)
			closeInputMethod();
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取影片列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Nvyou> data = allList=new ArrayList<Nvyou>();
				//allList.clear();
				if (TextUtils.isEmpty(cookie)) {
					getcookie();
				}
				try {

					Document doc = Jsoup
							.connect(
									hostServer+homeurl+page)
							.userAgent(
									"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
							.timeout(20000).header("Accept-Language", "zh-CN,zh;q=0.8")
							.header("DNT", "1")
							.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
							.header("Accept-Encoding", "gzip, deflate, sdch")
							.header("Cache-Control", "max-age=0").referrer("http://www.caopoo77.com/")
							
							.header("Cookie", cookie).get();

					 Elements imgs=doc.select(".video_box > a > img");
					 for (Element element : imgs) {
						 //http://ziyuan.caoboo.com/media/videos/mp4/zp/20446.mp4
						 //http://ziyuan.caoboo.com/media/videos/tmb/61526/1.jpg
						 String title=element.attr("title");
						 String src=element.attr("abs:src");
						 String url=src.replace("tmb", "mp4/zp").replace("/1.jpg", ".mp4");
//							System.out.println(title);
//							System.out.println(src);
//							System.out.println(url);
//							System.out.println();
							Nvyou nvyou=new Nvyou(); 
								nvyou.setName(title);	
								nvyou.setUrl(url);
								nvyou.setImg(src);		
							allList.add(nvyou);
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
				AbDialogUtil.removeDialog(CaoPooActivity.this);
				moreButton.setVisibility(View.VISIBLE);
				if (paramList == null || paramList.size() == 0) {  
				page--;
						
					AbToastUtil.showToast(CaoPooActivity.this, "查询结果为空！多试试");
					return;
				}  
		showList.addAll(allList);
		myGridViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item); 
	}
	 
	
}
 
