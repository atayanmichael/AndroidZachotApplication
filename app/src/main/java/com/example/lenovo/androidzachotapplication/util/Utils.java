package com.example.lenovo.androidzachotapplication.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.lenovo.androidzachotapplication.model.Song;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/7/2016.
 */

public class Utils {

    public static ArrayList<Song> getSdCardSongs(Context context) {
        ArrayList<Song> songs = new ArrayList<>();
        Cursor c = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.ALBUM_ID}, "1=1",
                null, null);
        if (c != null && c.moveToFirst()) {
            do {
                Song song = new Song();
                String title = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                if (title.trim().endsWith(".mp3") || title.trim().endsWith(".MP3")) {
                    song.setTitle(title);
                    song.setArtist(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    song.setId(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    song.setPath(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                    song.setDuration(c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

                    songs.add(song);
                }
            } while (c.moveToNext());
            c.close();
        }
        return songs;
    }
}
