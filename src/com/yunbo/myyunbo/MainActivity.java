package com.yunbo.myyunbo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ab.activity.AbActivity;
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbDateUtil;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken; 
import com.yunbo.control.DyUtil;
import com.yunbo.control.ENDEUtil;
import com.yunbo.control.HistoryUtil;
import com.yunbo.control.RandomUtil;
import com.yunbo.control.ToolUtil;
import com.yunbo.control.UpUtil;
import com.yunbo.mode.PageContent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AbActivity {

	private ProgressBar pro;
	int i=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_main);
		pro=(ProgressBar) findViewById(R.id.progressBar1);
		pro.setMax(30);
		DyUtil.PATH =  getFilesDir()+"/";
		//HistoryUtil.read(this);
			final AbImageLoader mAbImageDownloader = new AbImageLoader(MainActivity.this); 
			mAbImageDownloader.setLoadingImage(R.drawable.launcher);
			mAbImageDownloader.setEmptyImage(R.drawable.launcher);
			mAbImageDownloader.setErrorImage(R.drawable.launcher);
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
			// checkupdate();

			}
		});
		new Thread(new Runnable() { 
			@Override
			public void run() { 
				DyUtil.init();
				LanunyActivity.readkeys();

				/*
				PageContent data=DyUtil.getPageContent(1);
				 if (data!=null) { 
					 DyUtil.videos= data.getVideos();
				}*/
			}
		}).start();
try {
	new Thread(new Runnable() { 
			@Override
			public void run() { 
				 RandomUtil.init1(MainActivity.this);
			}
		}).start();
} catch (Exception e) {
	// TODO: handle exception
}
		
