package com.yunbo.mode;

import java.util.ArrayList;
import java.util.List;
 
public class Film  {

	private String url;
	private String img;
	private String infro;
	private String name; 
	private List<String> tags=new ArrayList<String>();
	

	public Film() {
		super();
	}  

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getInfro() {
		return infro;
	}

	public void setInfro(String infro) {
		this.infro = infro;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
 
	 
}
