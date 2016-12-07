package com.example.lenovo.androidzachotapplication.ui.fragment;

import android.view.View;

import com.example.lenovo.androidzachotapplication.ui.adapter.SongsAdapter;
import com.example.lenovo.androidzachotapplication.util.Utils;

/**
 * Created by Lenovo on 12/6/2016.
 */

public class PlayerFagment extends RecyclerViewFragment {

    @Override
    protected void init(View view) {
        SongsAdapter adapter = new SongsAdapter(getActivity());
        adapter.setSongs(Utils.getSdCardSongs(getActivity()));
        recyclerView.setAdapter(adapter);
    }
}
