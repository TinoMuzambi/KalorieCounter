package com.example.kaloriecounter;

import java.util.ArrayList;

public class Diary {

    static ArrayList<DiaryEntry> diaryEntries;

    static ArrayList<DiaryEntry> getDiaryEntries() {
        if (diaryEntries == null) {
            diaryEntries = new ArrayList<>();
        }
        return diaryEntries;
    }

    static void addEntry(DiaryEntry entry) {
        if (diaryEntries == null) {
            diaryEntries = new ArrayList<>();
        }

        diaryEntries.add(entry);
    }

    static int getSize() {
        if (diaryEntries == null) {
            diaryEntries = new ArrayList<>();
        }
        return diaryEntries.size();
    }

    static void clearEntries() {
        if (diaryEntries != null) {
            diaryEntries.clear();
        }
    }

}
