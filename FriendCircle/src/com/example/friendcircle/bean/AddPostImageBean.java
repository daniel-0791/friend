package com.example.friendcircle.bean;

/**
 * 发布帖子之图片Bean
 * 
 * @author Administrator
 * 
 */
public class AddPostImageBean {
	private String imgPic;// 图片的地址
	private int state;// state=0:代表是点击的“+”，state=1:代表是正常的图片

	public String getImgPic() {
		return imgPic;
	}

	public void setImgPic(String imgPic) {
		this.imgPic = imgPic;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
