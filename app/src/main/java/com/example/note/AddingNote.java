package com.example.note;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.note.Adapters.CategoryAdapter;
import com.example.note.DataBase.RoomDB;
import com.example.note.Models.Category;
import com.example.note.Models.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddingNote extends AppCompatActivity {

    EditText editTextTitle, editTextDescription;
    Spinner spinnerCategory;
    ImageButton lockNote, saveNote;

    List<Category> categories = new ArrayList<Category>();
    CategoryAdapter categoryAdapter;
    RoomDB database;
    Note notes;
    final Context context = this;
    String password;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_note);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        lockNote = findViewById(R.id.lockNote);
        saveNote = findViewById(R.id.saveNote);
        database = RoomDB.getInstance(this);
        categories = database.mainDAO().getAllCategories();
        categoryAdapter = new CategoryAdapter(this,android.R.layout.simple_spinner_dropdown_item,loadCategories());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        notes = new Note();
        try {
            notes = (Note) getIntent().getSerializableExtra("old_note");
            editTextTitle.setText(notes.getNoteTitle());
            editTextDescription.setText(notes.getNoteDescription());
            isOldNote = true;
          //  spinnerCategory.setSelection(notes.getCategoryId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(AddingNote.this, "Пожалуйста, введите название заметки заметки",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.isEmpty()) {
                    Toast.makeText(AddingNote.this, "Пожалуйста, введите текст заметки",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy h:mm");
                Date date = new Date();

                Category item = categoryAdapter.getItem(spinnerCategory.getSelectedItemPosition());

                if (!isOldNote) {
                    notes = new Note(item.getCategoryID());
                    notes.setNoteTitle(title);
                    notes.setNoteDescription(description);
                    notes.setNoteDateCreation(formatter.format(date));
                    database.mainDAO().insertNotes(notes);
                } else {
                    database.mainDAO().updateNoteCommon(notes.getNoteID(), title, description, item.getCategoryID());
                }

                Intent intent = new Intent();
                intent.putExtra("notes", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        lockNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOldNote) {
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.alert_for_pass, null);
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                    mDialogBuilder.setView(promptsView);
                    final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                    mDialogBuilder
                            .setTitle("Введите пароль: ")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            password = userInput.getText().toString();
                                            if (password.isEmpty() || password == null || password == ""){}
                                            else
                                                database.mainDAO().updateNotePassword(notes.getNoteID(), password);
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = mDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }
    private List<Category> loadCategories() {
        List<Category> list = database.mainDAO().getAllCategories();
        return list;
    }
}