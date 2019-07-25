package com.example.summer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;
import model.AnotherItems;
import model.Color;


public class diary extends BaseActivity {
    private Button back;
    private Toolbar toolbar;
    private EmbossMaskFilter emboss;
    private BlurMaskFilter blur;
    private DrawView drawView;
    static int i=0,j=0;
    private BottomSheetBehavior behavior;
    private SegmentedGroup mSegmentedGroup;
    private MylistView mylistView;
    private ArrayList<Color> Colors =new ArrayList<>();
    private ArrayList<AnotherItems> anotherWidth=new ArrayList<>();
    private ColorAdapter colorAdapter;
    private WidthAdapter widthAdapter;
    private normalAdapter normalAdapter;
    private functionAdapter functionAdapter;
    private ArrayList<String> pen=new ArrayList<>();
    private ArrayList<String> function=new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        mSegmentedGroup = (SegmentedGroup)findViewById(R.id.segmented_group);
        mylistView=findViewById(R.id.mylistview);
        //初始化物品集合
        initNews();
        initAnotherNews();
        //建立一个适配器





        mSegmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio0 :
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        colorAdapter =new ColorAdapter(diary.this,R.layout.colors,Colors);
                        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Color n=Colors.get(position);
                                drawView.paint.setColor(n.getColorre());
                            }
                        });
                         mylistView.setAdapter(colorAdapter);
                        break;
                    case R.id.radio1 :
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        widthAdapter=new WidthAdapter(diary.this,R.layout.width,anotherWidth);
                        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                AnotherItems anotherItems=anotherWidth.get(position);
                                drawView.paint.setStrokeWidth(anotherItems.getStroke());
                            }
                        });
                        mylistView.setAdapter( widthAdapter);

                        break;
                    case R.id.radio2 :
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        normalAdapter=new normalAdapter(diary.this,R.layout.width,pen);
                        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String s=pen.get(position);
                                drawView.paint.setMaskFilter(s=="喷漆"?blur:emboss);
                            }
                        });
                        mylistView.setAdapter(normalAdapter);

                        break;
                    case R.id.radio3 :
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        functionAdapter=new functionAdapter(diary.this,R.layout.width,function);
                        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position)
                                {
                                    case 0:
                                        drawView.clear();		//橡皮檫
                                        break;
                                    case 1:
                                        drawView.nothing();		//清屏
                                        break;
                                    case 2:
                                    {
                                        j=0;
                                        drawView.save(i,j);i++;	//保存绘画到APP目录
                                        Intent intent=new Intent();
                                        intent.putExtra("temp","one");
                                        setResult(RESULT_OK, intent);
                                        finish();
                                        break;
                                    }
                                    case 3:
                                    {
                                        j=1;
                                        drawView.save(i,j);i++;
                                        Intent intent=new Intent();
                                        intent.putExtra("temp","one");
                                        setResult(RESULT_OK, intent);
                                        finish();
                                        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))//若是获取权限失败，提示手动获取
                                            Toast.makeText(diary.this, "获取权限失败，请手动授权", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                            }
                        });
                        mylistView.setAdapter(functionAdapter);
                        break;
                }
            }
        });
        back=findViewById(R.id.back);
        toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("temp","nothing");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        drawView=findViewById(R.id.drawView1);
        emboss=new EmbossMaskFilter(new float[]{1.5f,1.5f,1.5f},0.6f,6f,4.2f);
        blur=new BlurMaskFilter(8f,BlurMaskFilter.Blur.NORMAL);
        View nest=findViewById(R.id.nest);
        behavior=BottomSheetBehavior.from(nest);
        behavior.setPeekHeight(100);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
        this.getFilesDir();
    }

    public class ColorAdapter extends ArrayAdapter<Color> {
        private int resourceId;

        public ColorAdapter(@NonNull Context context, int textViewResourceId, ArrayList<Color> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Color color = getItem(position);
            View view;
            if(convertView==null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

            }else {
                view=convertView;
            }
            ImageView colorView =  view.findViewById(R.id.Color);
            colorView.setImageResource(color.getColo());
            return view;
        }
    }
    public class WidthAdapter extends ArrayAdapter<AnotherItems> {
        private int resourceId;

        public WidthAdapter(@NonNull Context context, int textViewResourceId, ArrayList<AnotherItems> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AnotherItems anotherItem = getItem(position);
            View view;
            if(convertView==null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            }else {
                view=convertView;
            }
            TextView textView =  view.findViewById(R.id.text);
            textView.setText(anotherItem.getTitle());
            return view;
        }
    }
    public class normalAdapter extends ArrayAdapter<String> {
        private int resourceId;

        public normalAdapter(@NonNull Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String s = getItem(position);
            View view;
            if(convertView==null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            }else {
                view=convertView;
            }
            TextView textView =  view.findViewById(R.id.text);
            textView.setText(s);
            return view;
        }
    }
    public class functionAdapter extends ArrayAdapter<String> {
        private int resourceId;

        public functionAdapter(@NonNull Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String s = getItem(position);
            View view;
            if(convertView==null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            }else {
                view=convertView;
            }
            TextView textView =  view.findViewById(R.id.text);
            textView.setText(s);
            return view;
        }
    }
    private void initNews()
    {
        Color news1=new Color(android.graphics.Color.RED,R.color.red);
        Colors.add(news1);
        Color news2=new Color(android.graphics.Color.GREEN,R.color.green);
        Colors.add(news2);
        Color news3=new Color(android.graphics.Color.GRAY,R.color.grey);
        Colors.add(news3);
        Color news4=new Color(android.graphics.Color.BLUE,R.color.blue);
        Colors.add(news4);
        Color news5=new Color(android.graphics.Color.YELLOW,R.color.yellow);
        Colors.add(news5);
        Color news6=new Color(android.graphics.Color.WHITE,R.color.tinywhite);
        Colors.add(news6);
        Color news7=new Color(android.graphics.Color.BLACK,R.color.black);
        Colors.add(news7);
    }
    private void initAnotherNews()
    {
        AnotherItems anotherItems1=new AnotherItems("一像素",1);
        anotherWidth.add(anotherItems1);
        AnotherItems anotherItems2=new AnotherItems("二像素",2);
        anotherWidth.add(anotherItems2);
        AnotherItems anotherItems3=new AnotherItems("三像素",3);
        anotherWidth.add(anotherItems3);
        AnotherItems anotherItems4=new AnotherItems("四像素",4);
        anotherWidth.add(anotherItems4);
        AnotherItems anotherItems5=new AnotherItems("五像素",5);
        anotherWidth.add(anotherItems5);
        AnotherItems anotherItems6=new AnotherItems("六像素",6);
        anotherWidth.add(anotherItems6);
        AnotherItems anotherItems7=new AnotherItems("七像素",7);
        anotherWidth.add(anotherItems7);
        String n="喷漆";
        pen.add(n);
        String n1="蜡笔";
        pen.add(n1);
        function.add("橡皮擦");
        function.add("清屏");
        function.add("保存");
        function.add("保存至相册");
    }

}
