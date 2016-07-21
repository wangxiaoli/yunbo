package com.yunbo.myyunbo;

import com.ab.activity.AbActivity;
import com.ab.util.AbToastUtil;
import com.yunbo.control.DyUtil;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class SelectPlayerActivity extends AbActivity {

	private TextView link;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_select_player);
		
		link=(TextView)findViewById(R.id.link);
		link.setText(DyUtil.playUrl);
		
		if (DyUtil.playUrl.endsWith("pfv")) {
			((TextView)findViewById(R.id.textinfro)).setText("该视频为直播流，无法拉动");
		}
		findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
		copy();
			}
		});
		findViewById(R.id.textView1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
		copy();
			}
		});
		findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SelectPlayerActivity.this,
						VideoViewBuffer.class);
				SelectPlayerActivity.this.startActivity(intent);
			}
		});
		findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//outSysremPlay();
				//Intent intent = new Intent(SelectPlayerActivity.this,
				//		PlayActivity.class);
				//SelectPlayerActivity.this.startActivity(intent);
			}
		});
	}

	private void copy() {
		ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		ClipData textCd = ClipData.newPlainText(null, DyUtil.playUrl);
		  clipboard.setPrimaryClip(textCd);
		  AbToastUtil.showToast(this, "链接已复制");
	}
	
	private void outSysremPlay() {
		Uri uri = Uri.parse(DyUtil.playUrl);
		// 调用系统自带的播放器
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Log.v("URI:::::::::", uri.toString());
		intent.setDataAndType(uri, "video/*");
		startActivity(intent);
	}
 

}
