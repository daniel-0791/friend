package com.sanlingyi.android.photo.lib.util;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * 获取线程池
 * @author g
 *
 */
public class GetBitmapPoolUtil {
	private static GetBitmapPoolUtil mgBitmapPoolUtil;
	private Thread poolThread;
	private Semaphore initPoolThreadSemaphore=new Semaphore(1);
	private Handler poolExecuteHandler;
	private Semaphore poolThreadGetResourcesNumberSemaphore;
	private ExecutorService FixedSizePool;
	private LruCache<String, Bitmap> mLruCache;
	
	private LinkedList<Runnable> taskList;
	private Type  mType;
	
	private Handler uiHandler;
	
	private  int  threadCount=3;
	
/**
 * 调度方式
 * FIFO 先进先出
 * LIFO 后进先出
 * @author g
 *
 */
	public enum Type{
		FIFO,LIFO
	}
	/**
	 * 线程池单例及初始化
	 * @param threadCount
	 * @param type
	 * @return
	 */
	public static GetBitmapPoolUtil getInstance(Type type){
		if(mgBitmapPoolUtil==null){
			synchronized (GetBitmapPoolUtil.class) {
				if(mgBitmapPoolUtil==null){
					mgBitmapPoolUtil=new GetBitmapPoolUtil(type);
				}
			}
		}
		return mgBitmapPoolUtil;
	}

