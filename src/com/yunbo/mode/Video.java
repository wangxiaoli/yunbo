package com.yunbo.mode;

import java.util.List;

public class Video
{
    private String vid ;
    private String id ;
    private String title ;
    private List<Name> type ;
    private List<NameEnc> area ;
    private List<NameEnc> actor ;
    private String rating ;
    private List<NameEnc> director ;
    private String duration ;
    private String date ;
    private List<NameEnc> tags ;
    private String intro ;
    private String img ;
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Name> getType() {
		return type;
	}
	public void setType(List<Name> type) {
		this.type = type;
	}
	public List<NameEnc> getArea() {
		return area;
	}
	public void setArea(List<NameEnc> area) {
		this.area = area;
	}
	public List<NameEnc> getActor() {
		return actor;
	}
	public void setActor(List<NameEnc> actor) {
		this.actor = actor;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public List<NameEnc> getDirector() {
		return director;
	}
	public void setDirector(List<NameEnc> director) {
		this.director = director;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<NameEnc> getTags() {
		return tags;
	}
	public void setTags(List<NameEnc> tags) {
		this.tags = tags;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	} 
}