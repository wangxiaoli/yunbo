package com.yunbo.myyunbo;

import android.os.Bundle;

import com.ab.activity.AbActivity;

public class AddBaiduYunActivity extends AbActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_play_url);

		Bundle be = getIntent().getExtras();
	}
}
