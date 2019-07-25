package com.example.summer;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.lang.reflect.Method;
import de.hdodenhof.circleimageview.CircleImageView;
import fragement.four;
import fragement.one;
import fragement.three;
import fragement.two;
import jp.wasabeef.glide.transformations.BlurTransformation;
import model.people;
import static com.example.summer.R.*;

public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;//带有滑动菜单的布局
    private people user=new people();//一个people类的对象
    private Animation animation;//普通动画
    private Button[] button;//底部四个按钮
    private TextView[] textViews;//底部四个按钮的名字
    private static int[] flag=new int[]{1,0,0,0};//用于记录是那个界面占据主界面，默认是运动界面，即第一个界面
    private int temp=0;//用于记录上一个按下的按钮
    private int[] res;//底下四个按钮的图片资源
    private TextView textView;//toolbar顶上那个字的显示
    private  Toolbar toolbar;

    /**
    *让toolbar的溢出菜单有图片加文字
     **/
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //如果溢出菜单存在
        if (menu != null) {
            //
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        toolbar=findViewById(id.toolBar);
        textView=findViewById(id.what);
        drawerLayout=findViewById(id.drawerLayout);
        NavigationView navigationView=findViewById(id.nav);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(drawable.add));
        ActionBar actionBar=getSupportActionBar();
        View view2=navigationView.getHeaderView(0);
        //设置背景模糊
        ImageView imageViewtwo=view2.findViewById(id.personalBackground);
        ImageView imageViewthree=view2.findViewById(id.head);
        imageViewthree.setImageResource(user.getHead());
        Glide.with(MainActivity.this)
                    .load(user.getHead())
                    .bitmapTransform(new BlurTransformation(MainActivity.this,20,1))
                    .into(imageViewtwo);
        imageViewthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,personal.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("temp", user);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        //设置用户名
        TextView textView2=view2.findViewById(id.name);
        textView2.setText(user.getName());




        //改变标题栏的颜色
        getWindow().setStatusBarColor(Color.TRANSPARENT) ;

        getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                );


        if(actionBar!=null)
        {
            CircleImageView circleImageView=findViewById(id.showYourHead);
            circleImageView.setImageResource(user.getHead());
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });

        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case id.member:break;
                    case id.star:break;
                    case id.history:break;
                    case id.money:break;
                    case id.theme:break;
                }
                return true;
            }
        });

        res=new int[]{drawable.sport,drawable.sport2,drawable.find,drawable.find2,drawable.aim,drawable.aim2,drawable.statistic,drawable.statistic2};
        button=new Button[]{findViewById(id.one),findViewById(id.two),findViewById(id.three),findViewById(id.four)};
        textViews=new TextView[]{findViewById(id.on),findViewById(id.tw),findViewById(id.th),findViewById(id.fo)};
        scale(button[0],0,new one());
        scale(button[1],1,new two());
        scale(button[2],2,new three());
        scale(button[3],3,new four());
        button[0].setBackgroundResource(res[1]);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id.change, new one()).commit();
        textView.setText(textViews[0].getText());
    }
    public void scale(final Button Anotherbutton, final int i, final Fragment AnotherFragment)
    {
        Anotherbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation= AnimationUtils.loadAnimation(MainActivity.this, anim.myanimtwo);
                Anotherbutton.startAnimation(animation);
                textViews[i].startAnimation(animation);
                textView.setText(textViews[i].getText());
                if(flag[i]==0)
                {
                    Anotherbutton.setBackgroundResource(res[i*2+1]);
                    button[temp].setBackgroundResource(res[temp*2]);
                    flag[temp]=0;
                    temp=i;
                    flag[i]=1;
                }
                if(i!=0)
                {
                    //让溢出菜单值在第一个界面 运动界面展示
                }
                else {
//                    toolbar.showOverflowMenu();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(id.change, AnotherFragment).commit();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case id.scan:break;
            case id.search:break;
            case id.set:break;
            default:break;
        }
        return true;
    }
}
