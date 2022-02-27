package com.jpb.music.helper;

import com.jpb.music.MPConstants;
import com.jpb.music.model.Album;
import com.jpb.music.model.Artist;
import com.jpb.music.model.Music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kotlin.collections.CollectionsKt;

public class ListHelper {

    public static List<Music> searchMusicByName(List<Music> list, String query) {
        List<Music> newList = new ArrayList<>(list);
        return CollectionsKt.filter(newList, music ->
                (music.title.toLowerCase().contains(query) || music.displayName.toLowerCase().contains(query)) ||
                        (music.artist.toLowerCase().contains(query) || music.album.toLowerCase().contains(query))
        );
    }

    public static List<Music> sortMusicByDateAdded(List<Music> list, boolean reverse) {
        List<Music> newList = new ArrayList<>(list);
        Collections.sort(newList, new SongComparator(MPConstants.SORT_MUSIC_BY_DATE_ADDED));

        if (reverse)
            Collections.reverse(newList);

        return newList;
    }

    public static List<Music> sortMusic(List<Music> list, boolean reverse) {
        List<Music> newList = new ArrayList<>(list);
        Collections.sort(newList, new SongComparator(MPConstants.SORT_MUSIC_BY_TITLE));

        if (reverse)
            Collections.reverse(newList);

        return newList;
    }

    public static List<Artist> searchArtistByName(List<Artist> artistList, String query) {
        List<Artist> list = new ArrayList<>(artistList);
        return CollectionsKt.filter(list, artist ->
                artist.name.toLowerCase().contains(query));
    }

    public static List<Artist> sortArtistByName(List<Artist> artistList, boolean reverse) {
        List<Artist> list = new ArrayList<>(artistList);
        Collections.sort(list, new ArtistComparator(MPConstants.SORT_ARTIST_BY_NAME));

        if (reverse)
            Collections.reverse(list);

        return list;
    }

    public static List<Artist> sortArtistBySongs(List<Artist> artistList, boolean reverse) {
        List<Artist> list = new ArrayList<>(artistList);
        Collections.sort(list, new ArtistComparator(MPConstants.SORT_ARTIST_BY_SONGS));

        if (reverse)
            Collections.reverse(list);

        return list;
    }

    public static List<Artist> sortArtistByAlbums(List<Artist> artistList, boolean reverse) {
        List<Artist> list = new ArrayList<>(artistList);
        Collections.sort(list, new ArtistComparator(MPConstants.SORT_ARTIST_BY_ALBUMS));

        if (reverse)
            Collections.reverse(list);

        return list;
    }

    public static List<Album> searchByAlbumName(List<Album> albumList, String query) {
        List<Album> list = new ArrayList<>(albumList);
        return CollectionsKt.filter(list, album ->
                album.title.toLowerCase().contains(query) || album.artist.toLowerCase().equals(query));
    }

    public static List<Album> sortAlbumByName(List<Album> albumList, boolean reverse) {
        List<Album> list = new ArrayList<>(albumList);
        Collections.sort(list, new AlbumComparator(MPConstants.SORT_ALBUM_BY_TITLE));

        if (reverse)
            Collections.reverse(list);

        return list;
    }

    public static List<Album> sortAlbumBySongs(List<Album> albumList, boolean reverse) {
        List<Album> list = new ArrayList<>(albumList);
        Collections.sort(list, new AlbumComparator(MPConstants.SORT_ALBUM_BY_SONGS));

        if (reverse)
            Collections.reverse(list);

        return list;
    }

    public static List<Album> sortAlbumByDuration(List<Album> albumList, boolean reverse) {
        List<Album> list = new ArrayList<>(albumList);
        Collections.sort(list, new AlbumComparator(MPConstants.SORT_ALBUM_BY_DURATION));

        if (reverse)
            Collections.reverse(list);

        return list;
    }

    public static String ifNull(String val) {
        return val == null ? "" : val;
    }
}

class SongComparator implements Comparator<Music> {
    private final int mode;

    public SongComparator(int mode) {
        this.mode = mode;
    }

    @Override
    public int compare(Music m1, Music m2) {
        if (mode == MPConstants.SORT_MUSIC_BY_TITLE)
            return m1.title.compareTo(m2.title);

        else if (mode == MPConstants.SORT_MUSIC_BY_DATE_ADDED)
            return Long.compare(m2.dateAdded, m1.dateAdded);

        return 0;
    }
}

class ArtistComparator implements Comparator<Artist> {
    private final int mode;

    public ArtistComparator(int mode) {
        this.mode = mode;
    }

    @Override
    public int compare(Artist a1, Artist a2) {
        if (mode == MPConstants.SORT_ARTIST_BY_NAME)
            return a1.name.compareTo(a2.name);

        else if (mode == MPConstants.SORT_ARTIST_BY_SONGS)
            return Integer.compare(a2.songCount, a1.songCount);

        else if (mode == MPConstants.SORT_ARTIST_BY_ALBUMS)
            return Integer.compare(a2.albumCount, a1.albumCount);

        return 0;
    }
}

class AlbumComparator implements Comparator<Album> {
    private final int mode;

    public AlbumComparator(int mode) {
        this.mode = mode;
    }

    @Override
    public int compare(Album a1, Album a2) {
        if (mode == MPConstants.SORT_ALBUM_BY_TITLE)
            return a1.title.compareTo(a2.title);

        else if (mode == MPConstants.SORT_ALBUM_BY_SONGS)
            return Integer.compare(a2.music.size(), a1.music.size());

        else if (mode == MPConstants.SORT_ALBUM_BY_DURATION)
            return Long.compare(a2.duration, a1.duration);

        return 0;
    }
}


