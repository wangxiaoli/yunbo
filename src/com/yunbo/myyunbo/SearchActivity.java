package com.yunbo.myyunbo;

import java.util.ArrayList;
import java.util.List;

import com.ab.activity.AbActivity; 
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.task.AbTaskListener;
import com.ab.util.AbImageUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.control.SearchUtil;
import com.yunbo.mode.Dydata;
import com.yunbo.mode.Dyres;
import com.yunbo.mode.History;
import com.yunbo.mode.Node;
import com.yunbo.mode.Res;
import com.yunbo.mode.Resdata;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends AbActivity {

	private ListView mListView = null;
	private LayoutInflater mInflater;
	private BaseAdapter myListViewAdapter;
	private ArrayList<Node> allList = new ArrayList<Node>();

	private String wd = "";
	private LinearLayout loadingpro;
	private TextView textpage;
	private TextView showtext;
	private Button searchBtn;

	private int page = 0;

	private int sum = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setAbContentView(R.layout.activity_search);
		mListView = (ListView) this.findViewById(R.id.mListView);
		textpage = (TextView) this.findViewById(R.id.textpage);
		showtext = (TextView) this.findViewById(R.id.textView2);
		loadingpro = (LinearLayout) this.findViewById(R.id.loadingpro);
		searchBtn = (Button) this.findViewById(R.id.sreachBtn);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		wd = getIntent().getExtras().getString("key");

		TextView tx = (TextView) this.findViewById(R.id.textView1);
		tx.setText(wd);

		if (DyUtil.showVideo != null) {
			this.findViewById(R.id.infro).setVisibility(View.VISIBLE);
			AbImageLoader mAbImageDownloader = new AbImageLoader(this);
			mAbImageDownloader.setMaxWidth(180);
			mAbImageDownloader.setMaxHeight(240); 
			mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
			mAbImageDownloader.setErrorImage(R.drawable.image_error); 
			mAbImageDownloader.setEmptyImage(R.drawable.image_empty); 
			mAbImageDownloader.setLoadingView(this
					.findViewById(R.id.progressBar2));

			String imageUrl = DyUtil.showVideo.getImg().replace("\\/", "/");

			ImageView imageView1 = (ImageView) this
					.findViewById(R.id.imageView1);
			TextView actorText = (TextView) this.findViewById(R.id.actor);
			TextView directorText = (TextView) this.findViewById(R.id.director);
			TextView typeText = (TextView) this.findViewById(R.id.type);
			TextView infro = (TextView) this.findViewById(R.id.textinfro);
			mAbImageDownloader.display(imageView1, imageUrl);
			String arter = "主演：";
			for (int i = 0; i < DyUtil.showVideo.getActor().size(); i++) {
				arter = arter + " "
						+ DyUtil.showVideo.getActor().get(i).getName();
			}
			actorText.setText(arter.trim());
			String type = "";
			for (int i = 0; i < DyUtil.showVideo.getArea().size(); i++) {
				type = type + " " + DyUtil.showVideo.getArea().get(i).getName();
			}
			for (int i = 0; i < DyUtil.showVideo.getType().size(); i++) {
				type = type + " " + DyUtil.showVideo.getType().get(i).getName();
			}
			for (int i = 0; i < DyUtil.showVideo.getTags().size(); i++) {
				if (!type.contains(DyUtil.showVideo.getTags().get(i).getName()))
					type = type + " "
							+ DyUtil.showVideo.getTags().get(i).getName();
			}
			typeText.setText(type.trim());
			String director = "导演：";
			for (int i = 0; i < DyUtil.showVideo.getDirector().size(); i++) {
				director = director + " "
						+ DyUtil.showVideo.getDirector().get(i).getName();
			}
			directorText.setText(director.trim());
			infro.setText(DyUtil.showVideo.getIntro());
		}

		mListView.setAdapter(myListViewAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					// 使用自定义的list_items作为Layout
					convertView = mInflater.inflate(R.layout.sear_item, parent,
							false);
				}
				TextView itemsText = AbViewHolder
						.get(convertView, R.id.tx_item);
				itemsText.setText(allList.get(position).getTitle());
				// itemsText.setTextColor(R.color.dodgerblue);
				// itemsText.setTextColor(Color.parseColor("#1E90FF"));
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
				if (AbStrUtil.isEmpty(allList.get(position).getThunder())) {
					DyUtil.urlToPlay = allList.get(position).getUrl();
				} else
					DyUtil.nodePlay = allList.get(position);
				// DyUtil.
				// getPlayUrl(allList.get(position).getThunder(),SearchActivity.this);
				// AbToastUtil.showToast(SearchActivity.this,
				// playList.get(position));
				HistoryUtil.data=new History();
				HistoryUtil.data.setName(allList.get(position).getTitle());
				Intent intent = new Intent(SearchActivity.this,
						PlayUrlActivity.class);
				startActivity(intent);
			}
		});
		textpage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refreshTask();
			}
		});
		textpage.setVisibility(View.GONE);
		searchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textpage.setVisibility(View.GONE);
				searchBtn.setVisibility(View.GONE);
				searchMovie();
			}
		});
		// searchMovie();
		/**/
		handler.sendEmptyMessageDelayed(1, 200);
	}

	int count = 0;
	int listsum = 0;

	private void searchMovie() {
		// TODO Auto-generated method stub
		loadingpro.setVisibility(View.VISIBLE);

		count = 0;
		listsum = 0;
		allList.clear();
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListener() {

			@Override
			public void get() {
				// TODO Auto-generated method stub

				List<String> urls;
				int countSS=6;
				
				show("正在搜索2tu.cc");
				urls = SearchUtil.get2tucc(wd);
				listsum += urls.size();
				if (!isUp)
					return;
				addListGvodUrls(urls);

				show("正在搜索3344"+"  "+listsum+"/"+countSS);
				urls = SearchUtil.getys3344(wd);
				listsum += urls.size();
				if (!isUp)
					return;
				addList(urls);
				// System.out.println(listsum);

				show("正在搜索飘花电影"+"  "+listsum+"/"+countSS);
				urls = SearchUtil.getpiaohua(wd);
				listsum += urls.size();
				if (!isUp)
					return;
				addList(urls);
				// System.out.println(listsum);

				show("正在搜索电影天堂"+"  "+listsum+"/"+countSS);
				urls = SearchUtil.getygdy8(wd);
				listsum += urls.size();
				if (!isUp)
					return;
				// System.out.println(listsum);
				addList(urls);
				
				
				urls = SearchUtil.getdygod_net(wd);
				listsum += urls.size();
				if (!isUp)
					return;
				addList(urls);
				// System.out.println(listsum);
				
				
				if (listsum == 0) {
					try {
						Dyres dyres = DyUtil.getDyres(wd);
						if (dyres != null && dyres.getData() != null
								&& dyres.getData().getRes() != null) {
							for (Res res : dyres.getData().getRes()) {
								Node node = new Node();
								node.setTitle(res.getTitle());
								node.setUrl(res.getUrl());
								allList.add(node);
								if (!isUp)
									return;
								handler.post(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										myListViewAdapter
												.notifyDataSetChanged();
									}
								});
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

				// urls = SearchUtil.getdy558_com(wd);
				// listsum += urls.size();if (!isUp) return;
				// addListthunderHref(urls, "thunderResTitle");
				// System.out.println(listsum);
				
				
				show("正在搜索a67。com"+"  "+listsum+"/"+countSS);
				urls = SearchUtil.geta67_com(wd);
				listsum += urls.size();
				if (!isUp)
					return;
				//addList(urls);
				addListthunderHref(urls, "title");
			}

			@Override
			public void update() {
				// TODO Auto-generated method stub
				if (!isUp)
					return;
				if (listsum == 0) {
					refreshTask();
				}
				loadingpro.setVisibility(View.GONE);
			}

		});
		mAbTask.execute(item);
	}

	protected void addList(List<String> urls) {
		// TODO Auto-generated method stub
		int c = 0;
		for (final String url : urls) {
			c++;
			if (c > 1)
				return;
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {

						List<String> ftps = SearchUtil.getFtp(url);
						for (String ftp : ftps) {
							allList.add(SearchUtil.getNode(ftp));
						}
						if (!isUp)
							return;
						handler.sendEmptyMessage(0);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
	}

	protected void addListthunderHref(List<String> urls, final String title) {
		// TODO Auto-generated method stub
		int c = 0;
		for (final String url : urls) {
			c++;
			if (c > 1)
				return;
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {

						allList.addAll(SearchUtil.getthunderHref(url, title));
						if (!isUp)
							return;
						if (allList.size() > 0) {
							handler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});
			thread.start();
		}
	}
	
	protected void addListGvodUrls(List<String> urls) {
		// TODO Auto-generated method stub
		int c = 0;
		for (final String url : urls) {
			c++;
			if (c > 1)return;
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {

						allList.addAll(SearchUtil.getGvodUrls(url));
						if (!isUp)
							return;
						if (allList.size() > 0) {
							handler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});
			thread.start();
		}
	}

	public void refreshTask() {
		if (!isUp)
			return;
		page++;
		if (page > sum) {
			AbToastUtil.showToast(SearchActivity.this, "没有了！");
			return;
		}
		loadingpro.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				Dydata data = DyUtil.getDydata(page, wd);
				List<Node> all=new ArrayList<Node>();
				if (data != null) {
					sum = data.getData().getTotal_page();
					all.addAll(data.getData().getNodes());
					if(page>1)
					return all;
				}
				if (page==1) {
					all.addAll(DyUtil.getp2psearch(wd));
					if (all.size()>0) {
						return all;
					}
				}
						
				return null;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				if (!isUp)
					return;
				loadingpro.setVisibility(View.GONE);
				textpage.setVisibility(View.VISIBLE);
				if (paramList == null) {
					page--;
					return;
				}
				if (paramList.size() == 0) {
					AbToastUtil.showToast(SearchActivity.this, "查询结果为空！");
					return;
				}
				textpage.setText("↓ " + page + "/" + sum);
				List<Node> list = (List<Node>) paramList;
				allList.addAll(list);
				for (int i = 0; i < list.size(); i++) {
					// getPlayUrl(dy.getData().getNodes().get(i));
				}
				myListViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item);

	}

	boolean isUp = true;

	@Override
	protected void onPause() {
		isUp = false;
		super.onPause();
	}

	@Override
	protected void onResume() {
		isUp = true;
		super.onResume();
		loadingpro.setVisibility(View.GONE);
		myListViewAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		isUp = false;
		super.onDestroy();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				myListViewAdapter.notifyDataSetChanged();
				count++;
				if (count >= listsum) {
					//loadingpro.setVisibility(View.GONE);
					if (allList.size() == 0) {
						refreshTask();
					}
				}
				((TextView)SearchActivity.this.findViewById(R.id.textView1))
			.setText(wd+"  ("+allList.size()+"/"+listsum+")");
				break;
			case 1:

				if (DyUtil.showVideo == null) {
					refreshTask();
				} else {
					searchBtn.setVisibility(View.GONE);
					searchMovie();
				}

				DyUtil.showVideo = null;
				break;
			case 2:
				showtext.setText(msg.obj.toString());
				break;
			}

		}
	};

	private void show(String text) {
		Message msg=new Message();
		msg.what=2;
		msg.obj=text;
		handler .sendMessage(msg);
	}
}
