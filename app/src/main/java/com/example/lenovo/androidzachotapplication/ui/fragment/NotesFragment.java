package com.example.lenovo.androidzachotapplication.ui.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lenovo.androidzachotapplication.sql.DBHelper;
import com.example.lenovo.androidzachotapplication.ui.adapter.NotesAdapter;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/6/2016.
 */

public class NotesFragment extends RecyclerViewFragment {

    private NotesAdapter adapter;

    @Override
    protected void init(View view) {
        adapter = new NotesAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }
}
