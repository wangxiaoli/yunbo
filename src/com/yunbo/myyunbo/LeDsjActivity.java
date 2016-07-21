package com.yunbo.myyunbo;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
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
import com.yunbo.control.Yy97Util;
import com.yunbo.mode.Dydata;
import com.yunbo.mode.Movie;
import com.yunbo.mode.NameEnc;
import com.yunbo.mode.Node;
import com.yunbo.mode.PageContent;
import com.yunbo.mode.Urldata;
import com.yunbo.mode.Video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class LeDsjActivity extends AbActivity {

	private ListView mListView = null;
	private LayoutInflater mInflater;
	private ArrayList<Video> allList = new ArrayList<Video>();

	private BaseAdapter myListViewAdapter;
	private ImageButton moreButton;
	private EditText word;
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null;
	private int page = 0;
	private String homeurl = ""; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_home);

		mListView = (ListView) this.findViewById(R.id.listView1);
		((TextView) this.findViewById(R.id.textView1))
				.setText("乐视电视剧");
		
		homeurl = "http://list.le.com/listn/c2_t-1_a-1_y-1_s1_md_o17_d1_p.html";
		(findViewById(R.id.ssLinearLayout1)).setVisibility(View.GONE);
		word = (EditText) findViewById(R.id.editText1);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);
		moreButton.setVisibility(View.GONE);

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
				itemsIcon.setScaleType(ScaleType.FIT_CENTER);
				// 获取该行的数据
				String imageUrl = getItem(position).getImg();
				itemsTitle.setText(getItem(position).getTitle());
				String arter = "";
				if (getItem(position).getActor()!=null) {
					
				for (int i = 0; i < getItem(position).getActor().size(); i++) {
					arter = arter + " "
							+ getItem(position).getActor().get(i).getName();
				}
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
				final AbActivity abActivity = LeDsjActivity.this;
				AbDialogUtil.showProgressDialog(abActivity,
						R.drawable.progress_circular, "正在获取播放列表链接...");
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {
					String showtext = DyUtil.showVideo.getTitle();

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						AbDialogUtil.removeDialog(abActivity);
						final List<String> alls = (List<String>) obj;
						if (obj == null || alls.size() == 0) {
							AbToastUtil.showToast(abActivity,
									"没有播放链接，多试试或者请看其它影片！");
						} else {

							View mView = abActivity.mInflater.inflate(
									R.layout.dia_list_text_pro, null);
							final View pb=  mView
									.findViewById(R.id.progressBar1);
							pb.setVisibility(View.GONE);
							ListView listView = (ListView) mView
									.findViewById(R.id.listView1);
							final TextView textView1 = (TextView) mView
									.findViewById(R.id.textView1);
							if (AbStrUtil.isEmpty(showtext)) {
								textView1.setVisibility(View.GONE);
							} else {
								textView1.setText(showtext);
							}
							listView.setAdapter(  new BaseAdapter() {

								@Override
								public View getView(int position, View convertView, ViewGroup parent) {
									// TODO Auto-generated method stub

									if (convertView == null) {
										// 使用自定义的list_items作为Layout
										convertView = mInflater.inflate(R.layout.item_list_max, parent,
												false);
									}
							String[] mStrings =  getItem(position).split("\\|");
									ImageView itemsIcon = (ImageView) convertView
											.findViewById(R.id.itemsIcon);
									TextView itemsTitle = (TextView) convertView
											.findViewById(R.id.itemsTitle);
									TextView itemsChildTitle = (TextView) convertView
											.findViewById(R.id.itemsChildTitle);
									TextView itemsText = (TextView) convertView
											.findViewById(R.id.itemsText);
									itemsIcon.setScaleType(ScaleType.FIT_CENTER);
									// 获取该行的数据
									String imageUrl = mStrings[2];
									itemsTitle.setText(mStrings[0]);
									 
									itemsChildTitle.setText(mStrings[3]); 
									itemsText.setText(mStrings[4]);
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
								public String getItem(int position) {
									// TODO Auto-generated method stub
									return alls.get(position);
								}

								@Override
								public int getCount() {
									// TODO Auto-generated method stub
									return alls.size();
								}
							});
							listView.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									pb.setVisibility(View.VISIBLE);

									String url = alls.get(position)
											.split("\\|")[1];

									String name = alls.get(position).split(
											"\\|")[0];
									textView1.setText("正在解析" + name);
									Yy97Util.jiexi(abActivity, textView1, url,
											  name,pb);
								}
							});
							AbDialogUtil.showAlertDialog(mView);
						}
					}

					@Override
					public Object getObject() {
						// TODO Auto-generated method stub
						String url = DyUtil.showVideo.getId();
						System.out.println(url);
						List<String> list = new ArrayList<String>();
						try {
							Document doc = Jsoup.connect(url)
									.userAgent(DyUtil.userAgent1)
									.followRedirects(true).timeout(3000).get();

							Elements dls = doc.select(".listText").first()
									.select(".w120");
							 for (Element dl : dls) {
								Element a=dl.select("a").first();
								Element img=dl.select("img").first();
								Element span=dl.select(".time").first();
								Element p=dl.select(".p2").first();
								try {
									String text=img.attr("title")+"|"+
								a.attr("abs:href")+"|"+											
											img.attr("abs:src")+"|"+
											span.text()+"|"+
											p.text();
									list.add(text);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							return list;
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						return list;
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
				"正在获取电视剧列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Video> data = getPageContent(homeurl, page);
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				 moreButton.setVisibility(View.VISIBLE);
				AbDialogUtil.removeDialog(LeDsjActivity.this);
				if (paramList == null || paramList.size() == 0) {
					page--;
					AbToastUtil.showToast(LeDsjActivity.this, "查询结果为空！多试试");
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
				"http://www.kuai97.com/search.asp?searchword=%s", wd);
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub

				List<Video> data = Yy97Util.getPageContent(url, page = 1);

				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub
				// moreButton.setVisibility(View.VISIBLE);
				AbDialogUtil.removeDialog(LeDsjActivity.this);
				if (paramList == null || paramList.size() == 0) {
					AbToastUtil.showToast(LeDsjActivity.this, "查询结果为空！多试试");
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
	public static List<Video> getPageContent(String urlS, int page) {

		List<Video> data = new ArrayList<Video>();

		String url = urlS .replace(".html", page + ".html");
		if (page == 1) {
			url = urlS;
		}
		try {

			Document doc = Jsoup.connect(url).userAgent(DyUtil.userAgent1)
					.timeout(10000).get();

			Elements dls = doc.select("dl.dl_list");
			for (Element dl : dls) {
				Element img = dl.select("img").first();
				if (img != null) {
					Element a = img.parent();
					if (a.nodeName().equals("a")) {
						Video video = new Video();
						video.setId(a.attr("abs:href"));
						video.setImg(img.attr("abs:src"));
						video.setTitle(a.attr("title"));
						String upinfro="";
						try {
							
						  upinfro=dl.select(".number_txt").first().text();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						Element dd= dl.select("dd.dd_cnt").first();
						if (dd != null) { 
								Elements ps = dd.select("p.p_c");
							String info = upinfro;
							if (ps != null&&ps.size() > 0) {
								info = upinfro+ '\n'+ps.first().text() + '\n';
								List<NameEnc> actor = new ArrayList<NameEnc>();

								if (ps.size() > 1)
								{
									Elements spans=ps.get(1).select("span");
									for (Element span : spans) {
										
								NameEnc ne = new NameEnc();
								ne.setName( span.text() );
								actor.add(ne);
									}
									Elements as=ps.get(1).select("a");
									for (Element ae : as) {
										
								NameEnc ne = new NameEnc();
								ne.setName( ae.text() );
								actor.add(ne);
									}
								}
								video.setActor(actor);
								if (ps.size() > 2) {
									for (int i = 2; i < ps.size(); i++) {
										String text = ps.get(i)
												.text();
										if (text.contains("可播放")) {
											break
											;
										}
										info = info + text + '\n'; 
									}
								}
							}
							video.setIntro((""+info).trim());
						}
						data.add(video);
					}
				}
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
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
