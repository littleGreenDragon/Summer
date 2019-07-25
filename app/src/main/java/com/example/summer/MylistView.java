package com.example.summer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/11/10.
 */

public class MylistView extends ListView {
    public MylistView(Context context) {
        super(context);
    }

    public MylistView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MylistView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public MylistView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public void setOnItemClickListener( @Nullable AdapterView.OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设计一个较大的值和AT_MOST模式
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        // 调用原方法测量
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }
}