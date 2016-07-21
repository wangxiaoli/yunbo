package com.yunbo.myyunbo;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
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

public class TaoTuActivity extends AbActivity {

	private GridView mGridView = null;
	private ArrayList<Nvyou> allList = new ArrayList<Nvyou>();
	private ArrayList<Nvyou> showList = new ArrayList<Nvyou>();
	private BaseAdapter myGridViewAdapter;
	private ImageButton moreButton;
	private EditText word;

	private Button up  ;
	private Button next ;
	private  TextView textView  ;
	private DoubleScaleImageView image ;
	private View convertView ;
	private View show;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_taotu);

		mGridView = (GridView) this.findViewById(R.id.mGridView);
		word = (EditText) findViewById(R.id.editText1); 
		  up = (Button)  findViewById(R.id.left_btn);
		  next = (Button)  findViewById(R.id.right_btn);
		  textView = (TextView)  
				 findViewById(R.id.title_choices);
		  image=(DoubleScaleImageView)  
				 findViewById(R.id.imageView1);
			convertView= findViewById(R.id.probar);
			show= findViewById(R.id.show);
			show.setVisibility(View.GONE);
	//	((TextView) this.findViewById(R.id.textView1)).setText("美女套图");
//		(findViewById(R.id.imageView1)).setVisibility(View.GONE);

		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);
		//homeurl=String.format(homeurl, ""+page);
		//(findViewById(R.id.ssLinearLayout1)).setVisibility(View.GONE);
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
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				 nvyou=(Nvyou)myGridViewAdapter.getItem(position) ;
				String url = (nvyou).getUrl(); 
list=geturllist(url);
index=0;
show.setVisibility(View.VISIBLE);
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
				index--;
				setpicsrc();
			}
		});
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				index++;
				setpicsrc( );
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

	int index=0; 
	Nvyou nvyou;
	List<String >list;
			private void setpicsrc(  ) {
				// TODO Auto-generated method stub
				index=index+list.size();
				index=index%list.size();
				String imageUrl=imgServer+list.get(index);
				textView.setText(nvyou.getName()+"("+(index+1)+"/"+list.size()+")");
				image.doFirst();
				mAbImageDownloader .setLoadingView(convertView
						 );
				mAbImageDownloader .display( image, imageUrl);
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
	private String homeurl = "/ajax.aspx?fun=list&PageNow=";

	private String hostServer="http://98.126.146.154";
	private String imgServer="http://98.126.146.154:8081";
	public   void moreLoading( ){ 

		page++;
		moreButton.setVisibility(View.GONE);
		if (page < 2)
			closeInputMethod();
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取美女套图列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Nvyou> data = allList=new ArrayList<Nvyou>();
				//allList.clear();

				try {
					Document doc = Jsoup.connect(hostServer+homeurl+page).userAgent(DyUtil.userAgent1)
							.timeout(10000).get();
					Elements lis = doc.select("TempTableName") ;
					for (Element element : lis) {  
try {
	
						Nvyou nvyou=new Nvyou(); 
						Element title = element.select("title").first();
						if (title != null) {
							nvyou.setName(title.text());					 
						} 
						Element imgurl = element.select("imgurl").first();
						if (imgurl != null) {
							nvyou.setUrl(imgurl.text().replace(" ", "%20"));					 
						}
						Element topurl = element.select("topurl").first();
						if (topurl != null) {
							nvyou.setImg(imgServer+topurl.text().replace(" ", "%20"));					 
						} 
						nvyou.setName(nvyou.getName()+"【"+geturllist(nvyou.getUrl()).size()+"P】");
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
				AbDialogUtil.removeDialog(TaoTuActivity.this);
				moreButton.setVisibility(View.VISIBLE);
				if (paramList == null || paramList.size() == 0) {  
				page--;
						
					AbToastUtil.showToast(TaoTuActivity.this, "查询结果为空！多试试");
					return;
				}  
		showList.addAll(allList);
		myGridViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item); 
	}
	
	private List<String> geturllist(String url) {
		ArrayList<String>list=new  ArrayList<String>();
		for (String string : url.split("\\|")) {
			if (!TextUtils.isEmpty(string)) {
				list.add(string);
			}
		}
		return list;
	}
	
}
 
