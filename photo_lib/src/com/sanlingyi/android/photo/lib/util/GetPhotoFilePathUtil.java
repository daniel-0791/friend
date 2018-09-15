package com.sanlingyi.android.photo.lib.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.sanlingyi.android.photo.lib.base.PhotoFilePathBase;
/**
 * 通过内容提供者获取图片文件夹路径
 * @author g
 *范晓锋
 */
public class GetPhotoFilePathUtil {
	private Context mContext;
	private ContentResolver  mContentResolver;
	public GetPhotoFilePathUtil(Context mContext) {
		super();
		this.mContext = mContext;
		mContentResolver=mContext.getContentResolver();
	}
	/**
	 * 获取图片文件路径
	 * @return
	 */
	public List<PhotoFilePathBase> getFilePath(){
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			ToastUtil.showNewToast(mContext, "暂无外部存储");
			return new ArrayList<PhotoFilePathBase>();
		}
		List<PhotoFilePathBase> mList = new ArrayList<PhotoFilePathBase>();
		Uri mImageUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String mMStore=MediaStore.Images.Media.MIME_TYPE;
		String mMData=MediaStore.Images.Media.DATE_MODIFIED;
		String mMODIFIED=MediaStore.Images.Media.DATA;
		String [] mType={"image/png","image/jpg","image/jpeg"};
		
		Cursor  mCursor=mContentResolver.query(mImageUri, new String []{mMODIFIED}, mMStore+"=?or "+mMStore+"=?or "+mMStore+"=?", mType, mMData);
		
		if (mCursor != null) {
			if (mCursor.moveToLast()) {
				HashSet<String> cachePath = new HashSet<String>();
				while (true) {
					String allImagepath = mCursor.getString(0);
					File parentFile = null;
					if (!TextUtils.isEmpty(allImagepath)) {
						parentFile = new File(allImagepath).getParentFile();
					}
					if (parentFile==null || !parentFile.exists() || !parentFile.isDirectory()) {
						if (!mCursor.moveToPrevious()) {
							break;
						}
						continue;
					}
					String mFilePath = parentFile.getAbsolutePath();
					if (!cachePath.contains(mFilePath)) {
						String tmpImagePath = getFirstImagePath(parentFile);
						int tmpCount = getImageCount(parentFile);

						if (!TextUtils.isEmpty(tmpImagePath) && tmpCount > 0) {
							mList.add(new PhotoFilePathBase(mFilePath, tmpImagePath, tmpCount));
							cachePath.add(mFilePath);
						}
					}
					if (!mCursor.moveToPrevious()) {
						break;
					}
				}
			}
			mCursor.close();
		}
		return mList;
	}
	
	/**
     * 获取目录中图片的个数。
     */
	private int getImageCount(File folder) {
		if(folder == null)
			return 0;
		
        int count = 0;
        File[] files = folder.listFiles();
        if(files!=null && files.length>0) {
        	
        	for (File file : files) {
                if (isImage(file.getName())) {
                    count++;
                }
            }
        }
        
        return count;
    }
	
    /**判断该文件是否是一个图片。*/
    public static boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }
	
    /**
     * 获取目录中最新的一张图片的绝对路径。
     */
	private String getFirstImagePath(File folder) {
		if(folder == null)
			return null;
		
        File[] files = folder.listFiles();
        if(files!=null && files.length>0) {
        	for (int i = files.length - 1; i >= 0; i--) {
                File file = files[i];
                if (isImage(file.getName())) {
                    return file.getAbsolutePath();
                }
            }
        }
        return null;
    }
	
}
