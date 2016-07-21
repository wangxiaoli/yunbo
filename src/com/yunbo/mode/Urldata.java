package com.yunbo.mode;

import java.io.Serializable;

public class Urldata implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 4928587823762178401L;
	private String url ;
	 private String msg ;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
