package com.sanlingyi.android.photo.lib.base;
/**
 * 图片文件夹实体base
 * 范晓锋
 * @author g
 *ImageFilePath  文件夹路径
 *ImagePath     第一张图片路径
 *ImageCount 	   文件夹图片数目
 */
public class PhotoFilePathBase {
	private String ImageFilePath;
	private String ImagePath;
	private int    ImageCount;
	
	public PhotoFilePathBase(String imageFilePath, String imagePath,
			int imageCount) {
		super();
		ImageFilePath = imageFilePath;
		ImagePath = imagePath;
		ImageCount = imageCount;
	}
	public String getImageFilePath() {
		return ImageFilePath;
	}
	public void setImageFilePath(String imageFilePath) {
		ImageFilePath = imageFilePath;
	}
	public String getImagePath() {
		return ImagePath;
	}
	public void setImagePath(String imagePath) {
		ImagePath = imagePath;
	}
	public int getImageCount() {
		return ImageCount;
	}
	public void setImageCount(int imageCount) {
		ImageCount = imageCount;
	}
	
	
	
}
