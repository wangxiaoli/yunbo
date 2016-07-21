package com.yunbo.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.activity.AbActivity;
import com.ab.util.AbStrUtil;
import com.yunbo.mode.XFplayurl;
import com.yunbo.myyunbo.R;
import com.yunbo.myyunbo.VideoViewBuffer;

public class TvUtil {
	public static List<String> str2List(String str) {

		List<String> list = new ArrayList<String>();
		if (!TextUtils.isEmpty(str)) {
			str = str.replace("\r", "\n").trim();
			while (str.contains("\n\n")) {

				str = str.replace("\n\n", "\n").trim();
			}
			str = str.replace("\n", "|").trim();
			for (String all : str.split("\\|")) {
				if (all != null) {
					all = all.trim();
					if (!TextUtils.isEmpty(all)) {
						list.add(all.trim());
					}
				}

			}

		}
		return list;
	}

	public static List<XFplayurl> getZBY(List<String> strs,
			Map<String, String> map) {

		List<XFplayurl> list = new ArrayList<XFplayurl>();
		int index = map.size() + 1;
		for (String all : strs) {
			String[] s = all.split(",");
			if (s.length == 2 && map.get(s[1]) == null) {
				map.put(s[1], s[0]);
				if (s[1].contains("#")) {
					int j = 1;
					for (String string2 : s[1].split("#")) {
						if (!AbStrUtil.isEmpty(string2) && isCanzb(string2)
								&& map.get(string2) == null) {
							map.put(string2, s[0]);
							XFplayurl obj = new XFplayurl();
							obj.setName((index++) + "台 " + s[0].trim() + "(源"
									+ j++ + ")");
							obj.setUrl(string2.trim());
							list.add(obj);
						}
					}
					continue;
				}
				if (isCanzb(s[1])) {

					XFplayurl obj = new XFplayurl();
					obj.setName((index++) + "台 " + s[0].trim());
					obj.setUrl(s[1].trim());
					list.add(obj);
				}
			}
		}
		return list;
	}

	public static boolean isCanzb(String str) {
		return (!TextUtils.isEmpty(str))
				&& (str.trim().startsWith("http")
						|| str.trim().startsWith("rtmp://") || str.trim()
						.startsWith("rtsp://")|| str.trim()
						.startsWith("mms://"));
	}

	public static void playtv(XFplayurl obj, AbActivity context) {

		DyUtil.newPlayObj = obj;
		DyUtil.fileList = new ArrayList<String>();
		DyUtil.playUrl = DyUtil.newPlayObj.getUrl();
		HistoryUtil.isRecord = false;
		DyUtil.isLive = true;
		DyUtil.isCanDownload = false;
		Intent intent = new Intent(context, VideoViewBuffer.class);
		context.startActivity(intent);
	}

	public static View getView(final List<XFplayurl> listxf,
			final AbActivity context) {
		View mView = context.mInflater.inflate(R.layout.dia_list_text, null);
		ListView listView = (ListView) mView.findViewById(R.id.listView1);
		TextView textView1 = (TextView) mView.findViewById(R.id.textView1);
		textView1.setText("直播源：" + listxf.size());

		String[] itemstrs = new String[listxf.size()];
		for (int i = 0; i < itemstrs.length; i++)
			itemstrs[i] = listxf.get(i).getName();

		ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
				context, R.layout.dialog_list_item_1, itemstrs);
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				playtv(listxf.get(position), context);
			}
		});
		return mView;
	}
}
