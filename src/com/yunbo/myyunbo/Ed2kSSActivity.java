package com.yunbo.myyunbo;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.mode.Node;
import com.yunbo.mode.Urldata;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Ed2kSSActivity extends AbActivity implements OnClickListener {

	EditText word;
	View mView;
	private ImageButton moreButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_ed2k_ss);
		word = (EditText) findViewById(R.id.editText1);
		  hashet = (EditText) findViewById(R.id.editText2);
		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);
		String[] ss = new String[] { "ZEX-051", "zizg-001", "yrz-069",
				"XV-851", "VSPDS-574", "TSDV-41458", "TERA-005", "SW-116",
				"star-526", "SW-047", "STAR-418", "STAR-362", "star-297",
				"star 409", "SQTE-046", "SNIS-258", "SNIS-031", "SNIS-012",
				"SNIS012", "RBD518", "PGD-660", "PGD-268", "ODFW-006", "n0753",
				"mild-771", "MIDE-008", "MIAD-635", "Miad 576", "MDYD-789",
				"JUC-510", "JUC-375", "jbs-006", "ipz-015", "IPTD-920",
				"IPTD-651", "IPTD-343", "ibw248", "GUILD-009", "EKDV-273",
				"dgl 008", "BOD-277", "ABP142", "ABP-069", "DVDES-662",
				"iptd-788", "ABP-090", "ADN-021", "ABS-147", "AKB48",
				"BBAN-008", "BBI-142", "CWM-087", "DKDN-008", "DPMI-001",
				"DV-1575" };

		if (LanunyActivity.totalKeys != null
				&& LanunyActivity.totalKeys.length > 0)
			ss = LanunyActivity.totalKeys;
		word.setText(ss[new Random().nextInt(ss.length)]);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			String key = bundle.getString("key");
			if (!AbStrUtil.isEmpty(key)) {
				word.setText(key);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						AbDialogUtil.showAlertDialog(getView());
					}
				}, 500);
			}
		}
		findViewById(R.id.button1).setOnClickListener(this);
		mListView = (ListView) this.findViewById(R.id.listView1);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				// itemsText.setText(allList.get(position).split("\\|")[2]);
				itemsText.setText(getItem(position).toString());
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
				return strList.get(position);
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
				// DyUtil.urlToPlay = allList.get(position);
				// DyUtil.
				// getPlayUrl(allList.get(position).getThunder(),SearchActivity.this);
				// AbToastUtil.showToast(SearchActivity.this,
				// playList.get(position));
				// Intent intent = new
				// Intent(Ed2kSSActivity.this,PlayUrlActivity.class);
				// startActivity(intent)
				DyUtil.urlToPlay = null;
				String hash=allList.get(position);
					hash=DyUtil.changeED2K(hash);
					hashet.setText(hash);
					DyUtil.toGetPlayUrl(hash, Ed2kSSActivity.this); 
			}
		});
		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreLoading();
			}

		});
		moreButton.setVisibility(View.GONE);isnext=false;
		word.selectAll();

		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String hash=hashet.getText().toString();
				if (PlayUrlActivity.check(hash, Ed2kSSActivity.this)) {
					hash=DyUtil.changeED2K(hash);
					hashet.setText(hash);
					DyUtil.toGetPlayUrl(hash, Ed2kSSActivity.this);
				}
			}
		});
		// word.setText("");
	}
	EditText hashet;

	private View getView() {
		mView = mInflater.inflate(R.layout.dia_list_text, null);
		ListView listView = (ListView) mView.findViewById(R.id.listView1);
		TextView textView1 = (TextView) mView.findViewById(R.id.textView1);
		textView1.setText("请选择一个搜索引擎");
		ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
				R.layout.dialog_list_item_1, new String[] { "旋风BT分享(正规电影)(可以搜索动漫)",// 0
				"MP4ba(正规电影)",// 1 
						"torrentkitty",// 17
						"btsow",// 0
//						"yunbosou",// 0
						"haappy",// 0
						"iuiuw",// 0 
				"ciliyun",// 1
						"cili8",// 2
//						//"sosobta",// 3 
//						"btbili",// 15
//						"fuliso",// 15
//						"btgoogle",// 15
//						"h31bt",// 4
						"btcherry",// 5
						"btmayi",// 6
//						"btdao",// 8
//						"btfuli",// 9
//						"cilisou",// 10
//						"cililian",// 11
//						"btsoso",// 12
						"bthave",// 13
//						"nimasou",// 14
//						"kuyi",// 16
				});
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int i = 0;
				moreButton.setVisibility(View.GONE);
				if (position == i)
					mode("http://share.xfsub.com:88/search.php?keyword=%s");
				i++;
				if (position == i)
					mode("http://www.mp4ba.com/search.php?keyword=%s");
				i++;
				if (position == i)
					tk(); 
				i++;
				if (position == i)
					mode("https://btso.pw/search/%s");
				i++;
//				if (position == i)
//					mode("http://www.yunbosou.org/s/%s.html");
//				i++;
				if (position == i)
					mode("http://www.haappy.net/search/%s_ctime_1.html");
				i++;
				if (position == i)
					mode("http://www.iuiuw.cn/0/%s/1.htm");
				i++;
				if (position == i)
					mode("http://www.ciliyun.net/word/%s.html");
				i++;
				if (position == i)
					mode("http://www.cili8.org/s/%s.html");
				i++;
