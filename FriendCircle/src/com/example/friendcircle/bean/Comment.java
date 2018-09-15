package com.example.friendcircle.bean;

import cn.bmob.v3.BmobObject;

/**
 * 评论表
 * 
 * @author Administrator
 * 
 */
public class Comment extends BmobObject {
	private String content;// 评论内容
	private Post post;// 被评论的帖子
	private User user;// 评论人

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Comment [content=" + content + ", post=" + post + ", user="
				+ user + "]";
	}

}
