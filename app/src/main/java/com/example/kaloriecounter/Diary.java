package com.example.kaloriecounter;

import java.util.ArrayList;

class Diary {

    static ArrayList<String> diaryEntries;

    static ArrayList<String> getDiaryEntries() {
        if (diaryEntries == null) {
            diaryEntries = new ArrayList<>();
        }
        return diaryEntries;
    }

    /**
     * Add an entry to the Diary arraylist.
     * @param entry the entry to be added to the diary.
     */
    static void addEntry(String entry) {
        if (diaryEntries == null) {
            diaryEntries = new ArrayList<>();
        }

        diaryEntries.add(entry);
    }

    /**
     * Get the number of diary entries in the diary.
     * @return the size of the arraylist.
     */
    static int getSize() {
        if (diaryEntries == null) {
            diaryEntries = new ArrayList<>();
        }
        return diaryEntries.size();
    }

    /**
     * Clear all the entries in the diary.
     */
    static void clearEntries() {
        if (diaryEntries != null) {
            diaryEntries.clear();
        }
    }

}
