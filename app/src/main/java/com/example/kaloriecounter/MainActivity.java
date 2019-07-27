package com.example.kaloriecounter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import static android.util.Log.d;

public class MainActivity extends AppCompatActivity {
    public SharedPreferences.Editor entryEditor;
    public EntryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Retrieving existing notes
        SharedPreferences sharedPrefs = getSharedPreferences("diary_entries", Activity.MODE_PRIVATE);
        entryEditor = sharedPrefs.edit();

        // The brackets are a default value if the key notes can't be found
        String entriesJSONString = sharedPrefs.getString("entries", "[]");

        if (Diary.diaryEntries == null) {
            processJsonEntries(entriesJSONString);
        }

        RecyclerView itemListView = findViewById(R.id.itemTextView);
        adapter = new EntryAdapter();
        itemListView.setAdapter(adapter);
        itemListView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calculator = new Intent(getApplicationContext(), CalculatorActivity.class);
                startActivity(calculator);
            }
        });
    }

    private void processJsonEntries(String entriesJSONString) {
        JSONArray entriesJSON = null;
        d("Tino", entriesJSONString);

        try {
            entriesJSON = new JSONArray(entriesJSONString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < entriesJSON.length(); i++) {
            try {
                d("Tino", (String) entriesJSON.get(i));
                Diary.addEntry((String) entriesJSON.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveEntries() {
        JSONArray jsonEntryList = new JSONArray(Diary.getDiaryEntries());
        entryEditor.putString("entries", jsonEntryList.toString());
        entryEditor.apply();
    }

    public void clearEntries(View view) {
        Diary.clearEntries();
        Toast.makeText(this, "Cleared entries!", Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
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
