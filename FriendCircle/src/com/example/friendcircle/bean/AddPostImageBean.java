package com.example.friendcircle.bean;

/**
 * ��������֮ͼƬBean
 * 
 * @author Administrator
 * 
 */
public class AddPostImageBean {
	private String imgPic;// ͼƬ�ĵ�ַ
	private int state;// state=0:�����ǵ���ġ�+����state=1:������������ͼƬ

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
