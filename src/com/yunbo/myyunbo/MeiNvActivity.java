package com.yunbo.myyunbo;
 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList; 
import java.util.List; 
 













import java.util.Random;

import com.ab.activity.AbActivity;
import com.ab.image.AbImageLoader; 
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewHolder; 
import com.ab.view.pullview.AbMultiColumnListAdapter;
import com.ab.view.pullview.AbMultiColumnListView;
import com.ab.view.pullview.AbViewInfo;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.frame.DoubleScaleImageView;
import com.yunbo.mode.Nvyou;
import com.yunbo.mode.XFplayurl;

import android.content.Context;
import android.content.Intent; 
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView; 
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;  
import android.widget.GridView;
import android.widget.ImageView; 
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MeiNvActivity extends AbActivity {
 
	private ArrayList<Nvyou> allList = new ArrayList<Nvyou>();
	private ArrayList<Nvyou> showList = new ArrayList<Nvyou>(); 	
	private GridView mGridView = null;

	
	private BaseAdapter myGridViewAdapter;
	private EditText word;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null; 
	// 图片下载器
	private AbImageLoader mAbImageDownloader1 = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_meinv);

		mGridView = (GridView) this.findViewById(R.id.mGridView);
		word = (EditText) findViewById(R.id.editText1); 
		mAbImageDownloader = new AbImageLoader(this);
		mAbImageDownloader.setMaxWidth(360);
		mAbImageDownloader.setMaxHeight(600);
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setEmptyImage(R.drawable.image_empty);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);
		mAbImageDownloader1 = new AbImageLoader(this);
		mAbImageDownloader1.setMaxWidth(720);
		mAbImageDownloader1.setMaxHeight(1080);
		//mAbImageDownloader1.setLoadingImage(R.drawable.image_loading);
		//mAbImageDownloader1.setEmptyImage(R.drawable.image_empty);
		//mAbImageDownloader1.setErrorImage(R.drawable.image_error);
		/**/ 
		mGridView.setAdapter(myGridViewAdapter = new BaseAdapter() {
					
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						// TODO Auto-generated method stub

						if (convertView == null) {
							// 使用自定义的list_items作为Layout
							convertView = mInflater.inflate(R.layout.item_meinv, parent,
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
List<String>list=new ArrayList<String>();
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				index=0;
				list=new ArrayList<String>();
				final Nvyou nvyou=(Nvyou)myGridViewAdapter.getItem(position);

				String[] s = nvyou .getUrl().split("\\|");
				if (s.length>1) {
					for (int i = 1; i < s.length; i++) {
						list.add(s[i]);
					}
				}
				
				View view1 = mInflater.inflate(R.layout.dialog_image_button, null);
				Button up = (Button) view1.findViewById(R.id.left_btn);
				Button next = (Button) view1.findViewById(R.id.right_btn);
				final TextView textView = (TextView) view1
						.findViewById(R.id.title_choices);
				final DoubleScaleImageView image=(DoubleScaleImageView) view1
						.findViewById(R.id.imageView1);
				convertView=view1
						.findViewById(R.id.probar);
				textView.setText(nvyou.getName());
				up.setText("上一张");
				next.setText("下一张");
				//final WebView mWebView = (WebView) view1.findViewById(R.id.gitWebView);
				 
				up.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub 
						index--;
						setpicsrc(image,textView,nvyou.getName());
					}
				});
				next.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						index++;
						setpicsrc(image,textView,nvyou.getName());
					}

				}); 
				setpicsrc(image,textView,nvyou.getName());
				AbDialogUtil.showAlertDialog(view1);
			}
			int index=0;
			View convertView;
					private void setpicsrc( DoubleScaleImageView itemsIcon, TextView textView, String string) {
						// TODO Auto-generated method stub
						index=index+list.size();
						index=index%list.size();
						String imageUrl=list.get(index);
						textView.setText(string+"("+(index+1)+"/"+list.size()+")");
						itemsIcon.doFirst();
						mAbImageDownloader1.setLoadingView(convertView
								 );
						mAbImageDownloader1.display( itemsIcon, imageUrl);
						/*
						WebSettings webSettings = mWebView.getSettings();
						webSettings.setDefaultTextEncodingName("utf-8");
						mWebView.loadDataWithBaseURL(null,
								"<HTML><body bgcolor='#ffffff'><div align=center><IMG width='100%' src='"
										+ src + "'/></div></body></html>", "text/html",
								"UTF-8", null);*/
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
		}, 1000); 
		
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
					getResources().getAssets().open("mn1.txt"), "utf-8");
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
			if (s.length>1) {
				Nvyou nvyou=new Nvyou();
				//nvyou.setInfro(s[0]);
				nvyou.setUrl(list.get(i));
				nvyou.setName(s[0]);
				nvyou.setImg(s[1]);
				allList.add(nvyou);
			} 
		}
		Random random=new Random();
		ArrayList<Nvyou> tempsList=new ArrayList<Nvyou>();
		while (allList.size()>0) {
			int index=random.nextInt(allList.size());
			 Nvyou nvyou=allList.get(index);
			 tempsList.add(nvyou);
			 allList.remove(index);
		}
		allList=tempsList;
		showList.addAll(allList);
		myGridViewAdapter.notifyDataSetChanged();
	}
	

	 
}
 
