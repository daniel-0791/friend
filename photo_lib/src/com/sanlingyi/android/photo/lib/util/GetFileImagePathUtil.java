package com.sanlingyi.android.photo.lib.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取指定文件夹图片路径
 * @author g
 *
 */
public class GetFileImagePathUtil {
	
	private List<String> mList=new ArrayList<String>();
	/**
	 * 获取指定目录下所有图片路径
	 * @param imagePath
	 * @return
	 */
	public List<String> getImagePath(String filePath){
		
		if(getAllImagePathsByFolder(filePath)!=null){
			 mList.addAll(getAllImagePathsByFolder(filePath));
		 }
		return mList;
	}
	
	/**
     * 获取指定路径下的所有图片文件。
     */
    private ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }

        ArrayList<String> imageFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }

        return imageFilePaths;
    }
	
    /**判断该文件是否是一个图片。*/
    public static boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }
}
