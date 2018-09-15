package com.example.friendcircle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * �Զ���GridView 
 * @author Administrator
 *
 */
public class MyGridView extends GridView {

	public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * ����view�ķ������������㵱ǰ��ͼ�Ŀ�͸�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 //�˴��Ǵ���Ĺؼ� �������Լ�������ͼ�ĸ߶�
        //MeasureSpec.AT_MOST����˼����wrap_content  
        //Integer.MAX_VALUE >> 2 ��ʹ�����ֵ����˼,Ҳ�ͱ�ʾ���ޱ߽�ģʽ  
        //Integer.MAX_VALUE >> 2 �˴���ʾ�Ǹ������ܹ������ṩ�Ĵ�С  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec); 
	}
	

}
