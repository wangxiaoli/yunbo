package com.yunbo.myyunbo;
  
import java.io.BufferedReader;
import java.io.IOException; 
import java.io.InputStreamReader;
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
import com.ab.task.AbTaskObjectListener; 
import com.ab.util.AbDialogUtil;   
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;  
import com.ab.util.AbViewHolder;  
import com.yunbo.control.DyUtil; 
import com.yunbo.control.FanHaoUtil;
import com.yunbo.mode.Nvyou; 
import com.yunbo.mode.Video;

import android.os.Bundle;
import android.os.Handler; 
import android.text.TextUtils;
import android.view.KeyEvent; 
import android.view.View;
import android.view.ViewGroup; 
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView; 
import android.widget.BaseAdapter;
import android.widget.Button; 
import android.widget.ImageButton; 
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Sex8Activity extends AbActivity {

	private ListView mlistView = null;
	private ArrayList<Nvyou> allList = new ArrayList<Nvyou>();
	private ArrayList<Nvyou> showList = new ArrayList<Nvyou>();
	private BaseAdapter myAdapter;
	private ImageButton moreButton; 

	private Button kan  ; 
	private  TextView textView  ;
	private WebView mWebView ;
	private View convertView ;
	private View show; 
	// 图片下载器
	private AbImageLoader mAbImageDownloader = null;

	Pattern pat = Pattern.compile("【([A-Z0-9]+)\\/(.*?)】([A-Z]+-\\d+)(.+)");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_sex8);

		mlistView = (ListView) this.findViewById(R.id.listView1); 
		kan = (Button)  findViewById(R.id.one_btn); 
		  textView = (TextView)  
				 findViewById(R.id.title_choices);
		  mWebView=( WebView)  
				 findViewById(R.id.mWebView);

		  mWebView.getSettings().setLoadWithOverviewMode(true);
		  mWebView.getSettings().setUseWideViewPort(true);
			convertView= findViewById(R.id.probar);
			show= findViewById(R.id.show);
			show.setVisibility(View.GONE);
			mAbImageDownloader = new AbImageLoader(this);
//			mAbImageDownloader.setMaxWidth(180);
//			mAbImageDownloader.setMaxHeight(240);
			mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
			mAbImageDownloader.setEmptyImage(R.drawable.image_empty);
			mAbImageDownloader.setErrorImage(R.drawable.image_error);
	//	((TextView) this.findViewById(R.id.textView1)).setText(" ");
//		(findViewById(R.id.imageView1)).setVisibility(View.GONE);

		moreButton = (ImageButton) this.findViewById(R.id.imageButton1);
		//homeurl=String.format(homeurl, ""+page);
		//(findViewById(R.id.ssLinearLayout1)).setVisibility(View.GONE); 
		/**/
		mlistView.setAdapter(myAdapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					// 使用自定义的list_items作为Layout
					convertView = mInflater.inflate(R.layout.item_sex8, parent,
							false);
				} 
				TextView itemsText = AbViewHolder
						.get(convertView, R.id.tx_item); 
				LinearLayout text_item = AbViewHolder
						.get(convertView, R.id.text_item); 
				TextView itemsTitle = AbViewHolder
						.get(convertView, R.id.itemsTitle);
				TextView itemsTop = AbViewHolder
						.get(convertView, R.id.itemsTop);
				ImageView itemsIcon = AbViewHolder
						.get(convertView, R.id.itemsIcon); 
				RelativeLayout itemsImage = AbViewHolder
						.get(convertView, R.id.itemsImage); 
				String name=getItem(position).getName();

				Matcher mat = pat.matcher(name);
				if (mat.find()) {
					name = mat.group();
					String s1=mat.group(1);
					String s2=mat.group(2);
					String s3=mat.group(3);
					String s4=mat.group(4);
					itemsTitle.setText(s4);
					itemsTop.setText(String.format("【%s】%s.%s", s2,s3,s1.toLowerCase()));
					s3=s3.toLowerCase(); 
					s3=s3.replace("-", "");
					getItem(position).setInfro(s3);
					getItem(position).setImg(itemsTop.getText().toString()); 
					text_item.setVisibility(View.GONE);
					itemsImage.setVisibility(View.VISIBLE);
					  String imageUrl = FanHaoUtil.getImageUrl(s3,Sex8Activity.this);
					mAbImageDownloader.setLoadingView(convertView
							.findViewById(R.id.progressBar)); 
					mAbImageDownloader.display( itemsIcon, imageUrl);
				}else {
					itemsText.setText(name); 
					text_item.setVisibility(View.VISIBLE);
					itemsImage.setVisibility(View.GONE);
				} 
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
				return showList.get(position);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return showList.size();
			}
		});
		mlistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				 nvyou=(Nvyou)myAdapter.getItem(position) ;
				String url = (nvyou).getUrl();  
				textView.setText(nvyou.getName()); 
