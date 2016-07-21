package com.yunbo.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ab.activity.AbActivity;

public class FanHaoUtil {
	public static Pattern pat = Pattern
			.compile("([A-Za-z]{2,5})[-]*(\\d{2,5})");
	public static Pattern handpat = Pattern
			.compile("[a-z]{2,5}");
	public static Map<String,String>fanhaos=null;

	public static String getImageUrl(String s3, AbActivity abActivity) {

		String imageUrl = "http://pics.dmm.co.jp/mono/movie/adult/%s/%spl.jpg";
		s3 = s3.toLowerCase();
		Matcher mat = pat.matcher(s3);

		s3 = s3.replace("-", "");
		// if (s3.startsWith("bf")||
		// s3.startsWith("bgn")||
		// s3.startsWith("pgd")||
		// s3.startsWith("pgd")) {
		// }
		
//		if (mat.find()) {
//			String s1 = mat.group(1);
//			String s2 = mat.group(2);
//			//imageUrl = "https://jp.netcdn.space/digital/video/%s/%spl.jpg"; 
//			s3 = s1 + String.format("%d", Integer.parseInt(s2));
//			return String.format(imageUrl, s3, s3);
//		}
		if (mat.find()) {
			String s1 = mat.group(1);
			String s2 = mat.group(2);

			imageUrl = "http://pics.dmm.co.jp/digital/video/%s/%spl.jpg";

			if (FanHaoUtil.fanhaos==null) {
				try {
					FanHaoUtil.fanhaos=new HashMap<String, String>();
				InputStreamReader inputReader = new InputStreamReader(
						abActivity.getResources().getAssets().open("fanhao.txt"), "utf-8");
				BufferedReader bufReader = new BufferedReader(inputReader);
				String line;
				while ((line = bufReader.readLine()) != null) {

					Matcher mat1 = FanHaoUtil.handpat.matcher(line);
					if (mat1.find()) {
						String key=mat1.group();
						if (!FanHaoUtil.fanhaos.containsKey(key)) {
							FanHaoUtil.fanhaos.put(key, line);
						}
					}
				}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (fanhaos.containsKey(s1)) {
				s1=fanhaos.get(s1);
			}
//			s1 = s1.replace("sw", "1sw");
//			s1 = s1.replace("abp", "118abp");
//			s1 = s1.replace("bgn", "118bgn");
//			s1 = s1.replace("sga", "118sga");
//			s1 = s1.replace("dep", "118dep");
//			s1 = s1.replace("rdt", "118rdt");
//			s1 = s1.replace("mmy", "118mmy");
//			s1 = s1.replace("fst", "118fst");
//			s1 = s1.replace("njg", "118njg");
//			s1 = s1.replace("nto", "118nto");
//			s1 = s1.replace("rdd", "118rdd");
//			s1 = s1.replace("dld", "118dld");
//			s1 = s1.replace("ovg", "13ovg");
//			s1 = s1.replace("diy", "h_900diy");
//			s1 = s1.replace("ysn", "h_127ysn");
//			s1 = s1.replace("mxgs", "h_068mxgs");
//			s1 = s1.replace("boya", "55boya");
//			s1 = s1.replace("mist", "h_890mist");
//			s1 = s1.replace("scpx", "h_565scpx");
//			s1 = s1.replace("fset", "1fset");
//			s1 = s1.replace("hodv", "41hodv0");
//			s1 = s1.replace("scop", "84scop");
//			s1 = s1.replace("nanx", "84nanx");
//			s1 = s1.replace("vrtm", "h_910vrtm");
//			s1 = s1.replace("sama", "h_244sama");
//			s1 = s1.replace("dvaj", "53dvaj");
//			s1 = s1.replace("dvdes", "1dvdes");
//			s1 = s1.replace("nhdta", "1nhdta");
			s3 = s1 + String.format("%05d", Integer.parseInt(s2));
			return String.format(imageUrl, s3, s3);

		}
		return String.format(imageUrl, s3, s3);
	}
}
