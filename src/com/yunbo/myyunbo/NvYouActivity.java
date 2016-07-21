package com.yunbo.myyunbo;
 
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunbo.control.DyUtil;  
import com.yunbo.mode.Nvyou;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NvYouActivity extends AbActivity {

	private ListView mListView = null; 
	private ArrayList<Nvyou> allList = new ArrayList<Nvyou>();

	private NvyouListAdapter myListViewAdapter; 
	private EditText word;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_nvyou);

		mListView = (ListView) this.findViewById(R.id.listView1);
		word = (EditText) findViewById(R.id.editText1); 
		mAbImageDownloader = new AbImageLoader(this);
		mAbImageDownloader.setMaxWidth(208);
		mAbImageDownloader.setMaxHeight(290);
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setEmptyImage(R.drawable.image_empty);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);
		/**/
		mListView.setAdapter(myListViewAdapter = new NvyouListAdapter(this,new ArrayList<Nvyou>(),mAbImageDownloader));
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String key= myListViewAdapter.getItem(position).getName(); 
				String src= myListViewAdapter.getItem(position).getUrl();  
				Bundle bundle = new Bundle();
				bundle.putString("key",key);
				bundle.putString("src",src);
				Intent intent = new Intent();
				intent.setClass(NvYouActivity.this, NvyouDetailActivity.class);
				intent.putExtras(bundle);
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
		if(allList.size()==0)
				 moreLoading();
		else {
			myListViewAdapter.updateListView(allList); 
			closeInputMethod();
		}
			}
		}, 100); 
		
	}

	private void moreLoading() {  
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取女优列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub 


				List<Nvyou> data = new ArrayList<Nvyou>();
				try {

					Document doc = Jsoup.connect("http://www.nanrenvip.com/find.html")
							.userAgent(DyUtil.userAgent1).timeout(10000).get();

					Elements divs = doc.getElementById("all").select("div[class=vote item box]");
								AbCharacterParser	characterParser = AbCharacterParser.getInstance();
					for (Element div : divs) {
						Element img = div.select("img").first();
						if (img != null) {
							Element a = img.parent();
							if (a.nodeName().equals("a")) {
								Nvyou nvyou = new Nvyou();
								nvyou.setUrl(a.attr("abs:href").replace("http://www.nanrenvip.com/", ""));
								nvyou.setImg(img.attr("abs:src")); 
								if (!nvyou.getImg().contains("upload")) {
									nvyou.setImg(""); 
								}
								Element name = div .select(".name").first();
								if (name != null&&name.text().length()>0) { 
									nvyou.setName(name.text());
									String pinyin = characterParser.getSelling(name.text());
									String sortString = pinyin.substring(0, 1).toUpperCase();
									
									// 正则表达式，判断首字母是否是英文字母
									if(sortString.matches("[A-Z]")){
										nvyou.setFirstLetter(sortString.toUpperCase());
									}else 
										nvyou.setFirstLetter("#"); 
								} else 
									nvyou.setFirstLetter("#");
								Element head = div .select("div.head").first();
								Element bottom = div .select("div.bottom").first();
								if (head != null&&bottom!= null) { 
									nvyou.setInfro(head.text()+"\n"+bottom.text());
								}
								data.add(nvyou);
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
				AbDialogUtil.removeDialog(NvYouActivity.this);
				if (paramList == null || paramList.size() == 0) { 
					AbToastUtil.showToast(NvYouActivity.this, "查询结果为空！多试试");
					return;
				}
				List<Nvyou> list = (List<Nvyou>) paramList;
				allList.clear();
				allList.addAll(list);
				myListViewAdapter.updateListView(allList); 
				closeInputMethod();
				if(allList.size()!=0)save( );
			}
		});
		mAbTask.execute(item);

	}

	private void search() {
		// TODO Auto-generated method stub  
		String filterStr= word.getText().toString().trim();
			//实例化汉字转拼音类
			AbCharacterParser characterParser = AbCharacterParser.getInstance();
			List<Nvyou> filterDateList = new ArrayList<Nvyou>();
			if(!TextUtils.isEmpty(filterStr)){
				for(Nvyou nvyou : allList){
					String name = ""+nvyou.getName();
					if(name.indexOf(filterStr) != -1 ||
							characterParser.getSelling(name).startsWith(filterStr)){
						filterDateList.add(nvyou);
					}
				}
			}
			
			// 根据a-z进行排序
			Collections.sort(filterDateList);
			myListViewAdapter.updateListView(filterDateList);
		 
	}

	public   void save( ) {
		// TODO Auto-generated method stub 
		SharedPreferences SAVE = this.getSharedPreferences("nvyou", this.MODE_PRIVATE);  
	    Editor editor = SAVE.edit(); 
	    Gson gson = new Gson();  
	    String json = gson.toJson(allList); 
	    editor.putString("list",json);  
	    editor.commit(); 
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

		SharedPreferences SAVE = this.getSharedPreferences("nvyou", this.MODE_PRIVATE);  
		String json=SAVE.getString("list",null); 
		if (!AbStrUtil.isEmpty(json)) {
		    Gson gson = new Gson();  
			allList=gson.fromJson(json,  
	                new TypeToken<List<Nvyou>>() {  
	                }.getType());  
		}if (allList==null)  			
			allList = new ArrayList<Nvyou>(); 
	}
}

