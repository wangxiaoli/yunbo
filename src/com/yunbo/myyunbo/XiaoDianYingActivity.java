package com.yunbo.myyunbo;
 
import java.io.BufferedReader;
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

public class XiaoDianYingActivity extends AbActivity {

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

				//String key= ((Nvyou)myGridViewAdapter.getItem(position)).getName();  
				String url = ((Nvyou)myGridViewAdapter.getItem(position)).getUrl(); 

				HistoryUtil.isRecord = false;
				DyUtil.isLive = false;
				DyUtil.isCanDownload = true;
				DyUtil.fileList = new ArrayList<String>();
				DyUtil.newPlayObj = new XFplayurl();
				DyUtil.newPlayObj.setCookie("");
				DyUtil.playUrl = url;
				DyUtil.newPlayObj.setUrl(url);
				DyUtil.newPlayObj.setName(url.substring(url.lastIndexOf("/") + 1));
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
		read( );
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {  
			closeInputMethod(); 
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
	

	public   void read( ){ 
			List<String> list = new ArrayList<String>();
		try {
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open("xdyall.txt"), "utf-8");
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line;
			while ((line = bufReader.readLine()) != null) {
				if (!AbStrUtil.isEmpty(line) && line.contains("|")) {
					list.add(line.trim());
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		for ( int i=list.size()-1;i>=0;i--) { 
				String[] s = list.get(i) .split("\\|");
			if (s.length==4) {
				Nvyou nvyou=new Nvyou();
				nvyou.setInfro(s[0]);
				nvyou.setUrl(s[1]);
				nvyou.setName(s[2]);
				nvyou.setImg(s[3]);
				allList.add(nvyou);
			} 
		}
		showList.addAll(allList);
		myGridViewAdapter.notifyDataSetChanged();
	}
}
 
