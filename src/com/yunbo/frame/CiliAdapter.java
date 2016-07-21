package com.yunbo.frame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import com.ab.activity.AbActivity;
import com.ab.image.AbImageLoader;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil;
import com.yunbo.control.FanHaoUtil;
import com.yunbo.mode.Subfile_List;
import com.yunbo.myyunbo.R;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CiliAdapter extends BaseAdapter{

	private AbImageLoader mAbImageDownloader = null;
	private AbActivity abActivity;
	private List<Subfile_List> subfile_Lists; 
	public CiliAdapter(AbActivity abActivity,List<Subfile_List> listT ) {
		this.abActivity=abActivity;
		mAbImageDownloader = new AbImageLoader(this.abActivity); 
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setEmptyImage(R.drawable.image_empty);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);
		subfile_Lists=listT;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return subfile_Lists.size();
	}

	@Override
	public Subfile_List getItem(int position) {
		// TODO Auto-generated method stub
		return subfile_Lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			// 使用自定义的list_items作为Layout
			convertView = abActivity.mInflater.inflate(R.layout.item_cili, parent,
					false);
		} 
		TextView itemsText = AbViewHolder
				.get(convertView, R.id.tx_item); 
		LinearLayout text_item = AbViewHolder
				.get(convertView, R.id.text_item); 
		TextView itemsTitle = AbViewHolder
				.get(convertView, R.id.itemsTitle);
		TextView itemsTop = AbViewHolder
				.get(convertView, R.id.itemsTop);
		ImageView itemsIcon = AbViewHolder
				.get(convertView, R.id.itemsIcon); 
		RelativeLayout itemsImage = AbViewHolder
				.get(convertView, R.id.itemsImage); 
		String name=getItem(position).getName();
		
		String showtext="【"+DyUtil.convertFileSize(getItem(position).getFile_size())
				+"】 "+getItem(position).getName();

		Matcher mat = FanHaoUtil.pat.matcher(name);
		if (!name.toLowerCase().contains("mp4ba")
				&&!name.toLowerCase().contains("pondo")
				&&!name.toLowerCase().contains("carib")
				&&!name.toLowerCase().contains("hey")
				&&mat.find()) {
			name = mat.group(); 
			itemsTitle.setText(showtext);
			itemsTop.setText(name); 
			text_item.setVisibility(View.GONE);
			itemsImage.setVisibility(View.VISIBLE);
			  String imageUrl = FanHaoUtil.getImageUrl(name,abActivity);
			mAbImageDownloader.setLoadingView(convertView
					.findViewById(R.id.progressBar)); 
			mAbImageDownloader.display( itemsIcon, imageUrl);
		}else {
			itemsText.setText(showtext); 
			text_item.setVisibility(View.VISIBLE);
			itemsImage.setVisibility(View.GONE);
		} 
		return convertView;
	}

}
