package fragement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.summer.BaseFragment;
import com.example.summer.R;
import com.example.summer.diary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import model.Diary;

public class three extends BaseFragment {
    private Button button;
    private List<Diary> diaries = new ArrayList<Diary>();
    private static int all = 0;
    private DiaryAdapter adapter;
    private FrameLayout frameLayout;
    private static Bitmap bitmap;
    private diary diary;
//    private File currentParent;
//    private File[] currentFiles;
    private static  String temp;
//    private int flag=0;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        frameLayout = view.findViewById(R.id.frameLayout);
        isall();
        button = view.findViewById(R.id.begin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), diary.class);
                startActivityForResult(intent, 1);

            }
        });
//        bitmap=diary.getBitmap();
     recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryAdapter(diaries);

//        File root=new File(Environment.getExternalStorageDirectory().getPath());
//        if(root.exists())
//        {
//            currentParent=root;
//            currentFiles=root.listFiles();
//
//        }
        recyclerView.setAdapter(adapter);
        return view;

    }

    public static class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
        private Context mContext;
        private List<Diary> myDiary;

        public DiaryAdapter(List<Diary> diaries) {
            myDiary = diaries;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            ImageView imageView;
            TextView textView;

            public ViewHolder(View view) {
                super(view);
                cardView = (CardView) view;
                imageView = (ImageView) view.findViewById(R.id.picture);
                textView = (TextView) view.findViewById(R.id.text);
            }
        }

        @NonNull
        @Override
        public DiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (mContext == null) {
                mContext = viewGroup.getContext();
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.diary, viewGroup, false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull DiaryAdapter.ViewHolder viewHolder, int i) {
            Diary diary = myDiary.get(i);
            viewHolder.textView.setText(diary.getName());
            viewHolder.imageView.setImageResource(R.drawable.head);
//            Glide.with(mContext).load(diary.getImageId()).into(viewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return myDiary.size();
        }
    }

    public void isall() {
        if (all == 0) {
            frameLayout.setBackgroundResource(R.drawable.background);
        }
        else
        {
            frameLayout.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        Log.d("happy", "updateUI: happy");
        recyclerView.setAdapter(adapter);
        isall();
    }

    //    private String read()
//    {
//        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//        {
//            File sd=Environment.getExternalStorageDirectory();
//            try(
//                FileInputStream fis=new FileInputStream(sd.getCanonicalPath()+FILE_NAME);
//                BufferedReader br=new BufferedReader(new InputStreamReader(fis))){
//                    StringBuilder s=new StringBuilder();
//                    String line=null;
//                    while((line=br.readLine())!=null)
//                    {
//                        s.append(line);
//                    }
//                    return s.toString();
//                }catch (IOException e) {
//                e.printStackTrace();
//            }
//            }
//        return  null;
//        }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case 1:
//                if (resultCode == RESULT_OK) {
//                bitmap = data.getParcelableExtra("bitmap");
                temp=data.getStringExtra("temp");
                if(temp!="nothing")
                {
                    Diary newDiary=new Diary(temp,R.id.head);
                    diaries.add(newDiary);
                    all++;

                }
        }
    }
    }
