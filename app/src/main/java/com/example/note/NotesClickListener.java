package com.example.note;

import androidx.cardview.widget.CardView;

import com.example.note.Models.Note;

public interface NotesClickListener {

    void onClick(Note note);
    void onLongClick(Note note, CardView cardView);
}
