package com.example.noteskeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.noteskeeper.adapters.NotesRecyclerViewAdapter;
import com.example.noteskeeper.note.Note;
import com.example.noteskeeper.persistance.NoteRepository;
import com.example.noteskeeper.utils.SpacingItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesRecyclerViewAdapter.OnNoteListener,
        FloatingActionButton.OnClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView notesView;
    private ArrayList<Note> mNotes=new ArrayList<>();
    private NotesRecyclerViewAdapter mNotesRecyclerViewAdapter;
    private NoteRepository noteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteRepository = new NoteRepository(this);

        notesView=findViewById(R.id.notesView);
        initNotesView();
        retrieveNotes();

        findViewById(R.id.fab).setOnClickListener(this);

        setSupportActionBar((Toolbar)findViewById(R.id.notes_toolbar));
        setTitle("Notes");
    }
    private void retrieveNotes(){
        noteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(mNotes.size()>0){
                    mNotes.clear();
                }
                if(notes!=null){
                    mNotes.addAll(notes);
                }
                mNotesRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
    private void initNotesView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        notesView.setLayoutManager(linearLayoutManager);
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(10);
        notesView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(notesView);
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
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }
    private void deleteNote(Note note){
        mNotes.remove(note);
        mNotesRecyclerViewAdapter.notifyDataSetChanged();
        noteRepository.deleteNote(note);
    }
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}
