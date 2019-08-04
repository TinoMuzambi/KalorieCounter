package com.example.kaloriecounter;

import android.content.Context;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;


class DiaryEntry {

    int foodTotal, exerciseTotal, NKI;
    String foodCategory, exerciseCategory;
    private String date;
    private static final HashMap<String, String> foods = new HashMap<>();
    private static final HashMap<String, String> exercises = new HashMap<>();

    static {
        foods.put("Breakfast", "breakfast");
        foods.put("Lunch", "lunch");
        foods.put("Dinner", "dinner");
        foods.put("Snack", "a snack");
        foods.put("Dessert", "dessert");
    }

    static {
        exercises.put("Tennis", "for a tennis session");
        exercises.put("Squash", "for a squash session");
        exercises.put("Swimming", "swimming");
        exercises.put("Dance", "dancing");
        exercises.put("Soccer", "for a soccer game");
        exercises.put("Jogging", "jogging");
        exercises.put("Gym", "to the gym");
    }

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
    String mainToString(Context context) {
        return context.getResources().getString(R.string.entry_on) + date +
                "\n" + context.getResources().getString(R.string.nki) + NKI;
    }

    /**
     * toString for DiaryEntryActivity activity.
     * @return String containing more comprehensive information for the Diary activity.
     */
    String detailedToString(Context context) {
        return context.getResources().getString(R.string.entry_on) + date + "\n\n" +
                context.getResources().getString(R.string.you_had) +
                foods.get(foodCategory) + context.getResources().getString(R.string.which_added)
                + foodTotal + context.getResources().getString(R.string.kilojoules)
                + context.getResources().getString(R.string.then_you)
                + exercises.get(exerciseCategory) + context.getResources().getString(R.string.which_burned)
                + exerciseTotal + context.getResources().getString(R.string.kilojoules)
                + context.getResources().getString(R.string.leaving_you) + NKI + context.getResources().getString(R.string.kilojoules);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
