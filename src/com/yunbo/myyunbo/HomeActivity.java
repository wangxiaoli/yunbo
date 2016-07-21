package com.yunbo.myyunbo;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ab.activity.AbActivity;
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.mode.Dydata;
import com.yunbo.mode.Movie;
import com.yunbo.mode.NameEnc;
import com.yunbo.mode.Node;
import com.yunbo.mode.PageContent;
import com.yunbo.mode.Video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HomeActivity extends AbActivity {

	private ListView mListView = null;
	private LayoutInflater mInflater;
	private ArrayList<Video> allList = new ArrayList<Video>();

	private BaseAdapter myListViewAdapter;
	private ImageButton moreButton;
	private EditText word;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null;
	private int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_home);

		mListView = (ListView) this.findViewById(R.id.listView1);
		word = (EditText) findViewById(R.id.editText1);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);

		mAbImageDownloader = new AbImageLoader(this);
		mAbImageDownloader.setMaxWidth(180);
		mAbImageDownloader.setMaxHeight(240);
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
					convertView = mInflater.inflate(R.layout.item_list, parent,
							false);
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
				itemsTitle.setText(getItem(position).getTitle());
				String arter = "";
				for (int i = 0; i < getItem(position).getActor().size(); i++) {
					arter = arter + " "
							+ getItem(position).getActor().get(i).getName();
				}
				itemsChildTitle.setText(arter);
				itemsText.setText(getItem(position).getIntro());
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
				DyUtil.showVideo = allList.get(position);
				final AbActivity abActivity = HomeActivity.this;
				AbDialogUtil.showProgressDialog(abActivity,
						R.drawable.progress_circular, "正在获取播放列表链接...");
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {
					String  showtext="";
					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						// if ( isBack)return;
						AbDialogUtil.removeDialog(abActivity);
						if (obj == null) {
							AbToastUtil.showToast(abActivity,
									"没有播放链接，多试试或者请看其它影片！");
						} else {
							PlayUrlActivity.togetqqfxurl((String) obj,
									abActivity,showtext);
						}
					}

					@Override
					public Object getObject() {
						// TODO Auto-generated method stub
						String url = DyUtil.showVideo.getId();
						try {
							Document doc = Jsoup.connect(url)
									.userAgent(DyUtil.userAgent1)
									.followRedirects(true).timeout(10000).get();

							Elements div=doc.select("div.endtext");
						    if (div.size()>0) {
								showtext=div.first().text();
							}
							Elements as = doc.select("a[href]");
							for (Element a : as) {
								String href = a.attr("href");
								if (href.startsWith("http://urlxf.qq.com")
										|| href.startsWith("http://fenxiang.qq.com")) {
									return href;
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						return null;
					}
				});

				mAbTask.execute(item);
			}
		});
		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreLoading();
			}

		});
		(findViewById(R.id.imagess))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						// Intent intent = new Intent();
						// intent.setClass(HomeActivity.this,
						// LanunyActivity.class);
						// startActivity(intent);
						search();
					}
				});
		(findViewById(R.id.imageView1)).setVisibility(View.GONE);
		if (LanunyActivity.totalKeys != null
				&& LanunyActivity.totalKeys.length > 0) {
			String[] ss = LanunyActivity.totalKeys;
			word.setText(ss[new Random().nextInt(ss.length)]);
		}
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				 moreLoading();
			}
		}, 100); 
		
	}

	private void moreLoading() {
		page++;
		moreButton.setVisibility(View.GONE);
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取电影列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Video> data = DyUtil.getPageContent(page);
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				moreButton.setVisibility(View.VISIBLE);
				AbDialogUtil.removeDialog(HomeActivity.this);
				if (paramList == null || paramList.size() == 0) {
					page--;
					AbToastUtil.showToast(HomeActivity.this, "查询结果为空！多试试");
					return;
				}
				List<Video> list = (List<Video>) paramList;
				allList.addAll(list);
				myListViewAdapter.notifyDataSetChanged();
				if (page < 2)
					closeInputMethod();
			}
		});
		mAbTask.execute(item);

	}

	private void search() {
		// TODO Auto-generated method stub
		moreButton.setVisibility(View.GONE);
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取电影列表...");
		AbTask mAbTask = new AbTask();

		String wd = word.getText().toString().trim();
		try {
			wd = URLEncoder.encode(wd, "gbk");
		} catch (Exception e) {
			// TODO: handle exception
		}
		final String url = String.format(
				"http://www.xiamp4.com/search.asp?searchword=%s", wd);
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stubList<Video> data=new
				// ArrayList<Video>() ;

				List<Video> data = new ArrayList<Video>();
				try {

					Document doc = Jsoup.connect(url)
							.userAgent(DyUtil.userAgent1).timeout(10000).get();

					Elements lis = doc.select("ul.mlist > li");
					for (Element li : lis) {
						Element img = li.select("img").first();
						if (img != null) {
							Element a = img.parent();
							if (a.nodeName().equals("a")) {
								Video video = new Video();
								video.setId(a.attr("abs:href"));
								video.setImg(img.attr("abs:src"));
								video.setTitle(a.attr("title"));
								Element div = li.select("div.info").first();
								if (div != null) {
									Element p = div.select("p").first();
									if (p != null) {
										List<NameEnc> actor = new ArrayList<NameEnc>();
										NameEnc ne = new NameEnc();
										ne.setName(p.text());
										actor.add(ne);
										video.setActor(actor);
									}
									Elements ies = div.select("i");
									String info = "";
									for (Element i : ies) {
										String text = i.text();
										info = info + text + "\n";
										if (text.startsWith("地区：")) {
											List<NameEnc> area = new ArrayList<NameEnc>();
											NameEnc ne = new NameEnc();
											ne.setName(text.replace("地区：", ""));
											area.add(ne);
											video.setArea(area);
										}
									}
									video.setIntro(info);
								}
								data.add(video);
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
				// moreButton.setVisibility(View.VISIBLE);
				AbDialogUtil.removeDialog(HomeActivity.this);
				if (paramList == null || paramList.size() == 0) {
					AbToastUtil.showToast(HomeActivity.this, "查询结果为空！多试试");
					return;
				}
				List<Video> list = (List<Video>) paramList;
				allList.clear();
				allList.addAll(list);
				myListViewAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item);
	}

	private void closeInputMethod() {
		InputMethodManager imm = (InputMethodManager) abApplication
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen) {
			// imm.toggleSoftInput(0,
			// InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
			imm.hideSoftInputFromWindow(word.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
