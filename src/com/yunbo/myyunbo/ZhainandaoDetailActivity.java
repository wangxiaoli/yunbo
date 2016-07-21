package com.yunbo.myyunbo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.ab.util.AbImageUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.mode.Dydata;
import com.yunbo.mode.Movie;
import com.yunbo.mode.Node;
import com.yunbo.mode.PageContent;
import com.yunbo.mode.Video;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ZhainandaoDetailActivity extends AbActivity {

	private ListView mListView = null;
	private LayoutInflater mInflater;
	private ArrayList<Video> allList = new ArrayList<Video>();

	private BaseAdapter myListViewAdapter;
	private ImageButton moreButton;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null;
	private int page = 1;
	private String src = "";
	private String text = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_zhainandaodetail);
		Bundle bundle = this.getIntent().getExtras();
		((TextView) this.findViewById(R.id.textView1)).setText(text=bundle
				.getString("key"));
		src = bundle.getString("src");
		mListView = (ListView) this.findViewById(R.id.listView1);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);

		mAbImageDownloader = new AbImageLoader(this);
		//mAbImageDownloader.setMaxWidth(425);
		//mAbImageDownloader.setMaxHeight(540);
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
					convertView = mInflater.inflate(R.layout.item_list1d,
							parent, false);
				}
				ImageView itemsIcon = (ImageView) convertView
						.findViewById(R.id.itemsIcon);
				TextView itemsTitle = (TextView) convertView
						.findViewById(R.id.itemsTitle);

				// 获取该行的数据
				String imageUrl = getItem(position).getImg();
				itemsTitle.setText(getItem(position).getTitle());
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
			public Video getItem(int position) {
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
				String key = allList.get(position).getTitle();
				Bundle bundle = new Bundle();
				bundle.putString("key", key);
				Intent intent = new Intent();
				intent.setClass(ZhainandaoDetailActivity.this,
						Ed2kSSActivity.class);
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
				List<Video> data = getPageContent(page);
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				if (!isend)
					moreButton.setVisibility(View.VISIBLE);
				AbDialogUtil.removeDialog(ZhainandaoDetailActivity.this);

				if (paramList.size() == 0) {
					AbToastUtil.showToast(ZhainandaoDetailActivity.this,
							"查询结果为空！");
					return;
				}
				page++;
				List<Video> list = (List<Video>) paramList;
				allList.addAll(list);
				myListViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item);

	}

	private boolean isend = false;
	private String endurl = "";

	protected List<Video> getPageContent(int page) {
		// TODO Auto-generated method stub

		List<Video> data = new ArrayList<Video>();

		String url = src.replace(".html", "_" + page + ".html");
		if (page == 1) {
			url = endurl = src;
		}

		Pattern pat = Pattern.compile("[a-zA-Z0-9]+[-_]?\\d+");

		try {
			Document doc = Jsoup.connect(url).userAgent(DyUtil.userAgent1)
					.timeout(10000).get();

			Elements ps = doc.select(".main-body > p");
			for (int i = 0; i < ps.size(); i++) {
				Element element = ps.get(i);
				String title = (element.text() + "").trim();
				/**/String[] ss = title.replace("番号", "|").split("\\|");
				if (ss.length > 1) {
					title = ss[ss.length - 1];
				}
				if (!AbStrUtil.isEmpty(title)) {
					if (i != 0) {
						element = ps.get(i - 1);
						Elements iss = element.select("img");
						if (iss.size() == 1) {
							String img = (iss.get(0).attr("src") + "").trim();
							Matcher mat = pat.matcher(title);
							if (mat.find()) {
								title = mat.group();
							}else {
								mat = pat.matcher(text);
								if (mat.find())  
									title = mat.group();
							}
							Video video = new Video();
							video.setTitle(title);
							video.setImg(img);//"http://www.zhainandao.com" + 
							data.add(video);
						}
					}
				}
			}
			//if (page == 1) 
			{
				Elements as = doc.select("div.link_pages > a[href]");
				if (as.size() > 0) {
					//endurl = as.get(as.size() - 1).attr("href");
					isend=!((as.get(as.size() - 1).text()+ "").trim().equals("下一页"));
				}
			}
			//isend = url.equals(endurl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
