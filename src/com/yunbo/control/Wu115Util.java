package com.yunbo.control;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.yunbo.myyunbo.CiLiActivity;
import com.yunbo.myyunbo.R;

public class Wu115Util {
public static void jiexi(final String hash,final String name,final AbActivity packageContext) {

	AbDialogUtil.showProgressDialog(packageContext,
			R.drawable.progress_circular, "正在解析磁力链接115...");
	AbTask mAbTask1 = new AbTask();
	final AbTaskItem item1 = new AbTaskItem();
	item1.setListener(new AbTaskObjectListener() {

		@Override
		public void update(Object obj) {
			// TODO Auto-generated method stub 
				AbDialogUtil.removeDialog(packageContext);
			 if (obj==null) {
				AbToastUtil.showToast(packageContext, "没有播放链接");
			}else {
				String url=obj.toString();
				DyUtil.play(packageContext, url, name);
			}
		}

		@Override
		public Object getObject() {
			// TODO Auto-generated method stub
			 
			return get115url(hash);
		}
	});

	 mAbTask1.execute(item1); 
}
public static String jxhand="http://feiyuys.com/playm3u8/?type=magnet&vid=%s";
public static String rr="http://www.and110.com/";
private static String get115url(String cilihash) { 
		try {

			Document doc = Jsoup
					.connect(
							String.format(jxhand,cilihash))
					.userAgent(
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
					.timeout(20000).header("Accept-Language", "zh-CN,zh;q=0.8")
					.header("Upgrade-Insecure-Requests", "1")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Cache-Control", "max-age=0").referrer(rr).get();
			String html = doc.html(); 
			Pattern pat = Pattern
					.compile("play_url\\s*=\\s*\"(.+)\"");
		 
			Matcher mat = pat.matcher(html);
			if (mat.find()) {
				String url = mat.group(1);
				System.out.println(url);
				return url;
			}else {
				System.out.println(html);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		return null;
}
public static void jiexi1(final String hash,final String name,final int index, final AbActivity packageContext) {

	AbDialogUtil.showProgressDialog(packageContext,
			R.drawable.progress_circular, "正在解析磁力链接115...");
	AbTask mAbTask1 = new AbTask();
	final AbTaskItem item1 = new AbTaskItem();
	item1.setListener(new AbTaskObjectListener() {

		@Override
		public void update(Object obj) {
			// TODO Auto-generated method stub 
				AbDialogUtil.removeDialog(packageContext);
			 if (obj==null) {
				AbToastUtil.showToast(packageContext, "没有播放链接");
			}else {
				String url=obj.toString();
				DyUtil.play(packageContext, url, name);
			}
		}

		@Override
		public Object getObject() {
			// TODO Auto-generated method stub
			try {
 
				
				URL	urlx = new URL(String.format(jxhand,hash))	;
				HttpURLConnection connection = (HttpURLConnection) urlx
						.openConnection();

				connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
						
				connection.setRequestProperty("Connection", "Keep-Alive"); 
				connection.setReadTimeout(30000);
				connection.setFollowRedirects(true);
				connection.connect(); 
				String html =""; 
				InputStream inStrm = connection.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						inStrm,"utf-8"));

				String temp = "";
				while ((temp = br.readLine()) != null) {
					html += (temp + '\n');
				}
				br.close();
				connection.disconnect(); 
					String url=html.replace("\\/", "/");
					
					System.out.println(html); try {
						if (null != url) {
							JSONObject newsObject = new JSONObject(url);
							JSONObject jsonObject = newsObject.getJSONObject("result");
							JSONArray filelist = jsonObject.getJSONArray("filelist"); 
							for (int i = 0; i < filelist.length(); i++) { 
								if (i==index) {	 
								JSONObject newsInfoLeftObject = filelist.getJSONObject(i);
										 //if (newsInfoLeftObject.getString("name").equals(index)) {
											url=newsInfoLeftObject.getString("url"); 

											System.out.println(url);
											return url;
										}; 

										 
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} 
			return get115url(hash);
		}
	});

	 mAbTask1.execute(item1); 
}
}
