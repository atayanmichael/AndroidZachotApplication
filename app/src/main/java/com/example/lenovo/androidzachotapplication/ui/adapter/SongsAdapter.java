package com.example.lenovo.androidzachotapplication.ui.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.androidzachotapplication.R;
import com.example.lenovo.androidzachotapplication.model.Song;
import com.example.lenovo.androidzachotapplication.ui.view.PlayerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 12/7/2016.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {

    private List<Song> songs;
    private LayoutInflater inflater;
    private Context context;
    private MediaPlayer player;
    private Thread thread;
    private SongViewHolder playingViewHolder;
    private Song currentSong;

    private void stop() {
        currentSong = null;
        if (player != null) {
            player.stop();
            player = null;
            playingViewHolder.playerView.setProgress(0);
            playingViewHolder.playerView.setVisibility(View.GONE);
            playingViewHolder.stopButton.setVisibility(View.GONE);
            playingViewHolder = null;
            thread.interrupt();
        }
    }

    public void play(Song song) {
        currentSong = song;
        if (player != null) {
            player.stop();
        }
        if (thread != null) {
            thread.interrupt();
        }
        player = MediaPlayer.create(context, Uri.parse(song.getPath()));
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        if (player != null) {
                            playingViewHolder.playerView.setProgress(player.getCurrentPosition() / 1000);
                        }
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {

                }
            }
        });
        thread.start();
        player.start();
    }

    public SongsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        songs = new ArrayList<>();
    }

    public void setSongs(List<Song> songs) {
        this.songs.clear();
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SongViewHolder(inflater.inflate(R.layout.item_song, parent, false));
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
        final Song song = songs.get(position);
        holder.titleText.setText(song.getTitle());
        holder.artistText.setText(song.getArtist());
        if (song != currentSong) {
            holder.stopButton.setVisibility(View.GONE);
            holder.playerView.setVisibility(View.GONE);
        } else {
            holder.stopButton.setVisibility(View.VISIBLE);
            holder.playerView.setVisibility(View.VISIBLE);
        }
        holder.playerView.setDuration((int) (song.getDuration() / 1000));
        holder.playerView.setOnChangeProgressListener(new PlayerView.OnChangeProgressListener() {
            @Override
            public void onTouch(int progress) {
                player.seekTo(progress * 1000);
            }
        });
        holder.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setOnClickListener(null);
                stop();
                holder.stopButton.setVisibility(View.VISIBLE);
                holder.playerView.setVisibility(View.VISIBLE);
                playingViewHolder = holder;
                play(song);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView artistText;
        PlayerView playerView;
        TextView stopButton;

        public SongViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.song_title);
            artistText = (TextView) itemView.findViewById(R.id.song_artist);
            stopButton = (TextView) itemView.findViewById(R.id.song_stop);
            playerView = (PlayerView) itemView.findViewById(R.id.song_player);
        }
    }
}
