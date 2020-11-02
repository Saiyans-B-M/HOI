package com.example.hoi;

import java.util.ArrayList;
import java.util.Collections;
public class sort {
    private ArrayList<String> arrayList;

    public sort(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public ArrayList<String> sortAscending() {
        Collections.sort(this.arrayList);
        return this.arrayList;
    }

    public ArrayList<String> sortDecending() {
        Collections.reverse(this.arrayList);
        return this.arrayList;
    }

}
