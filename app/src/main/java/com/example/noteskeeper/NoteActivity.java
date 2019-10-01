package com.example.noteskeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.noteskeeper.note.Note;

public class NoteActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private EditText txt_title, txt_text;
    private TextView lbl_title;
    private Note myInitialNote;
    private boolean isNewNote;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        txt_text = findViewById(R.id.note_text);
        txt_title = findViewById(R.id.note_edit_title);
        lbl_title = findViewById(R.id.note_text_title);

        if(getIncomingIntent()){
            //editMode
            setNewNoteProperties();
        }else{
            //viewMode
            setNoteProperties();
        }
        setListener();
    }

    private void setListener(){
        txt_text.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this,this);
    }

    public boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_note")){
            myInitialNote = getIntent().getParcelableExtra("selected_note");
            isNewNote=false;
            return false;
        }
        isNewNote=true;
        return true;
    }

    private void setNoteProperties(){
        lbl_title.setText(myInitialNote.getTitle());
        txt_title.setText(myInitialNote.getTitle());
        txt_text.setText(myInitialNote.getContent());
    }

    private void setNewNoteProperties(){
        lbl_title.setText("Note Title");
        lbl_title.setText("Note Title");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
}
