package com.example.kaloriecounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import static android.util.Log.d;

public class DiaryEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);
        Intent intent = getIntent();
        TextView text = findViewById(R.id.detailsTextView);
        Gson gson = new Gson();
        FloatingActionButton next = findViewById(R.id.nextButton);
        FloatingActionButton prev = findViewById(R.id.previousButton);
        try {
            d("Tino", intent.getStringExtra("entry_index"));
            int pos = Integer.valueOf(intent.getStringExtra("entry_index"));
            if (pos == Diary.getSize()) {
                next.setEnabled(false);
            }
            else {
                next.setEnabled(true);
            }
            if (pos == 0) {
                prev.setEnabled(false);
            }
            else {
                prev.setEnabled(true);
            }
            String entry = Diary.getDiaryEntries().get(pos);
            String entryString = gson.fromJson(entry, DiaryEntry.class).detailedToString();
            text.setText(entryString);
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception thrown!", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchCalculator(View view) {
        Intent calculator = new Intent(getApplicationContext(), CalculatorActivity.class);
        calculator.setFlags(calculator.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(calculator);
    }

    public void goHome(View view) {
        Intent home = new Intent(getApplicationContext(), MainActivity.class);
        home.setFlags(home.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(home);
    }

    public void nextEntry(View view) {

    }

    public void prevEntry(View view) {

    }
}
