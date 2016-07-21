package com.yunbo.myyunbo;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
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
import com.ab.util.AbCharacterParser; 
import com.ab.util.AbDialogUtil;  
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;  
import com.ab.util.AbViewHolder; 
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView; 
import android.widget.BaseAdapter;
import android.widget.EditText; 
import android.widget.GridView;
import android.widget.ImageView; 
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DianYingShenQiActivity extends AbActivity {

	private GridView mGridView = null;
	private ArrayList<Nvyou> allList = new ArrayList<Nvyou>();
	private ArrayList<Nvyou> showList = new ArrayList<Nvyou>();
	private BaseAdapter myGridViewAdapter;
	private EditText word;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_xiaodianying);

		mGridView = (GridView) this.findViewById(R.id.mGridView);
		word = (EditText) findViewById(R.id.editText1); 
		((TextView) this.findViewById(R.id.textView1)).setText(page==30?"vip":"影片");
//		(findViewById(R.id.imageView1)).setVisibility(View.GONE);
		homeurl=String.format(homeurl, ""+page);
		(findViewById(R.id.ssLinearLayout1)).setVisibility(View.GONE);
		mAbImageDownloader = new AbImageLoader(this);
		mAbImageDownloader.setMaxWidth(360);
		mAbImageDownloader.setMaxHeight(270);
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

				 String key= ((Nvyou)myGridViewAdapter.getItem(position)).getName();  
				String url = ((Nvyou)myGridViewAdapter.getItem(position)).getUrl(); 

				if (url.startsWith("magnet")) {
					url=DyUtil.changeED2K(url); 
					DyUtil.jiexiIndex=4;
					DyUtil.toGetPlayUrl(url, DianYingShenQiActivity.this);
					return;
				}
				
				HistoryUtil.isRecord = false;
				DyUtil.isLive = false;
				DyUtil.isCanDownload = true;
				DyUtil.fileList = new ArrayList<String>();
				DyUtil.newPlayObj = new XFplayurl();
				DyUtil.newPlayObj.setCookie("");
				DyUtil.playUrl = url;
				DyUtil.newPlayObj.setUrl(url);
				DyUtil.newPlayObj.setName(key);
				Intent intent = new Intent(getApplicationContext(), VideoViewBuffer.class);
				startActivity(intent);
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
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {  
			//closeInputMethod(); 
				read( );
			}
		}, 100); 
		
	}

	 

	private void search() {
		// TODO Auto-generated method stub  
		String str= word.getText().toString().trim(); 
		showList.clear();
		 if (TextUtils.isEmpty(str)) {
			showList.addAll(allList);
		}else {
			
		 for(Nvyou nvyou:allList)	
		 {
			 if (nvyou.getName().contains(str)) {
				showList.add(nvyou);
			}
		 }
		}
		 myGridViewAdapter.notifyDataSetChanged();
		 
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
	
    public static int page=30;
	private String homeurl = "http://aqw894774.gotoip2.com/?s=vod-show-id-%s.html";
	public   void read( ){ 
		
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

				try {
					Document doc = Jsoup.connect(homeurl).userAgent(DyUtil.userAgent1)
							.timeout(10000).get();
					Elements uls=doc.select("div[class=vodlist_l box]").first().select("ul");
					for (Element ul : uls) {
try {
	
						Nvyou nvyou=new Nvyou(); 
						String text=ul.select("p.content").text();
						int start=text.indexOf("f:'")+3;
						int end=text.indexOf("'", start);
						nvyou.setUrl(text.substring(start, end));
						nvyou.setName(ul.select("h2 > a").text());
						nvyou.setImg(ul.select("img.lazy").first().attr("abs:data-original"));
						allList.add(nvyou);
} catch (Exception e) {
	// TODO: handle exception
}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub 
				AbDialogUtil.removeDialog(DianYingShenQiActivity.this);
				if (paramList == null || paramList.size() == 0) { 
					if(homeurl.contains("gotoip2")){
						homeurl=homeurl.replace("aqw894774.gotoip2.com", "mp4tv.net");
						allList=new ArrayList<Nvyou>();
						read();
						return;
					}
						
					AbToastUtil.showToast(DianYingShenQiActivity.this, "查询结果为空！多试试");
					return;
				}  
		showList.addAll(allList);
		myGridViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item); 
	}
}
 
