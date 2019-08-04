package com.example.kaloriecounter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class DiaryEntryActivity extends AppCompatActivity {

    TextView text;
    int pos;
    FloatingActionButton next, prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);
        Intent intent = getIntent();
        text = findViewById(R.id.detailsTextView);
        Gson gson = new Gson();
        next = findViewById(R.id.nextButton);
        prev = findViewById(R.id.previousButton);

        try {
            pos = Integer.valueOf(intent.getStringExtra("entry_index"));

            String entry = Diary.getDiaryEntries().get(pos);
            DiaryEntry diaryEntry = gson.fromJson(entry, DiaryEntry.class);
            String entryString = diaryEntry.detailedToString(getApplicationContext());
            text.setText(entryString, TextView.BufferType.SPANNABLE);
            setColours(text, entryString);
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this,     getApplicationContext().getResources().getString(R.string.exception_thrown), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets colours of the calorie counts.
     * @param text the textview whose colours are being manipulated.
     * @param entryString the text being set to the textview.
     */
    private void setColours(TextView text, String entryString) {
        Spannable foodColour = (Spannable) text.getText();
        int start = entryString.indexOf(getString(R.string.to_strict)) + getString(R.string.to_strict).length() + 1;
        int end = entryString.indexOf(getString(R.string.kilojoules_strict));
        foodColour.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int start2 = entryString.indexOf(getString(R.string.burned_strict)) + getString(R.string.burned_strict).length() + 1;
        int end2 = entryString.indexOf(getString(R.string.kilojoules_strict), end + 1);
        foodColour.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = entryString.indexOf(getString(R.string.of_strict)) + getString(R.string.of_strict).length() + 1;
        end = entryString.length() - getString(R.string.kilojoules_strict).length() - 1;
        foodColour.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * Launches the calculator activity.
     * @param view view.
     */
    public void launchCalculator(View view) {
        Intent calculator = new Intent(getApplicationContext(), CalculatorActivity.class);
        Gson gson = new Gson();
        String entry = Diary.getDiaryEntries().get(pos);
        String entryString = gson.fromJson(entry, DiaryEntry.class).detailedToString(getApplicationContext());
        calculator.putExtra("entry", entryString);
        calculator.putExtra("pos", String.valueOf(pos));
        calculator.setFlags(calculator.getFlags() | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(calculator);
    }

    /**
     * Launches the overview activity.
     * @param view view.
     */
    public void goHome(View view) {
        Intent home = new Intent(getApplicationContext(), MainActivity.class);
        home.setFlags(home.getFlags() | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(home);
    }

    /**
     * Proceeds to the next entry in the diary. Displays a message if this isn't possible.
     * @param view view.
     */
    public void nextEntry(View view) {
        Gson gson = new Gson();
        if (pos < Diary.getSize() - 1) {
            pos++;
            String entry = Diary.getDiaryEntries().get(pos);
            String entryString = gson.fromJson(entry, DiaryEntry.class).detailedToString(getApplicationContext());
            text.setText(entryString);
            setColours(text, entryString);
        }
        else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.last_entry), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Proceeds to the previous entry in the diary. Displays a message if this isn't possible.
     * @param view view.
     */
    public void prevEntry(View view) {
        Gson gson = new Gson();
        if (pos > 0) {
            pos--;
            String entry = Diary.getDiaryEntries().get(pos);
            String entryString = gson.fromJson(entry, DiaryEntry.class).detailedToString(getApplicationContext());
            text.setText(entryString);
            setColours(text, entryString);
        }
        else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.first_entry), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
