package com.example.kaloriecounter;

import android.content.res.Resources;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;


class DiaryEntry {

    private int foodTotal, exerciseTotal, NKI;
    private String foodCategory, exerciseCategory;
    private String date;

    DiaryEntry(int foodTotal, int exerciseTotal, int NKI, String foodCategory, String exerciseCategory) {
        this.foodTotal = foodTotal;
        this.exerciseTotal = exerciseTotal;
        this.NKI = NKI;
        this.foodCategory = foodCategory;
        this.exerciseCategory = exerciseCategory;
        DateFormat dateFormat = DateFormat.getDateInstance();
        this.date = dateFormat.format(new Date());
    }

    /**
     * toString for main overview activity.
     * @return String containing information needed on the overview activity.
     */
    String mainToString() {
        return Resources.getSystem().getString(R.string.entry_on) + date +
                "\n" + Resources.getSystem().getString(R.string.nki) + NKI;
    }

    /**
     * toString for DiaryEntryActivity activity.
     * @return String containing more comprehensive information for the Diary activity.
     */
    String detailedToString() {
        return Resources.getSystem().getString(R.string.entry_on) + date + "\n\n" +
                Resources.getSystem().getString(R.string.food_label) + "\n" +
                foodCategory + ": " + foodTotal + "\n\n" +
                Resources.getSystem().getString(R.string.exercise_label) + "\n" +
                exerciseCategory + ": " + exerciseTotal +
                "\n\n" + Resources.getSystem().getString(R.string.nki) + NKI;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
