package com.yunbo.myyunbo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbBase64;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbMd5;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.control.TvUtil;
import com.yunbo.control.UpUtil;
import com.yunbo.control.Wu115Util;
import com.yunbo.frame.CiliAdapter;
import com.yunbo.mode.Subfile_List;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.R.integer;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class CiLiActivity extends AbActivity {

	private ListView mListView; 
	private BaseAdapter myListViewAdapter; 
	private TextView textView1;
	private TextView textView2;

	private ArrayList<Subfile_List> allList = new ArrayList<Subfile_List>(); 
	private CiLiActivity packageContext;
	private  String hash=""; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_cili);
		packageContext=this;
		DyUtil.fileList=new ArrayList<String>();
		DyUtil.PATH = packageContext.getFilesDir() + "/";
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			  hash = bundle.getString("magnet");
			if (!AbStrUtil.isEmpty(hash)) { 

				if (PlayUrlActivity.check(hash,packageContext)) {
					hash=DyUtil.changeED2K(hash);
					((TextView)findViewById(R.id.textView4)).setText(hash); 
				}
			}
		} 
		findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				if (myClipboard != null) { 

					ClipData myClip = ClipData.newPlainText("text", "magnet:?xt=urn:btih:"+hash);
					myClipboard.setPrimaryClip(myClip);
					Toast.makeText(getApplicationContext(), "磁力链接已经复制到剪贴板",
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		mListView = (ListView) this.findViewById(R.id.listView1);
 
		    textView1 = (TextView) this
				.findViewById(R.id.textView1);
		    textView2 = (TextView) this
				.findViewById(R.id.textView2);
		final ProgressBar pb = (ProgressBar) this
				.findViewById(R.id.progressBar1);
		pb.setVisibility(View.GONE);
		
		final GridView gridView = (GridView) this
				.findViewById(R.id.mGridView);
		gridView.setVisibility(View.GONE);
		final int qqxf=2;
		final int s115=2;
		final int sum=qqxf+s115;
		final BaseAdapter myGridViewAdapter=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null){
					convertView=new TextView(packageContext);
				}
				TextView tv=(TextView)convertView;
				if (DyUtil.jiexiIndex==position) {
					tv.setBackgroundColor(Color.GREEN);
				}else {
					tv.setBackgroundColor(Color.WHITE);
				} 
				tv.setText(getItem(position).toString());
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
				if (position>=sum-s115) {
					return "115接口"+""+(s115-(sum-position)+1);
				}
				return "接口"+(position+1);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return sum;
			}
		};
		gridView.setAdapter(myGridViewAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				DyUtil.jiexiIndex=position;
				myGridViewAdapter.notifyDataSetChanged();
			}
		});

		mListView.setAdapter(myListViewAdapter=new CiliAdapter(packageContext,allList));
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) { 
				String info_hash=hash;
				String hashandindex=info_hash+"&index="+allList.get(position).getIndex();
				if (Cache) {

					String filehash = DyUtil.readFilehash(packageContext,
							hashandindex);
					if (AbStrUtil.isEmpty(filehash)) {
						filehash = hashMap(info_hash);
					}
					System.out.println(filehash);

					if (!AbStrUtil.isEmpty(filehash)
							&& !filehash.toLowerCase().equals(
									info_hash.toLowerCase())) {
						DyUtil.toGetPlayUrl3(
								filehash,
								"【"
										+ DyUtil.convertFileSize(allList.get(
												position).getFile_size())
										+ "】 "
										+ allList.get(position).getName(),
								packageContext, true);
						return;
					}
				}
				
//				if (DyUtil.jiexiIndex==0) {
//					
//					DyUtil.jx444urlhand="http://www.chenfuwl.com/chenfu2.php?";
//					DyUtil.toGetPlayUrl2_444(allList.get(position), info_hash, packageContext,textView1,pb,true); 
//				}
//				if (DyUtil.jiexiIndex==1) { 
//					DyUtil.jx444urlhand="http://120.24.170.195:88/842606559.php?";
//					DyUtil.toGetPlayUrl2_444(allList.get(position), info_hash, packageContext,textView1,pb);
//					
//				}
				if (DyUtil.jiexiIndex==0) {//http://www.srmc75.com/Android1.5
					DyUtil.jx5urlhand= "http://www.srmc75.com/Android1.5/screen.php?m="+
							AbMd5.MD5(info_hash.toUpperCase()+"http://www.srmc75.com/Android1.5").toLowerCase()+"&";
					DyUtil.toGetPlayUrl2_5_1(allList.get(position), info_hash.toUpperCase(), packageContext,textView1,pb);
					//DyUtil.toGetPlayUrl2_3(allList.get(position), info_hash, packageContext,textView1,pb);
				}
				if (DyUtil.jiexiIndex==1) {
					DyUtil.jx5urlhand= "http://aa7761610510.d203.cnaaa7.com/ax.php?";
					DyUtil.toGetPlayUrl2_555(allList.get(position), info_hash.toLowerCase(), packageContext,textView1,pb);
				}
//				if (DyUtil.jiexiIndex==4) {
//					DyUtil.jx5urlhand= "http://mt520.xyz:8080/CPServer/cloudplayer/geturlopen?";
//					DyUtil.toGetPlayUrl2_5(allList.get(position), info_hash.toUpperCase(), packageContext,textView1,pb);
//				}
//				if (DyUtil.jiexiIndex==5) {
//					DyUtil.toGetPlayUrl2_55(allList.get(position), info_hash, packageContext,textView1,pb); 
//				}
//				if (DyUtil.jiexiIndex==6) {
//					DyUtil.jx1urlhand="http://120.24.170.195:88/fx.php?";
//					DyUtil.toGetPlayUrl2_1_1_1_1(allList.get(position), info_hash, packageContext,textView1,pb);
//				}
//				if (DyUtil.jiexiIndex==7) {
//				}
//				if (DyUtil.jiexiIndex==8) {
//					DyUtil.toGetPlayUrl2_18(allList.get(position), info_hash.toUpperCase(), packageContext,textView1,pb);
//				}
				if((s115-(sum-DyUtil.jiexiIndex)+1)==1){
					Wu115Util.jxhand="http://m3u8.cc:8020/api?apikey=55bfaf1b2821826c&url=magnet:?xt=urn:btih:%s";
					Wu115Util.rr="http://m3u8.cc/";
					Wu115Util.jiexi1(info_hash.toLowerCase(),allList.get(position).getName(),position, packageContext);
				}
				if((s115-(sum-DyUtil.jiexiIndex)+1)==2){
					Wu115Util.jxhand="http://feiyuys.com/playm3u8/?type=magnet&vid=%s";
					Wu115Util.rr="http://www.feiyuys.com/";
					Wu115Util.jiexi(info_hash.toUpperCase(),allList.get(position).getName(), packageContext);
				}
				if((s115-(sum-DyUtil.jiexiIndex)+1)==3){
					Wu115Util.jxhand="https://and110app.applinzi.com/playm3u8/?url=magnet:?xt=urn:btih:%s";
					Wu115Util.rr="http://www.and110.com/";
					Wu115Util.jiexi(info_hash.toLowerCase(),allList.get(position).getName(), packageContext);
				}
				 
			} 
		}); 
		AbDialogUtil.showProgressDialog(packageContext,
				R.drawable.progress_circular, "正在获取播放列表...");
		final AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub 
				AbDialogUtil.removeDialog(packageContext);
				Urldata data = (Urldata) obj;
				if (!"ok".equals(data.getMsg())) { 
					String msg = data.getMsg(); 
					//AbToastUtil.showToast(packageContext, msg);
					((TextView) packageContext.findViewById(R.id.textView3))
							.setText(msg);
				} 
				if (DyUtil.dydatehashs!=null&&DyUtil.dydatehashs.getResp().getSubfile_list().size()>0) {
					
					final List<Subfile_List> list = DyUtil.dydatehashs.getResp().getSubfile_list(); 
					textView1.setText("如果文件解析失败,请再试一次.或者换其他其他接口.");
					textView2.setText("文件解析接口-先读本地缓存");//（建议测试顺序为接口6-接口5-接口1）
					textView2.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Cache=!Cache;
							if (Cache) {
								textView2.setText("文件解析接口-先读本地缓存");
							}else {
								textView2.setText("文件解析接口-直接联网解析。");								
							}
						}
					});
					gridView.setVisibility(View.VISIBLE);
					//pb.setVisibility(View.GONE);
					final String info_hash=DyUtil.dydatehashs.getResp().getInfo_hash();
					//allList =new ArrayList<Subfile_List>();
					allList.clear();
					for (Subfile_List subfile_List : list) {
						if (!subfile_List.getName().toLowerCase().endsWith("html") && 
								!subfile_List.getName().toLowerCase().endsWith("txt") && 
								!subfile_List.getName().toLowerCase().endsWith("jpg") && 
								!subfile_List.getName().toLowerCase().endsWith("png"))
						{
							allList.add(subfile_List);
						}
					}  
					if (allList.size()>0) {
						myListViewAdapter.notifyDataSetChanged();
					} else {
						 AbToastUtil.showToast(packageContext, "没有文件！");
					}
				} else {
					 AbToastUtil.showToast(packageContext, "没有文件！");
				}
			}

			@Override
			public Urldata getObject() {
				// TODO Auto-generated method stub
				Urldata urldata =DyUtil.getDyPlayUrl(hash);
				//if (packageContext.isBack)return null;
				if (AbStrUtil.isEmpty(urldata.getMsg())) {
					urldata.setMsg("ok");
					return urldata;
				} 
				return urldata;
			}
		});

		 

		
		mAbTask.execute(item); 
	}
	private boolean Cache=true;
	public static List<String> hashlist=new ArrayList<String>();
	public String hashMap(String infohash) {
		infohash=infohash.toUpperCase();
		if (hashlist==null) {
			hashlist=new ArrayList<String>();
		}
		if(hashlist.size()==0)
		try {
			hashlist=new ArrayList<String>();
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open("map.txt"), "utf-8");
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line;
			while ((line = bufReader.readLine()) != null) {
				if (!AbStrUtil.isEmpty(line) && line.contains("|")) {
					hashlist.add(line);
					}
				}
			bufReader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		for (String str : hashlist) {
			if (str.contains(infohash)) {
				return str.split("\\|")[0];
			}
		}
		return "";
	}

}
