package com.example.summer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import model.people;


public class personal extends AppCompatActivity {
    public static final int TAKE_PHOTO=1;
    private  people user;
    private Uri imageUri;
    private ImageView imageViewtwo,imageViewthree;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        setContentView(R.layout.activity_personal);
        user=(people) getIntent().getSerializableExtra("temp");
        imageViewtwo=findViewById(R.id.personalBackground);
        imageViewthree=findViewById(R.id.head);
        mohu(imageViewtwo);
        imageViewthree.setImageResource(user.getHead());
        imageViewthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists())
                    {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imageUri= FileProvider.getUriForFile(personal.this,"com.example.summer.fileprovider",outputImage);
                }else {
                    imageUri=Uri.fromFile(outputImage);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                    try {
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                        imageViewthree.setImageBitmap(bitmap);
                        imageViewtwo.setImageBitmap(bitmap);
//                        Drawable drawView=new BitmapDrawable(bitmap);
//                        user.setHead;
//                        mohu(imageViewtwo);
                    }catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                break;
                default:
                    break;
        }
    }

    public void mohu( ImageView two)
    {
        Glide.with(personal.this)
                .load(user.getHead())
                .bitmapTransform(new BlurTransformation(personal.this,20,1))
                .into(two);
    }

}