class NvyouListAdapter extends BaseAdapter implements SectionIndexer{
	private List<Nvyou> list = null;
	private Context mContext;
	private AbImageLoader mAbImageDownloader = null;
	
	public NvyouListAdapter(Context mContext, List<Nvyou> list,AbImageLoader mAbImageDownloader) {
		this.mContext = mContext;
		this.list = list;
		this.mAbImageDownloader=mAbImageDownloader;
	}

	public int getCount() {
		return this.list.size();
	}

	public Nvyou getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final Nvyou nvyou = list.get(position);
		if (view == null) { 
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_list_200, null); 
			viewHolder.itemsIcon = (ImageView) view
				.findViewById(R.id.itemsIcon);
			viewHolder. itemsTitle = (TextView) view
				.findViewById(R.id.itemsTitle);
			viewHolder. itemsChildTitle = (TextView) view
				.findViewById(R.id.itemsChildTitle);
			viewHolder. itemsText = (TextView) view
					.findViewById(R.id.itemsText);
			viewHolder.itemsImage = (RelativeLayout) view
					.findViewById(R.id.itemsImage);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// 获取该行的数据
		String imageUrl = getItem(position).getImg().replace("static.nanrenvip.com", "static.0717dey.com");
		viewHolder.itemsTitle.setText(getItem(position).getName());
		 
		viewHolder.itemsChildTitle.setText(getItem(position).getUrl());
		viewHolder.itemsText.setText(getItem(position).getInfro());
		if ( TextUtils.isEmpty(getItem(position).getImg())) {
			viewHolder.itemsImage.setVisibility(View.GONE);
		}else {

			viewHolder.itemsImage.setVisibility(View.VISIBLE);
		// 设置加载中的View
		mAbImageDownloader.setLoadingView(view
				.findViewById(R.id.progressBar));
		// 图片的下载
		mAbImageDownloader.display(viewHolder.itemsIcon, imageUrl);
		}
		//根据position获取分类的首字母的Char ascii值
		//int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		/*if(position == getPositionForSection(section)){
			viewHolder.cityLetter.setVisibility(View.VISIBLE);
			viewHolder.cityLetter.setText(nvyou.getFirstLetter());
		}else{
			viewHolder.cityLetter.setVisibility(View.GONE);
		}
	 
		*/
		return view;

	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<Nvyou> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	

	final static class ViewHolder {

		ImageView itemsIcon  ;
		TextView itemsTitle  ;
		TextView itemsChildTitle ;
		TextView itemsText   ;
		RelativeLayout itemsImage;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getFirstLetter().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getFirstLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
