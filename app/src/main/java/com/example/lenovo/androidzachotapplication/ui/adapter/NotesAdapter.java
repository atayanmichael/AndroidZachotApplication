package com.example.lenovo.androidzachotapplication.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.androidzachotapplication.R;
import com.example.lenovo.androidzachotapplication.model.Note;
import com.example.lenovo.androidzachotapplication.sql.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Lenovo on 12/7/2016.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private Context context;
    private LayoutInflater inflater;
    private AlertDialog dialog;
    private EditText dialogEditText;
    private TextView dialogTitle;
    private DBHelper dbHelper;
    private Random random;

    //init stuff
    public NotesAdapter(final Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        notes = new ArrayList<>();
        random = new Random();
        createDialog();
        initDB();
    }

    private void initDB() {
        dbHelper = new DBHelper(context);
        setNotes(dbHelper.getNotesFromDB());
    }

    private void createDialog() {
        View dialogView = inflater.inflate(R.layout.dialog_note, null);
        dialog = new AlertDialog.Builder(context).setView(dialogView).setCancelable(true).create();
        dialogEditText = (EditText) dialogView.findViewById(R.id.dialog_note_text);
        dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_note_title);
        dialogView.findViewById(R.id.dialog_note_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialogView.findViewById(R.id.dialog_note_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dialogEditText.getText())) {
                    Toast.makeText(context, "Note cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    int position = (int) dialogEditText.getTag();
                    if (position == notes.size()) {
                        Note newNote = new Note();
                        newNote.setTitle(dialogEditText.getText().toString());
                        newNote.setId(random.nextInt());
                        addNote(newNote);
                    } else {
                        changeNote(position, dialogEditText.getText().toString());
                    }
                    dialog.dismiss();
                }
            }
        });
    }

    //adapter stuff
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(inflater.inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        if (position == notes.size()) {
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setText("+ ADD NOTE");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickText(holder.getAdapterPosition(), true);
                }
            });

        } else {
            textView.setGravity(Gravity.NO_GRAVITY);
            textView.setText(notes.get(position).getTitle());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickText(holder.getAdapterPosition(), false);
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteNote(holder.getAdapterPosition());
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return notes.size() + 1;
    }

    //click listener
    private void clickText(int position, boolean add) {
        dialogEditText.setTag(position);
        dialogTitle.setText(add ? "Add Note" : "Change Note");
        dialogEditText.setText(add ? "" : notes.get(position).getTitle());
        dialog.show();
    }

    //notes setters
    private void setNotes(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
        notifyDataSetChanged();
    }

    private void addNote(Note note) {
        notes.add(note);
        notifyItemInserted(notes.size() - 1);
        dbHelper.addNoteToDB(note);
    }

    private void changeNote(int position, String title) {
        notes.get(position).setTitle(title);
        notifyItemChanged(position);
        dbHelper.updateNoteDB(notes.get(position));
    }

    private void deleteNote(int position) {
        if (position < 0 || position >= notes.size()) {
            return;
        }
        dbHelper.deleteNoteFromDB(notes.remove(position));
        notifyItemRemoved(position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        NoteViewHolder(View itemView) {
            super(itemView);
        }
    }


}
