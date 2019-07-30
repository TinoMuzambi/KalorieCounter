package com.example.kaloriecounter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    static SharedPreferences sharedPrefs;
    public static SharedPreferences.Editor entryEditor;
    public EntryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Retrieving existing notes.
        new Thread(new Runnable() {
            @Override
            public void run() {
                sharedPrefs = getSharedPreferences("diary_entries", Activity.MODE_PRIVATE);
                entryEditor = sharedPrefs.edit();

                // Retrieving stored average NKI.
                TextView averageNKI = findViewById(R.id.averageValueTextView);
                int storedNKI = sharedPrefs.getInt("avg", 0);
                averageNKI.setText(String.valueOf(storedNKI));

                String entriesJSONString = sharedPrefs.getString("entries", "[]");
                if (Diary.diaryEntries == null) {
                    processJsonEntries(entriesJSONString);
                }

                CalculatorActivity.avg = storedNKI;
                CalculatorActivity.sum = storedNKI * Diary.getSize();
            }
        });

        // Setting up RecyclerView and adapter.
        RecyclerView itemListView = findViewById(R.id.itemTextView);
        adapter = new EntryAdapter();
        itemListView.setAdapter(adapter);
        itemListView.setLayoutManager(new LinearLayoutManager(this));
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
     * Process entries stored in sharedPrefs.
     * @param entriesJSONString JSON string containing existing entries.
     */
    private void processJsonEntries(String entriesJSONString) {
        JSONArray entriesJSON = null;

        try {
            entriesJSON = new JSONArray(entriesJSONString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < entriesJSON.length(); i++) {
            try {
                Diary.addEntry((String) entriesJSON.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Write entries out to sharedPrefs.
     */
    private void saveEntries() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonEntryList = new JSONArray(Diary.getDiaryEntries());
                entryEditor.putString("entries", jsonEntryList.toString());
                entryEditor.apply();
            }
        });
    }

    /**
     * Clear all existing entries from sharedPrefs.
     * @param view view.
     */
    public void clearEntries(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Diary.clearEntries();
                TextView averageNKI = findViewById(R.id.averageValueTextView);
                averageNKI.setText(getString(R.string.zero_string));
                CalculatorActivity.sum = 0;
                CalculatorActivity.avg = 0;
                adapter.notifyDataSetChanged();
            }
        });
        Toast.makeText(this, getString(R.string.clear_entries), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        saveEntries();
        super.finish();
    }

    @Override
    protected void onStart() {
        saveEntries();
        adapter.notifyDataSetChanged();
        TextView averageNKI = findViewById(R.id.averageValueTextView);
        averageNKI.setText(String.valueOf(sharedPrefs.getInt("avg", 0)));
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // This and the onSaveInstanceState method below handle persisting state when the activity
    // is closed suddenly or by the user
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