//		new Thread(new Runnable() { 
//			@Override
//			public void run() {  
//				UpUtil.setStartdata();
//				UpUtil.getnowdata();
//				UpUtil.inittime();
//			}
//		}).start();
		findViewById(R.id.progressBar1).setVisibility(View.GONE);
		/*
		((Button)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, HomeActivity.class); 
				startActivity(intent);
			}
		});
((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, Ed2kSSActivity.class); 
				startActivity(intent);
			}
		});
( findViewById(R.id.imageView1)).setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		intent.setClass(MainActivity.this, VideoViewBuffer.class); 
		startActivity(intent);
	}
});*/

		SharedPreferences SAVE =  getSharedPreferences("mn",
				 MODE_PRIVATE);
		String contxet = SAVE.getString("context", "");
		String listString = SAVE.getString("list", "");
		final String ddd = SAVE.getString("version", "");
		final String dateString = SAVE.getString("date", "");
		final String version="1";
		 all=new Gson().fromJson(contxet, 
	                new TypeToken<List<List<String>>>() {  
	                }.getType());
		 imagelist=new Gson().fromJson(listString, 
	                new TypeToken<List<String>>() {  
	                }.getType());
		 ToolUtil.imgs=imagelist;
		DisplayMetrics display= new DisplayMetrics();
 
		getWindowManager().getDefaultDisplay().getMetrics(display);
		final boolean b=display.heightPixels>display.widthPixels;
		ToolUtil.isWidth=!b;
		AbTask mAbTask = new AbTask();
		  AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub 

				/*new Handler().postDelayed(new Runnable(	) {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putBoolean("ismain", true); 
						intent.setClass(MainActivity.this, WebActivity.class); 
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
				}, 3000);*/
					String url=(String)obj;
					if (!TextUtils.isEmpty(url)) {

						ImageView imageView=(ImageView) findViewById(R.id.imageView2);
						mAbImageDownloader.display(imageView, url);
						return;
					}
				if (all.size()==2) {
					List<String >imgs=all.get(b?0:1);
					int index=rd.nextInt(imgs.size());
					url=imgs.get(index);
					ImageView imageView=(ImageView) findViewById(R.id.imageView2);
					mAbImageDownloader.display(imageView, url);
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub 
				if (imagelist==null||imagelist.size()==0||!dateString.equals(AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMD))) {
					try {imagelist=new ArrayList<String >();
						
						Document doc=Jsoup.connect("http://jandan.net/ooxx").userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36").get();
						for (Element img : doc.select("a[href]")) {
							String src=img.attr("abs:href");
							try {
								if (src.contains(".sinaimg.cn/large/")&&src.endsWith(".jpg")) {
									imagelist.add(src);
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
						SharedPreferences SAVE =  getSharedPreferences("mn",
								 MODE_PRIVATE);
						Editor editor = SAVE.edit();
						editor.putString("list", new Gson().toJson(imagelist));
						editor.putString("date", AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMD));
						editor.commit();	
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
//				if (ToolUtil.isWidth) {
//					return "http://pics.dmm.co.jp/digital/video/tek00080/tek00080pl.jpg";
//					//return "https://img1.doubanio.com/view/photo/photo/public/p2360752009.jpg";
//				}
//				if(!ToolUtil.isWidth){
//					return "http://pics.dmm.co.jp/digital/video/tek00080/tek00080ps.jpg";
//					//return "https://img1.doubanio.com/view/photo/photo/public/p2362976267.jpg";
//				}
				if (imagelist!=null&&imagelist.size()>0) {
					return imagelist.get(rd.nextInt(imagelist.size()));
				}
				if (all==null||all.isEmpty()||!ddd.equals(version)) {
				all=new	ArrayList<List<String>>();
				if (version.equals("1")) {
					
				try { 
					Document doc = Jsoup.parse(getResources().getAssets().open("mn.txt"), "utf-8", "http://image.baidu.com/");
					ArrayList<String> all1=new ArrayList<String>();
					ArrayList<String> all2=new ArrayList<String>();
					for (Element img : doc.select("img")) {
						if (img.attr("src").contains("hiphotos.baidu.com/image/")) {
							try {

								if (Float.parseFloat(img.attr("height"))>Float.parseFloat(img.attr("width"))) {
									all1.add(img.attr("src"));
								}else {
									all2.add(img.attr("src"));
								}
								
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
					all.add(all1);
					all.add(all2);
				}catch	(Exception e){}
				}
				else {
					try {
						
					InputStreamReader inputReader = new InputStreamReader(
							getResources().getAssets().open("mn1.txt"), "utf-8");
					BufferedReader bufReader = new BufferedReader(inputReader);
					ArrayList<String> all1=new ArrayList<String>();
					ArrayList<String> all2=new ArrayList<String>(); 
					String line;
					while ((line = bufReader.readLine()) != null) {
						if (!AbStrUtil.isEmpty(line) && line.contains("|")) {
							String[]strs=line.split("\\|");
							for (int i = 1; i < strs.length; i++) {
								all1.add(strs[i]);all2.add(strs[i]);
							}
						}
					} 
					all.add(all1);
					all.add(all2);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				SharedPreferences SAVE =  getSharedPreferences("mn",
						 MODE_PRIVATE);
				Editor editor = SAVE.edit();
				editor.putString("context", new Gson().toJson(all));
				editor.putString("version", version);
				editor.commit();	
				}
				return null;
			}
		});
		
		mAbTask.execute(item);
		try {
			myuui=ENDEUtil.getMyUUID1(this);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "获取机器码失败，程序退出。", Toast.LENGTH_LONG).show();
			//System.exit(0);
			finish();
		}
		AbTask mAbTask1 = new AbTask();
		AbTaskItem item1 = new AbTaskItem();
		item1.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub 

				if (UpUtil.istimeintelok()) {
					SharedPreferences  SAVE =  getSharedPreferences("sqm",
								 MODE_PRIVATE);
					String sqm = SAVE.getString("context", myuui); 
					long ot = SAVE.getLong("time", 0L); 
					boolean  vip = SAVE.getBoolean("vip", true);
						if (AbStrUtil.isEmpty(sqm)||!sqm.equals(myuui)) {
							jumpsq() ;
						 
						}else { 

					if (vip) {
						  SAVE =  getSharedPreferences("sqm",
								 MODE_PRIVATE);
						Editor editor = SAVE.edit(); 

							editor.putBoolean("vip",  true);

							editor.commit();
						 jumpmain() ;
						return;
					}
							SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
							try { 
								java.util.Date end = dfs.parse("2016-05-17 23:59:59");
								long o=end.getTime() ;
								if (o<ot) {
									ot=o;
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							UpUtil.setinittime(ot);
							if (UpUtil.isappcanrun()) {
								
				/**/ jumpmain();
								handler.sendEmptyMessageDelayed(0, delayMillis);
							}
							else { 
								Editor editor = SAVE.edit();
								editor.putString("context", "");
								editor.putLong("time", 0L);
								editor.commit();
								Toast.makeText(MainActivity.this, "授权码时间过期，程序退出。请重新启动程序进行授权。", Toast.LENGTH_LONG).show();
								//System.exit(0); 
								finish();
							}
							
						}
				}
				else { 
					Toast.makeText(MainActivity.this, "联网同步时间失败，程序退出。", Toast.LENGTH_LONG).show();
					//System.exit(0); 
					finish();
				}
				
				
			}


			@Override
			public Object getObject() {
				// TODO Auto-generated method stub 
				 UpUtil.getnowdata();
				return null;
			}
		});

		mAbTask1.execute(item1);
		
		
		
		//handler.sendEmptyMessageDelayed(0, delayMillis);
	}
	
			private void jumpmain() {
				// TODO Auto-generated method stub
new Handler().postDelayed(new Runnable(	) {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub  PlayUrlActivity   
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putBoolean("ismain", true); 
						intent.setClass(MainActivity.this, BtdyttActivity.class); 
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
				}, 3000);
			}
	
	private void jumpsq() {
		byte[] key=Base64.decode("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4".getBytes(),Base64.DEFAULT);
        byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
        try {
			enuui=new String(Base64.encode(ENDEUtil.des3EncodeCBC(key, keyiv,myuui .getBytes("UTF-8")),  Base64.DEFAULT),"UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
        handler.sendEmptyMessageDelayed(1, delayMillis);
	}
	private String myuui="";
	private String enuui="";
	private  List<List<String>>all;
	private  List< String >imagelist;
	private Random rd=new Random();
	private static final long delayMillis=100;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				i++;
				if (rd.nextBoolean()) {
					pro.setProgress(i);
				}
				
				if(i<pro.getMax())
				handler.sendEmptyMessageDelayed(0, delayMillis);
				

				
				if(i==pro.getMax()/2)
				{
					findViewById(R.id.textView1).setVisibility(View.GONE);
					findViewById(R.id.progressBar1).setVisibility(View.GONE);
				}
				break;
				case 1:
					View view = mInflater.inflate(R.layout.dialog_main, null); 
					final EditText jqm = (EditText) view.findViewById(R.id.editText1);
					final EditText sqm = (EditText) view.findViewById(R.id.editText2);
					Button one_btn = (Button) view.findViewById(R.id.one_btn); 
					jqm.setText(enuui);
					one_btn.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							 byte[] key=Base64.decode("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4".getBytes(),Base64.DEFAULT);
						        byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
							String sss=sqm.getText().toString().trim();
							String jjj="";
							try {
							PrivateKey privateKey = ENDEUtil.loadPrivateKey(PRIVATE_KEY);   
							 byte[] decryptByte = ENDEUtil.decryptData(ENDEUtil.decode(sss), privateKey);  
							 decryptByte =  ENDEUtil.des3DecodeCBC(key, keyiv,decryptByte);
							 jjj=new String(decryptByte,"UTF-8");
							//jqm.setText(jjj);
							 if (jjj.startsWith(myuui+"|")) {
								long outtime=Long.parseLong(jjj.split("\\|")[1]);
								SharedPreferences SAVE =  getSharedPreferences("sqm",
										 MODE_PRIVATE);
								Editor editor = SAVE.edit();
								editor.putString("context", jjj.split("\\|")[0]);
								editor.putLong("time", outtime);
								try {

									editor.putBoolean("vip",  jjj.split("\\|")[2].equals("vip"));
								} catch (Exception e) {
									// TODO: handle exception
									editor.putBoolean("vip",  false);
								}
								editor.commit();	

								Toast.makeText(MainActivity.this, "授权成功，程序退出。请重新启动。", Toast.LENGTH_LONG).show();
								finish();
								//System.exit(0); 
							}
							} catch (Exception e) {
								// TODO: handle exception
								Toast.makeText(getBaseContext(), "授权码错误1。", Toast.LENGTH_LONG).show();
								finish();
							}
						}
					});

					(view.findViewById(R.id.choice_one_text)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub 
							ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							if (myClipboard != null) { 

								ClipData myClip = ClipData.newPlainText("text", enuui);
								myClipboard.setPrimaryClip(myClip);
								Toast.makeText(getApplicationContext(), "机器码已经复制到剪贴板",
										Toast.LENGTH_SHORT).show();
							}
						}
					});
					AbDialogUtil.showAlertDialog(view);
					break;
			}
		}
	}; 
	private void checkupdate() { 
		// TODO Auto-generated method stub  
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub 
						String objString=(String)obj;
						 if (! TextUtils.isEmpty(objString)) {
							 
							 PackageManager nPackageManager = MainActivity.this.getPackageManager();// 得到包管理器
								PackageInfo nPackageInfo;
								try {
									nPackageInfo = nPackageManager.getPackageInfo(MainActivity.this.getPackageName(),
											PackageManager.GET_CONFIGURATIONS);
									int nowversion = nPackageInfo.versionCode;// 得到现在app的版本号
									nowversion=25;
									Pattern pat = Pattern.compile("build\\s*(\\d+)");
									Matcher mat = pat.matcher(objString);
									if (mat.find()) {
										int xxx=Integer.parseInt(mat.group(1));
										if (xxx!=nowversion) {
											 Uri uri = Uri.parse(update);  
										     Intent it = new Intent(Intent.ACTION_VIEW, uri);  
										    startActivity(it);
										    AbToastUtil.showToast(MainActivity.this, "版本更新!");
										}
									}
								} catch (NameNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
						}
					}

					@Override
					public Object getObject() {
						// TODO Auto-generated method stub 
						
try { 
						
						Document doc=Jsoup.connect(update).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36").get();
						for (Element tr : doc.select("li")) {
							String text=tr.text().trim();
							 if (text.contains("build")) {
								
								 return text;
							}
						} 
					} catch (Exception e) {
						// TODO: handle exception
					}
						return null;
					}
				});

				mAbTask.execute(item); 
}
	private String update="https://www.pgyer.com/d2ev";
	 private static String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ9FN1w8gfXSBP1/"  
	            + "\r" + "fWtC4gicvB7t+XZ20Qn3eBOaMT1zYf6QtUQ1aAQKIlVDmyidA1/BOgwp07Rvc6V/" + "\r"  
	            + "imAEp4tOGtrP8vedgliVuqMcLeNONSdlzSW66alcayjHrb4+5IYGV9vzMk7qGLHg" + "\r"  
	            + "ZX++HJBUKkb1piqATvPJNFlhf1vJAgMBAAECgYA736xhG0oL3EkN9yhx8zG/5RP/" + "\r"  
	            + "WJzoQOByq7pTPCr4m/Ch30qVerJAmoKvpPumN+h1zdEBk5PHiAJkm96sG/PTndEf" + "\r"  
	            + "kZrAJ2hwSBqptcABYk6ED70gRTQ1S53tyQXIOSjRBcugY/21qeswS3nMyq3xDEPK" + "\r"  
	            + "XpdyKPeaTyuK86AEkQJBAM1M7p1lfzEKjNw17SDMLnca/8pBcA0EEcyvtaQpRvaL" + "\r"  
	            + "n61eQQnnPdpvHamkRBcOvgCAkfwa1uboru0QdXii/gUCQQDGmkP+KJPX9JVCrbRt" + "\r"  
	            + "7wKyIemyNM+J6y1ZBZ2bVCf9jacCQaSkIWnIR1S9UM+1CFE30So2CA0CfCDmQy+y" + "\r"  
	            + "7A31AkB8cGFB7j+GTkrLP7SX6KtRboAU7E0q1oijdO24r3xf/Imw4Cy0AAIx4KAu" + "\r"  
	            + "L29GOp1YWJYkJXCVTfyZnRxXHxSxAkEAvO0zkSv4uI8rDmtAIPQllF8+eRBT/deD" + "\r"  
	            + "JBR7ga/k+wctwK/Bd4Fxp9xzeETP0l8/I+IOTagK+Dos8d8oGQUFoQJBAI4Nwpfo" + "\r"  
	            + "MFaLJXGY9ok45wXrcqkJgM+SN6i8hQeujXESVHYatAIL/1DgLi+u46EFD69fw0w+" + "\r" + "c7o0HLlMsYPAzJw="  
	            + "\r";  
	
}
