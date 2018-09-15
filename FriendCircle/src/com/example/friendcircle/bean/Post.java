package com.example.friendcircle.bean;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 帖子表
 * 
 * @author Administrator
 * 
 */
public class Post extends BmobObject{

	private String content;// 帖子内容

	private User author;// 帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

	private String[] filePaths;//帖子图片集合
	
	private List<BmobFile> bmobFiles;//图片集合

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String[] getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(String[] filePaths) {
		this.filePaths = filePaths;
	}

	public List<BmobFile> getBmobFiles() {
		return bmobFiles;
	}

	public void setBmobFiles(List<BmobFile> bmobFiles) {
		this.bmobFiles = bmobFiles;
	}

	@Override
	public String toString() {
		return "Post [content=" + content + ", author=" + author
				+ ", filePaths=" + Arrays.toString(filePaths) + ", bmobFiles="
				+ bmobFiles + "]";
	}
	
}
