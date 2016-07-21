package com.yunbo.control;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList; 
import java.util.List; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yunbo.mode.Node;

public class SearchUtil {
 public static String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";
	public static List<String> getpiaohua(String wd) {
		// TODO Auto-generated method stub
			 List<String> list=new ArrayList<String>();
		String url="http://vod.cnzol.com/search.php?searchword=%s&x=0&y=0";
		try {
			wd = URLEncoder.encode(wd, "UTF-8");
			url=String.format(url, wd);
			Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout*3).get();
			//System.out.println(doc.html());
			Element content = doc.getElementById("list"); 
			 Elements lis = content.select("dt");
			 for (Element element : lis) {
				Element a = element.select("a").first();
				list.add(a.attr("abs:href"));
			}
			System.out.println(url+"  "+list.size()); 
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("errer :"+url);
			e.printStackTrace();
		}
		return list;
	}
	public static List<String> getFtp(String url) {
		// TODO Auto-generated method stub 
			 List<String> list=new ArrayList<String>();
		try { 
			System.out.println(url);
			Document doc = Jsoup.connect(url).userAgent(userAgent).followRedirects(true).timeout(10000).get();
			 
			 Elements as = doc.select("a");
			 for (Element element : as) {
				String href = element.text().trim();
				if (href.startsWith("ftp")) {
					list.add(href);
				}
				
			} 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	public static List<Node> getthunderHref(String url,String title) {
		// TODO Auto-generated method stub 
			 List<Node> list=new ArrayList<Node>();
		try { 
			Document doc ; 
			if (url.contains("www.dy558.com")) {
				doc = Jsoup.parse(getHtml(url, "gbk"), url);
			}else
			  doc = Jsoup.connect(url).userAgent(userAgent).followRedirects(true).timeout(10000).get();
			 
			 Elements as = doc.select("a[thunderHref]");
			 for (Element element : as) {
				 Node node=new Node();
				 node.setUrl(element.attr("thunderHref"));
				 node.setTitle(element.attr(title));
				 list.add(node);
			} 
			System.out.println(url+"  "+list.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
	public static Node getNode(String ftp) {
		// TODO Auto-generated method stub
		Node node =new Node();
		if (ftp.contains("/")) {
			//System.out.println(ftp);
			String name=""+ftp.substring(ftp.lastIndexOf("/")).replace("/", "");
			//System.out.println(name);
			node.setTitle(name);
			node.setUrl(ftp);
			
		}
		return node;
	}
	public static List<String> getdygod_net(String wd) {

		 List<String> list=new ArrayList<String>();
			String url="http://www.dygod.net/e/search/index.php";
			try {
				wd = URLEncoder.encode(wd, "gb2312");
				//url=String.format(url, wd);
				//System.out.println(url);
				 wd="show=title&tempid=1%C7&Submit=%C1%A2%BC%B4%CB%D1%CB%F7&keyboard="+wd;
					Document doc = Jsoup.parse(getPosthtml(url,wd,"gb2312"),url); 
				 Elements as = doc.select("a.ulink");
				 for (Element element : as) {
					String  href = element.attr("abs:href");
					if (!href.contains("dy/")) {
						continue;
					}
					list.add(href);
				} 
					System.out.println(url+"  "+list.size()); 
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("errer :"+url);
				e.printStackTrace();
			}
			return list;
	}
	public static List<String> getdy558_com(String wd) {

		 List<String> list=new ArrayList<String>();
		 String url="http://www.dy558.com/down/search.php?channelsearch=%2Fdown%2Fsearch.php&search=1&submit=+%CB%D1+%CB%F7+&keywords=";
			try {
				wd = URLEncoder.encode(wd, "gbk");

				url=url+wd;
					Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout).get();
				 Elements as = doc.select("a.searchtitle"); 
				 for (Element element : as) {
					String  href = element.attr("abs:href"); 
					list.add(href);
				} 
					System.out.println(url+"  "+list.size()); 
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("errer :"+url);
				e.printStackTrace();
			}
			return list;
	}
	public static List<String> geta67_com(String wd) {

		 List<String> list=new ArrayList<String>();

			String url="http://so.a67.com/?q=";
			try {
				wd = URLEncoder.encode(wd, "utf-8");
				url=url+wd;
					Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(10000).get();
				 Elements as = doc.select("h1 > a");
				// System.out.println(doc.html());
				 for (Element element : as) {
					String  href = element.attr("abs:href"); 
					if (!href.startsWith("http://www.a67.com/movie/")) {
						continue;
					}
					href=href.replace("movie/", "down/1_")+"_1/hdmp4";
					list.add(href);
				} 
					System.out.println(url+"  "+list.size()); 
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("errer :"+url);
				e.printStackTrace();
			}
			return list;
	}
	public static List<String> getys3344(String wd) {

		 List<String> list=new ArrayList<String>();
			String url="http://www.ys3344.com/movSearchList.asp";
			try {
				wd = URLEncoder.encode(wd, "gb2312");
				//url=String.format(url, wd);
				//System.out.println(url);
				 wd="keyWord="+wd+"&keyType=1&Submit=%CB%D1%CB%F7";
					Document doc = Jsoup.parse(getPosthtml(url,wd,"gb2312"),url); 
				 Elements as = doc.select("a.classlinkclass");
				 for (Element element : as) {
					String  href = element.attr("abs:href"); 
					list.add(href);
				} 
					System.out.println(url+"  "+list.size()); 
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("errer :"+url);
				e.printStackTrace();
			}
			return list;
	}
	public static int timeout=1000*10;
	public static List<String> getygdy8(String wd) {

		 List<String> list=new ArrayList<String>();
		 String url="http://s.kujian.com/plus/search.php?kwtype=0&searchtype=title&keyword=";
			try {
				wd = URLEncoder.encode(wd, "gb2312");

				url=url+wd;
					Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout).get();
				 Elements as = doc.select("a");
				 for (Element element : as) {
					String  href = element.attr("abs:href");
					if (!href.contains("/gndy/")||href.contains("index")) {
						continue;
					}
					list.add(href);
				} 
					System.out.println(url+"  "+list.size()); 
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("errer :"+url);
				e.printStackTrace();
			}
			return list;
	}

	public static String getPosthtml(String posturl,String postData,String encode){

		String html = "";
		URL url;
		try {
			url = new URL(posturl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection
					.setRequestProperty("User-Agent",
							userAgent);

			String postDataStr =postData; 
			byte[] bytes = postDataStr.getBytes("utf-8");
			connection.setRequestProperty("Content-Length", "" + bytes.length);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setDoOutput(true); 
			connection.setReadTimeout(timeout);
			connection.setFollowRedirects(true);
			connection.connect();
			OutputStream outStrm = connection.getOutputStream();
			outStrm.write(bytes);
			outStrm.flush();
			outStrm.close();
			InputStream inStrm = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					inStrm,encode));

			String temp = "";
			while ((temp = br.readLine()) != null) {
				html += (temp + '\n');
			}
			br.close();
			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return html;
		
	}
	public static String getHtml(String getUrl,String charsetName) {
		String html = "";
		URL url;
		try {
			url = new URL(getUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection
			.setRequestProperty("User-Agent",
					userAgent);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setReadTimeout(timeout);
			connection.setFollowRedirects(true);
			connection.connect();
			InputStream inStrm = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					inStrm, charsetName));
			String temp = "";

			while ((temp = br.readLine()) != null) {
				html = html + (temp + '\n');
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return html;
	}
	public static List<String> get2tucc(String wd) {
		// TODO Auto-generated method stub
		List<String> list=new ArrayList<String>();
		String url="http://www.2tu.cc/search.asp?searchword=%s";
		try {
			wd = URLEncoder.encode(wd, "gbk");
			url=String.format(url, wd);
			Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout*3).get();
			//System.out.println(doc.html());
			Elements as = doc.select("h2 > a"); 
			 for (Element element : as) {
				String  href = element.attr("abs:href");   
				list.add(href);
			}  
			 System.out.println(url+"  "+list.size()); 
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("errer :"+url);
			e.printStackTrace();
		}
		return list;
	}
	public static List<Node> getGvodUrls(String url) {
		// TODO Auto-generated method stub
		 List<Node> list=new ArrayList<Node>();
			try {  
				 
				String html=	 getHtml(url, "gbk");

				Pattern pat = Pattern.compile("var GvodUrls = \"(.*?)\";");
				Matcher mat = pat.matcher(html);
				if (mat.find()) {
					String GvodUrls = mat.group(1);
					GvodUrls = GvodUrls.replace("###", " ");
					String[]as=GvodUrls.split(" ");
					 for (String Url : as) {
						 Node node=new Node();
						 node.setTitle(HistoryUtil.getName(Url));
						 if (!Url.startsWith("thunder")) {
							Url=DyUtil.enThunder(Url, "utf-8");
						}
						 node.setUrl(Url);
						 list.add(node); 
						 }
				}
				System.out.println(url+"  "+list.size());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return list;
	}

}
