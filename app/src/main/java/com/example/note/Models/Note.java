package com.example.note.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "Note",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "CategoryID",
                childColumns = "CategoryId"))
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int NoteID = 0;

    @ColumnInfo (name = "Title")
    String NoteTitle = "";

    @ColumnInfo (name = "Description")
    String NoteDescription = "";

    @ColumnInfo (name = "Password")
    String NotePassword = "";

   // @ColumnInfo (name = "InputPassword")
  //  String NoteInputPassword = "";

    @ColumnInfo (name = "DateCreation")
    String NoteDateCreation = "";

    @ColumnInfo(name = "CategoryId")
    int CategoryId = 0;

    public Note(int categoryId) {
        CategoryId = categoryId;
    }

    public Note(){

    };

   // public String getNoteInputPassword() {
  //      return NoteInputPassword;
 //   }

  //  public void setNoteInputPassword(String noteInputPassword) {
  //      NoteInputPassword = noteInputPassword;
  //  }

    public int getNoteID() {
        return NoteID;
    }

    public void setNoteID(int noteID) {
        NoteID = noteID;
    }

    public String getNoteTitle() {
        return NoteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        NoteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return NoteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        NoteDescription = noteDescription;
    }

    public String getNotePassword() {
        return NotePassword;
    }

    public void setNotePassword(String notePassword) {
        NotePassword = notePassword;
    }

    public String getNoteDateCreation() {
        return NoteDateCreation;
    }

    public void setNoteDateCreation(String noteDateCreation) {
        NoteDateCreation = noteDateCreation;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }
}
