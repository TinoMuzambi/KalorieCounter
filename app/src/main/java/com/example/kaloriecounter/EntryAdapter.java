package com.example.kaloriecounter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import static android.util.Log.d;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {
    private Context parentContext;

    @NonNull
    @Override
    public EntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);

        View contactView = inflater.inflate(R.layout.entry_row, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryAdapter.ViewHolder holder, int position) {
        String entry = Diary.getDiaryEntries().get(position); //Extract relevant data.
        Gson gson = new Gson();
        DiaryEntry diaryEntry = gson.fromJson(entry, DiaryEntry.class);
        String entryString = diaryEntry.mainToString();

        TextView entryRowTextView = holder.entryItem;
        entryRowTextView.setText(entryString); //This is where I choose.
    }

    @Override
    public int getItemCount() {
        return Diary.getSize();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView entryItem;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            entryItem = itemView.findViewById(R.id.entryItemText);
            entryItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewEntry = new Intent(parentContext.getApplicationContext(), DiaryEntryActivity.class);
                    viewEntry.setFlags(viewEntry.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    int pos = getAdapterPosition();

                    viewEntry.putExtra("entry_index", String.valueOf(pos));
                    parentContext.startActivity(viewEntry);
                }
            });
        }
    }
}
