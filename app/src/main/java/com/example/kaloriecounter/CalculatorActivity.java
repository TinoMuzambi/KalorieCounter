package com.example.kaloriecounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.d;

public class CalculatorActivity extends AppCompatActivity {
    static int avg, sum, oldSum;
    Gson gson = new Gson();

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

        String pos = getIntent().getStringExtra("pos"); // Determining where activity was launched from.
        if (pos == null) {
            foodTotal.setText(getString(R.string.zero_string));
            exerciseTotal.setText(getString(R.string.zero_string));
            nettTotal.setText(getString(R.string.zero_string));
        }
        else {
            String entry = Diary.getDiaryEntries().get(Integer.valueOf(pos));
            DiaryEntry diaryEntry = gson.fromJson(entry, DiaryEntry.class);
            Spinner foodSpinner = findViewById(R.id.foodCategorySpinner);
            Spinner exerciseSpinner = findViewById(R.id.exerciseCategorySpinner);
            EditText foodEditText = findViewById(R.id.foodCalorieCountText);
            EditText exerciseEditText = findViewById(R.id.exerciseCalorieCountText);

            foodSpinner.setSelection(retrieveAllItems(foodSpinner).indexOf(diaryEntry.foodCategory));
            exerciseSpinner.setSelection(retrieveAllItems(exerciseSpinner).indexOf(diaryEntry.exerciseCategory));
            foodTotal.setText(String.valueOf(diaryEntry.foodTotal));
            exerciseTotal.setText(String.valueOf(diaryEntry.exerciseTotal));
            nettTotal.setText(String.valueOf(diaryEntry.NKI));
            oldSum = Integer.valueOf(nettTotal.getText().toString());
            foodEditText.setText(String.valueOf(diaryEntry.foodTotal));
            exerciseEditText.setText(String.valueOf(diaryEntry.exerciseTotal));
        }

        MainActivity.entryEditor = MainActivity.sharedPrefs.edit();
    }

    /**
     * Retrieve all items available in a spinner.
     * @param spinner the spinner's items needed to be retrieved.
     * @return a list of the spinner's items.
     */
    public List<String> retrieveAllItems(Spinner spinner) {
        Adapter adapter = spinner.getAdapter();
        int size = adapter.getCount();
        List<String> items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String item = (String) adapter.getItem(i);
            items.add(item);
        }
        return items;
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
            foodTotal.setText(foodEditText.getText().toString());
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
            exerciseTotal.setText(exerciseEditText.getText().toString());
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
                            String entryString = gson.toJson(diaryEntry);

                            String pos = getIntent().getStringExtra("pos");
                            if (pos == null) {
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
                            }
                            else {
                                Diary.getDiaryEntries().set(Integer.valueOf(pos), entryString);
                                Toast.makeText(getApplicationContext(), getString(R.string.updated_entry), Toast.LENGTH_SHORT).show();

                                sum -= oldSum;
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
