package com.yunbo.myyunbo;

import java.io.IOException;
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
import com.yunbo.control.DyUtil; 
import com.yunbo.mode.Nvyou; 

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NvyouDetailActivity extends AbActivity {

	private GridView mListView = null;
	private LayoutInflater mInflater;
	private ArrayList<Nvyou> allList = new ArrayList<Nvyou>();

	private BaseAdapter myListViewAdapter;
	private ImageButton moreButton;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null;
	private int page = 1;
	private String src = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_zhainandaomain);
		Bundle bundle = this.getIntent().getExtras();
		((TextView) this.findViewById(R.id.textView1)).setText(bundle
				.getString("key"));
		src = bundle.getString("src");
		mListView = (GridView) this.findViewById(R.id.mGridView);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);

		mAbImageDownloader = new AbImageLoader(this);
		mAbImageDownloader.setMaxWidth(200);
		mAbImageDownloader.setMaxHeight(200);
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setEmptyImage(R.drawable.image_empty);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);
		/**/
		mListView.setAdapter(myListViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					// 使用自定义的list_items作为Layout
					convertView = mInflater.inflate(R.layout.item_list_120,
							parent, false);
				}
				ImageView itemsIcon = (ImageView) convertView
						.findViewById(R.id.itemsIcon);
				TextView itemsTitle = (TextView) convertView
						.findViewById(R.id.itemsTitle);
				TextView itemsChildTitle = (TextView) convertView
						.findViewById(R.id.itemsChildTitle);
				TextView itemsText = (TextView) convertView
						.findViewById(R.id.itemsText);
				// 获取该行的数据
				String imageUrl = getItem(position).getImg();
				itemsTitle.setText(getItem(position).getName());
				itemsChildTitle.setText(getItem(position).getUrl());
				itemsText.setText(getItem(position).getInfro());
				// 设置加载中的View
				mAbImageDownloader.setLoadingView(convertView
						.findViewById(R.id.progressBar));
				// 图片的下载
				mAbImageDownloader.display(itemsIcon, imageUrl);
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
				String key = allList.get(position).getName();
				Bundle bundle = new Bundle();
				bundle.putString("key", key);
				Intent intent = new Intent();
				intent.setClass(NvyouDetailActivity.this, Ed2kSSActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreLoading();
			}

		});
		/*
		 * ( findViewById(R.id.imageView1)).setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * Intent intent = new Intent();
		 * intent.setClass(ZhainandaoMainActivity.this, LanunyActivity.class);
		 * startActivity(intent); } });
		 */

		moreLoading();
	}

	private void moreLoading() {
		moreButton.setVisibility(View.GONE);
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取番号列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Nvyou> data = getPageContent(page);
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				if (!isend)
					moreButton.setVisibility(View.VISIBLE);
				AbDialogUtil.removeDialog(NvyouDetailActivity.this);

				if (paramList.size() == 0) {
					AbToastUtil.showToast(NvyouDetailActivity.this, "查询结果为空！");
					return;
				}
				page++;
				List<Nvyou> list = (List<Nvyou>) paramList;
				allList.addAll(list);
				myListViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item);

	}

	private boolean isend = false;
	private String endurl = "";
	private String seurl = "";

	protected List<Nvyou> getPageContent(int page) {
		// TODO Auto-generated method stub

		List<Nvyou> data = new ArrayList<Nvyou>();
//http://m.nanrenvip.com/tianhaiyi/list_1_2.html
		String url = seurl.replace("2.html", page+ ".html");
		if (page == 1) {
			url = endurl = "http://m.nanrenvip.com/" + src + "/";
		}

		try {
			Document doc = Jsoup.connect(url).userAgent(DyUtil.userAgent1)
					.timeout(10000).get();

			Elements divs = doc.select("div[class=post-inner]");
			for (Element div : divs) {

				Element post_thumb = div.select("div[class=post-media standard-img]").first();
				if (post_thumb != null) {

					Element img = post_thumb.select("img").first();
					if (img != null) {
						Nvyou nvyou = new Nvyou();
						nvyou.setImg(img.attr("file")); 
						
						 Element post_content = div.select("h2.entry-title")
								.first();
						if (post_content != null) {
							Element a = post_content.select("a").first();
							if (a != null)
								nvyou.setName(a.text());
							Element p = div.parent().select("ul.post-footer").first();
							if (p != null)
								nvyou.setInfro(p.text());
						}
						Element date = div.select("[class=post-date]")
								.first();
						if (date != null)
							nvyou.setUrl(date.text()); 
						data.add(nvyou);
					}
				}
			}
			if (page == 1) {
				Elements as = doc.select("div.pagination").first().select("a[href]");
				if (as.size() > 0) {//
					endurl = as.get(as.size() - 1).attr("abs:href");
					seurl = as.get(0).attr("abs:href");
				}
			}
			isend = url.equals(endurl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
