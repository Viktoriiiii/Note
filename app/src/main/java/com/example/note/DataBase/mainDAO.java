package com.example.note.DataBase;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.note.Models.Category;
import com.example.note.Models.Note;

import java.util.List;

@Dao
public interface mainDAO {

    @Insert(onConflict = REPLACE)
    void insertNotes(Note notes);

    @Insert(onConflict = REPLACE)
    void insertCategory(Category category);

    @Query("select * from Note order by NoteID desc")
    List<Note> getAllNotesDesc();

    @Query("select CategoryID from Category where CategoryName = :CategoryName")
    int getIdCategory(String CategoryName);

    @Query("select CategoryName from Category where CategoryId = :CategoryId")
    String getNameCategory(int CategoryId);

    @Query("select * from Category order by CategoryName asc")
    List<Category> getAllCategories();

    @Query("select * from Note where CategoryId = :CategoryId order by NoteID desc")
    List<Note> getNotesForTrueCategories(int CategoryId);

    @Query("update Category set CategoryName = :CategoryName where CategoryID = :CategoryID")
    void updateCategory (int CategoryID, String CategoryName);

    @Query("update Note set Title = :NoteTitle, Description = :NoteDescription, " +
            " CategoryId = :CategoryId where NoteID = :NoteID")
    void updateNoteCommon (int NoteID, String NoteTitle, String NoteDescription,
                     int CategoryId);

    @Query("update Note set Password = :NotePassword where NoteID = :NoteID")
    void updateNotePassword (int NoteID, String NotePassword);

    @Delete
    void deleteNote(Note note);

    @Delete
    void deleteCategory(Category category);


//    @Query("update Note set InputPassword = :NoteInputPassword where NoteID = :NoteID")
//    void updateNoteInputPassword (int NoteID, String NoteInputPassword);
}
