package com.yunbo.mode;

import java.util.List;

public class Data {
	private List<String> bt ;
    private int current_page ;
    private List<String> group ;
    private List<Hilight> hilights ;
    private List<Node> nodes ;
    private int total_num ;
    private int total_page ;
	public List<String> getBt() {
		return bt;
	}
	public void setBt(List<String> bt) {
		this.bt = bt;
	}
	public int getCurrent_page() {
		return current_page;
	}
	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}
	public List<String> getGroup() {
		return group;
	}
	public void setGroup(List<String> group) {
		this.group = group;
	}
	public List<Hilight> getHilights() {
		return hilights;
	}
	public void setHilights(List<Hilight> hilights) {
		this.hilights = hilights;
	}
	public List<Node> getNodes() {
		return nodes;
	}
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	public int getTotal_num() {
		return total_num;
	}
	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}
	public int getTotal_page() {
		return total_page;
	}
	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}
}
