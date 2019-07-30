package com.example.kaloriecounter;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import static android.util.Log.d;

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
            String entryString = gson.fromJson(entry, DiaryEntry.class).detailedToString();
            text.setText(entryString);
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, Resources.getSystem().getString(R.string.exception_thrown), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Launches the calculator activity.
     * @param view view.
     */
    public void launchCalculator(View view) {
        Intent calculator = new Intent(getApplicationContext(), CalculatorActivity.class);
        calculator.setFlags(calculator.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(calculator);
    }

    /**
     * Launches the overview activity.
     * @param view view.
     */
    public void goHome(View view) {
        Intent home = new Intent(getApplicationContext(), MainActivity.class);
        home.setFlags(home.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
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
            String entryString = gson.fromJson(entry, DiaryEntry.class).detailedToString();
            text.setText(entryString);
        }
        else {
            Toast.makeText(this, Resources.getSystem().getString(R.string.last_entry), Toast.LENGTH_SHORT).show();
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
            String entryString = gson.fromJson(entry, DiaryEntry.class).detailedToString();
            text.setText(entryString);
        }
        else {
            Toast.makeText(this, Resources.getSystem().getString(R.string.first_entry), Toast.LENGTH_SHORT).show();
        }
    }
}
