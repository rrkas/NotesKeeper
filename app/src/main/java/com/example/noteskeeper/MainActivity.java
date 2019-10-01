package com.example.noteskeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.noteskeeper.adapters.NotesRecyclerViewAdapter;
import com.example.noteskeeper.note.Note;
import com.example.noteskeeper.utils.SpacingItemDecorator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NotesRecyclerViewAdapter.OnNoteListener{

    private static final String TAG = "MainActivity";

    private RecyclerView notesView;
    private ArrayList<Note> mNotes=new ArrayList<>();
    private NotesRecyclerViewAdapter mNotesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertFakeData();
        notesView=findViewById(R.id.notesView);
        initNotesView();

        setSupportActionBar((Toolbar)findViewById(R.id.notes_toolbar));
        setTitle("Notes");
    }

    private void insertFakeData(){
        for(int i=0;i<500;i++){
            Note note = new Note("title $ "+i,"content $ "+i,String.valueOf(i));
            mNotes.add(note);
        }
    }

    private void initNotesView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        notesView.setLayoutManager(linearLayoutManager);
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(10);
        notesView.addItemDecoration(itemDecorator);
        mNotesRecyclerViewAdapter = new NotesRecyclerViewAdapter(mNotes,this);
        notesView.setAdapter(mNotesRecyclerViewAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: " + position);

        Intent intent = new Intent(this,NoteActivity.class);
        intent.putExtra("selected_note",mNotes.get(position));
        startActivity(intent);
    }
}
