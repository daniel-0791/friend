package com.example.friendcircle.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
/**
 * 用户表
 * @author Administrator
 *
 */
public class User extends BmobUser{
	private String nickname;//昵称
	private BmobFile imgpic;//头像
	private String signature;//个性签
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public BmobFile getImgpic() {
		return imgpic;
	}
	public void setImgpic(BmobFile imgpic) {
		this.imgpic = imgpic;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "User [nickname=" + nickname + ", imgpic=" + imgpic
				+ ", signature=" + signature + "]";
	}
	
	

}
