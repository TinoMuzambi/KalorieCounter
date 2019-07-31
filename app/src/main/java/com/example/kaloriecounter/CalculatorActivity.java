package com.example.kaloriecounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
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
        foodTotal.setText(getString(R.string.zero_string));
        exerciseTotal.setText(getString(R.string.zero_string));
        nettTotal.setText(getString(R.string.zero_string));

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
            nettTotal.setText(getString(R.string.zero_string));
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
            nettTotal.setText(getString(R.string.zero_string));
        }
    }

    /**
     * Save an entry to the Diary.
     * @param view view.
     */
    public void saveEntry(View view) {
        EditText foodEditText = findViewById(R.id.foodCalorieCountText);
        EditText exerciseEditText = findViewById(R.id.exerciseCalorieCountText);
        if ((!(foodEditText.getText().toString().equals(""))) && ((!(exerciseEditText.getText().toString().equals(""))))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(getString(R.string.save_entry_button));
            builder.setMessage(getString(R.string.confirm_save));
            builder.setPositiveButton(getString(R.string.yes_save),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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

                            Toast.makeText(getApplicationContext(), getString(R.string.added_entry), Toast.LENGTH_SHORT).show();

                            sum += Integer.valueOf(nettTotal.getText().toString());
                            try {
                                avg = sum / Diary.getSize();
                                MainActivity.entryEditor.putString("avg", String.valueOf(avg));
                                MainActivity.entryEditor.apply();
                            } catch (NumberFormatException e) {
                                avg = 0;
                                MainActivity.entryEditor.putString("avg", String.valueOf(avg));
                                MainActivity.entryEditor.apply();
                            }

                            Intent viewEntry = new Intent(getApplicationContext(), DiaryEntryActivity.class);
                            viewEntry.setFlags(viewEntry.getFlags() | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            viewEntry.putExtra("entry_index", String.valueOf(Diary.getDiaryEntries().indexOf(entryString)));
                            startActivity(viewEntry);

                            finish();
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            Snackbar.make(getCurrentFocus(), getString(R.string.warning_text), Snackbar.LENGTH_LONG).show();
        }
    }

}
