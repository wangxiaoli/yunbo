package com.yunbo.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ab.activity.AbActivity;
import com.ab.util.AbStrUtil;

public class RandomUtil {
	public static List<String> list=new ArrayList<String>();
	private static int page=1;
	public static void init() {
		try {
			Pattern pat = Pattern.compile("´Å[a-zA-Z0-9]{40}´Å");
			try {
			String html = "";
			URL url = new URL("http://c7cc.cc/archiver/i/fg/huivip/"+(new Random().nextInt(83)+1)+".htm");page++;
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection
						.setRequestProperty("User-Agent",
								DyUtil.userAgent1);
				// connection.setRequestProperty("Connection", "Keep-Alive");
				// connection.setRequestProperty("Cache-Control", "no-cache");
				connection.setConnectTimeout(3000);
				connection.setReadTimeout(3000); 
				connection.connect();
				InputStream inStrm = connection.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						inStrm, "GBK"));
				String temp = "";

				while ((temp = br.readLine()) != null) {
					html = html + (temp + '\n');

					Matcher mat = pat.matcher(temp);
					if (mat.find()) {
						String hash = mat.group().replace("´Å", "");
						list.add(hash);
					}
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
			if (page<6) {
				init();
			}
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

	public static void init1(AbActivity abActivity) {
		list=new ArrayList<String>();
		try {
			InputStreamReader inputReader = new InputStreamReader(
					abActivity.getResources().getAssets().open("random.txt"), "utf-8");
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line;
			while ((line = bufReader.readLine()) != null) {
				line = line.trim();
				if (!AbStrUtil.isEmpty(line) && line.length()==40) {
					list.add(line);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	} 
}
