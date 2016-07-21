package com.yunbo.mode;
 
public class Nvyou implements Comparable<Nvyou>{

	private String url;
	private String img;
	private String infro;
	private String name;
	private String firstLetter;
	

	public Nvyou() {
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

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}
	

	@Override
	public int compareTo(Nvyou another) {
		if (this.getFirstLetter().equals("@")
				|| another.getFirstLetter().equals("#")) {
			return -1;
		} else if (this.getFirstLetter().equals("#")
				|| another.getFirstLetter().equals("@")) {
			return 1;
		} else {
			return this.getFirstLetter().compareTo(another.getFirstLetter());
		}
	}
}
