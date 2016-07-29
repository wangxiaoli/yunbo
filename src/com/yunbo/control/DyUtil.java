package com.yunbo.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
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

import android.R.integer;
import android.R.interpolator;
import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.util.AbBase64;
import com.ab.util.AbDateUtil;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunbo.frame.CiliAdapter;
import com.yunbo.mode.Dydata;
import com.yunbo.mode.Dyres;
import com.yunbo.mode.History;
import com.yunbo.mode.Iilpaylist;
import com.yunbo.mode.Iilpayobj;
import com.yunbo.mode.Iiplaydata;
import com.yunbo.mode.Movie;
import com.yunbo.mode.NameEnc;
import com.yunbo.mode.Node;
import com.yunbo.mode.PageContent;
import com.yunbo.mode.QQFenXiang;
import com.yunbo.mode.Subfile_List;
import com.yunbo.mode.Urldata;
import com.yunbo.mode.Video;
import com.yunbo.mode.XFPlay;
import com.yunbo.mode.XFplayurl;
import com.yunbo.mode.XunleiItem;
import com.yunbo.mode.Yayabt;
import com.yunbo.mode.Yayaobj;
import com.yunbo.myyunbo.CiLiActivity;
import com.yunbo.myyunbo.FilmActivity;
import com.yunbo.myyunbo.PlayActivity;
import com.yunbo.myyunbo.PlayUrlActivity;
import com.yunbo.myyunbo.R;
import com.yunbo.myyunbo.SearchActivity;
import com.yunbo.myyunbo.SelectPlayerActivity;
import com.yunbo.myyunbo.VideoViewBuffer;

public class DyUtil {
	public static String PATH;
	public static String playUrl;
	public static ArrayList<Urldata> urls;
	public static List<Video> videos;

	public static void init() {

		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "urls.obj");
		if (file.exists()) {

			ObjectInputStream in;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				urls = (ArrayList<Urldata>) in.readObject();

				in.close();

				/*
				 * for (Urldata urldata : urls) {
				 * System.out.println(urldata.getMsg());
				 * System.out.println(urldata.getUrl()); }
				 */

			} catch (Exception e) {
				e.printStackTrace();
				try {
					file.delete();
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		}
		if (urls == null) {
			urls = new ArrayList<Urldata>();
		}
	}

	public static String getHtml(String getUrl, int outtime, String charsetName) {
		String html = "";
		URL url;
		try {
			url = new URL(getUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection
					.setRequestProperty("User-Agent",
							userAgent);
			// connection.setRequestProperty("Connection", "Keep-Alive");
			// connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setConnectTimeout(outtime);
			connection.setReadTimeout(outtime); 
			connection.connect();
			InputStream inStrm = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					inStrm, charsetName));
			String temp = "";

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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return html;
	}

	public static void save() {

		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "urls.obj");

		try {

			file.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(file));
			out.writeObject(urls);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Urldata readUrl(String thunder) {
		// init();
		/**/if (urls == null) {
			init();
		}
		for (Urldata data : urls) {

			if (data.getMsg().equals(thunder)) {
				Urldata temp = new Urldata();
				temp.setMsg("");
				temp.setUrl(data.getUrl());
				return temp;
			}
		}
		return new Urldata();
	}

	public static List<String> readFiles(String http, String path) {
		// init();
		PATH = path;
		List<String> list = new ArrayList<String>();
		if (urls == null) {
			init();
		}
		for (Urldata data : urls) {

			if (data.getMsg().equals(http)) {

				for (String string : data.getUrl().split("-")) {
					if (!AbStrUtil.isEmpty(string)) {
						list.add(string);
					}
				}
				// temp.setUrl(data.getUrl());
				return list;
			}
		}
		return list;
	}
    public static XunleiItem dydatehashs;
	public static Urldata getDyPlayUrl(final String thunder) {
		Urldata playUrl = readUrl(thunder);
		 if (!AbStrUtil.isEmpty(playUrl.getUrl())) {
			 String text = playUrl.getUrl();
				Gson gson = new Gson();
				XunleiItem data = gson.fromJson(text, XunleiItem.class);
				playUrl = new Urldata();
				playUrl.setMsg(data.getResp().getSubfile_list().size()+"");
				//playUrl.setUrl(text);
				dydatehashs=data;
	 		return playUrl;
		 } 
					//URLEncoder.encode(thunder, "UTF-8");
			//temp = URLEncoder.encode(temp, "UTF-8");
			String url = "http://i.vod.xunlei.com/req_subBT/info_hash/"+thunder+"/req_num/1000/req_offset/0";
		try {

			// Document doc = Jsoup.connect(url).get();
			String text = getHtml(url, 10000, "utf-8");
				 // System.out.println(text);
			if (!AbStrUtil.isEmpty(text) && !text.contains("\"record_num\": 0")) {
				// System.out.println(text);
				text = text.trim();
				text = URLDecoder.decode(text,"utf-8");

				text = text.replace("【处理中,请等待】 ", "").replace("【无效链接】 ", "");
				Gson gson = new Gson();
				XunleiItem data = gson.fromJson(text, XunleiItem.class);
				playUrl = new Urldata();
				playUrl.setMsg(data.getResp().getSubfile_list().size()+""); 
				/**/
				if (!AbStrUtil.isEmpty(text)) {
					Urldata datax = new Urldata();

					datax.setMsg(thunder);
					datax.setUrl(text);
					urls.add(datax);
					save();
				}
				dydatehashs=data;
				/*
				if (condition) {
					
				} else {

				}*/
			} else {
				playUrl.setMsg("种子信息获取失败 ");//+\r\ntext
				dydatehashs=null;
			}
			// System.out.println(text);
			// System.out.println(allList.size());
		} catch (Exception e) {
			playUrl.setMsg(url);
			e.printStackTrace();
		}
		return playUrl;

	}

