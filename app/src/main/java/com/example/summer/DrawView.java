package com.example.summer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class DrawView extends View {
    private float preX;
    private float perY;
    private int Width;
    private int Height;
    private Path path=new Path();
     public Paint paint=new Paint(Paint.DITHER_FLAG);
    private Bitmap cacheBitmap;
    private Canvas cacheCanvas=new Canvas();
    private Paint bmpPaint=new Paint();


    public DrawView(Context context, AttributeSet set) {
        super(context, set);
        Width= context.getResources().getDisplayMetrics().widthPixels; // 获取屏幕的宽度
        Height= context.getResources().getDisplayMetrics().heightPixels; // 获取屏幕的高度
        System.out.println(Width + "*" + Height);
        // 创建一个与该View相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(Width, Height,
                Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();
        cacheCanvas.setBitmap(cacheBitmap);// 在cacheCanvas上绘制cacheBitmap
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.GREEN); // 设置默认的画笔颜色
        // 设置画笔风格
        paint.setStyle(Paint.Style.STROKE);	//设置填充方式为描边
        paint.setStrokeJoin(Paint.Join.ROUND);		//设置笔刷的图形样式
        paint.setStrokeCap(Paint.Cap.ROUND);	//设置画笔转弯处的连接风格
        paint.setStrokeWidth(5); // 设置默认笔触的宽度为5像素
        paint.setAntiAlias(true); // 使用抗锯齿功能
        paint.setDither(true); // 使用抖动效果
    }
    DrawView(Context context,int width,int height) {
        super(context);
        cacheBitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Width=width;
        Height=height;
        cacheCanvas.setBitmap(cacheBitmap);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x=event.getX();
        float y=event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                preX=x;
                perY=y;
                break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x,y);
                    perY=y;
                    preX=x;
                    break;
                    case MotionEvent.ACTION_UP:
                        cacheCanvas.drawPath(path,paint);
                        path.reset();
                        break;

        }
        invalidate();
        return true;
    }
    public void save(int i,int j) {

        try {
            saveBitmap("DrawingPicture","DrawingPicture_"+i,j);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(cacheBitmap,0f,0f,bmpPaint);
        canvas.drawPath(path,paint);
        canvas.save();
        canvas.restore();
    }
    public void clear() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(50);	//设置笔触的宽度
//        invalidate();
    }


    public void back() {
        paint.setXfermode(null);
    }
    public void nothing() {

        cacheBitmap=null;
        // 创建一个与该View相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(Width,Height,
                Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);// 在cacheCanvas上绘制cacheBitmap
        invalidate();
    }


    // 保存绘制好的位图到APP目录下
    public void saveBitmap(String filesize,String fileName,int j) throws IOException {

        String directoryPath;
        directoryPath=getFilePath(getContext(),filesize,fileName,j);
        //directoryPath=getFilePath(getContext(),filesize,fileName,j);
        File file = new File(directoryPath );	//创建文件对象
        try {
            file.createNewFile();	//创建一个新文件
            FileOutputStream fileOS = new FileOutputStream(file);	//创建一个文件输出流对象
            cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);	//将绘图内容压缩为PNG格式输出到输出流对象中
            fileOS.flush();	//将缓冲区中的数据全部写出到输出流中
            fileOS.close();	//关闭文件输出流对象
            Toast.makeText(getContext(), "成功保存到"+directoryPath, Toast.LENGTH_LONG).show();//垴村成功，提示保存的路径
        }catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();//保存失败，提示原因
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        getContext().sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦

    }

    public static String getFilePath(Context context,String filesize,String dir,int j) {  //获取APP当前目录并且设置图片保存路径
        String directoryPath="";
        if(j==0) {
            //判断SD卡是否可用
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {
                directoryPath =context.getExternalFilesDir(filesize).getAbsolutePath()+File.separator+ dir + ".png";

                // directoryPath =context.getExternalCacheDir().getAbsolutePath() ;
            }else{
                //没内存卡就存机身内存
                directoryPath=context.getFilesDir().getAbsolutePath()
                        +File.separator
                        +filesize+File.separator
                        + dir + ".png";

                // directoryPath=context.getCacheDir()+File.separator+dir;
            }}

        else
        {
            directoryPath =Environment.getExternalStorageDirectory()
                    + File.separator +
                    Environment.DIRECTORY_DCIM
                    +File.separator+"Camera"+File.separator+dir + ".png";
            	File file = new File(directoryPath);
            if(!file.exists()){//判断文件目录是否存在
            	file.mkdirs();
            directoryPath=directoryPath+File.separator+dir + ".png";
        }
//        LogUtil.i("filePath====>"+directoryPath);
        }
        return directoryPath;
    }

}

