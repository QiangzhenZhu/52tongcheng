package com.example.mylibrary.photoListView;

/**
 * Created by 赵自强 on 2017/8/26 13:57.
 * 这个类的用处
 */

public class PictureDataBean {
    private int positon;
    private String path;
    private boolean selected = false;
    public PictureDataBean(int positon, String path) {
        this.positon = positon;
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
