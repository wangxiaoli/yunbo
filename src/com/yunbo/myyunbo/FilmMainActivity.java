package com.yunbo.myyunbo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ab.activity.AbActivity;  
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewHolder;  
import com.yunbo.control.DyUtil;
import com.yunbo.mode.Dydata;
import com.yunbo.mode.Movie;
import com.yunbo.mode.Node;
import com.yunbo.mode.PageContent;
import com.yunbo.mode.Video; 

import android.content.Context;
import android.content.Intent;
import android.os.Bundle; 
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter; 
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class FilmMainActivity extends AbActivity {

		private GridView mListView = null;
	    private LayoutInflater mInflater;
	    private ArrayList<Video>allList=new ArrayList<Video>();  
	    private ArrayList<Video>showList=new ArrayList<Video>();  
			
	    private	BaseAdapter myListViewAdapter; 
	    //图片下载器
	    private AbImageLoader mAbImageDownloader = null;
		private EditText word;
		private TextView textView; 
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_filmmain);
		(findViewById(R.id.ssLinearLayout1)).setVisibility(View.GONE);
		textView = (TextView) this.findViewById(R.id.textView1);

		mListView = (GridView) this.findViewById(R.id.mGridView);
		mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		word = (EditText) findViewById(R.id.editText1);

        mAbImageDownloader = new AbImageLoader(this); 
//        mAbImageDownloader.setMaxWidth(240);
//        mAbImageDownloader.setMaxHeight(300); 
        mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setEmptyImage(R.drawable.image_empty); 
        mAbImageDownloader.setErrorImage(R.drawable.image_error); 
/**/
		mListView.setAdapter(myListViewAdapter=new BaseAdapter() {
			 
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

		          if(convertView == null){
			           //使用自定义的list_items作为Layout
			           convertView = mInflater.inflate(R.layout.item_list1, parent, false);
		          }
		          ImageView itemsIcon =(ImageView) convertView.findViewById(R.id.itemsIcon); 
		          TextView itemsTitle =(TextView) convertView.findViewById(R.id.itemsTitle); 
		          itemsIcon.setScaleType(ScaleType.FIT_CENTER);
				  //获取该行的数据
		          String imageUrl = getItem(position).getImg();
		          itemsTitle.setText(getItem(position).getTitle()); 
		          //设置加载中的View
		          mAbImageDownloader.setLoadingView(convertView.findViewById(R.id.progressBar));
		          //图片的下载
		          mAbImageDownloader.display(itemsIcon,imageUrl);
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public Video getItem(int position) {
				// TODO Auto-generated method stub
				return showList.get(position);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return showList.size();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) { 
				String src= showList.get(position).getId();  

				final List<Video> videos = new ArrayList<Video>(); 
				for (String string : src.split("#")) {
					if (!TextUtils.isEmpty(string)&&string.contains("$")) {
						try {
							Video video=new Video();
							video.setTitle("【" + DyUtil.convertFileSize(Long.parseLong(string.split("\\$")[0])) + "】 "
									+ showList.get(position).getTitle()+".mp4");
							video.setId(string.split("\\$")[1]);
							videos.add(video);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}else if (!TextUtils.isEmpty(string)) {
						try {
							Video video=new Video();
							video.setTitle(showList.get(position).getTitle());
							video.setId(string);
							videos.add(video);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
				if (videos.size()<2) {
					if (videos.size()==1) {
						if (videos.get(0).getId().startsWith("http://")) {
							DyUtil.play(FilmMainActivity.this, videos.get(0).getId(),
									videos.get(0).getTitle(),false);
						} else {

						DyUtil.toGetPlayUrl3(videos.get(0).getId(),
								videos.get(0).getTitle(), FilmMainActivity.this);
						}
					}
					return;
				}
				View mView =  mInflater.inflate(
						R.layout.dia_list_text, null);
				ListView listView = (ListView) mView
						.findViewById(R.id.listView1);
				TextView textView1 = (TextView) mView
						.findViewById(R.id.textView1); 
					textView1.setVisibility(View.GONE);

					String[] mStrings = new String[videos.size()];
					for (int i = 0; i < videos.size(); i++) {
						mStrings[i] = videos.get(i).getTitle();
					} 
					listView.setAdapter(new ArrayAdapter<String>(
							FilmMainActivity.this, R.layout.dialog_list_item_1, mStrings) );
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							if (videos.get(position).getId().startsWith("http://")) {
								DyUtil.play(FilmMainActivity.this, videos.get(position).getId(),
										videos.get(position).getTitle(),false);
							} else {

							DyUtil.toGetPlayUrl3(videos.get(position).getId(),
									videos.get(position).getTitle(), FilmMainActivity.this);
							}
//							DyUtil.toGetPlayUrl3(videos.get(position).getId(),
//									videos.get(position).getTitle(), FilmMainActivity.this);
						}
					});
					AbDialogUtil.showAlertDialog(mView);
			} 
		});
		(findViewById(R.id.imagess)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String wd = word.getText().toString().trim();

				textView.setText("搜索："+wd); 
				wd=(""+wd).toLowerCase();
				showList=new ArrayList<Video>();
				for (Video video : allList) {
					for (String str :video.getRating() .split("\\/"))  
					if ((""+str).toLowerCase().contains(wd)) {
						video.setTitle(str);
						showList.add(video);
					}
				}
				myListViewAdapter.notifyDataSetChanged();

			}

		}); 
		(findViewById(R.id.imageView1)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				(findViewById(R.id.imageView1)).setVisibility(View.GONE);

				(findViewById(R.id.ssLinearLayout1)).setVisibility(View.VISIBLE);

			}

		}); 
		(findViewById(R.id.imageButton1)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				(findViewById(R.id.imageView1)).setVisibility(View.VISIBLE);

				(findViewById(R.id.ssLinearLayout1)).setVisibility(View.GONE);

			}

		}); 
			
		 moreLoading(); 
	}
 
			private void moreLoading() { 
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListListener() {

					@Override
					public List<?> getList() {
						// TODO Auto-generated method stub 

						List<Video> data = new ArrayList<Video>();
						 Map<String, String>map=new HashMap<String, String>();

							try {
								InputStreamReader inputReader = new InputStreamReader(
										getResources().getAssets().open("dyall.txt"), "utf-8");
								BufferedReader bufReader = new BufferedReader(inputReader);
								String line;
								while ((line = bufReader.readLine()) != null) {
									if (!AbStrUtil.isEmpty(line) && line.contains("|")) {
										String strs[]=line.split("\\|");
										try {
											if (map.containsKey(strs[1])) {
												continue;
											}
			Video video = new Video();
										  
										video.setId(strs[2]); 
										video.setTitle(strs[0].split("\\/")[0]);  
										video.setImg(strs[1]); 
										video.setRating(strs[0]);
										map.put(strs[1], strs[2]);
										data.add(video);  
										} catch (Exception e) {
											// TODO: handle exception
										}
									}
								}
								
								bufReader.close();
								 
							} catch (Exception e) {
								e.printStackTrace();
							} 
									
							return data;  
					}

					@Override
					public void update(List<?> paramList) {
						// TODO Auto-generated method stub  
						  
						List<Video> list =(List<Video>) paramList;
						 allList.addAll(list );  
							showList.addAll(list);
						 myListViewAdapter.notifyDataSetChanged();  
					}
				});
				mAbTask.execute(item);
				
			}
 

}
