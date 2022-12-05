package com.example.courseproject;

public class ChipModel {

    private String title;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ChipModel(String title, boolean isChecked) {
        this.title = title;
        this.isChecked=isChecked;
    }
}