show.setVisibility(View.VISIBLE);
mWebView.setVisibility(View.INVISIBLE );
//kan.setVisibility(View.GONE);
kan.setText("");
setwebsrc(url);
				 
			}
		});  
		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreLoading();
			}

		}); 
		kan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				
				if (TextUtils.isEmpty(
kan.getText())) {
					return;
				}
				if (hash!=null) {
					DyUtil.toGetPlayUrl3(hash, nvyou.getImg(), Sex8Activity.this);
				}else {
					 DyUtil.toGetPlayUrl(nvyou.getInfro(), Sex8Activity.this);
				}
				
			}

		}); 
		homeurl="/forum.php?mod=forumdisplay&fid=[fid]&orderby=dateline&typeid=[typeid]&filter=author&orderby=dateline&typeid=[typeid]&page=%s";
		//homeurl = "/thread-htm-fid-"+type+"-orderway-postdate-asc-DESC-page-%s.html";
		String[]ss=type.replace("-type-", "#").split("#");
		homeurl=homeurl.replace("[fid]", ss[0]).replace("[typeid]", ss[1]);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {  
			//closeInputMethod(); 
				moreLoading( );
			}
		}, 100); 
		
	}
	@Override 
    //设置回退  
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {  
if (        	show.getVisibility()==View.VISIBLE) {
	show.setVisibility(View.GONE);
} else {
finish(); 
}
            return true;  
        }  
        
        return false;  
    }  
 
	Nvyou nvyou; 
	String hash=null;
			private void setwebsrc(  final String src) {
				// TODO Auto-generated method stub 
				convertView.setVisibility(View.VISIBLE);
				AbTask mAbTask = new AbTask(); 
				
				hash=null;
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public Element getObject() {
						// TODO Auto-generated method stub 
						String s1=nvyou.getInfro();
						if(!TextUtils.isEmpty(s1))
						try {
							InputStreamReader inputReader = new InputStreamReader(
									getResources().getAssets().open("hash.txt"), "utf-8");
							BufferedReader bufReader = new BufferedReader(inputReader);
							String line;
							while ((line = bufReader.readLine()) != null) {
								if (!AbStrUtil.isEmpty(line) && line.contains("|")) {
									String strs[]=line.split("\\|");
									try {
						 String sss=strs[0].replace("-", "").toLowerCase();
						 if (sss.startsWith(s1)) {
							hash=strs[1];
							break;
						}
									} catch (Exception e) {
										// TODO: handle exception
									}
								}
							}
							// UpUtil.upgif(getApplicationContext(), list);
						} catch (Exception e) {
							e.printStackTrace();
						} 
						try {
							Document doc = Jsoup.connect(src).userAgent(DyUtil.userAgent1)
									.timeout(10000).get();
							Element  read_tpc = doc.select("td.t_f").first() ;
							read_tpc.select("div").first().remove();
							read_tpc.select("div").first().remove();
							 return read_tpc;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}

					@Override
					public void update(Object param ) {
						// TODO Auto-generated method stub  
						mWebView.setVisibility(View.VISIBLE);
						convertView.setVisibility(View.GONE);
						if (hash!=null) {
							kan.setText("看");
						}
						if (param == null  ) {   
							AbToastUtil.showToast(Sex8Activity.this, "查询结果为空！多试试");
							return;
						} 
						Element  read_tpc =(Element) param;
						WebSettings webSettings = mWebView.getSettings();
						webSettings.setDefaultTextEncodingName("utf-8");
						mWebView.loadDataWithBaseURL(null,
								"<HTML><body> "
										+ read_tpc.outerHtml() + " </body></html>", "text/html",
								"UTF-8", null);
						String text=read_tpc.text();

						Pattern pat = Pattern.compile("[a-zA-Z0-9]{40}");
						Matcher mat = pat.matcher(text);
						if (mat.find()) {
							String hash = mat.group();
							nvyou.setInfro(hash);
							kan.setText("看");
						kan.setVisibility(View.VISIBLE);
						}
						
					}
 
 
				});
				mAbTask.execute(item); 
				}
	  
 
			public static String type;
	
			private   int page=0;
			private String homeurl = "/thread-htm-fid-96-type-3561-orderway-postdate-asc-DESC-page-%s.html";

	private String hostServer="http://girlse8.net"; 
	public   void moreLoading( ){ 

		page++;
		moreButton.setVisibility(View.GONE); 
		AbDialogUtil.showProgressDialog(this, R.drawable.progress_circular,
				"正在获取影片列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public List<?> getList() {
				// TODO Auto-generated method stub
				List<Nvyou> data = allList=new ArrayList<Nvyou>();
				//allList.clear();

				try {
					Document doc = Jsoup.connect(String.format(hostServer+homeurl,""+page)).userAgent(DyUtil.userAgent1)
							.timeout(10000).get();
					Elements as = doc.select("a[class=s xst]") ;
					for (Element element : as) {  
try {
	
						Nvyou nvyou=new Nvyou();  
							nvyou.setName(element.text());	  
							nvyou.setUrl(element.attr("abs:href"));	 
						allList.add(nvyou);
} catch (Exception e) {
	// TODO: handle exception
}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return data;
			}

			@Override
			public void update(List<?> paramList) {
				// TODO Auto-generated method stub 
				AbDialogUtil.removeDialog(Sex8Activity.this);
				moreButton.setVisibility(View.VISIBLE);
				if (paramList == null || paramList.size() == 0) {  
				page--;
						
					AbToastUtil.showToast(Sex8Activity.this, "查询结果为空！多试试");
					return;
				}  
		showList.addAll(allList);
		myAdapter.notifyDataSetChanged();
			}
		});
		mAbTask.execute(item); 
	}
	 
	
}
 
