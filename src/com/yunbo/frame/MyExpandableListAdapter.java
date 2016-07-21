package com.yunbo.frame;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.download.AbDownloadProgressListener;
import com.ab.download.AbDownloadThread;
import com.ab.download.AbFileDownloader;
import com.ab.download.DownFile;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.yunbo.control.DyUtil;
import com.yunbo.myyunbo.R;
import com.yunbo.myyunbo.VideoViewBuffer;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	// 连接超时
	public static final int timeOut = 12000;
	// 建立连接
	public static final int connectOut = 12000;
	// 获取数据
	public static final int getOut = 60000;

	// 1表示已下载完成
	public static final int downloadComplete = 1;
	// 1表示未开始下载
	public static final int undownLoad = 0;
	// 2表示已开始下载
	public static final int downInProgress = 2;
	// 3表示下载暂停
	public static final int downLoadPause = 3;
	private Context mContext;
	private ArrayList<ArrayList<DownFile>> mDownFileGroupList = null;
	private String[] mDownFileGroupTitle = null;
	public HashMap<String, AbFileDownloader> mFileDownloaders = null;

	public MyExpandableListAdapter(Context context,
			ArrayList<ArrayList<DownFile>> downFileGroupList,
			String[] downFileGroupTitle) {
		this.mContext = context;
		mDownFileGroupList = downFileGroupList;
		mDownFileGroupTitle = downFileGroupTitle;
		mFileDownloaders = new HashMap<String, AbFileDownloader>();
	}

	/**
	 * 获取指定组位置、指定子列表项处的子列表项数据
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mDownFileGroupList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mDownFileGroupList.get(groupPosition).size();
	}

	/**
	 * 该方法决定每个子选项的外观
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.down_items, parent, false);
		}
		final ViewHolder holder = new ViewHolder();
		holder.itemsIcon = (ImageView) convertView.findViewById(R.id.itemsIcon);
		holder.itemsTitle = (TextView) convertView
				.findViewById(R.id.itemsTitle);
		holder.itemsDesc = (TextView) convertView.findViewById(R.id.itemsDesc);
		holder.operateBtn = (Button) convertView.findViewById(R.id.operateBtn);
		holder.progress = (ProgressBar) convertView
				.findViewById(R.id.received_progress);
		holder.received_progress_percent = (TextView) convertView
				.findViewById(R.id.received_progress_percent);
		holder.received_progress_number = (TextView) convertView
				.findViewById(R.id.received_progress_number);
		holder.received_progressBar = (RelativeLayout) convertView
				.findViewById(R.id.received_progressBar);

		holder.itemsIcon.setFocusable(false);
		holder.operateBtn.setFocusable(false);
		holder.progress.setFocusable(false);

		final DownFile mDownFile = (DownFile) getChild(groupPosition,
				childPosition);
		if (mDownFile != null) {
			int resId = R.drawable.movie;
			if (!AbStrUtil.isEmpty(mDownFile.getSuffix())) {
				if (mDownFile.getSuffix().endsWith("mp4"))
					resId = R.drawable.mp4;
				if (mDownFile.getSuffix().endsWith("mkv"))
					resId = R.drawable.mkv;
				if (mDownFile.getSuffix().endsWith("flv"))
					resId = R.drawable.flv;
				if (mDownFile.getSuffix().endsWith("mov"))
					resId = R.drawable.mov;
				if (mDownFile.getSuffix().endsWith("wmv"))
					resId = R.drawable.wmv;
				if (mDownFile.getSuffix().endsWith("avi"))
					resId = R.drawable.avi;
				if (mDownFile.getSuffix().endsWith("wma"))
					resId = R.drawable.wma;
				if (mDownFile.getSuffix().endsWith("rm")
						|| mDownFile.getSuffix().endsWith("rmvb"))
					resId = R.drawable.rm;
			}

			holder.itemsIcon.setImageResource(resId);

			// holder.itemsIcon.setImageResource(mDownFile.getIcon());
			holder.itemsTitle.setText(mDownFile.getName());
			holder.itemsDesc.setText(mDownFile.getDescription());
			if (mDownFile.getState() == undownLoad) {
				holder.operateBtn.setBackgroundResource(R.drawable.down_load);
				holder.received_progressBar.setVisibility(View.GONE);
				holder.itemsDesc.setVisibility(View.VISIBLE);
				holder.progress.setProgress(0);
				holder.received_progress_percent.setText(0 + "%");
				holder.received_progress_number.setText("0KB/"
						+ DyUtil.convertFileSize(mDownFile.getTotalLength()));
			} else if (mDownFile.getState() == downInProgress) {
				holder.operateBtn.setBackgroundResource(R.drawable.down_pause);
				if (mDownFile.getDownLength() != 0
						&& mDownFile.getTotalLength() != 0) {
					int c = (int) (mDownFile.getDownLength() * 100 / mDownFile
							.getTotalLength());
					holder.itemsDesc.setVisibility(View.GONE);
					holder.received_progressBar.setVisibility(View.VISIBLE);
					holder.progress.setProgress(c);
					holder.received_progress_percent.setText(c + "%");
					holder.received_progress_number
							.setText(DyUtil.convertFileSize(mDownFile
									.getDownLength())
									+ "/"
									+ DyUtil.convertFileSize(mDownFile
											.getTotalLength()));
				}
			} else if (mDownFile.getState() == downLoadPause) {
				holder.operateBtn.setBackgroundResource(R.drawable.down_load);
				// 下载了多少
				if (mDownFile.getDownLength() != 0
						&& mDownFile.getTotalLength() != 0) {
					int c = (int) (mDownFile.getDownLength() * 100 / mDownFile
							.getTotalLength());
					holder.itemsDesc.setVisibility(View.GONE);
					holder.received_progressBar.setVisibility(View.VISIBLE);
					holder.progress.setProgress(c);
					holder.received_progress_percent.setText(c + "%");
					holder.received_progress_number
							.setText(DyUtil.convertFileSize(mDownFile
									.getDownLength())
									+ "/"
									+ DyUtil.convertFileSize(mDownFile
											.getTotalLength()));
				} else {
					holder.itemsDesc.setVisibility(View.VISIBLE);
					holder.received_progressBar.setVisibility(View.GONE);
					holder.progress.setProgress(0);
					holder.received_progress_percent.setText(0 + "%");
					holder.received_progress_number
							.setText("0KB/"
									+ DyUtil.convertFileSize(mDownFile
											.getTotalLength()));
				}
			} else if (mDownFile.getState() == downloadComplete) {

				holder.operateBtn.setVisibility(View.GONE);
				holder.operateBtn.setBackgroundResource(R.drawable.down_delete);
				holder.received_progressBar.setVisibility(View.GONE);
				holder.itemsDesc.setVisibility(View.VISIBLE);
				String dir = AbFileUtil.getFileDownloadDir(mContext);
				File saveFile = new File(dir, mDownFile.getName()
						+ mDownFile.getSuffix());
				holder.itemsDesc.setText(saveFile.getAbsolutePath());
				// holder.itemsDesc.setText(mDownFile.getDownPath());
			}

			final AbDownloadProgressListener mDownloadProgressListener = new AbDownloadProgressListener() {
				// 实时获知文件已经下载的数据长度
				long temp = mDownFile.getDownLength();
				long rate = 0L;
				long ratev = 0L;
				long time;

				@Override
				public void onDownloadSize(final long size) { 
					ratev = size - temp;
					ratev = ratev / 2;
					temp = size;
					final int c = (int) (size * 100 / mDownFile
							.getTotalLength());
					if (ratev != 0L)
						time = (mDownFile.getTotalLength() - size) / ratev;
					if (c != holder.progress.getProgress() || ratev != rate) {
						holder.progress.post(new Runnable() {
							@Override
							public void run() {
								rate = ratev;
								holder.progress.setProgress(c);
								holder.received_progress_percent.setText(c
										+ "%");
								holder.received_progress_number.setText(DyUtil
										.convertFileSize(size)
										+ "/"
										+ DyUtil.convertFileSize(mDownFile
												.getTotalLength())
										+ "|"
										+ DyUtil.convertFileSize(rate)
										+ "/s-"
										+ GetTime(time));
							}
						});
					}
					if (mDownFile.getTotalLength() == size) {
						// if(D)Log.d(TAG, "下载完成:"+size);
						mDownFile.setState(downloadComplete);
						// 下载完成
						mDownFileGroupList.get(1).remove(mDownFile);
						mDownFileGroupList.get(0).add(mDownFile);
						holder.progress.post(new Runnable() {
							@Override
							public void run() {
								notifyDataSetChanged();
							}
						});

					}
				}
			};

			// 处理按钮事件
			holder.operateBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						// 无sd卡
						AbToastUtil.showToast(mContext, "没找到存储卡");
						return;
					}

					if (mDownFile.getState() == undownLoad
							|| mDownFile.getState() == downLoadPause) {
						// 下载

						holder.itemsDesc.setVisibility(View.GONE);
						holder.received_progressBar.setVisibility(View.VISIBLE);
						holder.operateBtn
								.setBackgroundResource(R.drawable.down_pause);
						mDownFile.setState(downInProgress);
						AbTask mAbTask = new AbTask();
						final AbTaskItem item = new AbTaskItem();
						item.setListener(new AbTaskListener() {
							boolean isok = false;

							@Override
							public void update() {
								if (!isok) {
									holder.itemsDesc.setVisibility(View.VISIBLE);
									holder.itemsDesc.setText("下载失败"); 
								}
							}

							@Override
							public void get() {
								try {
									// 检查文件总长度
									int totalLength = AbFileUtil
											.getContentLengthFromUrlWitchCookie(
													mDownFile.getDownUrl(),
													mDownFile.getCookie());
									mDownFile.setTotalLength(totalLength);
									isok = totalLength > 0;
									// 开始下载文件
									AbFileDownloader loader = new AbFileDownloader(
											mContext, mDownFile, 1);
									mFileDownloaders.put(
											mDownFile.getDownUrl(), loader);
									loader.download(mDownloadProgressListener);
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
						});
						mAbTask.execute(item);

					} else if (mDownFile.getState() == downInProgress) {
						// 暂停
						holder.operateBtn
								.setBackgroundResource(R.drawable.down_load);
						mDownFile.setState(undownLoad);
						AbFileDownloader mFileDownloader = mFileDownloaders
								.get(mDownFile.getDownUrl());
						holder.itemsDesc.setText(mFileDownloader.getSaveFile()
								.getAbsolutePath());
						// 释放原来的线程
						if (mFileDownloader != null) {
							mFileDownloader.setFlag(false);
							AbDownloadThread mDownloadThread = mFileDownloader
									.getThreads();
							if (mDownloadThread != null) {
								mDownloadThread.setFlag(false);
								mFileDownloaders.remove(mDownFile.getDownUrl());
								mDownloadThread = null;
							}
							mFileDownloader = null;
						}
					} else if (mDownFile.getState() == downloadComplete) {
						// 删除
						mDownFileGroupList.get(0).remove(mDownFile);
						mDownFile.setState(undownLoad);
						mDownFileGroupList.get(1).add(mDownFile);
						notifyDataSetChanged();
					}

				}
			});

		}
		return convertView;
	}

	/**
	 * 获取指定组位置处的组数据
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return mDownFileGroupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mDownFileGroupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * 该方法决定每个组选项的外观
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.down_title, parent, false);
		}
		TextView mTextView = (TextView) convertView
				.findViewById(R.id.title_text);
		mTextView.setText(mDownFileGroupTitle[groupPosition] + "("
				+ mDownFileGroupList.get(groupPosition).size() + ")");
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * 描述：释放线程
	 */
	public void releaseThread() {
		Iterator it = mFileDownloaders.entrySet().iterator();
		AbFileDownloader mFileDownloader = null;
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			mFileDownloader = (AbFileDownloader) e.getValue();
			// System.out.println("Key: " + e.getKey() + "; Value: " +
			// e.getValue());
			if (mFileDownloader != null) {
				mFileDownloader.setFlag(false);
				AbDownloadThread mDownloadThread = mFileDownloader.getThreads();
				if (mDownloadThread != null) {
					mDownloadThread.setFlag(false);
					mDownloadThread = null;
				}
				mFileDownloader = null;
			}
		}
	}

	public void delete(DownFile mDownFile) {
		Iterator it = mFileDownloaders.entrySet().iterator();
		AbFileDownloader mFileDownloader = null;

		AbFileDownloader loader = null;
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			mFileDownloader = (AbFileDownloader) e.getValue();
			// System.out.println("Key: " + e.getKey() + "; Value: " +
			// e.getValue());
			if (mFileDownloader != null) {
				mFileDownloader.setFlag(false);
				AbDownloadThread mDownloadThread = mFileDownloader.getThreads();
				if (mDownloadThread != null) {
					mDownloadThread.setFlag(false);
					mDownloadThread = null;
				}
				if (mDownFile.getDownUrl().equals(e.getKey())) {
					loader = mFileDownloader;
					break;
				}
				mFileDownloader = null;

			}
		}
		if (loader == null)
			loader = new AbFileDownloader(mContext, mDownFile, 1);
		try {
			loader.getSaveFile().delete();
		} catch (Exception e) {
		}
	}

	private String GetTime(long whs) {
		String time = "";
		long h = whs / 3600;
		long m = (whs / 60) % 60;
		long s = (whs) % 60;
		if (h > 24L) {
			h = h % 24;
			time += String.valueOf(h / 24) + ".";
		}
		time += String.valueOf(h);
		if (String.valueOf(m).length() < 2)
			time += ":0" + String.valueOf(m);
		else
			time += ":" + String.valueOf(m);

		if (String.valueOf(s).length() < 2)
			time += ":0" + String.valueOf(s);
		else
			time += ":" + String.valueOf(s);
		return time;
	}

	public class ViewHolder {
		public ImageView itemsIcon;
		public TextView itemsTitle;
		public TextView itemsDesc;
		public Button operateBtn;
		public ProgressBar progress;
		public TextView received_progress_percent;
		public TextView received_progress_number;
		public RelativeLayout received_progressBar;
	}
}
