package denisov.ru.GUI;

import java.awt.*;

public class AxiliusJButton {

    private String name;
    private int Plain =0;
    private String title;
    private int size;
    private Dimension minsize = new Dimension(200,80);
    private Dimension maxsize = new Dimension(200,100);

    public AxiliusJButton(String name,int Plain,String title,int size)
    {
        this.name = name;
        this.Plain = Plain;
        this.title = title;
        this.size = size;
    }

    public AxiliusJButton(String name,int Plain,String title,int size,Dimension minsize,Dimension maxsize)
    {
        this.name = name;
        this.Plain = Plain;
        this.title = title;
        this.size = size;
        this.minsize = minsize;
        this.maxsize = maxsize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Font getFont() {
        return new Font(title,Plain,size);
    }

    public void setFont(String title,int Plain,int size) {
        this.Plain = Plain;
        this.title = title;
        this.size = size;
    }

    public Dimension getMinsize() {
        return minsize;
    }

    public void setMinsize(Dimension minsize) {
        this.minsize = minsize;
    }

    public Dimension getMaxsize() {
        return maxsize;
    }

    public void setMaxsize(Dimension maxsize) {
        this.maxsize = maxsize;
    }
}
