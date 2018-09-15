package com.example.friendcircle.bean;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * ���ӱ�
 * 
 * @author Administrator
 * 
 */
public class Post extends BmobObject{

	private String content;// ��������

	private User author;// ���ӵķ����ߣ��������ֵ���һ��һ�Ĺ�ϵ������������ĳ���û�

	private String[] filePaths;//����ͼƬ����
	
	private List<BmobFile> bmobFiles;//ͼƬ����

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
