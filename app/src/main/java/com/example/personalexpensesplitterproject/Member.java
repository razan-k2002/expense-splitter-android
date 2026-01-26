package com.example.personalexpensesplitterproject;

import java.io.Serializable;

public class Member implements Serializable {
    String name;
    int iconRes;
    public Member(String name, int iconRes){
        this.name=name;
        this.iconRes=iconRes;
    }
    public String getName() {
        return name;
    }
    public int getIcon() {
        return iconRes;
    }
}

