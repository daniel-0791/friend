package com.sanlingyi.android.photo.lib.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.util.SparseBooleanArray;

public class PhotoGlobalVariable {
	/**
	 * 存放选中的图片路径
	 */
	public static List<String> mList =new LinkedList<String>();
	/**
	 * 存放选中图片文件集合
	 */
	public static List<String> selectFile=new ArrayList<String>();
	/**
	 * 存放checkbox选中的位置及状态
	 */
	public static Map<String, SparseBooleanArray> mSelectState=new HashMap<String, SparseBooleanArray>();
	/**
	 * 选中图片的数量
	 */
	public static int   SELECT_IMAGE_NUMBER=0;

}
