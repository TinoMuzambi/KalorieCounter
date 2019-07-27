package com.example.kaloriecounter;

import java.text.DateFormat;
import java.util.Date;

class DiaryEntry {

    private int id, foodTotal, exerciseTotal, NKI;
    private String foodCategory, exerciseCategory;
    private String date;

    public DiaryEntry(int foodTotal, int exerciseTotal, int NKI, String foodCategory, String exerciseCategory) {
        this.foodTotal = foodTotal;
        this.exerciseTotal = exerciseTotal;
        this.NKI = NKI;
        this.foodCategory = foodCategory;
        this.exerciseCategory = exerciseCategory;
        DateFormat dateFormat = DateFormat.getDateInstance();
        this.date = dateFormat.format(new Date());
    }

    Object mainToString() {
        return "Entry on " + date + "\nNKI: " + NKI;
    }
}
