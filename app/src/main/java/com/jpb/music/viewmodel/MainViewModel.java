package com.jpb.music.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.jpb.music.MPPreferences;
import com.jpb.music.helper.MusicLibraryHelper;
import com.jpb.music.model.Album;
import com.jpb.music.model.Artist;
import com.jpb.music.model.Folder;
import com.jpb.music.model.Music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class MainViewModel extends ViewModel {

    public List<Music> songsList = new ArrayList<>();
    public List<Album> albumList = new ArrayList<>();
    public List<Artist> artistList = new ArrayList<>();
    public List<Folder> folderList = new ArrayList<>();

    public MainViewModel(Context context) {
        initSongList(context);
    }

    private void initSongList(Context context) {
        Set<String> excludedFolderList = MPPreferences.getExcludedFolders(context);
        List<Music> musicList = MusicLibraryHelper.fetchMusicLibrary(context);
        HashMap<String, Folder> map = new HashMap<>();

        for (Music music : musicList) {
            Folder folder;
            if (map.containsKey(music.relativePath)) {
                folder = map.get(music.relativePath);
                assert folder != null;
                folder.songsCount += 1;
            } else {
                folder = new Folder(1, music.relativePath);
                folderList.add(folder);
            }
            map.put(music.relativePath, folder);
        }

        for (Music music : musicList) {
            if (!excludedFolderList.contains(music.relativePath))
                songsList.add(music);
        }

        Collections.sort(songsList, new SongComparator());
    }

    public List<Music> getSongs(boolean reverse) {
        if (reverse)
            Collections.reverse(songsList);

        return songsList;
    }

    public List<Album> getAlbums(boolean reverse) {
        if (albumList.size() == 0) {

            HashMap<String, Album> map = new HashMap<>();
            albumList = new ArrayList<>();

            for (Music music : songsList) {
                if (map.containsKey(music.album)) {
                    Album album = map.get(music.album);
                    assert album != null;
                    album.duration += music.duration;
                    album.music.add(music);

                    map.put(music.album, album);

                } else {
                    List<Music> list = new ArrayList<>();
                    list.add(music);
                    Album album = new Album(music.artist, music.album, String.valueOf(music.year), music.duration, list);
                    map.put(music.album, album);
                }
            }

            for (String k : map.keySet()) {
                albumList.add(map.get(k));
            }
        }
        Collections.sort(albumList, new AlbumComparator());
        if (reverse)
            Collections.reverse(albumList);

        return albumList;
    }

    public List<Artist> getArtists(boolean reverse) {
        if (artistList.size() == 0) {

            HashMap<String, Artist> map = new HashMap<>();
            artistList = new ArrayList<>();
            albumList = getAlbums(false);

            for (Album album : albumList) {
                if (map.containsKey(album.artist)) {
                    Artist artist = map.get(album.artist);
                    assert artist != null;

                    artist.albums.add(album);
                    artist.songCount += album.music.size();
                    artist.albumCount += 1;
                    map.put(album.artist, artist);

                } else {
                    List<Album> list = new ArrayList<>();
                    list.add(album);

                    Artist artist = new Artist(album.artist, list, album.music.size(), list.size());
                    map.put(album.artist, artist);
                }
            }

            for (String k : map.keySet()) {
                artistList.add(map.get(k));
            }
        }

        Collections.sort(artistList, new ArtistComparator());
        if (reverse)
            Collections.reverse(artistList);

        return artistList;
    }

    public List<Folder> getFolders(boolean reverse) {
        Collections.sort(folderList, new FolderComparator());
        if (reverse)
            Collections.reverse(folderList);

        return folderList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        songsList = null;
        albumList = null;
        artistList = null;
        folderList = null;
    }
}

class AlbumComparator implements Comparator<Album> {
    @Override
    public int compare(Album a1, Album a2) {
        return a1.title.compareTo(a2.title);
    }
}

class SongComparator implements Comparator<Music> {
    @Override
    public int compare(Music m1, Music m2) {
        return m1.title.compareTo(m2.title);
    }
}

class ArtistComparator implements Comparator<Artist> {
    @Override
    public int compare(Artist a1, Artist a2) {
        return a1.name.compareTo(a2.name);
    }
}

class FolderComparator implements Comparator<Folder> {

    @Override
    public int compare(Folder f1, Folder f2) {
        return f1.name.compareTo(f2.name);
    }
}