	private GetBitmapPoolUtil(Type type) {
		init(type);
	}	
	
	
	private void init( Type type){
		poolThread=new Thread(){
			@Override
			public void run() {
				try {
					initPoolThreadSemaphore.acquire();
					Looper.prepare();
					poolExecuteHandler=new Handler(){
						@Override
						public void handleMessage(Message msg) {
							try {
								FixedSizePool.execute(getTask());
								poolThreadGetResourcesNumberSemaphore.acquire();
							} catch (InterruptedException e) {
							}
						}
						
					};
					
					initPoolThreadSemaphore.release();
					Looper.loop();
				} catch (InterruptedException e) {
				}
			}
			
		};
		poolThread.start();
		FixedSizePool=Executors.newFixedThreadPool(threadCount);
		poolThreadGetResourcesNumberSemaphore=new Semaphore(threadCount);
		int maxMemory=(int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mLruCache=new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes()*value.getHeight();
			}
		};
		taskList=new LinkedList<Runnable>();
		 mType=type==null?Type.LIFO:type;
	}
	
	
	
	private synchronized Runnable getTask(){
		if(mType==Type.FIFO){
			return taskList.removeFirst();
		}else if(mType==Type.LIFO){
			return taskList.removeLast();
		}
		return null;
	}
	
	private synchronized void addTask(Runnable runnable){
		try {
			if(poolExecuteHandler==null)
			initPoolThreadSemaphore.acquire();
		} catch (Exception e) {
		}
		taskList.add(runnable);
		poolExecuteHandler.sendEmptyMessage(0x100);
	}
	
	/**
	 * 关闭线程池并清空缓存
	 */
	public void  poolStop(){
		if(FixedSizePool!=null&&FixedSizePool.isShutdown()){
			FixedSizePool.shutdownNow();
			if(mLruCache!=null&&mLruCache.size()>0){
				mLruCache.evictAll();
			}
		}
	}
	
	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 */
	private Bitmap getBitmapFromLruCache(String key)
	{
		return mLruCache.get(key);
	}
	
	/**
	 * 往LruCache中添加一张图片
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToLruCache(String key, Bitmap bitmap)
	{
		if (getBitmapFromLruCache(key) == null)//取出的图片为null则内存中不存在图片 添加进内存
		{
			if (bitmap != null)
				mLruCache.put(key, bitmap);
		}
	}
	
	/*************************************完成初始化工作**************************************/
	
	public void imageLoader(final String imagepath,final ImageView imageview){
		imageview.setTag(imagepath);
		if(uiHandler==null){
			uiHandler=new Handler(){
				@Override
				public void handleMessage(Message msg) {
					ImgBeanHolder mImgBeanHolder=(ImgBeanHolder) msg.obj;
					ImageView imageView = mImgBeanHolder.imageView;
					Bitmap bm = mImgBeanHolder.bitmap;
					String path = mImgBeanHolder.path;
					if (imageView.getTag().toString().equals(path))
					{
						imageView.setImageBitmap(bm);
					}
				}
			};
		}
		Bitmap	bm=getBitmapFromLruCache(imagepath);
		if(bm!=null){
			ImgBeanHolder holder = new ImgBeanHolder();
			holder.bitmap = bm;
			holder.imageView = imageview;
			holder.path = imagepath;
			Message message = Message.obtain();
			message.obj = holder;
			uiHandler.sendMessage(message);
		}else {
			addTask(new Runnable() {
				@Override
				public void run() {
					ImageSize imageSize = getImageViewWidth(imageview);
					int reqWidth = imageSize.width;
					int reqHeight = imageSize.height;
					Bitmap bm = decodeSampledBitmapFromResource(imagepath, reqWidth,//解析获得图片
							reqHeight);
					addBitmapToLruCache(imagepath, bm);
					
					ImgBeanHolder holder = new ImgBeanHolder();
					holder.bitmap = getBitmapFromLruCache(imagepath);//从内存中取出图片
					holder.imageView = imageview;
					holder.path = imagepath;
					Message message = Message.obtain();
					message.obj = holder;
					
					uiHandler.sendMessage(message);
					poolThreadGetResourcesNumberSemaphore.release();
				}
			});
			
			
		}
		
	}
	
	private class ImgBeanHolder
	{
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}
	private class ImageSize
	{
		int width;
		int height;
	}
	
	/**
	 * 根据ImageView获得适当的压缩的宽和高
	 * 
	 * @param imageView
	 * @return
	 */
	private ImageSize getImageViewWidth(ImageView imageView)
	{
		ImageSize imageSize = new ImageSize();
		final DisplayMetrics displayMetrics = imageView.getContext()//返回手机实际像素大小
				.getResources().getDisplayMetrics();
		final LayoutParams params = imageView.getLayoutParams();//获取控件参数

		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getWidth(); // Get actual image width
		if (width <= 0)
			width = params.width; // Get layout width parameter
	
		if (width <= 0)
			width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check  通过反射机制获取Imageview的最大宽度
																	// maxHeight
																	// parameter
		if (width <= 0)
			width = displayMetrics.widthPixels;//显示像素的绝对宽度
			
		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getHeight(); // Get actual image height
		if (height <= 0)
			height = params.height; // Get layout height parameter
			
		
		if (height <= 0)
			height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check 通过反射机制获取Imageview的最大高度
																	 // maxHeight
																	// parameter
		if (height <= 0)
			height = displayMetrics.heightPixels;
		
		imageSize.width = width;
		imageSize.height = height;
		
		return imageSize;

	}
	
	/**
	 * 反射获得ImageView设置的最大宽度和高度
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName)
	{
		int value = 0;
		try
		{
			/*Field 存储一个类的属性值（字段），getDeclaredField（）能够访问本类中定义的所有方法（反射机制）
			 * 返回一个Field对象该对象反映此 Class 对象所表示的类或接口的指定已声明字段。
			 */
			Field field = ImageView.class.getDeclaredField(fieldName);//获取Imageview的fieldName属性
			field.setAccessible(true);//打破封装
			int fieldValue = (Integer) field.get(object);//获取当前对象（object）的fieldName属性的value值
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;
			}
		} catch (Exception e)
		{
		}
		return value;
	}
	
	/**
	 * 根据计算的inSampleSize，得到压缩后图片
	 * 
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHeight)
	{
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);//解析图片
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,//获取图片压缩的比例
				reqHeight);
		
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);//再次解析图片

		return bitmap;
	}
	/**
	 * 计算inSampleSize，用于压缩图片
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight)
	{
		// 源图片的宽度
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight)
		{
			// 计算出实际宽度和目标宽度的比例
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}
	
}
