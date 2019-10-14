package com.example.noteskeeper.persistance;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.noteskeeper.async.DeleteAsyncTask;
import com.example.noteskeeper.async.InsertAsyncTask;
import com.example.noteskeeper.async.UpdateAsyncTask;
import com.example.noteskeeper.note.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase noteDatabase;

    public NoteRepository(Context context) {
        noteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note){
        new InsertAsyncTask(noteDatabase.getNoteDao()).execute(note);
    }

    public void updateNote(Note note){
        new UpdateAsyncTask(noteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask(){
        return noteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Note note){
        new DeleteAsyncTask(noteDatabase.getNoteDao()).execute(note);
    }

}
