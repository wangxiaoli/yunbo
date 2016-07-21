package com.yunbo.myyunbo;

import java.util.ArrayList;

import com.ab.activity.AbActivity;
import com.ab.util.AbViewHolder;
import com.yunbo.control.DyUtil; 
import com.yunbo.mode.Movie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NewMovieActivity extends AbActivity {

	private ListView mListView = null;
    private LayoutInflater mInflater;
    private	BaseAdapter myListViewAdapter;
    private ArrayList<Movie>allList=new ArrayList<Movie>(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_new_movie);
		mListView = (ListView) this.findViewById(R.id.mListView);
		mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mListView.setAdapter(myListViewAdapter=new BaseAdapter() {
			 
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

		          if(convertView == null){
			           //使用自定义的list_items作为Layout
			           convertView = mInflater.inflate(R.layout.sear_item, parent, false);
		          }
		          TextView itemsText = AbViewHolder.get(convertView,R.id.tx_item );
		          itemsText.setText(allList.get(position).getName());
		         // itemsText.setTextColor(R.color.dodgerblue);
		         // 
		          itemsText.setTextColor(Color.parseColor("#0099CC"));
		          itemsText.setTextSize(16);
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
				return allList.get(position);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return allList.size();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DyUtil. playUrl=allList.get(position).getUrl(); 
				//AbToastUtil.showToast(SearchActivity.this, playList.get(position));
				 Intent intent = new Intent(NewMovieActivity.this,SelectPlayerActivity.class);  
				  startActivity(intent); 
			}
		});
		getNewMovie() ;
		myListViewAdapter.notifyDataSetChanged();
	}
 private void getNewMovie() {
	 allList=new ArrayList<Movie>(); 
	 Movie movie=new Movie();
		 movie.setName("芽森しずく ABP-161 奴隷ソープ嬢");
		 movie.setUrl("http://vod.xa.ftn.qq.com:80/74be677b52fcea084aac22a63de7ef9663667b81");
		 allList.add(movie);
		   movie=new Movie();
		 movie.setName("RIO ipz370 快递员与少妇贞操带");
		 movie.setUrl("http://vod.cd.ftn.qq.com:80/6ec349e2bc220540e84c70f48ab44f39e009f2d6");
		 allList.add(movie);
		 movie=new Movie();
		 movie.setName("西野翔 RBD-582 美人OLの悲惨な凌辱日記");
		 movie.setUrl("http://vod.xa.ftn.qq.com:80/4c2548ffc91fd52ed3f8513bd6b7d87fa989f28e");
		 allList.add(movie);
		 
		 movie=new Movie();
		 movie.setName("白石茉莉奈 STAR-551 与义父的乱伦");
		 movie.setUrl("http://vod.xa.ftn.qq.com:80/caf25ed0ee0c1458fa9a239d25cc282c029962fd");
		 allList.add(movie);
		 
		 movie=new Movie();
		 movie.setName("京香Julia BOMN-026 让人垂涎三尺的极品大奶");
		 movie.setUrl("http://vod.xa.ftn.qq.com:80/dc997d64e98b275db483cb12321875186127a6bb");
		 allList.add(movie);

	 
}

}
