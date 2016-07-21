package com.yunbo.mode;

import java.util.List;

public class QQFenXiang {
	private List<QQDatum> data; 
	private String msg; 
	private int ret; 
	private String taskname; 
	private String torrent_hash; 
	private int uin;
	public List<QQDatum> getData() {
		return data;
	}
	public void setData(List<QQDatum> data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getTorrent_hash() {
		return torrent_hash;
	}
	public void setTorrent_hash(String torrent_hash) {
		this.torrent_hash = torrent_hash;
	}
	public int getUin() {
		return uin;
	}
	public void setUin(int uin) {
		this.uin = uin;
	}
}
