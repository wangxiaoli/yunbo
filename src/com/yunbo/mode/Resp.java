package com.yunbo.mode;

import java.util.List;

public class Resp {
	private int userid;
	private int ret;
	private List<Subfile_List> subfile_list;
	private String main_task_url_hash;
	private String info_hash;
	private int record_num;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public List<Subfile_List> getSubfile_list() {
		return subfile_list;
	}

	public void setSubfile_list(List<Subfile_List> subfile_list) {
		this.subfile_list = subfile_list;
	}

	public String getMain_task_url_hash() {
		return main_task_url_hash;
	}

	public void setMain_task_url_hash(String main_task_url_hash) {
		this.main_task_url_hash = main_task_url_hash;
	}

	public String getInfo_hash() {
		return info_hash;
	}

	public void setInfo_hash(String info_hash) {
		this.info_hash = info_hash;
	}

	public int getRecord_num() {
		return record_num;
	}

	public void setRecord_num(int record_num) {
		this.record_num = record_num;
	}
}
