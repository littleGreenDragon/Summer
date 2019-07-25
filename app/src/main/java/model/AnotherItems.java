package model;

public class AnotherItems {
    private String title;
    private int stroke;
  public   AnotherItems(String title,int stroke)
    {
        this.title=title;
        this.stroke=stroke;
    }

    public int getStroke() {
        return stroke;
    }

    public String getTitle() {
        return title;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
