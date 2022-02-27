package com.jpb.music.model;

import com.jpb.music.helper.ListHelper;

public class Folder {
    public int songsCount;
    public String name;

    public Folder(int songsCount, String name) {
        this.songsCount = songsCount;
        this.name = ListHelper.ifNull(name);
    }
}
