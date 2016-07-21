package com.yunbo.myyunbo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter; 
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ZhainandaoMainActivity extends AbActivity {

		private GridView mListView = null;
	    private LayoutInflater mInflater;
	    private ArrayList<Video>allList=new ArrayList<Video>();  
			
	    private	BaseAdapter myListViewAdapter;
	    private ImageButton moreButton ;
	    //图片下载器
	    private AbImageLoader mAbImageDownloader = null;
	    private int page=1;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_zhainandaomain);

		mListView = (GridView) this.findViewById(R.id.mGridView);
		mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		moreButton = (ImageButton)this.findViewById(R.id.imageButton1);

        mAbImageDownloader = new AbImageLoader(this); 
        mAbImageDownloader.setMaxWidth(240);
        mAbImageDownloader.setMaxHeight(300); 
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
				String key= allList.get(position).getTitle(); 
				String src= allList.get(position).getId();  
				Bundle bundle = new Bundle();
				bundle.putString("key",key);
				bundle.putString("src",src);
				Intent intent = new Intent();
				intent.setClass(ZhainandaoMainActivity.this, ZhainandaoDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);} 
		});
		moreButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreLoading();
			}

		});
		/*
( findViewById(R.id.imageView1)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(ZhainandaoMainActivity.this, LanunyActivity.class); 
				startActivity(intent);
			}
		});*/ 
			
		 moreLoading(); 
	}
 
			private void moreLoading() {
				moreButton.setVisibility(View.GONE);
				AbDialogUtil.showProgressDialog(this,R.drawable.progress_circular, "正在获取番号列表...");
				AbTask mAbTask = new AbTask();
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListListener() {

					@Override
					public List<?> getList() {
						// TODO Auto-generated method stub
						List<Video> data=getPageContent(page); 
							return data;  
					}

					@Override
					public void update(List<?> paramList) {
						// TODO Auto-generated method stub 
						moreButton.setVisibility(View.VISIBLE);
						AbDialogUtil.removeDialog(ZhainandaoMainActivity.this);
						 
						if (paramList.size()==0) {
							AbToastUtil.showToast(ZhainandaoMainActivity.this, "查询结果为空！");
							return;
						} page++;
						List<Video> list =(List<Video>) paramList;
						 allList.addAll(list );  
						 myListViewAdapter.notifyDataSetChanged();  
					}
				});
				mAbTask.execute(item);
				
			}

	protected List<Video> getPageContent(int page) {
				// TODO Auto-generated method stub

		List<Video> data=new ArrayList<Video>()  ; 

		  String url = "http://www.lameshuang.com/meinv/fanhao/index_" + page + ".html";
		if (page==1) {
			url = "http://www.lameshuang.com/meinv/fanhao/";
		}

		try {
			Document doc = Jsoup
					.connect(url)
					.userAgent( DyUtil.userAgent1)
					.timeout(10000).get();

			Elements as = doc.select("a[class=imageLink image loading]"); 
			for (Element element : as) {
				String href = (element.attr("href") + "").trim(); 
				String title = (element.child(0).attr("alt") + "").trim();  
				String src =""+ (element.child(0).attr("abs:original") + "").trim(); 
				if (title.contains("番号作品封面,")&&title.contains("步兵番号ed2k持续更新")) {
//
					src =src//.replace("http://www.zhainandao.com/d/fh/", "http://www.xitongzijia.net/meinv/e/data/tmp/titlepic/")
	        		  .replace("_150", "_200");
				title =title.replace("番号作品封面,", "(").replace("步兵番号ed2k持续更新", ")");
				}
				Video video=new Video();
				video.setTitle(title);
				video.setImg(src);
				video.setId(href);
				data.add(video);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				return data;
			}

}
