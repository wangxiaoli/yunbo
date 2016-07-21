package com.yunbo.myyunbo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ab.activity.AbActivity;
import com.ab.util.AbBase64;
import com.ab.util.AbStrUtil;
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

public class TwoAvActivity extends AbActivity {

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
				Urldata data = showList.get(position); 
				DyUtil.toGetPlayUrl(data.getUrl(), TwoAvActivity.this);
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

		((TextView)findViewById(R.id.textView2)).setText("AV");
		((TextView)findViewById(R.id.textView3)).setText("");
		initList();

	}

	private void initList() { 

		try {
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open("toav.txt"), "utf-8");
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line; 
			allList.clear();
			while ((line = bufReader.readLine()) != null) {
				line=line.trim();
				if (!AbStrUtil.isEmpty(line) &&line.length()>40 ) {
					Urldata data=new Urldata();
					data.setMsg(line.substring(40));
					data.setUrl(line.substring(0, 40));
			allList.add(data); 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		allList.get(0).setUrl("BEAC0405B6FA370601EAC81AD6D00C5D1E09D23B");
		allList.get(0).setMsg("nhdt469");
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
