package com.example.kaloriecounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import static android.util.Log.d;

public class CalculatorActivity extends AppCompatActivity {
    static int avg;
    static int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialising text views.
        TextView foodTotal = findViewById(R.id.foodNumericTotalTextView);
        TextView exerciseTotal = findViewById(R.id.exerciseNumericTotalTextView);
        TextView nettTotal = findViewById(R.id.nettNumericTotalTextView);
        foodTotal.setText("0");
        exerciseTotal.setText("0");
        nettTotal.setText("0");

        MainActivity.entryEditor = MainActivity.sharedPrefs.edit();

    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Update the food total.
     * @param view view.
     */
    public void updateFood(View view) {
        EditText foodEditText = findViewById(R.id.foodCalorieCountText);
        TextView foodTotal = findViewById(R.id.foodNumericTotalTextView);
        TextView exerciseTotal = findViewById(R.id.exerciseNumericTotalTextView);
        TextView nettTotal = findViewById(R.id.nettNumericTotalTextView);

        try {
            int foodSum = Integer.valueOf(foodTotal.getText().toString()) + Integer.valueOf(foodEditText.getText().toString());
            foodTotal.setText(String.valueOf(foodSum));
        }
        catch(Exception e) {
            d("Tino", e.getMessage());
        }
        int nettSum = Integer.valueOf(foodTotal.getText().toString()) - Integer.valueOf(exerciseTotal.getText().toString());
        if (nettSum > 0) {
            nettTotal.setText(String.valueOf(nettSum));
        }
        else {
            nettTotal.setText("0");
        }
    }

    /**
     * Update the exercise total.
     * @param view view.
     */
    public void updateExercise(View view) {
        EditText exerciseEditText = findViewById(R.id.exerciseCalorieCountText);
        TextView foodTotal = findViewById(R.id.foodNumericTotalTextView);
        TextView exerciseTotal = findViewById(R.id.exerciseNumericTotalTextView);
        TextView nettTotal = findViewById(R.id.nettNumericTotalTextView);

        try {
            int exerciseSum = Integer.valueOf(exerciseTotal.getText().toString()) + Integer.valueOf(exerciseEditText.getText().toString());
            exerciseTotal.setText(String.valueOf(exerciseSum));
        }
        catch(Exception e){
            d("Tino", e.getMessage());
        }
        int nettSum = Integer.valueOf(foodTotal.getText().toString()) - Integer.valueOf(exerciseTotal.getText().toString());
        if (nettSum > 0) {
            nettTotal.setText(String.valueOf(nettSum));
        }
        else {
            nettTotal.setText("0");
        }
    }

    /**
     * Save an entry to the Diary.
     * @param view view.
     */
    public void saveEntry(View view) {
        Spinner foodType = findViewById(R.id.foodCategorySpinner);
        Spinner exerciseType = findViewById(R.id.exerciseCategorySpinner);
        TextView foodTotal = findViewById(R.id.foodNumericTotalTextView);
        TextView exerciseTotal = findViewById(R.id.exerciseNumericTotalTextView);
        TextView nettTotal = findViewById(R.id.nettNumericTotalTextView);

        DiaryEntry diaryEntry = new DiaryEntry(Integer.valueOf(foodTotal.getText().toString()),
                Integer.valueOf(exerciseTotal.getText().toString()),
                Integer.valueOf(nettTotal.getText().toString()),
                foodType.getSelectedItem().toString(),
                exerciseType.getSelectedItem().toString());
        Gson gson = new Gson();
        String entryString = gson.toJson(diaryEntry);
        Diary.addEntry(entryString);

        Toast.makeText(this, "Added Entry!", Toast.LENGTH_SHORT).show();

        sum += Integer.valueOf(nettTotal.getText().toString());
        try {
            avg = sum / Diary.getSize();
            MainActivity.entryEditor.putInt("avg", avg);
            MainActivity.entryEditor.apply();
        }
        catch (Exception e) {
            avg = 0;
            MainActivity.entryEditor.putInt("avg", avg);
            MainActivity.entryEditor.apply();
        }

        Intent viewEntry = new Intent(getApplicationContext(), DiaryEntryActivity.class);
        viewEntry.putExtra("entry_index", String.valueOf(Diary.getDiaryEntries().indexOf(entryString)));
        startActivity(viewEntry);

        finish();
    }

}
