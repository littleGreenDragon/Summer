package model;

import com.example.summer.R;

public class Diary
{
    private String name;
    private int imageId;
    public Diary()
    {
        name="Demo";
        imageId= R.drawable.head;
    }
    public Diary(String name,int imageId)
    {
        this.name=name;
        this.imageId= imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
