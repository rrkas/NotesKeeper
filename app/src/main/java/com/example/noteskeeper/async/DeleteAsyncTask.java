package com.example.noteskeeper.async;

import android.os.AsyncTask;

import com.example.noteskeeper.note.Note;
import com.example.noteskeeper.persistance.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao noteDao;

    public DeleteAsyncTask(NoteDao dao) {
        noteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        noteDao.delete(notes);
        return null;
    }


}
