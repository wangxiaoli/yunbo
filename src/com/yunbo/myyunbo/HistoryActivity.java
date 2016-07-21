package com.yunbo.myyunbo;

import java.util.ArrayList; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ab.activity.AbActivity; 
import com.ab.util.AbDialogUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.mode.History;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;  
import android.content.Context;
import android.content.Intent;

public class HistoryActivity extends AbActivity {

	private ListView mListView;
	private LayoutInflater mInflater;
	private BaseAdapter myListViewAdapter;

	private ArrayList<History> allList = new ArrayList<History>();

	private ArrayList<String> likes = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_history);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mListView = (ListView) this.findViewById(R.id.listView1);
		mListView.setAdapter(myListViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					// 使用自定义的list_items作为Layout
					convertView = mInflater.inflate(R.layout.his_item, parent,
							false);
				}
				TextView itemsText = AbViewHolder
						.get(convertView, R.id.textView1);
				ImageView itemslike = AbViewHolder
						.get(convertView, R.id.imagelike);
				String name=allList.get(position).getName();
				String hash=allList.get(position).getUrl();
				itemslike.setVisibility(islike(hash)?View.VISIBLE:View.GONE);
				/*if (name.startsWith("/")&&name.length()>1) {
					name=name.substring(1);
				}
				if (name.startsWith(".")&&name.length()>1) {
					name=name.substring(1);
				}
				if (name.startsWith("[")&&!name.endsWith("]")&&name.contains("]")) {
					try {
						name=name.substring(name.indexOf("]")+1);
					} catch (Exception e) {
						// TODO: handle exception
						name=name.substring(name.indexOf("]"));
					}					
				}
				if (name.startsWith("【")&&!name.endsWith("】")&&name.contains("】")) {
					try {
						name=name.substring(name.indexOf("】")+1);
					} catch (Exception e) {
						// TODO: handle exception
						name=name.substring(name.indexOf("】 "));
					}					
				}
				if (name.startsWith("/")&&name.length()>1) {
					name=name.substring(1);
				}
				if (name.startsWith(".")&&name.length()>1) {
					name=name.substring(1);
				}
				name=name.replace("飘花电影piaohua.com", "");
				name=name.replace("飘花电影www.piaohua.com", "");
				name=name.replace("a67手机电影a67.com", "");
				*/
				//System.out.println(name);
				itemsText.setText(name);

				TextView itemsTextsub = AbViewHolder
						.get(convertView, R.id.textView2);
				itemsTextsub.setText(allList.get(position).getDate()
						+" 播放至 "+HistoryUtil.GetTime(allList.get(position).getSeek()));
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
				HistoryUtil.data=allList.get(position);//new History();
				//HistoryUtil.data.setName(allList.get(position).getName());
				//DyUtil.playUrl=null;//allList.get(position).getUrl();
				//HistoryUtil.seek=allList.get(position).getSeek();
				//PlayUrlActivity.isHistory=true;
				//Intent intent = new Intent(HistoryActivity.this,PlayUrlActivity.class); 
				//startActivity(intent);
				AbDialogUtil.showAlertDialog(getView());
			}
		});
		findViewById(R.id.textView1)
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		
	}
	private boolean islike(String url) {
		for (String hash	 : likes) {
			if (hash.equals(url)) {
				return true;
			}
		}
		return false;
	}

	Pattern pat = Pattern.compile("[a-zA-Z]+[-]?\\d+");
	View mView;
	private View getView() {
		mView = mInflater.inflate(R.layout.dia_list_text, null);
		ListView listView = (ListView) mView.findViewById(R.id.listView1);
		TextView textView1 = (TextView) mView.findViewById(R.id.textView1);
		textView1.setText("请选择一个操作");
		String hash=HistoryUtil.data.getUrl();
		String name=HistoryUtil.data.getName();
		textView1.setText(name);
		String[]itemstrs=new String[] { "继续播放",// 0
						"删除所选",// 1 
				};
		boolean like=islike(hash);
		Matcher mat = pat.matcher(name);
		if (mat.find()) {
			name = mat.group();
		}else {
			name=null;
		}
		final ArrayList<String> list=new ArrayList<String>();
		list.add("继续播放");
		list.add("删除所选");
		if(name!=null)
			list.add("搜索："+name);
		final String key=name;
		if(!like)list.add("标记喜欢");
		itemstrs=new String[list.size()];
		for (int i = 0; i < itemstrs.length; i++) 
			itemstrs[i]=list.get(i);
		ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
				R.layout.dialog_list_item_1,itemstrs );
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0){
					DyUtil.playUrl=null; 
					HistoryUtil.seek=HistoryUtil.data.getSeek();
					DyUtil.toGetPlayUrl3(HistoryUtil.data.getUrl(), HistoryUtil.data.getName(), HistoryActivity.this);
				}
				if (position == 1){
					AbDialogUtil.removeDialog(HistoryActivity.this); 
					HistoryUtil.delete(HistoryActivity.this );
					HistoryUtil.read(HistoryActivity.this);
					setList();
				}
				if (position == 2){
					if (!list.get(position).equals("标记喜欢")) {
						Bundle bundle = new Bundle();
						bundle.putString("key", key);
						Intent intent = new Intent();
						intent.setClass(HistoryActivity.this,
								Ed2kSSActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}else {
						dolike();
					}
				}
				if (position == 3){
					dolike();
				}
			}

			private void dolike() {
				// TODO Auto-generated method stub
				//HistoryUtil.likes.add(HistoryUtil.data.getUrl());
				HistoryUtil.saveLike(HistoryActivity.this);
				HistoryUtil.readLike(HistoryActivity.this);
				likes=HistoryUtil.likes;
				AbDialogUtil.removeDialog(HistoryActivity.this);
				myListViewAdapter.notifyDataSetChanged();
				
			}
		});
		return mView;
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		((TextView)findViewById(R.id.textView3)).setText("");
		setList();
		if (HistoryUtil.likes==null) {
			HistoryUtil.likes=new ArrayList<String>();
		}
		if (HistoryUtil.likes.size()==0) {
			HistoryUtil.readLike(this);
		}
		likes=HistoryUtil.likes;
	}


	private void setList() {
		HistoryUtil.init(this);
		allList= new ArrayList<History>();
		for (int i = HistoryUtil.allList.size()-1; i >=0; i--) {
			allList.add(HistoryUtil.allList.get(i));
		}
		myListViewAdapter.notifyDataSetChanged();
	} 

}
