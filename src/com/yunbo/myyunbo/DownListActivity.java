package com.yunbo.myyunbo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.download.DownFile;
import com.ab.download.DownFileDao; 
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;  
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunbo.control.DyUtil;
import com.yunbo.frame.MyExpandableListAdapter; 

public class DownListActivity extends AbActivity{ 
	private DownFileDao mDownFileDao = null;
	private ArrayList<DownFile> mDownFileList1 = null;
	private ArrayList<DownFile> mDownFileList2 = null;
	private ArrayList<ArrayList<DownFile>> mGroupDownFileList = null;
	private MyExpandableListAdapter mExpandableListAdapter = null; 
    
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.down_list); 
        mDownFileDao = DownFileDao.getInstance(this);
        mDownFileList1 = new ArrayList<DownFile>();
        mDownFileList2 = new ArrayList<DownFile>();
        mGroupDownFileList = new ArrayList<ArrayList<DownFile>>();
        mGroupDownFileList.add(mDownFileList1);
        mGroupDownFileList.add(mDownFileList2); 
        
        String[] mDownFileGroupTitle = new String[]{"已下载","未下载"};
        
        //创建一个BaseExpandableListAdapter对象
      	mExpandableListAdapter = new MyExpandableListAdapter(this,mGroupDownFileList,mDownFileGroupTitle);
      	ExpandableListView mExpandListView = (ExpandableListView)findViewById(R.id.mExpandableListView);
      	mExpandListView.setAdapter(mExpandableListAdapter);
        //Indicator靠右
        int width = getWindowManager().getDefaultDisplay().getWidth();
        mExpandListView.setIndicatorBounds(width-40, width-25);
        mExpandListView.setChildIndicatorBounds(5, 53);
        mExpandListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					final int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub 
				for (int i = 0; i < mDownFileList2.size(); i++) {
					if (mDownFileList2.get(i).getState()==MyExpandableListAdapter.downInProgress) {
						return true;
					}
				}
				 final DownFile downFile=mGroupDownFileList.get(groupPosition).get(childPosition);
				 if (downFile.getState()!=MyExpandableListAdapter.downInProgress) {

						View mView = mInflater.inflate(R.layout.dialog_text_button,null);
						Button leftBtn1 = (Button)mView.findViewById(R.id.left_btn);
						Button rightBtn1 = (Button)mView.findViewById(R.id.right_btn);
						TextView title = (TextView)mView.findViewById(R.id.title_choices);
						TextView text = (TextView)mView.findViewById(R.id.choice_one_text);
						title.setText(downFile.getName()+downFile.getSuffix());
						text.setText("是否删除？");
						leftBtn1.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								AbDialogUtil.removeDialog(DownListActivity. this);
							}
							
						});
						
						rightBtn1.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								mGroupDownFileList.get(groupPosition).remove(downFile);
								for (int i = 0; i < mDownFileList.size(); i++) {
									if (mDownFileList.get(i).getDownUrl().equals(downFile.getDownUrl())) {
										mDownFileList.remove(i);
										break;
									}
								}
								SharedPreferences SAVE =  getSharedPreferences("download",  MODE_PRIVATE);  
							    Editor editor = SAVE.edit(); 
							    Gson gson = new Gson();  
							    String json = gson.toJson(mDownFileList); 
							    editor.putString("list",json);  
							    editor.commit(); 
							    if (downFile.getState()!=1) {

									 String dir=AbFileUtil.getFileDownloadDir(DownListActivity.this); 
									 File saveFile = new File(dir,downFile.getName()+downFile.getSuffix());
									 if (saveFile.exists()) {
										try {
											saveFile.delete();
										} catch (Exception e) {
											// TODO: handle exception
										}
									}
							    }
								AbDialogUtil.removeDialog(DownListActivity.this);
								mExpandableListAdapter.notifyDataSetChanged();
							}
							
						});
						AbDialogUtil.showDialog(mView);
				}
				return true;
			}
		});

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			boolean add = bundle.getBoolean("add", false);
			if (add) {
				init();
				addFile();
			}
		}
        initDownFileList();
     }

	private void addFile() {
		String name=DyUtil.newPlayObj.getName()+ "";
		String fiString=".avi";
		if (name.contains(".")) {
			fiString= name.substring(name.lastIndexOf("."))
					.trim(); 
		}
		if (name.startsWith("【")&&name.contains("】")) {
			name=name.substring(name.lastIndexOf("】")+1).trim(); 
		}
		name=name.replace(fiString,"");
		DownFile mDownFile1 = new DownFile();
		mDownFile1.setName(name);
		mDownFile1.setDescription(fiString);
		mDownFile1.setPakageName("");
		mDownFile1.setState(MyExpandableListAdapter.undownLoad);
		mDownFile1.setIcon(String.valueOf(R.drawable.movie));
		mDownFile1.setDownUrl(DyUtil.newPlayObj.getUrl());
		mDownFile1.setSuffix(fiString.toLowerCase());
		mDownFile1.setCookie("FTN5K="+DyUtil.newPlayObj.getCookie());;
		mDownFileList.add(mDownFile1); 
		SharedPreferences SAVE =  getSharedPreferences("download",  MODE_PRIVATE);  
	    Editor editor = SAVE.edit(); 
	    Gson gson = new Gson();  
	    String json = gson.toJson(mDownFileList); 
	    editor.putString("list",json);  
	    editor.commit(); 
		//initDownFileList();
	}

	private List<DownFile> mDownFileList = new ArrayList<DownFile>();
	/**
	 * 初始化所有文件
	 */
	private void initDownFileList() { 
		init();
		//初始化文件已经下载的长度，计算已下载的进度
		for(DownFile mDownFile:mDownFileList){
			  //本地数据
			  DownFile mDownFileT = mDownFileDao.getDownFile(mDownFile.getDownUrl());
	          if(mDownFileT != null){
	        	  mDownFile = mDownFileT;
	        	  if(mDownFile.getDownLength() == mDownFile.getTotalLength() && mDownFile.getTotalLength()!=0){
	    	    	  mDownFile.setState(MyExpandableListAdapter.downloadComplete);
	    	    	  mDownFileList1.add(mDownFile);
	    	    	  mExpandableListAdapter.notifyDataSetChanged();
				  }else{
					  //显示为暂停状态
		        	  mDownFile.setState(MyExpandableListAdapter.downLoadPause);
	        	      mDownFileList2.add(mDownFile);
	        	      mExpandableListAdapter.notifyDataSetChanged();
				  }
	          }else{
	        	    mDownFile.setState(MyExpandableListAdapter.undownLoad);
	        	    mDownFileList2.add(mDownFile);
	        	    mExpandableListAdapter.notifyDataSetChanged();
	          }
	    }
	}
	
	
	@Override
	public void finish() {
		super.finish();
		
		//释放所有的下载线程
		mExpandableListAdapter.releaseThread();

		
	}
	private void init() {

		SharedPreferences SAVE =  getSharedPreferences("download",  MODE_PRIVATE);  
		String json=SAVE.getString("list",null); 
		if (!AbStrUtil.isEmpty(json)) {
		    Gson gson = new Gson();  
		    mDownFileList=gson.fromJson(json,  
	                new TypeToken<List<DownFile>>() {  
	                }.getType());  
		}if (mDownFileList==null) {
			
			mDownFileList = new ArrayList<DownFile>();
		}
	}
	

}
