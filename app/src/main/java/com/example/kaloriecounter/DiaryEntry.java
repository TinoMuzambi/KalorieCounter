package com.example.kaloriecounter;

import java.text.DateFormat;
import java.util.Date;

class DiaryEntry {

    private int id, foodTotal, exerciseTotal, NKI;
    private String foodCategory, exerciseCategory;
    Date date;
    DateFormat dateFormat;

    public DiaryEntry(int foodTotal, int exerciseTotal, int NKI, String foodCategory, String exerciseCategory) {
        this.foodTotal = foodTotal;
        this.exerciseTotal = exerciseTotal;
        this.NKI = NKI;
        this.foodCategory = foodCategory;
        this.exerciseCategory = exerciseCategory;
        this.dateFormat = DateFormat.getDateInstance();
        this.date = new Date();
    }
}
