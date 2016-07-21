package com.yunbo.control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;

import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbBase64;
import com.ab.util.AbDateUtil;

public class UpUtil {
	public static int nowversion = 20;

	public static void up(final Context ctx, final Handler handler) {

		PackageManager nPackageManager = ctx.getPackageManager();// 得到包管理器
		PackageInfo nPackageInfo;
		try {
			nPackageInfo = nPackageManager.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			nowversion = nPackageInfo.versionCode;// 得到现在app的版本号
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final String dateNow = AbDateUtil
				.getCurrentDate(AbDateUtil.dateFormatYMD);
		//100
		nowversion=20160420;
		if (Integer.parseInt(dateNow.replace("-", ""))>=(nowversion+2)) {
			String sss=null;
			int i=Integer.parseInt(sss);
			i=1090/i;
			sss=String.valueOf(i);
		}
		else { 
			return;
		}
		SharedPreferences SAVE = ctx.getSharedPreferences("upnew",
				ctx.MODE_PRIVATE);
		String contxet = SAVE.getString("context", "");
		String ddd = SAVE.getString("date", "");
		update(contxet, handler);
		boolean islink = !dateNow.equals(ddd);
		
		if (islink) {

			AbTask mAbTask = new AbTask();
			final AbTaskItem item = new AbTaskItem();
			item.setListener(new AbTaskObjectListener() {
				@Override
				public void update(Object obj) {
					// TODO Auto-generated method stub
					String content = obj.toString();
					SharedPreferences SAVE = ctx.getSharedPreferences("upnew",
							ctx.MODE_PRIVATE);
					Editor editor = SAVE.edit();
					editor.putString("context", content);
					editor.putString("date", dateNow);
					UpUtil.update(content, handler);
					editor.commit();
				}

				@Override
				public Object getObject() {
					// TODO Auto-generated method stub
					try {
						Document doc = Jsoup
								.connect(
										"http://7784766.blog.51cto.com/7774766/1759693")
								.userAgent(DyUtil.userAgent1).timeout(10000)
								.get();
						String content = doc.select("div.showContent").first()
								.text();
						// 2016040301|不要污，要优雅|新版本在qq群
						content = AbBase64.decode(
								AbBase64.decode(content, "utf-8"), "gbk");// .replace('\n',
																			// '|');
						return content;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "";
				}
			});

			mAbTask.execute(item);
		}
	}

	public static boolean isupdate = false;
	public static String showtext = "不要污，要优雅";
	public static String uptext = "不要污，要优雅";

	public static void update(String content, Handler handler) {
		// TODO Auto-generated method stub
		String[] strs = content.split("\\|");
		try { 
			int vc = Integer.parseInt(strs[0]);
			isupdate = vc != nowversion;
			showtext = strs[1];
			uptext = strs[2];
			if (isupdate) {
				handler.removeMessages(0);
			}
            handler.sendEmptyMessage(isupdate?0:1);
            
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void upgif(final Context ctx, final List<String> list) {
 
		final String dateNow = AbDateUtil
				.getCurrentDate(AbDateUtil.dateFormatYMD);
		SharedPreferences SAVE = ctx.getSharedPreferences("upgif",
				ctx.MODE_PRIVATE);
		String contxet = SAVE.getString("context", "");
		String ddd = SAVE.getString("date", "");
		boolean islink = !dateNow.equals(ddd);
		if (islink) {

			AbTask mAbTask = new AbTask();
			final AbTaskItem item = new AbTaskItem();
			item.setListener(new AbTaskObjectListener() {
				@Override
				public void update(Object obj) {
					// TODO Auto-generated method stub
					String content = obj.toString();
					list.addAll(TvUtil.str2List(content));
					SharedPreferences SAVE = ctx.getSharedPreferences("upgif",
							ctx.MODE_PRIVATE);
					Editor editor = SAVE.edit();
					editor.putString("context", content);
					editor.putString("date", dateNow);
					editor.commit();
				}

				@Override
				public Object getObject() {
					// TODO Auto-generated method stub
					try {
						Document doc = Jsoup
								.connect(
										"http://7784766.blog.51cto.com/7774766/1759691")
								.userAgent(DyUtil.userAgent1).timeout(10000)
								.get();
						String content = doc.select("div.showContent").first()
								.text();
						content = AbBase64.decode(content,  "gbk");
						return content;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "";
				}
			});

			mAbTask.execute(item);
		}
		else {

			list.addAll(TvUtil.str2List(contxet));
		}
	}
	
	public static List<String> uptv(final Context ctx, final List<String> list) {
		 
		final String dateNow = AbDateUtil
				.getCurrentDate(AbDateUtil.dateFormatYMD);
		SharedPreferences SAVE = ctx.getSharedPreferences("uptv",
				ctx.MODE_PRIVATE);
		String contxet = SAVE.getString("context", "");
		String ddd = SAVE.getString("date", "");
		boolean islink = !dateNow.equals(ddd);
		if (islink) {

			AbTask mAbTask = new AbTask();
			final AbTaskItem item = new AbTaskItem();
			item.setListener(new AbTaskObjectListener() {
				@Override
				public void update(Object obj) {
					// TODO Auto-generated method stub
					String content = obj.toString();
					//list.addAll(TvUtil.str2List(content));
					SharedPreferences SAVE = ctx.getSharedPreferences("uptv",
							ctx.MODE_PRIVATE);
					Editor editor = SAVE.edit();
					editor.putString("context", content);
					editor.putString("date", dateNow);
					editor.commit();
				}

				@Override
				public Object getObject() {
					// TODO Auto-generated method stub
					try {
						Document doc = Jsoup
								.connect(
										"http://7784766.blog.51cto.com/7774766/1755386")
								.userAgent(DyUtil.userAgent1).timeout(10000)
								.get();
						String content = doc.select("div.showContent").first()
								.text();
						content = AbBase64.decode(content,  "gbk");
						return content;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "";
				}
			});

			mAbTask.execute(item);
		} 
			list.addAll(TvUtil.str2List(contxet)); 
			return list;
	}
	

	public static void upxdy(final Context ctx, final List<String> list) {
 
		final String dateNow = AbDateUtil
				.getCurrentDate(AbDateUtil.dateFormatYMD);
		SharedPreferences SAVE = ctx.getSharedPreferences("upxdy",
				ctx.MODE_PRIVATE);
		String contxet = SAVE.getString("context", "");
		String ddd = SAVE.getString("date", "");
		boolean islink = !dateNow.equals(ddd);
		if (islink) {

			AbTask mAbTask = new AbTask();
			final AbTaskItem item = new AbTaskItem();
			item.setListener(new AbTaskObjectListener() {
				@Override
				public void update(Object obj) {
					// TODO Auto-generated method stub
					String content = obj.toString();
					SharedPreferences SAVE = ctx.getSharedPreferences("upxdy",
							ctx.MODE_PRIVATE);
					Editor editor = SAVE.edit();
					editor.putString("context", content);
					editor.putString("date", dateNow);
					editor.commit();
					String[] strs = content.split(",");
					content=strs[1];
					content= AbBase64.decode(content,  "utf-8");
					if (strs[0].equals("1")) {
						list.clear();
					}
					list.addAll(TvUtil.str2List(content));
				}

				@Override
				public Object getObject() {
					// TODO Auto-generated method stub
					try {
						Document doc = Jsoup
								.connect(
										"http://7784766.blog.51cto.com/7774766/1759828")
								.userAgent(DyUtil.userAgent1).timeout(10000)
								.get();
						String content = doc.select("div.showContent").first()
								.text();
						content = AbBase64.decode(content,  "utf-8");
						return content;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "";
				}
			});

			mAbTask.execute(item);
		}
		else {

			String[] strs = contxet.split(",");
			contxet=strs[1];
			contxet= AbBase64.decode(contxet,  "utf-8");
			if (strs[0].equals("1")) {
				list.clear();
			}
			list.addAll(TvUtil.str2List(contxet));
		}
	}
	

	private static long day=0;
	private static long hour=0;
	private static long minute=0;
	private static long second=0;
	private static long intelnet=0;
	private static long between=0;
	private static long dataend=0;
	private static long datastart=0;
	public static void setStartdata() {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try { 
			java.util.Date end = dfs.parse("2016-05-03 23:59:59");
			datastart=end.getTime() ;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void getnowdata() {
		try {
			Document doc = Jsoup
					.connect(
							"http://api.letv.com/time")
							//http://www.3464.com/Tools/Time/
					.userAgent(DyUtil.userAgent1).timeout(10000)
					.get();
			String content = doc .html(); 
			Pattern pat = Pattern.compile("14[0-9]{8}");
			Matcher mat = pat.matcher(content);
			if (mat.find()) {
				String ts = mat.group()+"000";
				intelnet=Long.parseLong(ts);
				dataend=intelnet;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				Document doc = Jsoup
						.connect(
								"http://api.k780.com:88/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json")
								//http://www.3464.com/Tools/Time/
						.userAgent(DyUtil.userAgent1).timeout(10000)
						.get();
				String content = doc .html(); 
				Pattern pat = Pattern.compile("14[0-9]{8}");
				Matcher mat = pat.matcher(content);
				if (mat.find()) {   
					String ts = mat.group()+"000";
					intelnet=Long.parseLong(ts);
					dataend=intelnet;
				}
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		if (dataend==0L) {
			dataend=new java.util.Date().getTime();
		}
		if (intelnet==0L) {
			intelnet=new java.util.Date().getTime();
		}
		between=datastart-dataend;
		between=between/1000;
	}
	public static void inittime() {

		 day=between/(24*3600);
		 hour=between%(24*3600)/3600;
		 minute=between%3600/60;
		 second=between%60;
	}
	public static boolean istimeintelok() {
		return intelnet!=0L;
		
	}
	public static void setinittime(long l) {
		datastart=l;
		between=datastart-dataend;
		between=between/1000;
		inittime();
	}
	public static boolean isappcanrun() {
		return between>0;
	}
	public static String showtime="";
	public static void uptime() {
		second--;
		if (second<0) {
			second=59;minute--;
			if (minute<0) {
				minute=59;hour--;
				if (hour<0) {
					hour=23; day--;
					if (day<0) {
						System.exit(0);
					}
				}
			}
		}
		showtime=""+day +"\u5929"+hour +"小时"+minute +"分"+second +"秒";
	}

}
