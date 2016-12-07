package com.example.lenovo.androidzachotapplication.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lenovo.androidzachotapplication.model.Note;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/7/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_ENTRY_ID = "entryid";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notes.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NAME_ENTRY_ID +
                    TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_TITLE + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void updateNoteDB(Note note) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME_TITLE, note.getTitle());
        String selection = COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(note.getId())};
        db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public void deleteNoteFromDB(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = DBHelper.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(note.getId())};
        db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public void addNoteToDB(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, note.getTitle());
        values.put(COLUMN_NAME_ENTRY_ID, note.getId());
        db.insert(TABLE_NAME, "nullable", values);
    }

    public ArrayList<Note> getNotesFromDB() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Note> notes = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Note note = new Note();
                note.setTitle(c.getString(c.getColumnIndex(DBHelper.COLUMN_NAME_TITLE)));
                note.setId(c.getInt(c.getColumnIndex(DBHelper.COLUMN_NAME_ENTRY_ID)));
                notes.add(note);
            } while (c.moveToNext());
        }
        return notes;
    }
}
