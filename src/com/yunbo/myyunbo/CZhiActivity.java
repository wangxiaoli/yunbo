package com.yunbo.myyunbo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbBase64;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.control.TvUtil;
import com.yunbo.control.UpUtil;
import com.yunbo.mode.Urldata;
import com.yunbo.mode.XFplayurl;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Context;
import android.content.Intent;

public class CZhiActivity extends AbActivity {

	private ListView mListView;
	private LayoutInflater mInflater;
	private BaseAdapter myListViewAdapter;

	private ArrayList<Urldata> allList = new ArrayList<Urldata>();

	private ArrayList<Urldata> showList = new ArrayList<Urldata>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_tvlive);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mListView = (ListView) this.findViewById(R.id.listView1);
		mListView.setAdapter(myListViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					// 使用自定义的list_items作为Layout
					convertView = mInflater.inflate(R.layout.tv_item, parent,
							false);
				}
				TextView itemsText = AbViewHolder
						.get(convertView, R.id.tx_item);
				itemsText.setText(getItem(position).getMsg());
				itemsText.setTextSize(12F);
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Urldata getItem(int position) {
				// TODO Auto-generated method stub
				return showList.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return showList.size();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Urldata data = showList.get(position); 
				final AbActivity packageContext=CZhiActivity.this;
				AbDialogUtil.showProgressDialog(packageContext,
						R.drawable.progress_circular, "正在获取播放链接...");
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						AbDialogUtil.removeDialog(packageContext);
						 String playurl=(String)obj;
						 if (!TextUtils.isEmpty(playurl)) {

								DyUtil.play(packageContext, playurl, data.getMsg());
						}else {
							AbToastUtil.showToast(packageContext, "没有播放地址，多试试。");
						}
					}

					@Override
					public Object getObject() {
						// TODO Auto-generated method stub 
						URL url;
						try { 
							String[] jx=new String[]{//"http://ik345api.duapp.com/youkuyun/1.php?v=%s"
									"http://proxy.dy208.com/youyun/%s",
									"http://www.zaogood.com/acfun.php?vid=%s&type=mp4"
						 	,"http://vipwobuka.bceapp.com/acfun87.php?vid=%s&type=mp4"
									,"http://www.jisuphp.com/0525/ac.php?id=%s"
							, "http://api.ourder.com/video/ssl/YkcrefHandler.ashx?id=%s",
									"http://vip.4410yy.com/acfun87.php?vid=%s&type=mp4",
									"http://ik345api.duapp.com/youkuyun/1.php?v=%s"};
							String urlString=String.format(jx[jxindex=jxindex%jx.length], data.getUrl());
							if (urlString.startsWith("http://proxy.dy208.com")) {
								urlString=urlString.replace("==", "");
							}
							url = new URL( urlString);
							jxindex++;
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection(); 
							connection.setConnectTimeout(8000);
							connection.setFollowRedirects(true);
							//connection.connect();
							connection.getResponseCode();
							return connection.getURL().toString();
						} catch (Exception e) {
							e.printStackTrace(); 
						}
						return null;
					}
				});

				mAbTask.execute(item);
			}
		});
		
		findViewById(R.id.textView1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}

				});
		findViewById(R.id.textView3).setVisibility(View.VISIBLE);

		((TextView)findViewById(R.id.textView2)).setText("优酷云C值资源");
		((TextView)findViewById(R.id.textView3)).setText("");
		initList();

	}
	private int jxindex=0;
	private void initList() { 

		try {
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open("C.txt"), "utf-8");
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line; 
			allList.clear();
			while ((line = bufReader.readLine()) != null) {
				line=line.trim();
				if (!AbStrUtil.isEmpty(line) ) {
					String[]ss=line.split("\\$");
					if (ss.length>1) {
						for (int i = 0; i < ss.length; i++) {
							if (ss[i].startsWith("C")) {
								
								Urldata data=new Urldata();
								try {
								if (i==1&&ss.length==2) {

									data.setMsg(ss[0]);
									data.setUrl(ss[i].endsWith("==")?ss[i]:ss[i]+"==");
							allList.add(data);
							continue;
								}	
								data.setMsg(ss[i+1]);
								data.setUrl(ss[i].endsWith("==")?ss[i]:ss[i]+"==");
						allList.add(data);
								} catch (Exception e) {
									// TODO: handle exception
								} 
						break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		List<Urldata>tempList=new ArrayList<Urldata>();
		Map<String, String>map=new HashMap<String, String>();
		for (Urldata urldata : allList) {
			if (!map.containsKey(urldata.getUrl())) {
				map.put(urldata.getUrl(), urldata.getUrl());
				tempList.add(urldata);
			}
		}
		allList.clear();
		allList.addAll(tempList);
		showList.clear();
		showList.addAll(allList);
		myListViewAdapter.notifyDataSetChanged();
		(findViewById(R.id.imagess))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub 
						search();
					}
				});
		((EditText) findViewById(R.id.editText1)).addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				search();
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// moreLoading();
				findViewById(R.id.imagess).setVisibility(View.GONE);
			}
		}, 100);
	}

	private void search() {
		String wd = ((EditText) findViewById(R.id.editText1)).getText()
				.toString().trim();
		showList.clear();
		if (TextUtils.isEmpty(wd)) {
			showList.addAll(allList);
		} else {

			ArrayList<Urldata> tempList = new ArrayList<Urldata>();
			for (Urldata i : allList) {
				if (i.getMsg().toLowerCase().contains(wd.toLowerCase())) {
					tempList.add(i);
				}
			}
			if (tempList.size() > 0) {
				showList.addAll(tempList);
			}
		}
		myListViewAdapter.notifyDataSetChanged();
	}
 

}
