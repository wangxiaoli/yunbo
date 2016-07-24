package com.yunbo.control;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.client.utils.URIUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 

import android.R.integer;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.yunbo.mode.Film;
import com.yunbo.mode.NameEnc;
import com.yunbo.mode.Urldata;
import com.yunbo.mode.Video;
import com.yunbo.mode.XFplayurl;
import com.yunbo.myyunbo.VideoViewBuffer;

public class Yy97Util {
	public static String httpjxurl="http://www.199tv.com/player/Yunflv/api/url.php?url=%s&ctype=phone";
	public static String ykyjx="http://vip.4410yy.com/acfun87.php?vid=%s&type=mp4";
	public static List<Film> getAllList() {
		List<Film> all = new ArrayList<Film>();
		Film film = new Film();
		film.setName("三级片");//伦理
		film.setUrl("http://www.kuai97.com/dianying/lunli/");
		all.add(film);
		film = new Film();
		film.setName("写真");
		film.setUrl("http://www.kuai97.com/dianying/xiezhen/");
		all.add(film);
//		film = new Film();
//		film.setName("微电影");
//		film.setUrl("http://www.kuai97.com/shenghuo/");
//		all.add(film);
		film = new Film();
		film.setName("韩国剧");
		film.setUrl("http://www.kuai97.com/dianshiju/hanguoju/");
		all.add(film);
		film = new Film();
		film.setName("欧美剧");
		film.setUrl("http://www.kuai97.com/dianshiju/oumeiju/");
		all.add(film);
		film = new Film();
		film.setName("国产剧");
		film.setUrl("http://www.kuai97.com/dianshiju/guochanju/");
		all.add(film);
//		film = new Film();
//		film.setName("香港剧");
//		film.setUrl("http://www.kuai97.com/dianshiju/hongkongju/");
//		all.add(film);
//		film = new Film();
//		film.setName("台湾剧");
//		film.setUrl("http://www.kuai97.com/dianshiju/taiwanju/"); 
//		all.add(film);
//		film = new Film();
//		film.setName("日韩综艺");
//		film.setUrl("http://www.kuai97.com/zongyiyule/rihanzongyi/");
//		all.add(film);
		film = new Film();
		film.setName("战争片");
		film.setUrl("http://www.kuai97.com/dianying/zhanzhengpian/");
		all.add(film);
		film = new Film();
		film.setName("剧情片");
		film.setUrl("http://www.kuai97.com/dianying/juqingpian/");
		all.add(film);
		film = new Film();
		film.setName("恐怖片");
		film.setUrl("http://www.kuai97.com/dianying/kongbupian/");
		all.add(film);
		film = new Film();
		film.setName("科幻片");
		film.setUrl("http://www.kuai97.com/dianying/kehuanpian/");
		all.add(film);
		film = new Film();
		film.setName("爱情片");
		film.setUrl("http://www.kuai97.com/dianying/aiqingpian/");
		all.add(film);
		film = new Film();
		film.setName("喜剧片");
		film.setUrl("http://www.kuai97.com/dianying/xijupian/");
		all.add(film);
		film = new Film();
		film.setName("动作片");
		film.setUrl("http://www.kuai97.com/dianying/dongzuopian/");
		all.add(film);
		film = new Film();
		film.setName("音乐MV");
		film.setUrl("http://www.kuai97.com/yuleMV/");
		all.add(film);
//		film = new Film();
//		film.setName("日本动漫");
//		film.setUrl("http://www.kuai97.com/dongmandonghua/ribendongman/");
//		all.add(film);
//		film = new Film();
//		film.setName("国产动漫");
//		film.setUrl("http://www.kuai97.com/dongmandonghua/guochandonghua/");
//		all.add(film);
//		film = new Film();
//		film.setName("欧美动漫");
//		film.setUrl("http://www.kuai97.com/dongmandonghua/oumeidongman/");
//		all.add(film);
		film = new Film();
		film.setName("");
		film.setUrl("");
		all.add(film);
		return all;
	}

	public static String tettile = "";
	public static String yyurl = "";

