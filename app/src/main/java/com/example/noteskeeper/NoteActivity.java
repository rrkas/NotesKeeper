package com.example.noteskeeper;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noteskeeper.note.Note;
import com.example.noteskeeper.persistance.NoteRepository;
import com.example.noteskeeper.utils.Utility;

public class NoteActivity extends AppCompatActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    private EditText txt_title, txt_text;
    private TextView lbl_title;

    private Note myFinalNote;
    private Note myInitialNote;
    private boolean isNewNote;

    private RelativeLayout checkContainer, backArrowContainer;
    private ImageButton btn_check,btn_backArrow;

    private NoteRepository noteRepository;
    private static final int EDIT_MODE_DISABLED = 0;
    private static final int EDIT_MODE_ENABLED = 1;
    private int mode;

    private GestureDetector gestureDetector;

    private void updateNote(){
        noteRepository.updateNote(myFinalNote);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        myInitialNote=new Note();
        myFinalNote = new Note();

        txt_text = findViewById(R.id.note_text);
        txt_title = findViewById(R.id.note_edit_title);
        lbl_title = findViewById(R.id.note_text_title);

        checkContainer = findViewById(R.id.check_container);
        backArrowContainer = findViewById(R.id.back_arrow_container);

        btn_check = findViewById(R.id.toolbar_check);
        btn_backArrow = findViewById(R.id.toolbar_back_arrow);

        noteRepository = new NoteRepository(this);

        if(getIncomingIntent()){
            //editMode
            setNewNoteProperties();
            enableEditMode();

        }else{
            //viewMode
            setNoteProperties();
            disableInteraction();
        }
        setListener();
    }
    private void saveChanges(){
        if (isNewNote){
            saveNewNotes();
        }else{
            updateNote();
        }
    }
    private void saveNewNotes(){
        noteRepository.insertNoteTask(myFinalNote);
    }
    private void setListener(){
        txt_text.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this,this);
        lbl_title.setOnClickListener(this);
        btn_check.setOnClickListener(this);
        btn_backArrow.setOnClickListener(this);
        txt_text.addTextChangedListener(this);
    }
    public boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_note")){
            myInitialNote = getIntent().getParcelableExtra("selected_note");
            myFinalNote.setTitle(myInitialNote.getTitle());
            myFinalNote.setContent(myInitialNote.getContent());
            myFinalNote.setTimeStamp(myInitialNote.getTimeStamp());
            myFinalNote.setId(myInitialNote.getId());
            isNewNote=false;
            mode= EDIT_MODE_DISABLED;
            return false;
        }
        isNewNote=true;
        mode=EDIT_MODE_ENABLED;
        return true;
    }
    private void setNoteProperties(){
        lbl_title.setText(myInitialNote.getTitle());
        txt_text.setText(myInitialNote.getContent());

        myInitialNote = new Note();
        myFinalNote = new Note();

        myInitialNote.setTitle("NoteTitle");
        myFinalNote.setTitle("NoteTitle");


    }
    private void enableEditMode() {
        backArrowContainer.setVisibility(View.GONE);
        checkContainer.setVisibility(View.VISIBLE);

        lbl_title.setVisibility(View.GONE);
        txt_title.setVisibility(View.VISIBLE);
        if(getIncomingIntent()) {
            txt_title.setHint(lbl_title.getText());
        }else{
            txt_title.setText(lbl_title.getText());
        }
        mode = EDIT_MODE_ENABLED;
        enableInteraction();
    }
    private void disableEditMode(){
        backArrowContainer.setVisibility(View.VISIBLE);
        checkContainer.setVisibility(View.GONE);

        lbl_title.setVisibility(View.VISIBLE);
        txt_title.setVisibility(View.GONE);
        if(txt_title.getText().toString().equals("")){
            lbl_title.setText(txt_title.getHint());
        }else {
            lbl_title.setText(txt_title.getText());
        }
        mode = EDIT_MODE_DISABLED;
        disableInteraction();

        String temp = txt_text.getText().toString();
        temp = temp.replace("\n","");
        temp = temp.replace(" ","");
        if(temp.length()>0){
            myFinalNote.setTitle(txt_title.getText().toString());
            myFinalNote.setContent(txt_text.getText().toString());
            String timeStamp = Utility.getCurrentTimeStamp();
            myFinalNote.setTimeStamp(timeStamp);

            if(!myFinalNote.getContent().equals(myInitialNote.getContent())
                || !myFinalNote.getTitle().equals(myInitialNote.getTitle())){
                saveChanges();
            }
        }
        hideKeyBoard();
    }
    private void setNewNoteProperties(){
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
        enableEditMode();
        return false;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.toolbar_check:
                disableEditMode();
                break;

            case R.id.note_text_title:
                enableEditMode();
                txt_text.requestFocus();
                txt_text.setSelection(txt_text.length());
                break;

            case R.id.toolbar_back_arrow:
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if(mode==EDIT_MODE_ENABLED){
            onClick(btn_check);
        }else {
            super.onBackPressed();
        }
    }
    private void disableInteraction(){
        txt_text.setKeyListener(null);
        txt_text.setFocusable(false);
        txt_text.setFocusableInTouchMode(false);
        txt_text.setCursorVisible(false);
        txt_text.clearFocus();
    }
    private void enableInteraction(){
        txt_text.setKeyListener(new EditText(this).getKeyListener());
        txt_text.setFocusable(true);
        txt_text.setFocusableInTouchMode(true);
        txt_text.setCursorVisible(true);
        txt_text.requestFocus();
    }
    private void hideKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null){
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mode=savedInstanceState.getInt("mode");
        if(mode==EDIT_MODE_ENABLED){
            enableEditMode();
        }else{
            disableEditMode();
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode",mode);
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        lbl_title.setText(charSequence.toString());
    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void afterTextChanged(Editable editable) {

    }
}
