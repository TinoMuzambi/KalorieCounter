package com.example.kaloriecounter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.util.Log.d;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView foodTotal = findViewById(R.id.foodNumericTotalTextView);
        TextView exerciseTotal = findViewById(R.id.exerciseNumericTotalTextView);
        TextView nettTotal = findViewById(R.id.nettNumericTotalTextView);

        foodTotal.setText("0");
        exerciseTotal.setText("0");
        nettTotal.setText("0");
    }

    // This and the onSaveInstanceState method below handle persisting state when the activity
    // is closed suddenly or by the user
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
    }

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

    public void saveEntry(View view) {
        EditText foodEditText = findViewById(R.id.foodCalorieCountText);
        EditText exerciseEditText = findViewById(R.id.exerciseCalorieCountText);
        Spinner foodType = findViewById(R.id.foodCategorySpinner);
        Spinner exerciseType = findViewById(R.id.exerciseCategorySpinner);
        TextView foodTotal = findViewById(R.id.foodNumericTotalTextView);
        TextView exerciseTotal = findViewById(R.id.exerciseNumericTotalTextView);
        TextView nettTotal = findViewById(R.id.nettNumericTotalTextView);

//        String entryText = "TODO";
        DiaryEntry diaryEntry = new DiaryEntry(Integer.valueOf(foodTotal.getText().toString()),
                Integer.valueOf(exerciseTotal.getText().toString()),
                Integer.valueOf(nettTotal.getText().toString()),
                foodType.getSelectedItem().toString(),
                exerciseType.getSelectedItem().toString());
        Diary.addEntry(diaryEntry);

        Toast.makeText(this, "Added Entry!", Toast.LENGTH_SHORT).show();

        Intent viewEntry = new Intent(getApplicationContext(), DiaryEntryActivity.class);
        viewEntry.putExtra("entry_index", Diary.getDiaryEntries().indexOf(diaryEntry));
        startActivity(viewEntry);

        finish();
    }

}
