package model;

public class Color {

    private int colorre;
    private int colo;
    public  Color(int res,int colo)
    {
        colorre=res;
        this.colo=colo;
    }
    public int getColorre() {
        return colorre;
    }

    public int getColo() {
        return colo;
    }

    public void setColo(int colo) {
        this.colo = colo;
    }

    public void setColorre(int colorre) {
        this.colorre = colorre;
    }
}
