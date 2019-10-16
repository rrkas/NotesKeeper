package com.example.noteskeeper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteskeeper.R;
import com.example.noteskeeper.note.Note;
import com.example.noteskeeper.utils.Utility;

import java.util.ArrayList;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Note> mNotes = new ArrayList<>();
    private OnNoteListener onNoteListener;

    public NotesRecyclerViewAdapter(ArrayList<Note> mNotes, OnNoteListener onNoteListener) {
        this.mNotes = mNotes;
        this.onNoteListener=onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,parent,false);
        return new ViewHolder(view,onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try{
            String date = mNotes.get(position).getTimeStamp().substring(0,2);
            String month = mNotes.get(position).getTimeStamp().substring(3,5);
            month = Utility.getMonthFromNumber(month);
            String year = mNotes.get(position).getTimeStamp().substring(6);
            String timeStamp = date + " " + month + " " + year;
            holder.timeStamp.setText(timeStamp);
            holder.title.setText(mNotes.get(position).getTitle());
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title,timeStamp;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            title=itemView.findViewById(R.id.note_title);
            timeStamp=itemView.findViewById(R.id.note_time);
            this.onNoteListener=onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        public void onNoteClick(int position);
    }
}
