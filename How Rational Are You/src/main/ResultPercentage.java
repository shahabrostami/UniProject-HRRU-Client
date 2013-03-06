package main;

import org.newdawn.slick.Image;

public class ResultPercentage implements Comparable<ResultPercentage>{
	private String name;
	private Image imgRank;
	private int percentage;
	
	public ResultPercentage(String name, Image imgRank, int percentage)
	{
		super();
		this.setName(name);
		this.setImgRank(imgRank);
		this.setPercentage(percentage);
	}
	@Override
	public int compareTo(ResultPercentage resultPercentage) {
		// TODO Auto-generated method stub
		int percentage = ((ResultPercentage)resultPercentage).getPercentage();
		return percentage - this.percentage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Image getImgRank() {
		return imgRank;
	}
	public void setImgRank(Image imgRank) {
		this.imgRank = imgRank;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

}
