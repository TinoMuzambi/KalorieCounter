package com.example.kaloriecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import static android.util.Log.d;

public class DiaryEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);

        TextView text = findViewById(R.id.detailsTextView);
        try {
            if (Diary.getSize() != 0) {
                text.setText(Diary.getDiaryEntries().get((Integer) savedInstanceState.get("entry_index")));
            }
            else {
                text.setText(R.string.no_entries);
            }
        }
        catch (Exception e) {
            d("Tino", e.getLocalizedMessage());
        }
    }
}