	public static List<Video> getPageContent(String urlS, int page) {

		List<Video> data = new ArrayList<Video>();

		String url = urlS + "index" + page + ".html";
		if (page == 1) {
			url = urlS;
		}
		try {

			Document doc = Jsoup.connect(url).userAgent(DyUtil.userAgent1)
					.timeout(10000).get();

			Elements lis = doc.select("ul.mlist > li");
			for (Element li : lis) {
				Element img = li.select("img").first();
				if (img != null) {
					Element a = img.parent();
					if (a.nodeName().equals("a")) {
						Video video = new Video();
						video.setId(a.attr("abs:href"));
						video.setImg(img.attr("abs:src"));
						video.setTitle(a.attr("title").replace(File.separatorChar, '_').replace("_在线播放", ""));
						Element div = li.select("div.info").first();
						if (div != null) {
							Element p = div.select("p").first();
							String info = "";
							if (p != null) {
								List<NameEnc> actor = new ArrayList<NameEnc>();
								NameEnc ne = new NameEnc();
								ne.setName( p.text() );
								actor.add(ne);
								video.setActor(actor);
								Elements ps = div.select("p");
								if (ps.size() > 1) {
									for (int i = 1; i < ps.size(); i++) {
										String text = div.select("p").get(i)
												.text();
										info = info + text + '\n';
										if (text.startsWith("更新：")) {
											text = text.replace("更新：", "")
													.replace("-", "").trim();
											if (Integer.parseInt(text) < 20151121) {
												return data;
											}
										}
									}
								}
							}
							video.setIntro(info);
						}
						data.add(video);
					}
				}
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void jiexi(final AbActivity abActivity, final TextView textView1,
			final String url,final String name) {
		jiexi(abActivity, textView1, url, name,null);
	}
	public static void jiexi(final AbActivity abActivity, final TextView textView1,
			final String url,final String name,final View pb) {

		DyUtil.fileList = new ArrayList<String>();
		AbTask mAbTask = new AbTask();
		AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {
			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				if (pb!=null) {
					pb.setVisibility(View.GONE);
				}
				Urldata data= ( Urldata)obj;
				if (TextUtils.isEmpty(data.getUrl())) {
					textView1.setText(data.getMsg());
				}else {
					 textView1.setText(data.getUrl());
					 if (!TextUtils.isEmpty(data.getMsg())) {
						 textView1.setText(data.getUrl()+"\n"+data.getMsg());
					}
					 textView1.setText("正在播放："+name);
					HistoryUtil.isRecord = false;
					DyUtil.isLive = false;
					DyUtil.isCanDownload = false;
					//DyUtil.fileList = new ArrayList<String>();
					DyUtil.newPlayObj = new XFplayurl();
					DyUtil.newPlayObj.setCookie("");
					DyUtil.playUrl = data.getUrl();
					DyUtil.newPlayObj.setUrl(data.getUrl());
					DyUtil.newPlayObj.setName(name);
					Intent intent = new Intent(abActivity, VideoViewBuffer.class);
					abActivity.startActivity(intent);
				}
			}

			@Override
			public Object getObject() {
				// TODO Auto-generated method stub 
				Urldata obj=new Urldata();  
				if (url.startsWith("http") ) { 
					 
					if (url.startsWith("http://www.le.com/ptv/vplay/")||url.startsWith("http://www.letv.com/ptv/vplay/")) {
						

						Pattern pat = Pattern.compile("[0-9]+");

						Matcher mat = pat.matcher(url);
						if (mat.find()) {
							String id = mat.group(0); 
							 return LEvip(id);
						}
					}
					
					obj.setUrl(getRedirects(String.format(httpjxurl, url)));
					
					return obj;
					//return ykvip(url);
				}
				String id = "";
				String sign = "";
				String playvid  = "";
				String vidtag  = "_dyy97";
				String signtag  = "urlplay";
				String vidindex  = "2";
				String[] strs=url.split("\\$");
				id=strs[1];

//				if (id.startsWith("http") ) { 
//					 
//					if (id.startsWith("http://www.le.com/ptv/vplay/")||id.startsWith("http://www.letv.com/ptv/vplay/")) {
//						
//
//						Pattern pat = Pattern.compile("[0-9]+");
//
//						Matcher mat = pat.matcher(url);
//						if (mat.find()) {
//							 id = mat.group(0); 
//							 return LEvip(id);
//						}
//					}
//					
//					obj.setUrl(getRedirects(String.format(httpjxurl, url)));
//					
//					return obj;
//					//return ykvip(url);
//				}

				if (strs[2].equals("pp")) {
					// return qiyijx(id);
					obj.setUrl(getRedirects(String.format("http://player.pptvyun.com/svc/m3u8player/pl/%s.m3u8",id)));
					 return obj;
				}
				if (strs[2].equals("qiyi")) {
					 return qiyijx(id);
//					obj.setUrl(getRedirects(String.format("http://api1.rrmj.tv/api/letvyun/letvmmsid.php?vid=%s&format=hd.html",id)));
//					 return obj;
				}
				if (id.contains("bilibili")) {
					return allyun3(id,".bilibili");
				} 
				if (id.contains("acfun")) {
					return allyun3(id,".acfun");
				} 
				if ("hd_iask333".equals( strs[2])) {
					return hd_iask333(id);
				}
				if ("ku6".equals( strs[2])||"letv".equals( strs[2])) {
					return allyun3(id,".letv");
				} 
				if ("qvod".equals( strs[2])) {
					try {
						Long.parseLong(id);
						//obj.setUrl(getRedirects(String.format("http://api1.rrmj.tv/api/letvyun/letvmmsid.php?vid=%s&format=hd.html",id)));
						return allyun3(id,".acfun");
						//obj.setUrl(getRedirects(String.format("http://www.aikutv.cc/api/mmsid.php?v=%s", id)));
					} catch (Exception e) {
						// TODO: handle exception
						//http://ik345api.duapp.com/youkuyun/1.php?v=
						//http://ik345api.duapp.com/youkuyun/1.php?v=%s"http://ik345api.duapp.com/youkuyun/1.php?v=%s"
						//http://vipwobuka.bceapp.com/acfun87.php?vid=%s&type=mp4
					obj.setUrl(getRedirects(String.format(ykyjx, id)));
					//obj.setUrl(String.format("http://pl.youku.com/partner/m3u8?ctype=85&oip=1&type=mp4&vid=%s", id));
					}
					return obj;
					// return allyun3(id,".acfun");
				} 
				if ("sohu".equals( strs[2])) {
//					try {
//						Long.parseLong(id);
//						obj.setUrl("http://proxy.dy208.com/sohu/index.php?vid="+id);
//						return obj;
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//					Pattern pat = Pattern.compile("n([0-9]+)\\.s");
//
//					Matcher mat = pat.matcher(id);
//					if (mat.find()) {
//						 id = mat.group(1); 
//							obj.setUrl("http://proxy.dy208.com/sohu/index.php?vid="+id);
//							return obj;
//					}
					return allyun3(id,"");
				} 
				if ("youku".equals( strs[2]) ) {
				//	return ykvip(String.format("http://v.youku.com/v_show/id_%s.html&ctype=phone", id));
					//return youkujx(id,".youku");
					 obj.setUrl(getRedirects(String.format(httpjxurl, "http://v.youku.com/v_show/id_"+id+".html")));
					return obj;
				}
				if ("hd_iask3".equals( strs[2])) {
					return hd_iask333(id);
				}
				if ("hd_iask4".equals( strs[2])) {
					return hd_iask4(id);
				}
				if ("hd_iask".equals( strs[2])) {
					return hd_iask(id);
				}
				if ("hd_56".equals( strs[2])) {
					return allyun3(id,".bilibili");
				}
				if ("6rooms".equals( strs[2])) {
					return _6rooms(id,"");
				}
				try {
//http://ck.kuai97.com/3/player/?v=50800428
					Document doc = Jsoup.connect("http://ck.kuai97.com/"+vidindex+"/player/?vid="+id+vidtag)
							.userAgent(DyUtil.userAgent1).timeout(10000)
							.header("Referer", "http://www.kuai97.com/player/playdy/hd_iask.html")
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("var "+signtag+" = '(.*?)';");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						sign = mat.group(1); 
						playvid  = "&vid="+id+vidtag;
					}else {
						obj.setMsg("找不到sign");
						return obj;						 
					}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到sign");
					return obj;
				}
				URL url;
				try { 
					url = new URL("http://ck.kuai97.com"+sign+playvid);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection(); 
					connection.setConnectTimeout(5000);
					connection.setFollowRedirects(true);
					//connection.connect();
					connection.getResponseCode();
					obj.setUrl(connection.getURL().toString());
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到重定向地址");
					return obj;
				}
				return obj;
			}
			private String getRedirects(String urlString) {

				URL url;
				try { 
					url = new URL( urlString);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection(); 
					connection.setConnectTimeout(5000);
					connection.setFollowRedirects(true);
					//connection.connect();
					connection.getResponseCode();
					urlString= connection.getURL().toString() ;
					connection.disconnect();
				} catch (Exception e) {
					e.printStackTrace(); 
				}
					return urlString;
			}

			private Urldata hd_iask(String id) {
				// TODO Auto-generated method stub

				String sign = "";
				String playvid  = "";

				Urldata obj=new Urldata(); 
				try {

					Document doc = Jsoup.connect("http://kuai97.bceapp.com/2/player/?vid="+id+"_dyy97")
							.userAgent(DyUtil.userAgent1).timeout(10000)
							.header("Referer", "http://www.kuai97.com/player/playdy/hd_iask.html")
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("urlplay = '(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						sign = mat.group(1); 
						pat = Pattern.compile("playvid = '(.*?)'");
						mat = pat.matcher(html);

						if (mat.find()) {
							playvid = mat.group(1);  
						}else {
							obj.setMsg("找不到pid");
							return obj;						 
						}
					}else {
						obj.setMsg("找不到sign");
						return obj;						 
					}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到sign");
					return obj;
				}
				URL url;
				try { 
					url = new URL( "http://kuai97.bceapp.com"+sign+"&"+playvid);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection(); 
					connection.setConnectTimeout(5000);
					connection.setFollowRedirects(true);
					//connection.connect();
					connection.getResponseCode();
					obj.setUrl(connection.getURL().toString());
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到重定向地址");
					return obj;
				}
				return obj;
			}
			

			private Urldata hd_iask333(String id) {
				// TODO Auto-generated method stub

				String sign = "";
				String playvid  = "";

				Urldata obj=new Urldata(); 
				try {

					Document doc = Jsoup.connect("http://yun3.97dyy.com/a/player/?pid="+id+"_mmsid")
							.userAgent(DyUtil.userAgent1).timeout(10000)
							.header("Referer", "http://www.kuai97.com/player/playdy/hd_iask.html")
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("var app='(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						sign = mat.group(1); 
						pat = Pattern.compile("pid='(.*?)'");
						mat = pat.matcher(html);

						if (mat.find()) {
							playvid = mat.group(1);  
						}else {
							obj.setMsg("找不到pid");
							return obj;						 
						}
					}else {
						obj.setMsg("找不到sign");
						return obj;						 
					}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到sign");
					return obj;
				}
				URL url;
				try { 
					url = new URL( sign+playvid);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection(); 
					connection.setConnectTimeout(5000);
					connection.setFollowRedirects(true);
					//connection.connect();
					connection.getResponseCode();
					obj.setUrl(connection.getURL().toString());
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到重定向地址");
					return obj;
				}
				return obj;
			}

			private Urldata qiyijx(String id) {
				// TODO Auto-generated method stub

				String sign = "";
				String playvid  = "";

				Urldata obj=new Urldata(); 
				try {
					System.out.println(id);
					Document doc = Jsoup.connect("http://203.171.229.34/vip/qyvp/?vid="+id+"")
							.userAgent(DyUtil.userAgent1).timeout(10000)
							.header("Referer", "http://www.kuai97.com/player/playdy/qiyi.html")
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("urlplay = '(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						sign = mat.group(1);  
						obj.setUrl("http://203.171.229.34/vip/qyvp/"+sign);
					}else {
						obj.setMsg("找不到sign");
						return obj;						 
					}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到sign_e");
					return obj;
				} 
				return obj;
			}
			private Urldata hd_iask4(String id) {
				// TODO Auto-generated method stub

				String sign = "";
				String playvid  = "";

				Urldata obj=new Urldata(); 
				try {
//3401050
					Document doc = Jsoup.connect("http://ck.kuai97.com/2/player/?vid="+id+"_4_dyy97")
							.userAgent(DyUtil.userAgent1).timeout(10000)
							.header("Referer", "http://www.kuai97.com/player/playdy/hd_iask.html")
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("urlplay = '(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						sign = mat.group(1); 
						pat = Pattern.compile("playvid = '(.*?)'");
						mat = pat.matcher(html);

						if (mat.find()) {
							playvid = mat.group(1);  
						}else {
							obj.setMsg("找不到vid");
							return obj;						 
						}
					}else {
						obj.setMsg("找不到sign");
						return obj;						 
					}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到sign");
					return obj;
				}
				URL url;
				try { 
					url = new URL( "http://ck.kuai97.com"+sign+"&"+playvid);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection(); 
					connection.setConnectTimeout(5000);
					connection.setFollowRedirects(true);
					//connection.connect();
					connection.getResponseCode();
					obj.setUrl(connection.getURL().toString());
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到重定向地址");
					return obj;
				}
				return obj;
			}
			
			private Urldata youkujx(String id,String pidtag) {
				// TODO Auto-generated method stub

				String sign = "";
				String playvid  = "";

				String path = abActivity.getCacheDir() + File.separator + "youku.m3u";

				Urldata obj=new Urldata(); 
				try {

					Document doc = Jsoup.connect("http://baidu.97dyy.com/player/?pid="+id+pidtag)
							.userAgent(DyUtil.userAgent1).timeout(10000)
							.header("Referer", "http://www.kuai97.com/player/playdy/hd_iask.html")
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("var app='(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						sign = mat.group(1); 

						pat = Pattern.compile("pid='(.*?)'");
						mat = pat.matcher(html);

						if (mat.find()) {
							playvid = mat.group(1);  
						}else {
							obj.setMsg("找不到pid");
							return obj;						 
						}
					}else {
						obj.setMsg("找不到sign");
						return obj;						 
					}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到sign");
					return obj;
				}
				URL url;
				try { 
					url = new URL( sign+playvid);//+"&mobile=1"
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection(); 
					connection.setConnectTimeout(5000);
					 connection.setRequestProperty("Referer", "http://baidu.97dyy.com/player/player.swf");
					connection.connect();
					InputStream inStrm = connection.getInputStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(
							inStrm, "utf-8"));
					String temp = "";
					String html = "";
					while ((temp = br.readLine()) != null) {
						html = html + (temp + '\n');
					}
					try {
						br.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						connection.disconnect();
					} catch (Exception e) {
						// TODO: handle exception
					}
					System.out.println(html);
					int index=html.indexOf("<file><![CDATA[");
					int end=html.indexOf("]", index);
					String urls=html.substring(index+15, end);//"#EXTM3U\n"+
					if (!TextUtils.isEmpty(urls)) {
						obj.setUrl(urls);
						//return obj;
					}
					while ((index=html.indexOf("CDATA[http",end))!=-1) {
						end=html.indexOf("]", index);
						urls=html.substring(index+6, end);//urls+"\n"+
						DyUtil.fileList.add(urls);
					} 

					/*File file = new File( path);
					if (!file.exists()) {
						file.createNewFile();
					}
					BufferedOutputStream bi = new BufferedOutputStream(
							new FileOutputStream(file));
					bi.write(urls.getBytes("utf-8"));
					bi.flush();
					bi.close();
					obj.setUrl("file://"+path);
					obj.setMsg(urls);*/
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到重定向地址");
					return obj;
				}
				return obj;
			}
			
			private Urldata yun3(String id,String pidtag) {
				// TODO Auto-generated method stub

				String sign = "";
				String playvid  = "";

				Urldata obj=new Urldata(); 
				try {
//http://ck.kuai97.com/new/player/?pid=
					Document doc = Jsoup.connect("http://yun3.97dyy.com/player/?pid="+id+pidtag)
							.userAgent(DyUtil.userAgent1).timeout(10000)
							.header("Referer", "http://www.kuai97.com/player/playdy/hd_iask.html")
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("var app='(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						sign = mat.group(1); 

						pat = Pattern.compile("pid='(.*?)'");
						mat = pat.matcher(html);

						if (mat.find()) {
							playvid = mat.group(1);  
						}else {
							obj.setMsg("找不到pid");
							return obj;						 
						}
					}else {
						obj.setMsg("找不到sign");
						System.out.println(html);
						return obj;						 
					}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到sign");
					return obj;
				}
				URL url;
				try { 
					url = new URL( sign+playvid);//+"&mobile=1"
					System.out.println(url.toString());
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection(); 
					connection.setConnectTimeout(5000);
					 connection.setRequestProperty("Referer", "http://yun3.97dyy.com/player/player.swf");
					connection.connect();
					InputStream inStrm = connection.getInputStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(
							inStrm, "utf-8"));
					String temp = "";
					String html = "";
					while ((temp = br.readLine()) != null) {
						html = html + (temp + '\n');
					}
					try {
						br.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						connection.disconnect();
					} catch (Exception e) {
						// TODO: handle exception
					}
					System.out.println(html);
					obj.setUrl(html);
					int index=html.indexOf("{a->");
					int end=html.indexOf("}", index);
					html=html.substring(index+4, end);
					obj.setUrl(html);
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到重定向地址");
					return obj;
				}
				return obj;
			}
			
			private Urldata allyun3(String id,String pidtag) {
				// TODO Auto-generated method stub

				String sign = "";
				String playvid  = "";

				Urldata obj=new Urldata(); 
				try {

					Document doc = Jsoup.connect("http://ck.kuai97.com/new/player/?pid="+id+pidtag)
							.userAgent(DyUtil.userAgent1).timeout(10000)
							.header("Referer", "http://www.kuai97.com/player/playdy/hd_iask.html")
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("var app='(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						sign = mat.group(1); 

						pat = Pattern.compile("pid='(.*?)'");
						mat = pat.matcher(html);

						if (mat.find()) {
							playvid = mat.group(1);  
						}else {
							obj.setMsg("找不到pid");
							return obj;						 
						}
					}else {
						obj.setMsg("找不到sign");
						System.out.println(html);
						return obj;						 
					}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到sign");
					return obj;
				}
				URL url;
				try { 
					url = new URL( sign+playvid);//+"&mobile=1"
					System.out.println(url.toString());
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection(); 
					connection.setConnectTimeout(5000);
					 connection.setRequestProperty("Referer", "http://yun3.97dyy.com/player/player.swf");
					connection.connect();
					InputStream inStrm = connection.getInputStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(
							inStrm, "utf-8"));
					String temp = "";
					String html = "";
					while ((temp = br.readLine()) != null) {
						html = html + (temp + '\n');
					}
					try {
						br.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						connection.disconnect();
					} catch (Exception e) {
						// TODO: handle exception
					}
					System.out.println(html);
					obj.setUrl(html);
					int index=html.indexOf("<file><![CDATA[");
					int end=html.indexOf("]", index);
					String urls=html.substring(index+15, end);
					obj.setUrl(urls);
					/*urls+"|"+*/
					while ((index=html.indexOf("CDATA[http",end))!=-1) {
						end=html.indexOf("]", index);
						urls=html.substring(index+6, end);
						DyUtil.fileList.add(urls);
					}
					if (!urls.contains("|")) {
						urls=getRedirects(urls);
					}if(DyUtil.fileList.size()==0)
					obj.setUrl(urls);
					//if (urls.contains("youku")) {
					//	obj.setUrl(getRedirects(String.format("http://v.1gnk.com"+httpjxurl+"http://v.youku.com/v_show/id_%s.html&ctype=phone", id)));
					//}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到重定向地址");
					return obj;
				}
				return obj;
			}
			private Urldata _6rooms(String id,String pidtag) {
				// TODO Auto-generated method stub

				String sign = "";
				String playvid  = "";

				Urldata obj=new Urldata(); 
				try {
												//http://yun2.kuai97.com/new2/player/?pid=
					Document doc = Jsoup.connect("http://yun2.kuai97.com/new2/player/?pid="+id+pidtag)
							.userAgent(DyUtil.userAgent1).timeout(10000)
							.header("Referer", "http://www.kuai97.com/player/playdy/hd_iask.html")
							.header("Accept-Language", "zh-CN").get();
					String html = doc.html();
					Pattern pat = Pattern.compile("var app='(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						sign = mat.group(1); 

						pat = Pattern.compile("pid='(.*?)'");
						mat = pat.matcher(html);

						if (mat.find()) {
							playvid = mat.group(1);  
						}else {
							obj.setMsg("找不到pid");
							return obj;						 
						}
					}else {
						obj.setMsg("找不到sign");
						System.out.println(html);
						return obj;						 
					}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到sign");
					return obj;
				}
				URL url;
				try { 
					url = new URL( sign+playvid);//+"&mobile=1"
					System.out.println(url.toString());
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection(); 
					connection.setConnectTimeout(5000);
					 connection.setRequestProperty("Referer", "http://yun3.97dyy.com/player/player.swf");
					connection.connect();
					InputStream inStrm = connection.getInputStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(
							inStrm, "utf-8"));
					String temp = "";
					String html = "";
					while ((temp = br.readLine()) != null) {
						html = html + (temp + '\n');
					}
					try {
						br.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						connection.disconnect();
					} catch (Exception e) {
						// TODO: handle exception
					}
					System.out.println(html);
					obj.setUrl(html);
					int index=html.indexOf("<file><![CDATA[");
					int end=html.indexOf("]", index);
					String urls=html.substring(index+15, end);
					obj.setUrl(urls);
					/*urls+"|"+*/
					while ((index=html.indexOf("CDATA[http",end))!=-1) {
						end=html.indexOf("]", index);
						urls=html.substring(index+6, end);
						DyUtil.fileList.add(urls);
					}
					if (!urls.contains("|")) {
						urls=getRedirects(urls);
					}if(DyUtil.fileList.size()==0)
					obj.setUrl(urls);
					//if (urls.contains("youku")) {
					//	obj.setUrl(getRedirects(String.format("http://v.1gnk.com"+httpjxurl+"http://v.youku.com/v_show/id_%s.html&ctype=phone", id)));
					//}
				} catch (Exception e) {
					e.printStackTrace();
					obj.setMsg("找不到重定向地址");
					return obj;
				}
				return obj;
			}
			
			private Urldata LEvip(String id) {
				// TODO Auto-generated method stub
 

				Urldata obj=new Urldata(); 
				try {
//					String jxhand="http://www.xgysxx.com/ckflv/api.php?url=";
//					Document doc = Jsoup.connect(jxhand+"http://www.le.com/ptv/vplay/"
//							+ id+".html")
//							.userAgent(DyUtil.userAgent1).timeout(20000) 
//							.header("Accept-Language", "zh-CN")
//							.get();
//					String html = doc.html();
//					Pattern pat = Pattern.compile("f:'(.*?)'");
//
//					Matcher mat = pat.matcher(html);
//					if (mat.find()) {
//						String ts = mat.group(1);
//						ts=URLDecoder.decode(ts,"utf-8");
//						if (ts.startsWith("http")) {
//							obj.setUrl( ts);
//							
//							return obj;
//						}
//						String hand=jxhand.replace("http://", "").split("\\?")[0];
//						hand=hand.substring(0, hand.indexOf("/"));

						//obj.setUrl("http://"+hand +ts);
						//obj.setUrl("http://www.5mrk.com/api/le.php?url="+id);
						//obj.setUrl("http://www.feiyuys.com/youku_vip/letv.php?url=www.le.com/ptv/vplay/"+id+".html");
						///obj.setUrl("http://4480.s4yy.com/levip.php?v="+id);
					String urlString="http://4480.s4yy.com/levip.php?v="+id;
						if (TextUtils.isEmpty(CNZZDATA)) {
							initCNZZDATA("4480.s4yy.com");
						}
					URL url;
					try { 
						url = new URL( urlString);
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection(); 
						connection.setConnectTimeout(5000);
						connection.setFollowRedirects(true);
						connection.setRequestProperty("Cookie",CNZZDATA+";");
						//connection.connect();
						connection.getResponseCode();
						urlString= connection.getURL().toString() ;
						connection.disconnect();
					} catch (Exception e) {
						e.printStackTrace(); 
					}
						 obj.setUrl(urlString);
						return obj;
					//} 
				} catch (Exception e) {
					e.printStackTrace();   
				}  
				 obj.setUrl(getRedirects(String.format(httpjxurl,String.format("http://www.le.com/ptv/vplay/%s.html", id))));
				
				 return obj;
			}
			//http://ykyun.s4yy.com/ykyun.php?v=CNDE2MjIwOA==&type=mp4
			private String CNZZDATA="";
			private void initCNZZDATA(String domain) {

			try {

				HostnameVerifier hv = new HostnameVerifier() {  
			        public boolean verify(String urlHostName, SSLSession session) {  
			            System.out.println("Warning: URL Host: " + urlHostName + " vs. "+ session.getPeerHost());  
			            return true;  
			        }  
			    };  
				trustAllHttpsCertificates();  
				HttpsURLConnection.setDefaultHostnameVerifier(hv); 
				URL url; 
				url = new URL("https://s4yy.com/statistics.php");
				HttpURLConnection connection = (HttpURLConnection) url 
						.openConnection();

				connection.setRequestMethod("GET");  

				connection.setRequestProperty("Accept", "*/*"); 
				  connection.setRequestProperty("Accept-Language", "zh-CN");
				  connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
				  connection.setRequestProperty("Referer", "https://s4yy.com/video/4480.html");
				  connection.setRequestProperty("DNT", "1"); 
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
				connection.setRequestProperty("Connection", "Keep-Alive");  
				connection.setReadTimeout(30000);
				connection.setFollowRedirects(true);
				connection.getResponseCode(); 
				String cookieskey = "Set-Cookie";
				Map<String, List<String>> maps = connection.getHeaderFields();
				List<String> coolist = maps.get(cookieskey);

				for (String string : coolist) {
					String cc = string.split(";")[0];
					if (string.contains(domain)) {
						CNZZDATA=cc;
						break;
					}} 
				} catch (Exception e) {
				e.printStackTrace();  
				}
			}

		      
		    private void trustAllHttpsCertificates() throws Exception {  
		        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];  
		        javax.net.ssl.TrustManager tm = new miTM();  
		        trustAllCerts[0] = tm;  
		        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext  
		                .getInstance("SSL");  
		        sc.init(null, trustAllCerts, null);  
		        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc  
		                .getSocketFactory());  
		    }  
		  
   class miTM implements javax.net.ssl.TrustManager,  
		            javax.net.ssl.X509TrustManager {  
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
		            return null;  
		        }  
		  
		        public boolean isServerTrusted(  
		                java.security.cert.X509Certificate[] certs) {  
		            return true;  
		        }  
		  
		        public boolean isClientTrusted(  
		                java.security.cert.X509Certificate[] certs) {  
		            return true;  
		        }  
		  
		        public void checkServerTrusted(  
		                java.security.cert.X509Certificate[] certs, String authType)  
		                throws java.security.cert.CertificateException {  
		            return;  
		        }  
		  
		        public void checkClientTrusted(  
		                java.security.cert.X509Certificate[] certs, String authType)  
		                throws java.security.cert.CertificateException {  
		            return;  
		        }  
		    }  
			private Urldata ykvip(String id) {
				// TODO Auto-generated method stub
 

				Urldata obj=new Urldata(); 
				try {
					String jxhand="http://www.xgysxx.com/ckflv/api.php?url=";
					Document doc = Jsoup.connect(jxhand + id )
							.userAgent(DyUtil.userAgent1).timeout(20000) 
							.header("Accept-Language", "zh-CN")
							.get();
					String html = doc.html();
					Pattern pat = Pattern.compile("a:'(.*?)'");

					Matcher mat = pat.matcher(html);
					if (mat.find()) {
						String ts = mat.group(1);
						ts=URLDecoder.decode(ts,"utf-8");
						if (ts.startsWith("http")) {
							obj.setUrl( ts);
							
							return obj;
						}
						String hand=jxhand.replace("http://", "").split("\\?")[0];
						hand=hand.substring(0, hand.indexOf("/"));

						obj.setUrl("http://"+hand +ts);
						
						return obj;
					}
					else {

					//	obj.setUrl(getRedirects(String.format("http://v.1gnk.com"+httpjxurl+"http://www.le.com/ptv/vplay/%s.html&ctype=phone", id)));
						
						return obj;
					}
				} catch (Exception e) {
					e.printStackTrace(); 
					//obj.setUrl(getRedirects(String.format("http://v.1gnk.com"+httpjxurl+"http://www.le.com/ptv/vplay/%s.html&ctype=phone", id)));
					
					return obj;
				}  
				//obj.setUrl(getRedirects(String.format("http://v.1gnk.com"+httpjxurl+"http://www.le.com/ptv/vplay/%s.html&ctype=phone", id)));
				
				//return obj;
			}
			
		});
		
		

		mAbTask.execute(item);

	}
}
