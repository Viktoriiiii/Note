package com.example.note.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.Models.Note;
import com.example.note.NotesClickListener;
import com.example.note.R;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder>{

    Context context;
    List<Note> listNotes;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Note> listNotes, NotesClickListener listener) {
        this.context = context;
        this.listNotes = listNotes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.textViewTitle.setText(listNotes.get(position).getNoteTitle());
        holder.textViewTitle.setSelected(true);

        holder.textViewDate.setText(listNotes.get(position).getNoteDateCreation());
        holder.textViewDate.setSelected(true);

        if (listNotes.get(position).getNotePassword().isEmpty()) {
            holder.imageViewLock.setImageResource(R.drawable.unlock);
            holder.textViewDescription.setText(listNotes.get(position).getNoteDescription());
        } else {
            holder.imageViewLock.setImageResource(R.drawable.lock);
            holder.textViewDescription.setText("**********");
        }

         holder.notesContainer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 listener.onClick(listNotes.get(holder.getAdapterPosition()));
             }
         });

        holder.notesContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(listNotes.get(holder.getAdapterPosition()), holder.notesContainer);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notesContainer;
    TextView textViewTitle, textViewDescription, textViewDate;
    ImageView imageViewLock;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notesContainer = itemView.findViewById(R.id.notesContainer);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
        textViewDate = itemView.findViewById(R.id.textViewDate);
        imageViewLock = itemView.findViewById(R.id.imageViewLock);
    }
}