//				if (position == i)
//					mode("http://www.sosobta.com/s/%s");
//				i++; 
//				if (position == i)
//					mode("http://www.btbili.com/word-%s.html");
//				i++;
//				if (position == i)
//					mode("http://www.fuliso.com/?keyword=%s");
//				i++;
//				if (position == i)
//					mode("http://www.btgoogle.cc/search2/%s");
//				i++;
//				if (position == i)
//					mode("http://www.h31bt1.net/Search/%s"); 
//				i++;
				if (position == i)
					mode("http://www.btcherry.org/search?keyword=%s");
				i++;
				if (position == i)
					mode("http://www.btmayi.me/search/%s-first-asc-1");
				i++;
//				if (position == i)
//					mode("http://www.btdao.xyz/list/%s-s1d-1.html");
//				i++;
//				if (position == i)
//					mode("http://www.btfuli.com/list/%s-s1d-1.html");
//				i++;
//				if (position == i)
//					mode("http://www.cilisou.cn/s.php?q=%s");
//				i++;
//				if (position == i)
//					mode("http://cililian.me/list/%s/1.html");
//				i++;
//				if (position == i)
//					mode("http://www.btsoso.com/search/%s_ctime_1.html");
//				i++;
				if (position == i)
					mode("http://www.bthave.com/search/%s/1-1.html");
				i++;
//				if (position == i)
//					mode("http://www.nimasou.com/l/%s-hot-desc-1");
//				i++;
//				if (position == i)
//					mode("http://so.kuyi.tv/s/%s-1-d.html");
//				i++;

			}
		});
		return mView;
	}

	@Override
	public void onClick(View v) {

		AbDialogUtil.showAlertDialog(getView());
	}

	public void mode(String format) {
		mode(format, "UTF-8");
	}
private String nextpageurl="";
		  String ssurl;
	public void mode(final String format, String charsetName) {
		// TODO Auto-generated method stub
		if (isnext) {
			ssurl=nextpageurl;
		} else {

		String wd = word.getText().toString().trim();
		try {
			wd = URLEncoder.encode(wd, charsetName);
		} catch (Exception e) {
			// TODO: handle exception
		}ssurl = String.format(format, wd);
		}
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取磁力链接...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {
			List<String> list1 = new ArrayList<String>();
			List<String> list2 = new ArrayList<String>();

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(Ed2kSSActivity.this);
				if (isnext) {
					moreButton.setVisibility(View.VISIBLE);
				}
				if (list1.size() == 0) {
					AbToastUtil.showToast(abApplication, "查询结果为空");
				} else
				{
					if (!TextUtils.isEmpty(format)) {
						
					allList.clear();
					strList.clear();
					}
				allList.addAll(list1);
				strList.addAll(list2);
					myListViewAdapter.notifyDataSetChanged();
					
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub
				isnext=false;
				try {
					Document doc = Jsoup.connect(ssurl).userAgent(useragent1)
							.timeout(10000).get();
					Pattern pat = Pattern.compile("[a-zA-Z0-9]{40}");
					Elements as = doc.select("a[href]");
					//list1.addAll(allList);
					//list2.addAll(strList);
					Map<String, String> map = new HashMap<String, String>();
					for (Element element : as) {
							String text = (element.text()+ "").trim();
						String href = (element.attr("abs:href") + "").trim();
						if (text.contains("下一页")) {
isnext=true;nextpageurl=href;
						}
						if (href.startsWith("thunder"))
							continue;
						Matcher mat = pat.matcher(href);
						if (mat.find()) {
							String hash = mat.group();
								text = (text + "").trim();
							if (map.get(hash) == null
									&& !AbStrUtil.isEmpty(text)&&!text.equals("null")) {
								map.put(hash.toLowerCase(), text);
								list2.add(text);
								list1.add("magnet:?xt=urn:btih:" + hash);
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
		closeInputMethod();
	}

	public void tk() {
		// TODO Auto-generated method stub
		String wd = word.getText().toString().trim();
		try {
			wd = URLEncoder.encode(wd, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		final String url = "https://www.torrentkitty.tv/search/" + wd + "";
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取磁力链接...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {
			List<String> list1 = new ArrayList<String>();
			List<String> list2 = new ArrayList<String>();

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				AbDialogUtil.removeDialog(Ed2kSSActivity.this);

				if (list1.size() == 0) {
					AbToastUtil.showToast(abApplication, "查询结果为空");
				} else {
					allList.clear();
					strList.clear();
					allList.addAll(list2);
					strList.addAll(list1);
					myListViewAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub

				try {
					Document doc = Jsoup.connect(url).userAgent(useragent1)
							.timeout(10000).get();
					Pattern pat = Pattern.compile("[a-zA-Z0-9]{40}");
					Elements as = doc.select("a[rel=magnet]");
					for (Element element : as) {
						String href = (element.attr("href") + "").trim();

						Matcher mat = pat.matcher(href);
						if (mat.find()) {
							list1.add((element.attr("title") + "").trim());

							list2.add("magnet:?xt=urn:btih:" + mat.group());
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		});

		mAbTask.execute(item);
		closeInputMethod();
	}

	private boolean isnext=false;
	private void moreLoading() { 
		moreButton.setVisibility(View.GONE); 
		mode("");
	}

	 
 

 
 

	 
 

	private static String useragent1 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 BIDUBrowser/7.0 Safari/537.36";

	 

	@Override
	protected void onResume() {
		super.onResume();
		((TextView) findViewById(R.id.textView3)).setText("");
	}

	private ArrayList<String> allList = new ArrayList<String>();
	private ArrayList<String> strList = new ArrayList<String>();
	private ListView mListView = null;
	private LayoutInflater mInflater;
	private BaseAdapter myListViewAdapter;

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