	public static void getPlayUrl(final String thunder,
			final PlayUrlActivity packageContext) {
		AbDialogUtil.showProgressDialog(packageContext,
				R.drawable.progress_circular, "正在获取磁力信息...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				if (packageContext.isBack)
					return;
				//AbDialogUtil.removeDialog(packageContext);
				Urldata data = (Urldata) obj;
				/*
				if (!AbStrUtil.isEmpty(data.getMsg())) {
					// System.out.println(playUrl);
					String msg = data.getMsg();
					if (msg.contains("downloading")) {
						msg = "资源正在下载中";
					}
					if (msg.equals("url null")) {
						msg = "链接错误或者版权原因无法播放";
					}
					AbToastUtil.showToast(packageContext, msg);
				}*/
				if (dydatehashs!=null&&dydatehashs.getResp().getSubfile_list().size()>0) {
					
					final List<Subfile_List> list = dydatehashs.getResp().getSubfile_list();
					View mView = packageContext.mInflater.inflate(R.layout.dia_list_nowb, null);
					ListView listView = (ListView) mView
							.findViewById(R.id.listView1);
					final String info_hash=dydatehashs.getResp().getInfo_hash();
					List<Subfile_List> listT =new ArrayList<Subfile_List>();
					for (Subfile_List subfile_List : list) {
						if (!subfile_List.getName().endsWith("html") && 
								!subfile_List.getName().endsWith("txt") && 
								!subfile_List.getName().endsWith("jpg") && 
								!subfile_List.getName().endsWith("png"))
						{
							listT.add(subfile_List);
						}
					}
					String[] mStrings = new String[listT.size()];
					for (int i = 0; i < listT.size(); i++) {
						mStrings[i] = "【"+convertFileSize(listT.get(i).getFile_size())
								+"】 "+listT.get(i).getName();
					}
					ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
							packageContext, R.layout.dialog_list_item_1,
							mStrings);
					listView.setAdapter(listViewAdapter);
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// AbDialogUtil.removeDialog(FilmActivity.this);

							/*HistoryUtil.data = new History();
							HistoryUtil.data.setName(list.get(position).getName());
							DyUtil.urlToPlay = list.get(position).getUrl();
							Intent intent = new Intent(FilmActivity.this,
									PlayUrlActivity.class);
							startActivity(intent);*/
						}
					}); 
					//System.out.println(listT.size());
					AbDialogUtil.showAlertDialog(mView);
					
					/*String playurl = data.getUrl();
					// AbToastUtil.showToast(SearchActivity.this, playurl);
					DyUtil.playUrl = playurl.replace("\\/", "/");
					;
					// Bundle bundle = new Bundle();
					// bundle.putString("playurl", playurl);
					Intent intent = new Intent(packageContext,
							SelectPlayerActivity.class);

					packageContext.startActivity(intent);*/
				} else {
					 AbToastUtil.showToast(packageContext, "没有文件！");
				}
			}

			@Override
			public Urldata getObject() {
				// TODO Auto-generated method stub
				PATH = packageContext.getFilesDir() + "/";
				return DyUtil.getDyPlayUrl(thunder);
			}
		});

		mAbTask.execute(item);
	}
	public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
 
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

	public static Dyres getDyres(String wd) {
		try {
			// ArrayList<String>keys=new ArrayList<String>();
			String url = "http://www.dychao.com/movie/api.php?ap=res&callback=back&wd="
					+ URLEncoder.encode(wd, "UTF-8");

			Document doc = Jsoup.connect(url).get();
			String text = doc.text();
			if (!AbStrUtil.isEmpty(text) && text.contains("back")) {
				text = text.trim().substring(5, text.length() - 2);
				Gson gson = new Gson();
				Dyres dy = gson.fromJson(text, Dyres.class);
				return dy;
			}
			System.out.println(text);
			// System.out.println(allList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Dydata getDydata(int page, String wd) {
		try {
			// ArrayList<String>keys=new ArrayList<String>();
			String url = "http://vod.dychao.com/search?p=" + page
					+ "&sort=8&wd=" + URLEncoder.encode(wd, "UTF-8")
					+ "&ie=utf-8&callback=jQuery1_2&_="
					+ System.currentTimeMillis();

			Document doc = Jsoup.connect(url).get();
			String text = doc.text();
			if (!AbStrUtil.isEmpty(text) && text.contains("jQuery1_2")) {
				text = text.trim().substring(10, text.length() - 1);
				Gson gson = new Gson();
				Dydata dy = gson.fromJson(text, Dydata.class);
				return dy;
			}
			// System.out.println(text);
			// System.out.println(allList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static List<Video> getPageContent(int page) {

		List<Video> data=new ArrayList<Video>()  ; 

		  String url = "http://www.xiamp4.com/GvodHtml/15_" + page + ".html";
		if (page==1) {
			url = "http://www.xiamp4.com/GvodHtml/15.html";
		}
		try { 

			Document doc = Jsoup.connect(url).userAgent(  userAgent1)
					.timeout(10000).get();  

			Elements lis = doc.select("ul.mlist > li"); 
			for (Element li : lis) {
				Element img=li.select("img").first();
				if (img!=null) {
					Element a=img.parent();
					if (a.nodeName().equals("a")) {
						Video video=new Video();
						video.setId(a.attr("abs:href"));
						video.setImg(img.attr("abs:src"));
						video.setTitle(a.attr("title"));
						Element div=li.select("div.info").first();
						if (div!=null) { 
							Element p=div.select("p").first();
							if (p!=null) {
								List<NameEnc>actor=new ArrayList<NameEnc>();
								NameEnc ne=new NameEnc();
								ne.setName(p.text());
								actor.add(ne);
								video.setActor(actor);
							}
							Elements ies=div.select("i");
							String info="";
							for (Element i : ies) {
								String text=i.text();
								info=info+text+"\n";
								if (text.startsWith("地区：")) {
									List<NameEnc>area=new ArrayList<NameEnc>();
									NameEnc ne=new NameEnc();
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
			 				return data;  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String u2s(String u) {
		Pattern pat = Pattern.compile("[\\\\U|\\\\u]([0-9a-fA-F]{4})");
		Matcher mat = pat.matcher(u);
		while (mat.find()) {
			String HEX = mat.group(1);
			char v = (char) (Integer.parseInt(HEX.toUpperCase(), 16));
			mat = pat.matcher(u = u.replace("\\" + mat.group(), "" + v));
		}
		return u;
	}

	public static String u2s2(String u) { 
		if (TextUtils.isEmpty(u)) {
			return "";
		}
		u=u.replace("\\U", "\\u");
		int start=0;
		int end=0;
		List<String>us=new ArrayList<String>();
		Map<String, String>map=new HashMap<String, String>();
		while ((start=u.indexOf("\\u",start))!=-1 ) {
			start=start+2;
			end=start+4;
			if(end>=u.length())break;
			String HEX=u.substring(start, end);
			if (map.get(HEX)!=null) continue;
			map.put(HEX, HEX);
			us.add(HEX);
			start=end ;
		} 
		for (String HEX : us) {
			try {
				
			char v = (char) (Integer.parseInt(HEX.toUpperCase(), 16));
			u = u.replace("\\u" + HEX, "" + v);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
			
		return u;
	}

	public static Video showVideo;
	public static Node nodePlay;
	public static String urlToPlay;

	public static void thunderPlay(final String thunder,
			final PlayUrlActivity packageContext) {
		String url = thunder;
		if (thunder.startsWith("ftp://") || thunder.startsWith("http://")) {
			url = enThunder(url);
		}
		getPlayUrl(url, packageContext);
	}

	public static String enThunder(String url,String charsetName) {
		url = "AA" + url + "ZZ";
		try {
			byte[] bytes = url.getBytes("GBK");
			String temp = "" + new String(Base64.encode(bytes, Base64.DEFAULT));
			url = "";
			char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E',
					'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
					'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
					'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
					'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0',
					'1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', '=' };
			for (int i = 0; i < temp.length(); i++)
				for (int j = 0; j < base64EncodeChars.length; j++) {
					if ((int) temp.charAt(i) == (int) base64EncodeChars[j]) {
						url = url + temp.charAt(i);
						break;
					}
				}
			url = "thunder://" + url;
			// System.out.println(key);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	
	public static String enThunder(String url) {
		 
		return enThunder(url, "GBK");
	}

	public static void qqxfPlay(final String url,
			final PlayUrlActivity packageContext) {
		// TODO Auto-generated method stub
		PATH = packageContext.getFilesDir() + "/";
		AbDialogUtil.showProgressDialog(packageContext,
				R.drawable.progress_circular, "正在获取播放链接...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				if (packageContext.isBack)
					return;
				AbDialogUtil.removeDialog(packageContext);
				Urldata data = (Urldata) obj;
				/*
				if (!AbStrUtil.isEmpty(data.getMsg())) {
					// System.out.println(playUrl);
					String msg = data.getMsg();
					if (msg.contains("downloading")) {
						msg = "资源正在下载中";
					}
					if (msg.equals("url null")) {
						msg = "链接错误或者版权原因无法播放";
					}
					AbToastUtil.showToast(packageContext, msg);
				}*/
				if (dydatehashs!=null&&dydatehashs.getResp().getSubfile_list().size()>0) {
					
					final List<Subfile_List> list = dydatehashs.getResp().getSubfile_list();
					View mView = packageContext.mInflater.inflate(R.layout.dia_list_nowb, null);
					ListView listView = (ListView) mView
							.findViewById(R.id.listView1);
					final String info_hash=dydatehashs.getResp().getInfo_hash();
					List<Subfile_List> listT =new ArrayList<Subfile_List>();
					for (Subfile_List subfile_List : list) {
						if (!subfile_List.getName().endsWith("html") && 
								!subfile_List.getName().endsWith("txt") && 
								!subfile_List.getName().endsWith("jpg") && 
								!subfile_List.getName().endsWith("png"))
						{
							listT.add(subfile_List);
						}
					}
					String[] mStrings = new String[listT.size()];
					for (int i = 0; i < listT.size(); i++) {
						mStrings[i] = "【"+convertFileSize(listT.get(i).getFile_size())
								+"】 "+listT.get(i).getName();
					}
					ArrayAdapter<String> listViewAdapter;
					listView.setAdapter(listViewAdapter = new ArrayAdapter<String>(
							packageContext, R.layout.dialog_list_item_1,
							mStrings));
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// AbDialogUtil.removeDialog(FilmActivity.this);

							/*HistoryUtil.data = new History();
							HistoryUtil.data.setName(list.get(position).getName());
							DyUtil.urlToPlay = list.get(position).getUrl();
							Intent intent = new Intent(FilmActivity.this,
									PlayUrlActivity.class);
							startActivity(intent);*/
						}
					}); 
					System.out.println(listT.size());
					AbDialogUtil.showAlertDialog(mView);
					
					/*String playurl = data.getUrl();
					// AbToastUtil.showToast(SearchActivity.this, playurl);
					DyUtil.playUrl = playurl.replace("\\/", "/");
					;
					// Bundle bundle = new Bundle();
					// bundle.putString("playurl", playurl);
					Intent intent = new Intent(packageContext,
							SelectPlayerActivity.class);

					packageContext.startActivity(intent);*/
				} else {
					 AbToastUtil.showToast(packageContext, "没有文件！");
				}
			}

			@Override
			public Urldata getObject() {
				// TODO Auto-generated method stub
				Urldata urldata = DyUtil.getQQxfPlayUrl(url, packageContext);
				if (packageContext.isBack)
					return null;

				if (!"ok".equals(urldata.getMsg()))
					urldata = getPlayurl(url);
				return urldata;
			}
		});

		mAbTask.execute(item);
	}

	static WebView mWebView = null;
	public static Handler myHandler = null;

	protected static Urldata getQQxfPlayUrl(String url,
			PlayUrlActivity packageContext) {		
		final String ori = url;
		PATH = packageContext.getFilesDir() + "/";
		Urldata urldata = readUrl(url);
		if (!AbStrUtil.isEmpty(urldata.getUrl())) {
			urldata.setMsg("ok");
			return urldata;
		}
		urldata.setMsg("获取失败");
		String temp;
		try {

			String text;
			String method;
			Pattern pat;
			Matcher mat;
			// if (AbStrUtil.isEmpty(valueString)) {
			String apiurl = "";
			try {
				Document doc = Jsoup.connect(
						"http://www.dayunbo.com/api/api.php").get();
				apiurl = doc.getElementById("frompost").attr("action");
				System.out.println(apiurl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				urldata.setMsg("连接服务器失败，请检查网络或者稍后重试。");
				return urldata;
			}
			// text =
			// getHtml("http://api.dayunbo.com:8090/vod/api_2.php?method=high&u=",5000,"utf-8");
			if (AbStrUtil.isEmpty(apiurl)) {
				urldata.setMsg("获取api服务器失败，请检查网络或者稍后重试。");
				return urldata;
			}
			if (packageContext.isBack)
				return null;
			/*
			 * method = getMethod(text, packageContext); if
			 * (AbStrUtil.isEmpty(method)) { method = "&method3311080="; pat =
			 * Pattern .compile("method \\+= \"ri_ni_jia_([\\w_]+)\";"); mat =
			 * pat.matcher(text); if (mat.find()) { method += mat.group(1);
			 * 
			 * } else { urldata.setMsg("匹配不到获取方法"); return urldata; } }
			 */

			// } else method = valueString;
			// temp = URLEncoder.encode(url, "UTF-8").replace(" ", "+") +
			// method;
			// text = getHtml("http://api.dayunbo.com:8090/vod/playapi2.php?u="+
			// temp,3000,"utf-8");
			// temp=URLEncoder.encode( temp, "UTF-8");
			text = "";
			try {
				Document doc = Jsoup.connect(apiurl).timeout(30000)
						.data("u", url).post();
				text = doc.html();
				try {
					String nameStr=doc.select("div em").first().text();
					if (!nameStr.contains("不影响播放")
						&& AbStrUtil.isEmpty(HistoryUtil.data.getName())) {
						HistoryUtil.data.setName(nameStr);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				urldata.setMsg("获取hash值失败，请检查网络或者稍后重试。");
				return urldata;
			}

			if (AbStrUtil.isEmpty(text)) {
				// System.out.println(temp);
				urldata.setMsg("该视频暂时无法播放！");
				return urldata;
			}

			// http://api.dayunbo.com:8090/vod/api_2.php?method=high&u=
			// System.out.println(text);
			System.out.println(text);
			if (text.contains("<font color=\"red\"><b>影片转码中！请更换链接或稍后重试！</b>")) {
				urldata.setMsg("影片转码中！请更换链接或稍后重试！");
				return urldata;
			}
			if (text.contains("<font color=\"red\"><b>暂无种子信息！请更换链接或稍后重试！</b></font>")) {
				urldata.setMsg("暂无种子信息！请更换链接或稍后重试");
				return urldata;
			}
			pat = Pattern.compile("tPlayHash = '(\\w+)';");
			mat = pat.matcher(text);
			if (mat.find()) {
				String HEX = mat.group(1);
				// valueString = method;
				String back = "jsonp" + System.currentTimeMillis()
						+ (1000 + new Random().nextInt(8999));
				// http://api.dayunbo.com:8090/vod/get_playurl.php?type=html5&callback=jsonp&url=
				apiurl = apiurl.substring(0, apiurl.lastIndexOf("/"));
				System.out.println(apiurl);
				url = apiurl + "/get_playurl_api.php?type=html5&callback="
						+ back + "&url=" + HEX;
				// http://api.dayunbo.com:8092/vod/get_playurl_api.php?type=flash&url=&time=
				final String flashurl = apiurl
						+ "/get_playurl_api.php?type=flash&url=" + HEX
						+ "&time=" + System.currentTimeMillis();

				// System.out.println(url);
				text = getHtml(url, 3000, "utf-8");
				// System.out.println(text);
				// Document doc = Jsoup.connect(url).get();
				if (!AbStrUtil.isEmpty(text) && text.contains(back)) {

					text = text.trim();
					text = text.substring(back.length() + 1, text.length() - 1);
					System.out.println(text);
					if (text.contains("{\"msg\":\"url null\"}")) {
						urldata.setMsg("暂无播放地址，请换个链接试试");
						return urldata;
					}
					Gson gson = new Gson();
					final Urldata data = gson.fromJson(text, Urldata.class);
					// /playUrl=data;

					if ("ok".equals(data.getMsg())
							&& !AbStrUtil.isEmpty(data.getUrl())) {
						getflash(flashurl, data.getUrl());
						//new Thread(new Runnable() {
						//	@Override
						//	public void run() {
						//		// TODO Auto-generated method stub								
						//	} 
						//}).start();/**/
						Urldata xxx = new Urldata();

						xxx.setMsg(ori);
						xxx.setUrl(data.getUrl());
						urls.add(xxx);
						save();

						return data;

					}

				} else
					urldata.setMsg("视频暂时无法播放");
			} else
				urldata.setMsg("链接错误");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urldata;
	}
	
	public static void getflash(final String flashurl,
			final String orgurl) {
		try {
			String s = "";
			Document doc = Jsoup.connect(flashurl)
					.timeout(30000).get();
			Elements files = doc.select("file");
			for (Element element : files) {
				String t = element.text()
						.replace("<![CDATA[", "")
						.replace("]]>", "");
				DyUtil.fileList.add(t);
				s = s + t + '-';
			}
			if (DyUtil.fileList.size() > 0) {
				System.out.println(s);
				Urldata x = new Urldata();
				x.setMsg(orgurl);
				x.setUrl(s);
				urls.add(x);
				save();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(flashurl);
			e.printStackTrace();
		}
	}

	// private static String htmlData;
	private static String valueString;
	public static List<String> fileList = new ArrayList<String>();

	// private static valueString;

	private static String getMethod(final String htmlData,
			final Context packageContext) {
		// TODO Auto-generated method stub
		if (mWebView == null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					myHandler.post(new Runnable() {
						@Override
						public void run() {
							mWebView = new WebView(packageContext);
							mWebView.getSettings().setJavaScriptEnabled(true);
							mWebView.getSettings().setDefaultTextEncodingName(
									"UTF -8");
							mWebView.setWebViewClient(new WebViewClient() {
								public boolean shouldOverrideUrlLoading(
										WebView view, String url) {
									// view.loadUrl(url);
									if (url.startsWith("http://api.dayunbo.com:8090/vod/playapi2.php")) {
										System.out.println(url);
										if (url.contains("&")) {
											valueString = url.substring(url
													.indexOf("&"));
										}
									}
									return true;
								}
							});
						}
					});
				}
			}).start();
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				myHandler.post(new Runnable() {
					public void run() {
						mWebView.loadData(htmlData, "text/html; charset=UTF-8",
								null);
					}
				});
			}
		}).start();
		int c = 0;
		while (AbStrUtil.isEmpty(valueString) && c < 2) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c++;
		}
		return valueString;
	}

	public static String changeED2K(String url) {
		// TODO Auto-generated method stub
		Pattern pat = Pattern
				.compile("ed2k:\\/\\/\\|file\\|([^\\|]+?)\\|(\\d+)\\|([A-Z0-9]{32})\\|(h=[A-Z0-9]{32}\\|)?\\/?");
		Matcher mat = pat.matcher(url);
		// System.out.println(url);
		if (mat.find()) {
			String name = "" + mat.group(1);

			if (name.contains(".")) {

				String temp = "1" + name.substring(name.lastIndexOf("."));
				url = url.replace(name, temp);
				// System.out.println(url);
			}
		}
		if (url.startsWith("magnet:?xt=urn:btih:") && url.contains("&")) {
			url = url.substring(0, url.indexOf("&"));
		}
		url=url.replace("magnet:?xt=urn:btih:","");		  
				url = url.toLowerCase();
		return url;
	}

	private static Urldata getPlayurl(String url) {
		String apiurl = "";
		String text = "";
		Urldata data = new Urldata();
		data.setMsg("该视频暂时无法播放！获取huoyanAPI失败");
		try {
			Document doc = Jsoup.connect("http://www.huoyan.tv/api.php").get();
			apiurl = "http://www.huoyan.tv/"
					+ doc.getElementById("frompost").attr("action");
			// System.out.println(apiurl);
			// System.out.println(apiurl.substring(0, apiurl.lastIndexOf("/"))
			// );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return data;
		}
		try {
			Map<String, String> datas = new HashMap<String, String>();
			datas.put("u", url);
			datas.put("class", "api");
			Document doc = Jsoup.connect(apiurl).timeout(30000)
					.referrer("http://www.huoyan.tv/api.php").data(datas)
					.userAgent(userAgent).followRedirects(true).post();
			text = doc.html();
			// System.out.println(text);
			try {
				String name = doc.select(".left").first().text();
				if (!name.contains("不影响播放")
						&& AbStrUtil.isEmpty(HistoryUtil.data.getName())) {
					HistoryUtil.data.setName(name);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			Pattern pat = Pattern.compile("f:'(\\S+)',");
			Matcher mat = pat.matcher(text);
			if (mat.find()) {
				String HEX = "http://www.huoyan.tv/" + mat.group(1);
				System.out.println(HEX);
				String Location = null;
				try {
					Location = Jsoup
							.connect(HEX)
							.timeout(30000)
							.referrer(apiurl)
							.userAgent(userAgent)
							.header("Accept",
									"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
							.followRedirects(false).execute()
							.header("Location");
					System.out.println(Location);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (AbStrUtil.isEmpty(Location)) {
					Location = Jsoup
							.connect(HEX)
							.timeout(30000)
							.referrer(apiurl)
							.userAgent(userAgent)
							.header("Accept",
									"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
							.followRedirects(false).execute()
							.header("Location");
					System.out.println(Location);
				}
				if (!AbStrUtil.isEmpty(Location)) {

					data.setMsg("ok");
					data.setUrl(Location);
					Urldata x = new Urldata();
					x.setMsg(url);
					x.setUrl(Location);
					urls.add(x);
					save();
				}
			} else {
				String err = doc.select(".errinfo").first().text()
						.replace("可按[F5]刷新几次试试；如果依旧不能播放，请过3、5个小时后再试", "");
				System.out.println(err);
				System.out.println(url);
				data.setMsg(err);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			data.setMsg("该视频暂时无法播放！获取播放数据失败");
		}
		return data;
	}

	public final static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";
	public final static String userAgent1 = "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; 125LA; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)";
	public final static String userAgent2 = "Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.0; MyIE2; .NET CLR 1.1.4322)";
	
	public final static String userAgent3="Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; .NET4.0E)";
	public final static String userAgent4="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	public final static String userAgent5="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2716.0 Safari/537.36 OPR/39.0.2234.0 (Edition developer)";
	public final static String userAgent6="Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 10.0; WOW64; Trident/7.0)";
	public static List<Node> getp2psearch(String wd) {
		// TODO Auto-generated method stub
		List<Node> all=new ArrayList<Node>();
		try {
			
			String getUrl = "http://api.p2psearchers.com/?ap=dht&wd=" + URLEncoder.encode(wd, "UTF-8") ;
			String html = "";
			URL url = new URL(getUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection(); 
				connection.setRequestProperty("Connection", "Keep-Alive");
				// connection.setRequestProperty("Cache-Control", "no-cache");
				 
				connection.connect();
				InputStream inStrm = connection.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						inStrm, "UTF-8"));
				String temp = "";

				while ((temp = br.readLine()) != null) {
					html = html + (temp + '\n');
				}
			Document doc = Jsoup.parse(html);
			 
			for (Element ele : doc.select("item")) {
				 Node node=new Node();
				 node.setTitle(ele.select("title").first().text());
				 node.setUrl(ele.select("url").first().text());
				 all.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return all;
	}
	public static int jiexiIndex=0;
	public static void toGetPlayUrl(  String urlT,
			final AbActivity packageContext) {
		// TODO Auto-generated method stub
		DyUtil.fileList=new ArrayList<String>();
		PATH = packageContext.getFilesDir() + "/";
		if (urlT.startsWith("ftp://") || urlT.startsWith("http://")) {
			urlT = enThunder(urlT);
		}
		if (!TextUtils.isEmpty(urlT)) {

			 Bundle bundle = new Bundle();
			 bundle.putString("magnet", urlT);
			Intent intent = new Intent(packageContext,
					CiLiActivity.class);
			intent.putExtras(bundle); 
			packageContext.startActivity(intent);
			return;
		}
		final String url=urlT;SharedPreferences  SAVE = packageContext.getSharedPreferences("sqm",
				packageContext.MODE_PRIVATE);
		final  boolean  vip = SAVE.getBoolean("vip", false); 
		AbDialogUtil.showProgressDialog(packageContext,
				R.drawable.progress_circular, "正在获取播放列表...");
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//if (packageContext.isBack)return;
				AbDialogUtil.removeDialog(packageContext);
				Urldata data = (Urldata) obj;
				if (!"ok".equals(data.getMsg())) {
					// System.out.println(playUrl);
					String msg = data.getMsg(); 
					//AbToastUtil.showToast(packageContext, msg);
					((TextView) packageContext.findViewById(R.id.textView3))
							.setText(msg);
				} 
				if (dydatehashs!=null&&dydatehashs.getResp().getSubfile_list().size()>0) {
					
					final List<Subfile_List> list = dydatehashs.getResp().getSubfile_list();
					View mView = packageContext.mInflater.inflate(R.layout.dia_list_text_pro_gri, null);
					ListView listView = (ListView) mView
							.findViewById(R.id.listView1);
					final TextView textView1 = (TextView) mView
							.findViewById(R.id.textView1);
					final TextView textView2 = (TextView) mView
							.findViewById(R.id.textView2);
					final ProgressBar pb = (ProgressBar) mView
							.findViewById(R.id.progressBar1);
					GridView gridView = (GridView) mView
							.findViewById(R.id.mGridView);
					textView1.setText("如果文件解析失败,请再试一次.或者换其他其他接口.");
					textView2.setText("文件解析接口");
					pb.setVisibility(View.GONE);
					final String info_hash=dydatehashs.getResp().getInfo_hash();
					final	List<Subfile_List> listT =new ArrayList<Subfile_List>();
					for (Subfile_List subfile_List : list) {
						if (!subfile_List.getName().toLowerCase().endsWith("html") && 
								!subfile_List.getName().toLowerCase().endsWith("txt") && 
								!subfile_List.getName().toLowerCase().endsWith("jpg") && 
								!subfile_List.getName().toLowerCase().endsWith("png"))
						{
							listT.add(subfile_List);
						}
					}
					String[] mStrings = new String[listT.size()];
					for (int i = 0; i < listT.size(); i++) {
						mStrings[i] = "【"+convertFileSize(listT.get(i).getFile_size())
								+"】 "+listT.get(i).getName();
					}
//					ArrayAdapter<String> listViewAdapter;
//					listView.setAdapter(listViewAdapter = new ArrayAdapter<String>(
//							packageContext, R.layout.dialog_list_item_1,
//							mStrings));
					listView.setAdapter(new CiliAdapter(packageContext,listT));
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// AbDialogUtil.removeDialog(FilmActivity.this);

							/*HistoryUtil.data = new History();
							HistoryUtil.data.setName(list.get(position).getName());
							DyUtil.urlToPlay = list.get(position).getUrl();
							Intent intent = new Intent(FilmActivity.this,
									PlayUrlActivity.class);
							startActivity(intent);*/
							//toGetPlayUrl2(listT.get(position), info_hash, packageContext);
//							if (!vip)  
//							if (jiexiIndex!=0&&jiexiIndex!=2&&jiexiIndex!=13) {
//								 
//								AbToastUtil.showToast(packageContext, "接口失效，换其他的。");
//								return ;
//							}
							//
							String hashandindex=info_hash+"&index="+listT.get(position).getIndex();
							String filehash=readFilehash(packageContext, hashandindex);
							if (!AbStrUtil.isEmpty(filehash)) {
								toGetPlayUrl3(filehash, "【"+convertFileSize(listT.get(position).getFile_size())
										+"】 "+listT.get(position).getName(),packageContext);
								return;
							}
							
							if (jiexiIndex==0) {
								jx1urlhand="http://120.24.170.195:88/fx.php?";
								toGetPlayUrl2_1_1_1_1(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==1) {
								toGetPlayUrl2_18(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==2) {
								toGetPlayUrl2_3(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==3) {
								jx5urlhand= "http://mt520.xyz:8080/CPServer/cloudplayer/geturlopen?";
								toGetPlayUrl2_5(listT.get(position), info_hash.toUpperCase(), packageContext,textView1,pb);
							}
							if (jiexiIndex==4) {
								jx444urlhand="http://120.24.170.195:88/842606559.php?";
								toGetPlayUrl2_444(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==5) {
								toGetPlayUrl2_55(listT.get(position), info_hash, packageContext,textView1,pb);
//								jx6urlhand="http://842606559.vip.cantonshop.cn/xf.php?";
//								toGetPlayUrl2_6(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==6) {
								jx444urlhand="http://www.chenfuwl.com/chenfu2.php?";
								toGetPlayUrl2_444(listT.get(position), info_hash, packageContext,textView1,pb);
//								jx6urlhand="http://bd-dy.com/api/checkExist?info_";
//								toGetPlayUrl2_6(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==7) {
								jx1urlhand="http://jx.taoka123.com:88/fx.php?";
								toGetPlayUrl2_1_1_1(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==8) { 
								// toGetPlayUrl2_22(listT.get(position), info_hash, packageContext,textView1,pb);
								jiexihand9="http://www.dybeta.com/Api";
								toGetPlayUrl2_9(position, info_hash.toUpperCase(),"【"+convertFileSize(listT.get(position).getFile_size())
										+"】 "+listT.get(position).getName(), packageContext,textView1,pb);
							}
							if (jiexiIndex==9) {
								jx6urlhand="http://www.yhaohuo.cn/fx/fx.php?";
								toGetPlayUrl2_6(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==10) {
								jx6urlhand="http://1.112233.sinaapp.com/jx.php?";
								toGetPlayUrl2_6(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==11) {
								jx6urlhand="http://1.jxbt.applinzi.com/jx.php?";
								toGetPlayUrl2_6(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==12) {
								jx6urlhand="http://chenfu.97dns.com/chenfu2.php?";
								toGetPlayUrl2_6(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==13) { 
								toGetPlayUrl2_14(position, info_hash.toUpperCase(),"【"+convertFileSize(listT.get(position).getFile_size())
										+"】 "+listT.get(position).getName(), packageContext,textView1,pb);
							}

							if (jiexiIndex==14) {
								toGetPlayUrl2_15(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==15) {

								jx5urlhand= "http://1.hihiso.applinzi.com/jxx.php?";
								toGetPlayUrl2_5(listT.get(position), info_hash, packageContext,textView1,pb);
							}
							if (jiexiIndex==16) { 
								jiexihand9="http://api.aivam.com/Api";
								toGetPlayUrl2_9(position, info_hash.toUpperCase(),"【"+convertFileSize(listT.get(position).getFile_size())
										+"】 "+listT.get(position).getName(), packageContext,textView1,pb);
							}
							if (jiexiIndex==17) { 

								toGetPlayUrl2_4(listT.get(position), info_hash, packageContext,textView1,pb);
								
							}
						} 
					}); 
					//System.out.println(listT.size());
					final int sum=7;
					final BaseAdapter myGridViewAdapter=new BaseAdapter() {
						
						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							// TODO Auto-generated method stub
							if (convertView == null){
								convertView=new TextView(packageContext);
							}
							TextView tv=(TextView)convertView;
							if (jiexiIndex==position) {
								tv.setBackgroundColor(Color.GREEN);
							}else {
								tv.setBackgroundColor(Color.WHITE);
							}
							//if (!vip)
//								if (position!=0&&position!=1&&position!=2&&position!=5) {
//									tv.setBackgroundColor(Color.RED);
//								}
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
							jiexiIndex=position;
							myGridViewAdapter.notifyDataSetChanged();
						}
					});
					AbDialogUtil.showAlertDialog(mView);
					
					/*String playurl = data.getUrl();
					// AbToastUtil.showToast(SearchActivity.this, playurl);
					DyUtil.playUrl = playurl.replace("\\/", "/");
					;
					// Bundle bundle = new Bundle();
					// bundle.putString("playurl", playurl);
					Intent intent = new Intent(packageContext,
							SelectPlayerActivity.class);

					packageContext.startActivity(intent);*/
				} else {
					 AbToastUtil.showToast(packageContext, "没有文件！");
				}
			}

			@Override
			public Urldata getObject() {
				// TODO Auto-generated method stub
				Urldata urldata =DyUtil.getDyPlayUrl(url);
				//if (packageContext.isBack)return null;
				if (AbStrUtil.isEmpty(urldata.getMsg())) {
					urldata.setMsg("ok");
					return urldata;
				}
				/*
				  urldata = DyUtil.getQQxfPlayUrl(url, packageContext);
				if (packageContext.isBack)
					return null;

				if (!"ok".equals(urldata.getMsg()))
					urldata = getPlayurl(url);
				*/
				return urldata;
			}
		});

		mAbTask.execute(item);
	}
	public static  List<Urldata> filehashList=new ArrayList<Urldata>();
	public static String readFilehash(AbActivity ctx,String hashandindex) {
		String filehash="";
		if (filehashList==null) {
			filehashList=new ArrayList<Urldata>();
		}
		if (filehashList.size()==0) {

			SharedPreferences SAVE = ctx.getSharedPreferences("filehash",
					ctx.MODE_PRIVATE);
			String contxet = SAVE.getString("context", "");
			 Gson gson=new Gson();
			 filehashList=gson.fromJson(contxet, new TypeToken<List<Urldata>>() {  
	                }.getType());
		}
		if (filehashList==null) {
			filehashList=new ArrayList<Urldata>();
		}
		for (Urldata urldata : filehashList) {
			if ((""+urldata.getUrl()).equals(hashandindex)) {
				return urldata.getMsg();
			}
		}
		return filehash;
	}
	public static void saveFilehash(AbActivity ctx,String hashandindex,String filehash) {

		if ((""+filehash).length()<20) {
			return;
		}
		if (filehashList==null) {
			filehashList=new ArrayList<Urldata>();
		} Urldata urldata =new Urldata();
		urldata.setMsg(filehash);
		urldata.setUrl(hashandindex);
		filehashList.add(urldata); 
			SharedPreferences SAVE =  ctx.getSharedPreferences("filehash",
					ctx. MODE_PRIVATE);
			Editor editor = SAVE.edit();
			editor.putString("context", new Gson().toJson(filehashList)); 
			editor.commit();	
	}

	public static void deleteFilehash(AbActivity ctx,String filehash) {
		 
		if (filehashList==null) {
			filehashList=new ArrayList<Urldata>();
		}
		if (filehashList.size()==0) {

			SharedPreferences SAVE = ctx.getSharedPreferences("filehash",
					ctx.MODE_PRIVATE);
			String contxet = SAVE.getString("context", "");
			 Gson gson=new Gson();
			 filehashList=gson.fromJson(contxet, new TypeToken<List<Urldata>>() {  
	                }.getType());
		}
		if (filehashList==null) {
			filehashList=new ArrayList<Urldata>();
		}
		if (filehashList.size()==0) {
			return;
		}
		
		if ((""+filehash).length()<20) {
			return;
		}
		List<Urldata>templist=new ArrayList<Urldata>();
		templist.addAll(filehashList);
		filehashList=new ArrayList<Urldata>();
		for (Urldata urldata : templist) {

			if ((""+urldata.getMsg()).toLowerCase().equals(filehash.toLowerCase())) {
				
			}else {
				filehashList.add(urldata);
			}
		}
			SharedPreferences SAVE =  ctx.getSharedPreferences("filehash",
					ctx. MODE_PRIVATE);
			Editor editor = SAVE.edit();
			editor.putString("context", new Gson().toJson(filehashList)); 
			editor.commit();	
	}
	public static void toGetPlayUrl2(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext) {
		// TODO Auto-generated method stub 
				AbDialogUtil.showProgressDialog(packageContext,
						R.drawable.progress_circular, "正在提取种子信息...");
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						AbDialogUtil.removeDialog(packageContext); 
						QQFenXiang data = (QQFenXiang) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||data.getRet()==2 ) {
							//msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
						//tv.setText(msg);
							//AbToastUtil.showToast(packageContext, msg);
							//toGetPlayUrl2_1(sf,info_hash,packageContext);
						} 	
						else {
							String File_hash=data.getData().get(0).getFile_hash();
							if (!File_hash.equals("0000000000000000000000000000000000000000")) {
								msg = File_hash; 
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								toGetPlayUrl3(File_hash, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),packageContext);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								tv.setText(msg);
								//toGetPlayUrl2_1(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public QQFenXiang getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://fenxiang.qq.com/upload/index.php/upload_c/checkExist" );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("POST"); 
							connection.setRequestProperty("Referer", "http://fenxiang.qq.com/upload/index.php/upload_c/checkExist");
							connection.setRequestProperty("Accept", "*/*");
							connection.setRequestProperty("Accept-Language", "zh-cn");
							connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
							connection.setRequestProperty("User-Agent", userAgent1);
							

							String postDataStr =postData; 
							byte[] bytes = postDataStr.getBytes("utf-8");
							connection.setRequestProperty("Content-Length", "" + bytes.length);
							connection.setRequestProperty("Connection", "Keep-Alive");
							connection.setRequestProperty("Cache-Control", "no-cache");
							connection.setDoOutput(true); 
							connection.setReadTimeout(5000);
							connection.setFollowRedirects(true);
							connection.connect();
							OutputStream outStrm = connection.getOutputStream();
							outStrm.write(bytes);
							outStrm.flush();
							outStrm.close();
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect();
							Gson gson = new Gson();
							System.out.println(html);
							try {
								
								QQFenXiang data = gson.fromJson(html, QQFenXiang.class);
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_1(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//				AbDialogUtil.showProgressDialog(packageContext,
//						R.drawable.progress_circular, "正在提取种子信息...接口1");
		textView.setText("正在解析...接口1...提取种子信息");
		pb.setVisibility(View.VISIBLE);
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						//AbDialogUtil.removeDialog(packageContext); 
						pb.setVisibility(View.GONE);
						QQFenXiang data = (QQFenXiang) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||data.getRet()==2 ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
						tv.setText(msg);
						textView.setText(msg);
						//toGetPlayUrl2_2(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String File_hash=data.getData().get(0).getFile_hash();
							if (!File_hash.equals("0000000000000000000000000000000000000000")) {
								msg = File_hash; 
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								saveFilehash(packageContext,info_hash+"&index="+sf.getIndex() , File_hash);
								toGetPlayUrl3(File_hash, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),packageContext);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								textView.setText(msg);
								tv.setText(msg);
								//toGetPlayUrl2_2(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public QQFenXiang getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://120.24.170.195:88/fx.php?hash="+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET"); 
							connection.setRequestProperty("Referer", "http://fenxiang.qq.com/upload/index.php/upload_c/checkExist");
							connection.setRequestProperty("Accept", "*/*");
							connection.setRequestProperty("Accept-Language", "zh-cn");
							//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
							connection.setRequestProperty("User-Agent", userAgent1); 
							connection.setRequestProperty("Connection", "Keep-Alive");
							connection.setRequestProperty("Cache-Control", "no-cache"); 
							connection.setReadTimeout(15000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect();
							Gson gson = new Gson();
							System.out.println(html);
							try {
								
								QQFenXiang data = gson.fromJson(html, QQFenXiang.class);
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_1_1(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//				AbDialogUtil.showProgressDialog(packageContext,
//						R.drawable.progress_circular, "正在提取种子信息...接口1");
		textView.setText("正在解析...接口"+(jiexiIndex+1)+"...提取种子信息");
		pb.setVisibility(View.VISIBLE);
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						//AbDialogUtil.removeDialog(packageContext); 
						pb.setVisibility(View.GONE);
						QQFenXiang data = (QQFenXiang) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||data.getRet()==2 ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
						tv.setText(msg);
						textView.setText(msg);
						//toGetPlayUrl2_2(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String File_hash=data.getData().get(0).getFile_hash();
							if (!File_hash.equals("0000000000000000000000000000000000000000")) {
								msg = File_hash; 
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								saveFilehash(packageContext,info_hash+"&index="+sf.getIndex() , File_hash);
								toGetPlayUrl3(File_hash, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),packageContext);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								textView.setText(msg);
								tv.setText(msg);
								//toGetPlayUrl2_2(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public QQFenXiang getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL(jx1urlhand+"hash="+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET"); 
							connection.setRequestProperty("Referer", "http://fenxiang.qq.com/upload/index.php/upload_c/checkExist");
							connection.setRequestProperty("Accept", "*/*");
							connection.setRequestProperty("Accept-Language", "zh-cn");
							//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
							connection.setRequestProperty("User-Agent", userAgent1); 
							connection.setRequestProperty("Connection", "Keep-Alive");
							connection.setRequestProperty("Cache-Control", "no-cache"); 
							connection.setReadTimeout(15000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect();
							Gson gson = new Gson();
							System.out.println(html);
							
							try {
							int end=html. indexOf("123456")+5;
							if (end<=html.length()) {
								html=html.substring(0, end)+"}";
							}
								QQFenXiang data = gson.fromJson(html, QQFenXiang.class);
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
public static String jx1urlhand="http://120.24.170.195:88/fx.php?";
public static String jx6urlhand="http://842606559.vip.cantonshop.cn/xf.php?";
	public static void toGetPlayUrl2_6(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//				AbDialogUtil.showProgressDialog(packageContext,
//						R.drawable.progress_circular, "正在提取种子信息...接口1");
		textView.setText("正在解析...接口"+(jiexiIndex+1)+"...提取种子信息");
		pb.setVisibility(View.VISIBLE);
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						//AbDialogUtil.removeDialog(packageContext); 
						pb.setVisibility(View.GONE);
						QQFenXiang data = (QQFenXiang) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||data.getRet()==2 
								||data.getData()==null||data.getData().size()==0
								||TextUtils.isEmpty(data.getData().get(0).getFile_hash())
										||data.getData().get(0).getFile_hash().length()!=40) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
						tv.setText(msg);
						textView.setText(msg);
						//toGetPlayUrl2_2(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String File_hash=data.getData().get(0).getFile_hash();
							if (!File_hash.equals("0000000000000000000000000000000000000000")) {
								msg = File_hash; 
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								saveFilehash(packageContext,info_hash+"&index="+sf.getIndex() , File_hash);
								toGetPlayUrl3(File_hash, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),packageContext);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								textView.setText(msg);
								tv.setText(msg);
								//toGetPlayUrl2_2(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public QQFenXiang getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							//http://842606559.vip.cantonshop.cn/xf.php?hash=7DF44BD02F0976458E87872A39316B2C19F31B02&index=6
							String urlString=jx6urlhand+"hash="+info_hash+"&index="+sf.getIndex();
							url = new URL(urlString );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET"); 
							connection.setRequestProperty("Referer", urlString);
							connection.setRequestProperty("Accept", "*/*");
							connection.setRequestProperty("Accept-Language", "zh-cn");
							 connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
							connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1)"); 
							connection.setRequestProperty("Connection", "Keep-Alive");
							connection.setRequestProperty("Cache-Control", "no-cache"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect();
							Gson gson = new Gson();
							System.out.println(html);
							try {
								
								QQFenXiang data = gson.fromJson(html, QQFenXiang.class);
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_4(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口"+(jiexiIndex+1)+ "...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
						Iilpaylist data = (Iilpaylist) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||data.getStatus()==0 ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							tv.setText(msg);
							textView.setText(msg);
						//toGetPlayUrl2_5(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.getData().get(0).getUrl();
							String cook=data.getData().get(0).getCookie().replace("FTN5K=", "");
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口"+(jiexiIndex+1)+ "解析成功。");
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								tv.setText(msg);
								textView.setText(msg);
								//toGetPlayUrl2_5(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iilpaylist getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://www.iiplayer.cn:8080/iiplayer/iiplayerServer?hash="+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET"); 
							connection.setRequestProperty("Referer", "http://fenxiang.qq.com/upload/index.php/upload_c/checkExist");
							connection.setRequestProperty("Accept", "*/*");
							connection.setRequestProperty("Accept-Language", "zh-cn");
							//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
							connection.setRequestProperty("User-Agent", userAgent1); 
							connection.setRequestProperty("Connection", "Keep-Alive");
							connection.setRequestProperty("Cache-Control", "no-cache"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect();
							Gson gson = new Gson();
							System.out.println(html);
							try {
								
								Iilpaylist data = gson.fromJson(html, Iilpaylist.class);
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static String  jx5urlhand= "http://1.hihiso.applinzi.com/jxx.php?";
	public static void toGetPlayUrl2_5(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口"+(jiexiIndex+1)+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
						Iiplaydata data = (Iiplaydata) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||!"Success".equals(data.getName()) ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							if(tv!=null)tv.setText(msg);
							textView.setText(msg);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.  getUrl()+"?compressed=0&dtype=1&fname=m.mkv";
							String cook=data .getCookie().replace("FTN5K=", "");
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口"+(jiexiIndex+1)+"解析成功。");
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								if(tv!=null)tv.setText(msg); 
								textView.setText(msg);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iiplaydata getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL(jx5urlhand+"hash="+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
							try {
								if (!html.contains("#")) {
									html=AbBase64.decode(html, "utf-8");
								}
								Iiplaydata data = new Iiplaydata();
								String[]strs=html.trim().replace("#", "|").split("\\|");
								if (strs.length==3) {
									data.setCookie(strs[2]);
									data.setName(strs[0]);
									data.setUrl(strs[1]);
								}
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_5_1(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口"+(jiexiIndex+1)+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
						Iiplaydata data = (Iiplaydata) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||!"Success".equals(data.getName()) ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							if(tv!=null)tv.setText(msg);
							textView.setText(msg);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.  getUrl()+"?compressed=0&dtype=1&fname=m.mkv";
							String cook=data .getCookie().replace("FTN5K=", "");
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口"+(jiexiIndex+1)+"解析成功。");
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								if(tv!=null)tv.setText(msg); 
								textView.setText(msg);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iiplaydata getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
String urlstr=jx5urlhand+"hash="+info_hash+"&index="+sf.getIndex() ;
							url = new URL(urlstr);
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET"); 
						//	connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("Referer", urlstr);   
							//connection.setRequestProperty("Accept-Encoding", "gzip"); 
							connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							try {
								if (!html.contains("#")) {
									html=new StringBuffer(html).reverse().toString()+"=";
									html=AbBase64.decode(html, "utf-8");
								}
							System.out.println(html);
								Iiplaydata data = new Iiplaydata();
								String[]strs=html.trim().replace("#", "|").split("\\|");
								if (strs.length==3) {
									data.setCookie(strs[2]);
									data.setName(strs[0]);
									data.setUrl(strs[1]);
								}
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_1_1_1(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口"+(jiexiIndex+1)+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
						Iiplaydata data = (Iiplaydata) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||!"Success".equals(data.getName()) ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							tv.setText(msg);
							textView.setText(msg);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.  getUrl();//+"?compressed=0&dtype=1&fname=m.mkv";
							String cook=data .getCookie().replace("FTN5K=", "").trim();
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口"+(jiexiIndex+1)+"解析成功。|"+url+"|"+msg);
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								tv.setText(msg); 
								textView.setText(msg);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iiplaydata getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL(jx1urlhand+"hash="+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
							try {
								if (!html.contains("#")) {
									html=AbBase64.decode(html, "utf-8");
								}
								Iiplaydata data = new Iiplaydata();
								String[]strs=html.trim().replace("#", "|").split("\\|");
								if (strs.length==3) {
									data.setCookie(strs[1].replace("cookie:", ""));
									data.setName("Success");
									data.setUrl(strs[0].replace("code:", "").replace("taoka123.com", "qq.com"));
								}
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_3(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口3...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
						Iiplaydata data = (Iiplaydata) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||!"Success".equals(data.getName()) ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							if(tv!=null)tv.setText(msg);
							textView.setText(msg);
						//toGetPlayUrl2_4(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.  getUrl()+"?compressed=0&dtype=1&fname=m.mkv";
							String cook=data .getCookie().replace("FTN5K=", "");
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口3解析成功。");
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								if(tv!=null)tv.setText(msg);
								textView.setText(msg);
								//toGetPlayUrl2_4(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iiplaydata getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://xiayifa.com/player.php?url=magnet:?xt=urn:btih:"+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
							String cookieskey = "Set-Cookie";
							Map<String, List<String>> maps = connection.getHeaderFields();
							List<String> coolist = maps.get(cookieskey);
							Document jsoup=Jsoup.parse(html);
							try {
								
								Iiplaydata data = new Iiplaydata();
							for (String string : coolist) {
								String cc = string.split(";")[0];
								if (cc.startsWith("FTN5K=")) {
									data.setCookie(cc);
								}} 
									
									//data.setName(strs[0]);
									data.setUrl(jsoup.select("source").first().attr("src")
											.replace("/play.mp4", "").replace("xiayifa", "qq")); 
									data.setName("Success");
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_555(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口"+(jiexiIndex+1)+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
						Iiplaydata data = (Iiplaydata) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||!"Success".equals(data.getName()) ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							if(tv!=null)tv.setText(msg);
							textView.setText(msg);
						//toGetPlayUrl2_4(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.  getUrl()+"?compressed=0&dtype=1&fname=m.mkv";
							String cook=data .getCookie().replace("FTN5K=", "");
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口"+(jiexiIndex+1)+"解析成功。");//+url+"ccc"+cook
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								if(tv!=null)tv.setText(msg);
								textView.setText(msg);
								//toGetPlayUrl2_4(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iiplaydata getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://aa7761610510.d203.cnaaa7.com/ax.php?"+"hash="+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
						 
							//Document jsoup=Jsoup.parse(html);
							try {
								
								Iiplaydata data = new Iiplaydata();
							 
									
									//data.setName(strs[0]);
								int start=html.indexOf("http://");
								int end=html.indexOf("</",start);
								String srcString=html.substring(start, end);
								data.setUrl(srcString); 
								start=html.indexOf("e:");
								 end=html.indexOf("|",start);
								 srcString=html.substring(start+2, end);
								data.setCookie(srcString); 
									data.setName("Success");
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_55(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口"+(jiexiIndex+1)+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
						Iiplaydata data = (Iiplaydata) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||!"Success".equals(data.getName()) ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							if(tv!=null)tv.setText(msg);
							textView.setText(msg);
						//toGetPlayUrl2_4(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.  getUrl();//+"?compressed=0&dtype=1&fname=m.mkv";
							String cook=data .getCookie().replace("FTN5K=", "");
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口"+(jiexiIndex+1)+"解析成功。");//+url+"ccc"+cook
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								if(tv!=null)tv.setText(msg);
								textView.setText(msg);
								//toGetPlayUrl2_4(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iiplaydata getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL(jx5urlhand+"hash="+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
						 
							Document jsoup=Jsoup.parse(html);
							try {
								
								Iiplaydata data = new Iiplaydata();
							 
									
									//data.setName(strs[0]);
								int start=html.indexOf("http://");
								int end=html.indexOf("&fname=m.mkv");
								String srcString=html.substring(start, end)+"&fname=m.mkv";
								data.setUrl(srcString); 
								data.setCookie(jsoup.select("cookie").first().text()); 
									data.setName("Success");
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_18(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口"
		+(jiexiIndex+1)+ "...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
				Yayabt data = (Yayabt) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
							if (data==null||data.getBtinfo()==null||data.getBtinfo().size()==0 ) {
								msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
								if(tv!=null)tv.setText(msg);
							textView.setText(msg);
							//toGetPlayUrl2_2(sf,info_hash,packageContext);
								//AbToastUtil.showToast(packageContext, msg);
							} 	
							else {
								String File_hash="0000000000000000000000000000000000000000";
								for (Yayaobj yaya : data.getBtinfo()) {
									if (yaya.getIndex()==sf.getIndex()) {
										File_hash=yaya.getSha1();
									}
								}
								if (!File_hash.equals("0000000000000000000000000000000000000000")) {
									msg = File_hash; 
									//tv.setText(msg);
								    //AbToastUtil.showToast(packageContext, msg);
									saveFilehash(packageContext,info_hash+"&index="+sf.getIndex() , File_hash);
									toGetPlayUrl3(File_hash, "【"+convertFileSize(sf.getFile_size())
											+"】 "+sf.getName(),packageContext,true);
								} else {
									msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
									textView.setText(msg);
									if(tv!=null)tv.setText(msg);
									//toGetPlayUrl2_2(sf,info_hash,packageContext);
									//	AbToastUtil.showToast(packageContext, msg);
								}
							}
					}

					@Override
					public Yayabt getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://yayabt.com/jk/xf.php?hash="+info_hash.toUpperCase()//+"&i="+sf.getIndex()
									);
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
							Gson gson = new Gson(); 
							try {
								
								Yayabt data = gson.fromJson(html, Yayabt.class);
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							} 
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 


	public static void toGetPlayUrl2_44(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
		int indexx=jiexiIndex+1;
		textView.setText("正在解析...接口"+indexx+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
				String data = (String) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
							if (data==null) {
								msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							tv.setText(msg);
							textView.setText(msg);
							//toGetPlayUrl2_2(sf,info_hash,packageContext);
								//AbToastUtil.showToast(packageContext, msg);
							} 	
							else {
								String File_hash=data; 
								if (!File_hash.equals("0000000000000000000000000000000000000000")) {
									msg = File_hash; 
									//tv.setText(msg);
								    //AbToastUtil.showToast(packageContext, msg);
									saveFilehash(packageContext,info_hash+"&index="+sf.getIndex() , File_hash);
									toGetPlayUrl3(File_hash, "【"+convertFileSize(sf.getFile_size())
											+"】 "+sf.getName(),packageContext);
								} else {
									msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
									textView.setText(msg);
									tv.setText(msg);
									//toGetPlayUrl2_2(sf,info_hash,packageContext);
									//	AbToastUtil.showToast(packageContext, msg);
								}
							}
					}

					@Override
					public String getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://mt520.xyz:8080/CPServer/cloudplayer/getlistopen?hash="+info_hash.toUpperCase()//+"&i="+sf.getIndex()
									);
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html); 
							String indexz=sf.getIndex()+"";
							
							try {
								 String[]strs=html.split("\\^");
								 for (int i = 0; i < strs.length; i++) {
									if (indexz.equals(strs[i])) {
 
										 
										 if (Pattern.matches("[0-9a-zA-Z]{40}", strs[i+1])) {
											return strs[i+1];
										}
									}
								}
								return null;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							} 
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 

	public static String jx444urlhand= "http://120.24.170.195:88/842606559.php?";

	public static void toGetPlayUrl2_444(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		toGetPlayUrl2_444(sf, info_hash, packageContext, textView, pb, true);
	}
	public static void toGetPlayUrl2_444(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb,final boolean save) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
		int indexx=jiexiIndex+1;
		textView.setText("正在解析...接口"+indexx+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
				String data = (String) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
							if (data==null) {
								msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							if(tv!=null)tv.setText(msg);
							textView.setText(msg);
							//toGetPlayUrl2_2(sf,info_hash,packageContext);
								//AbToastUtil.showToast(packageContext, msg);
							} 	
							else {
								String File_hash=data; 
								if (!info_hash.toLowerCase().equals(File_hash.toLowerCase())&&
										!File_hash.equals("0000000000000000000000000000000000000000")) {
									msg = File_hash; 
									//tv.setText(msg);
								    //AbToastUtil.showToast(packageContext, msg);
									if(save)
									saveFilehash(packageContext,info_hash+"&index="+sf.getIndex() , File_hash);
									toGetPlayUrl3(File_hash, "【"+convertFileSize(sf.getFile_size())
											+"】 "+sf.getName(),packageContext,true);
								} else {
									msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
									textView.setText(msg);
									if(tv!=null)tv.setText(msg);
									//toGetPlayUrl2_2(sf,info_hash,packageContext);
									//	AbToastUtil.showToast(packageContext, msg);
								}
							}
					}

					@Override
					public String getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL(jx444urlhand+"hash="+info_hash.toUpperCase()+"&index="+sf.getIndex()//
									);
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);  
							
							try {
								 String[]strs=html.split("\"");
								 for (int i = 0; i < strs.length; i++) { 
										 if (Pattern.matches("[0-9a-zA-Z]{40}", strs[i+1])) {
											return strs[i+1];
										} 
								}
								return null;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							} 
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 

	public static void toGetPlayUrl2_1_1_1_1(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
		int indexx=jiexiIndex+1;
textView.setText("正在解析...接口"+indexx+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
				Yayabt data = (Yayabt) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
							if (data==null||data.getBtinfo()==null||data.getBtinfo().size()==0 ) {
								msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
								if(tv!=null)tv.setText(msg);
							textView.setText(msg);
							//toGetPlayUrl2_2(sf,info_hash,packageContext);
								//AbToastUtil.showToast(packageContext, msg);
							} 	
							else {
								String File_hash="0000000000000000000000000000000000000000";
								for (Yayaobj yaya : data.getBtinfo()) {
									if (yaya.getIndex()==sf.getIndex()) {
										File_hash=yaya.getSha1();
									}
								}
								if (!File_hash.equals("0000000000000000000000000000000000000000")) {
									msg = File_hash; 
									//tv.setText(msg);
								    //AbToastUtil.showToast(packageContext, msg);
									saveFilehash(packageContext,info_hash+"&index="+sf.getIndex() , File_hash);
									toGetPlayUrl3(File_hash, "【"+convertFileSize(sf.getFile_size())
											+"】 "+sf.getName(),packageContext,true);
								} else {
									msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
									textView.setText(msg);
									if(tv!=null)tv.setText(msg);
									//toGetPlayUrl2_2(sf,info_hash,packageContext);
									//	AbToastUtil.showToast(packageContext, msg);
								}
							}
					}

					@Override
					public Yayabt getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://bt.ttzx.tv/info_hash/f7/2b/"+info_hash.toUpperCase()//+"&i="+sf.getIndex()
									);
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
							Gson gson = new Gson(); 
							try {
								
								Yayabt data = gson.fromJson(html, Yayabt.class);
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							} 
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_15(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口15...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
						Iiplaydata data = (Iiplaydata) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||!"Success".equals(data.getName()) ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							tv.setText(msg);
							textView.setText(msg);
						//toGetPlayUrl2_4(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.  getUrl()+"?compressed=0&dtype=1&fname=m.mkv";
							String cook=data .getCookie().replace("FTN5K=", "");
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口15解析成功。");
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								tv.setText(msg);
								textView.setText(msg);
								//toGetPlayUrl2_4(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iiplaydata getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://yun.xfplayer.net/play.php?q="+info_hash+"&i="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
							String cookieskey = "Set-Cookie"; 
							Document jsoup=Jsoup.parse(html);
							try {
								url = new URL(jsoup.select("script").first().attr("src"));
								  connection = (HttpURLConnection) url
										.openConnection();

								connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
								connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
								connection.setRequestProperty("Connection", "Keep-Alive"); 
								connection.setReadTimeout(30000);
								connection.setFollowRedirects(true);
								connection.connect(); 
								  inStrm = connection.getInputStream();

								  br = new BufferedReader(new InputStreamReader(
										inStrm,"utf-8"));

								  temp = "";
								while ((temp = br.readLine()) != null) {
									html += (temp + '\n');
								}
								br.close();
								connection.disconnect();  
								Map<String, List<String>> maps = connection.getHeaderFields();
								List<String> coolist = maps.get(cookieskey);
								
								Iiplaydata data = new Iiplaydata();
							for (String string : coolist) {
								String cc = string.split(";")[0];
								if (cc.startsWith("FTN5K=")) {
									data.setCookie(cc);
								}} 
									
									//data.setName(strs[0]);
									data.setUrl(jsoup.select("source").first().attr("src")
											.replace("/play.mp4", "").replace("xfplayer.net", "qq.com").replace('-', '.')); 
									data.setName("Success");
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_22(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口2...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
						Iiplaydata data = (Iiplaydata) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||!"Success".equals(data.getName()) ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							tv.setText(msg);
							textView.setText(msg);
						//toGetPlayUrl2_4(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.  getUrl()+"?compressed=0&dtype=1&fname=m.mkv";
							String cook=data .getCookie().replace("FTN5K=", "");
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口2解析成功。");
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								tv.setText(msg);
								textView.setText(msg);
								//toGetPlayUrl2_4(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iiplaydata getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://daxuekong.cn/player.php?url=magnet:?xt=urn:btih:"+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
							connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
							connection.setRequestProperty("Connection", "Keep-Alive"); 
							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
							String cookieskey = "Set-Cookie";
							Map<String, List<String>> maps = connection.getHeaderFields();
							List<String> coolist = maps.get(cookieskey); 
							try {
								
								Iiplaydata data = new Iiplaydata();
							for (String string : coolist) {
								String cc = string.split(";")[0];
								if (cc.startsWith("FTN5K=")) {
									data.setCookie(cc);
								}} 
							Pattern pat = Pattern.compile("f:'(.*?)'");

							Matcher mat = pat.matcher(html);
							if (mat.find()) { 
									String src=mat.group(1); 
									//data.setName(strs[0]);
									data.setUrl(
											src.replace("/play.mp4", "").replace("daxuekong.cn", "qq.com")); 
									data.setName("Success");
							}
							else data=null;
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_2(final Subfile_List sf,final String info_hash,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//				AbDialogUtil.showProgressDialog(packageContext,
//						R.drawable.progress_circular, "正在提取种子信息...接口2");
		textView.setText("正在解析...接口2...提取种子信息");
		pb.setVisibility(View.VISIBLE);
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						//AbDialogUtil.removeDialog(packageContext); 
						pb.setVisibility(View.GONE);
						String data = (String) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null  ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							tv.setText(msg); 
							textView.setText(msg); 
							//AbToastUtil.showToast(packageContext, msg);
						//toGetPlayUrl2_3(sf,info_hash,packageContext);
						} 	
						else {
							String File_hash=data ;
							if (!File_hash.equals("0000000000000000000000000000000000000000")) {
								msg = File_hash; 
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								saveFilehash(packageContext,info_hash+"&index="+sf.getIndex() , File_hash);
								toGetPlayUrl3(File_hash, "【"+convertFileSize(sf.getFile_size())
										+"】 "+sf.getName(),packageContext);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								tv.setText(msg); 
								textView.setText(msg); 
								//toGetPlayUrl2_3(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public String getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
							String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
										info_hash+
										"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
										sf.getIndex()+
										"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
						try {
							url = new URL("http://soft1.kyli.cn/yunbo.php?yuntype=MACenterView&hash="+info_hash+"&index="+sf.getIndex() );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("GET"); 
							connection.setRequestProperty("Accept", "*/*");
							connection.setRequestProperty("Accept-Language", "zh-cn");
							//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
							connection.setRequestProperty("User-Agent", userAgent1); 
							connection.setRequestProperty("Connection", "Keep-Alive");

							connection.setReadTimeout(30000);
							connection.setFollowRedirects(true);
							connection.connect(); 
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect(); 
							System.out.println(html);
							if (!html.contains("file_hash")) {
								return null;
							}
							try {

								Pattern pat = Pattern.compile("[a-zA-Z0-9]{40}");
								Matcher mat = pat.matcher(html);
								if (mat.find()) {
									String data = mat.group();
									return data;
								}
								return null;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	
public static String jiexihand9="http://www.dybeta.com/Api";
	public static void toGetPlayUrl2_9(final int dex,final String info_hash,final String filename,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口"+(jiexiIndex+1)+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
				Iilpayobj data = (Iilpayobj) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||data.getRet()!=0||!data.getMsg().equals("ok") ) {
							msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
							tv.setText(msg);
							textView.setText(msg);
						//toGetPlayUrl2_5(sf,info_hash,packageContext);
							//AbToastUtil.showToast(packageContext, msg);
						} 	
						else {
							String url=data.getData(). getUrl();
							String cook=data.getData(). getCookie().replace("FTN5K=", "");
							if (!cook.equals("00000000")) {
								msg = cook; 
								textView.setText("接口"+(jiexiIndex+1)+"解析成功。");
								//tv.setText(msg);
							    //AbToastUtil.showToast(packageContext, msg);
								playwitchcook(packageContext,url, filename,cook);
							} else {
								msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
								tv.setText(msg);
								textView.setText(msg);
								//toGetPlayUrl2_5(sf,info_hash,packageContext);
								//	AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Iilpayobj getObject() {
						// TODO Auto-generated method stub 
						
						String html = "";
						URL url;
						String postData = "url=magnet%3A%3Fxt%3Durn%3Abtih%3A"
								+ info_hash+"%2F"
										+ dex+"_yunbo&filename=play.mp4&mold=mp4&token=token"  ;
				try {
					url = new URL(jiexihand9 );
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();

					connection.setRequestMethod("POST"); 
					connection.setRequestProperty("Referer", "http://api.beyond6293.com/play.php?url=magnet:?xt=urn:btih:"+info_hash);
					connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
					connection.setRequestProperty("Accept-Language", "zh-cn");
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.8.1000 Chrome/30.0.1599.101 Safari/537.36");
					

					String postDataStr =postData; 
					byte[] bytes = postDataStr.getBytes("utf-8");
					connection.setRequestProperty("Content-Length", "" + bytes.length);
					connection.setRequestProperty("Connection", "Keep-Alive");
					connection.setRequestProperty("DNT", "1");
					connection.setRequestProperty("Origin", "http://api.beyond6293.com");
					connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
					connection.setDoOutput(true); 
					connection.setReadTimeout(10000);
					connection.setFollowRedirects(true);
					connection.connect();
					OutputStream outStrm = connection.getOutputStream();
					outStrm.write(bytes);
					outStrm.flush();
					outStrm.close();
					InputStream inStrm = connection.getInputStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(
							inStrm,"utf-8"));

					String temp = "";
					while ((temp = br.readLine()) != null) {
						html += (temp + '\n');
					}
					br.close();
					connection.disconnect(); 
							System.out.println(html);
							try {
								
								Iilpayobj data = new Gson().fromJson(html, Iilpayobj.class);
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
						//return null;
					}
				});

				mAbTask.execute(item);
	} 
	public static void toGetPlayUrl2_14(final int dex,final String info_hash,final String filename,
			final AbActivity packageContext,final TextView textView,final ProgressBar pb) {
		// TODO Auto-generated method stub 
//		AbDialogUtil.showProgressDialog(packageContext,
//				R.drawable.progress_circular, "正在提取种子信息...接口2");
textView.setText("正在解析...接口"+(jiexiIndex+1)+"...提取种子信息");
pb.setVisibility(View.VISIBLE);
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskObjectListener() {

			@Override
			public void update(Object obj) {
				// TODO Auto-generated method stub
				//AbDialogUtil.removeDialog(packageContext); 
				pb.setVisibility(View.GONE); 
				Iiplaydata data = (Iiplaydata) obj;
				TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
						
					String msg = ""; 
				if (data==null||!"Success".equals(data.getName()) ) {
					msg = "文件信息提取失败,请重试一次，可能网络问题，重试3次后请找其他资源。"; 
					tv.setText(msg);
					textView.setText(msg);
				//toGetPlayUrl2_4(sf,info_hash,packageContext);
					//AbToastUtil.showToast(packageContext, msg);
				} 	
				else {
					String url=data.  getUrl()+"?compressed=0&dtype=1&fname=m.mkv";
					String cook=data .getCookie().replace("FTN5K=", "");
					if (!cook.equals("00000000")) {
						msg = cook; 
						textView.setText("接口"+(jiexiIndex+1)+"解析成功。");
						//tv.setText(msg);
					    //AbToastUtil.showToast(packageContext, msg);
						playwitchcook(packageContext,url,  filename,cook);
					} else {
						msg = "资源太新或者太冷门，云服务器没有下载完成。"; 
						tv.setText(msg);
						textView.setText(msg);
						//toGetPlayUrl2_4(sf,info_hash,packageContext);
						//	AbToastUtil.showToast(packageContext, msg);
					}
				}
			}

			@Override
			public Iiplaydata getObject() {
				// TODO Auto-generated method stub 
				
				String html = "";
				URL url;
//					String postData = "torrent_para={\"uin\":\"123456\",\"hash\":\""+
//								info_hash+
//								"\",\"taskname\":\"M\",\"data\":[{\"index\":\""+
//								sf.getIndex()+
//								"\",\"filesize\":\"1\",\"filename\":\"M.mkv\"}]}" ;
				try {
					//http://api.btjson.com/magnet.php?v=78F44467DA46BC5841031B99E2682D75B6ABCD8C&i=0
					url = new URL("http://api.btjson.com/magnet.php?v="+info_hash.toUpperCase()+"&i="+dex );
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();

					connection.setRequestMethod("GET");  connection.setRequestProperty("Accept", "*/*"); 
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)"); 
					connection.setRequestProperty("Connection", "Keep-Alive"); 
					connection.setReadTimeout(30000);
					connection.setFollowRedirects(true);
					connection.connect(); 
					InputStream inStrm = connection.getInputStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(
							inStrm,"utf-8"));

					String temp = "";
					while ((temp = br.readLine()) != null) {
						html += (temp + '\n');
					}
					br.close();
					connection.disconnect(); 
					System.out.println(html);
					String cookieskey = "Set-Cookie";
					Map<String, List<String>> maps = connection.getHeaderFields();
					List<String> coolist = maps.get(cookieskey);
					Document jsoup=Jsoup.parse(html);
					try {
						
						Iiplaydata data = new Iiplaydata();
					for (String string : coolist) {
						String cc = string.split(";")[0];
						if (cc.startsWith("FTN5K=")) {
							data.setCookie(cc);
						}} 
							
							//data.setName(strs[0]);
							data.setUrl(jsoup.select("video").first().attr("src")
									.replace("/btjson.mp4", "").replace("btjson", "qq")); 
							data.setName("Success");
						return data;
					} catch (Exception ex) {
						// TODO: handle exception
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace(); 
					return null;
				}
				//return null;
			}
		});

		mAbTask.execute(item);
	} 

	public static void toGetPlayUrl3( final String file_hash,final String name,
			final AbActivity packageContext) {
		toGetPlayUrl3(file_hash, name, packageContext, false);
	}
	public static void toGetPlayUrl3( final String file_hash,final String name,
			final AbActivity packageContext,final boolean del) {
		// TODO Auto-generated method stub 
				AbDialogUtil.showProgressDialog(packageContext,
						R.drawable.progress_circular, "正在咨询文件信息...");
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						AbDialogUtil.removeDialog(packageContext);
						XFPlay data = (XFPlay) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = ""; 
						if (data==null||(""+data.getRet()).equals("-1") ) {
							msg = "文件信息咨询失败"; 
						if(tv!=null)tv.setText(msg);
							AbToastUtil.showToast(packageContext, msg);
							if(del)
							deleteFilehash(packageContext, file_hash);
						} 	
						else {

				            String com_url = data.getData().getCom_url();
				            String comkey = com_url.substring(com_url.indexOf("ftn_handler/"),
				            		com_url.indexOf("/loli") ).//- com_url.indexOf("ftn_handler/")
				            		replace("ftn_handler/", "");
				            String urlstart = com_url.substring(com_url.indexOf("http://"), 
				            		com_url.indexOf(".qq.com") ).//- com_url.indexOf("http://")
				            		replace("http://", "").replace("xflx", "").replace("store", "").
				            		replace("ctfs", "").replace("btfs", "").replace("ftn", "").
				            		replace(".", "").replace("src", "xa");
				            String com_cookie = data.getData().getCom_cookie(); 
							if (!AbStrUtil.isEmpty(com_cookie)&&!com_cookie.equals("000000")) {
								msg = com_cookie; 
								//tv.setText(msg);
								//	AbToastUtil.showToast(packageContext, msg);
								HistoryUtil.data=new History();
								HistoryUtil.data.setName(name);
								HistoryUtil.data.setUrl(file_hash.toLowerCase());
								toGetPlayUrl4(comkey, urlstart, com_cookie,name,packageContext);
							} else {
								msg = "种子文件信息咨询失败"; 
								if (com_cookie.equals("00000000")) {
									msg="文件无法播放，请试试其他的资源。";
								}if(tv!=null)
								tv.setText(msg);
									AbToastUtil.showToast(packageContext, msg);
							}
						}
					}

					@Override
					public Object getObject() {
						// TODO Auto-generated method stub 
						String html = "";
						URL url;
					    String postData = "hash="+file_hash.toLowerCase()+"&filename=loli";
						try {
							url = new URL("http://lixian.qq.com/handler/lixian/get_http_url.php" );
							HttpURLConnection connection = (HttpURLConnection) url
									.openConnection();

							connection.setRequestMethod("POST"); 
							connection.setRequestProperty("Accept-Encoding" ,"deflate" ); //identity
							connection.setRequestProperty("Accept-Language", "zh-cn");
							connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
							connection.setRequestProperty("Referer", "http://lixian.qq.com/handler/lixian/get_http_url.php");
							connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
							
							connection.setRequestProperty("User-Agent", userAgent1);
							connection.setRequestProperty("Connection", "Keep-Alive");
							connection.setRequestProperty("Expect", "100-continue");
							

							String postDataStr =postData; 
							byte[] bytes = postDataStr.getBytes("utf-8");
							connection.setRequestProperty("Content-Length", "" + bytes.length);
							//connection.setRequestProperty("Cache-Control", "no-cache");
							connection.setDoOutput(true); 
							connection.setReadTimeout(5000);
							connection.setFollowRedirects(true);
							connection.connect();
							OutputStream outStrm = connection.getOutputStream();
							outStrm.write(bytes);
							outStrm.flush();
							outStrm.close();
							InputStream inStrm = connection.getInputStream();

							BufferedReader br = new BufferedReader(new InputStreamReader(
									inStrm,"utf-8"));

							String temp = "";
							while ((temp = br.readLine()) != null) {
								html += (temp + '\n');
							}
							br.close();
							connection.disconnect();
						    html=html.trim( );
						    int subindex=0;
						    for (int i = 0; i < html.length(); i++) {
								if (html.charAt(i)!='?') {
									subindex=i;
									break;
								}
							}
						    html=html.substring(subindex);
							System.out.println(html);
							Gson gson = new Gson();
							try {
								
								XFPlay data = gson.fromJson(html, XFPlay.class);
								return data;
							} catch (Exception ex) {
								// TODO: handle exception
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace(); 
							return null;
						}
					}
				});

				mAbTask.execute(item);
	} 
	public static XFplayurl newPlayObj=null;
	public static void toGetPlayUrl4( final String comkey, final String urlstart, final String com_cookie,
			final String name,
			
			final AbActivity packageContext) {
		// TODO Auto-generated method stub 
		//final String fiString=name.substring(name.indexOf("."));
		DyUtil.fileList=new ArrayList<String>();
				AbDialogUtil.showProgressDialog(packageContext,
						R.drawable.progress_circular, "正在查询播放服务器...");
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						AbDialogUtil.removeDialog(packageContext);
						XFplayurl data = (XFplayurl) obj;
						TextView tv	=((TextView) packageContext.findViewById(R.id.textView3));
								
							String msg = "播放服务器查询成功"; 
						if (data==null ) {
							msg = "播放服务器查询失败"; 
							if(tv!=null)tv.setText(msg);
							AbToastUtil.showToast(packageContext, msg);
						} 	
						else { 
							playUrl=data.getUrl();
							if(tv!=null)tv.setText("不成功多试试。");
							//AbToastUtil.showToast(packageContext, msg);
							newPlayObj=data;
							HistoryUtil.isRecord=true;
							isLive=false;
							isCanDownload=true;
							Intent intent = new Intent(packageContext,
									VideoViewBuffer.class);
							packageContext.startActivity(intent);
						}
					}

					@Override
					public Object getObject() {
						
						// TODO Auto-generated method stub 
						XFplayurl play=null;
						String[] cs = new String[]
								{
									"xf"+urlstart+".ctfs.ftn" ,
									urlstart+".ctfs.ftn" ,
									urlstart+".ftn" ,
									urlstart+".btfs.ftn" ,
									"xfcd.ctfs.ftn",
									"sh.ctfs.ftn",
									"xfsh.ctfs.ftn",
									"xfxa.ctfs.ftn",
									"xa.ctfs.ftn",
									"hz.ftn"
								};
						for (int i = 0; i < cs.length; i++) {

							String newurl = "http://"+cs[i]+".qq.com/ftn_handler/"+comkey+"?compressed=0&dtype=1&fname=m.mkv";//+fiString;
							 DyUtil.fileList.add(newurl);
							/*URL url;
							try {
								System.out.println(com_cookie+"   "+newurl);
								url = new URL(newurl);
								HttpURLConnection connection = (HttpURLConnection) url
										.openConnection();
								connection.setRequestProperty("Accept-Encoding" ,"identity" ); //deflate
								connection.setRequestProperty("User-Agent",userAgent3);
								//connection.setRequestProperty("Referer", newurl);
								connection.setRequestProperty("Cache-Control", "no-cache");
								  
								connection.setRequestProperty("Cookie", "FTN5K=" + com_cookie); 
								connection.setConnectTimeout(5000);
								connection.setFollowRedirects(false);
								connection.connect(); 
								boolean flag=200==connection.getResponseCode();
								
								try {
									connection.disconnect();
								} catch (Exception e) {
									// TODO: handle exception
								}
								if (flag) {
									play=new XFplayurl();
									play.setCookie(com_cookie);
									play.setName(name);
									play.setUrl(newurl);
									DyUtil.fileList.add(newurl);
									break;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}*/
						}
						play=new XFplayurl();
						play.setCookie(com_cookie);
						play.setName(name);
						play.setUrl(DyUtil.fileList.get(0));
						
						return play;
					}
				});

				mAbTask.execute(item);
	} 

	public static boolean isLive=false;
	public static boolean isCanDownload=false;
	public static boolean isCanCopy=true;
	public static void play(final AbActivity abActivity ,
			final String url,final String name)
			{
		play(abActivity, url, name, true);
			}
	public static void play(final AbActivity abActivity ,
			final String url,final String name,boolean isCanCopy) {

		HistoryUtil.isRecord = false;
		 isLive = false;
		 isCanDownload = false;
		 fileList = new ArrayList<String>();
		 newPlayObj = new XFplayurl();
		 newPlayObj.setCookie("");
		DyUtil.playUrl = url;
		DyUtil.newPlayObj.setUrl(url);
		DyUtil.newPlayObj.setName(name);
		DyUtil.isCanCopy=isCanCopy;
		Intent intent = new Intent(abActivity, VideoViewBuffer.class);
		abActivity.startActivity(intent);
		
	}
	public static void play(final AbActivity abActivity ,
			final String url,final String name,boolean isCanCopy,boolean isCanDownload) {

		HistoryUtil.isRecord = false;
		 isLive = false;
		 DyUtil. isCanDownload = isCanDownload;
		 fileList = new ArrayList<String>();
		 newPlayObj = new XFplayurl();
		 newPlayObj.setCookie("");
		DyUtil.playUrl = url;
		DyUtil.newPlayObj.setUrl(url);
		DyUtil.newPlayObj.setName(name);
		DyUtil.isCanCopy=isCanCopy;
		Intent intent = new Intent(abActivity, VideoViewBuffer.class);
		abActivity.startActivity(intent);
		
	}

	public static void play(final AbActivity abActivity ,final List<String>list,
			final String url,final String name,boolean isCanCopy) {

		HistoryUtil.isRecord = false;
		 isLive = false;
		 isCanDownload = false;
		 if (list==null) {
			fileList = new ArrayList<String>();
		}else {
			fileList=list;
		}
		 
		 newPlayObj = new XFplayurl();
		 newPlayObj.setCookie("");
		DyUtil.playUrl = url;
		DyUtil.newPlayObj.setUrl(url);
		DyUtil.newPlayObj.setName(name);
		DyUtil.isCanCopy=isCanCopy;
		Intent intent = new Intent(abActivity, VideoViewBuffer.class);
		abActivity.startActivity(intent);
		
	}

	public static void playwitchcook(final AbActivity abActivity ,
			final String url,final String name,final String cook ) {

		HistoryUtil.isRecord = false;
//		HistoryUtil.data=new History();
//		HistoryUtil.data.setName(name);
//		HistoryUtil.data.setUrl(file_hash);
		 isLive = false;
		 isCanDownload = true;
		 fileList = new ArrayList<String>();
		 newPlayObj = new XFplayurl();
		 newPlayObj.setCookie(cook);
		DyUtil.playUrl = url;
		DyUtil.newPlayObj.setUrl(url);
		DyUtil.newPlayObj.setName(name);
		DyUtil.isCanCopy=false;
		Intent intent = new Intent(abActivity, VideoViewBuffer.class);
		abActivity.startActivity(intent);
		
	}
	public static void toGetPlayUrlT(  String s,
			final PlayUrlActivity packageContext) {
		// TODO Auto-generated method stub 
				AbDialogUtil.showProgressDialog(packageContext,
						R.drawable.progress_circular, "正在...");
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskObjectListener() {

					@Override
					public void update(Object obj) {
						// TODO Auto-generated method stub
						AbDialogUtil.removeDialog(packageContext);
						 
					}

					@Override
					public Object getObject() {
						// TODO Auto-generated method stub 
						
						return null;
					}
				});

				mAbTask.execute(item);
	} 
}
