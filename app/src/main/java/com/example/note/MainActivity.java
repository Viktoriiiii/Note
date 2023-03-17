package com.example.note;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.note.Adapters.CategoryAdapter;
import com.example.note.Adapters.NotesListAdapter;
import com.example.note.DataBase.RoomDB;
import com.example.note.Models.Category;
import com.example.note.Models.Note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    ImageButton imageButtonAddCategory, imageButtonAddNote;
    Spinner spinnerCategory;
    NotesListAdapter notesListAdapter;
    RoomDB database;
    List<Note> notes = new ArrayList<>();
    List<Category> categories = new ArrayList<Category>();
    CategoryAdapter categoryAdapter;
    Note selectedNote;
    String passwordInput;
    String alertCategory;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_notes_list);
        imageButtonAddCategory = findViewById(R.id.add_new_category);
        imageButtonAddNote = findViewById(R.id.add_new_note);
        spinnerCategory = findViewById(R.id.categories);

        database = RoomDB.getInstance(this);
        categories = database.mainDAO().getAllCategories();
        notes = database.mainDAO().getAllNotesDesc();

        categoryAdapter = new CategoryAdapter(this,android.R.layout.simple_spinner_dropdown_item,loadCategories());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        updateRecycle(notes);

        imageButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddingNote.class);
                startActivityForResult(intent, 101);
            }
        });

        imageButtonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.alert_dialog, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        alertCategory = userInput.getText().toString();
                                        Category newCategory = new Category();
                                        newCategory.setCategoryName(alertCategory);
                                        database.mainDAO().insertCategory(newCategory);
                                        categoryAdapter = new CategoryAdapter(context,android.R.layout.simple_spinner_dropdown_item,loadCategories());
                                        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerCategory.setAdapter(categoryAdapter);
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
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Category item = categoryAdapter.getItem(position);
                    notes = database.mainDAO().getNotesForTrueCategories(item.getCategoryID());
                    updateRecycle(notes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Note newNote = (Note) data.getSerializableExtra("note");
                notes.clear();
                notes.addAll(database.mainDAO().getAllNotesDesc());
                notesListAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Note newNote = (Note) data.getSerializableExtra("note");
                notes.clear();
                notes.addAll(database.mainDAO().getAllNotesDesc());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycle(List<Note> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, this.notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Note note) {
            if (note.getNotePassword() != null && note.getNotePassword() != "" && !note.getNotePassword().isEmpty()) {
                Check(note);
            } else {
                Intent intent = new Intent(MainActivity.this, AddingNote.class);
                intent.putExtra("old_note", note);
                startActivityForResult(intent, 102);
            }
        }

        @Override
        public void onLongClick(Note note, CardView cardView) {
            selectedNote = new Note();
            selectedNote = note;
            showPopUp(cardView);
        }
    };

    private void Check(Note note) {
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
                                passwordInput = userInput.getText().toString();
                                Log.v("TAG", passwordInput +" l");
                                Log.v("TAG", note.getNotePassword() +" l");
                                if (passwordInput.equals(note.getNotePassword())) {
                                    Intent intent = new Intent(MainActivity.this, AddingNote.class);
                                    intent.putExtra("old_note", note);
                                    startActivityForResult(intent, 102);
                                }
                                else {
                                    Toast.makeText(context, passwordInput+" l", Toast.LENGTH_SHORT).show();
                                    return;
                                }
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

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    private List<Category> loadCategories() {
        List<Category> list = database.mainDAO().getAllCategories();
        return list;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.delete:
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder
                        .setTitle("Вы уверены, что хотите удалить заметку?")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    database.mainDAO().deleteNote(selectedNote);
                                    notes.remove(selectedNote);
                                    notesListAdapter.notifyDataSetChanged();
                                    Toast.makeText(MainActivity.this, "Заметка удалена", Toast.LENGTH_SHORT).show();
                                }
                            })
                        .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return false;
        }
    }
}