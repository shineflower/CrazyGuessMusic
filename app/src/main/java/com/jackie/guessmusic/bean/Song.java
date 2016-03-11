package com.jackie.guessmusic.bean;

/**
 * Created by Jackie on 2016/3/10.
 * 歌曲数据结构
 */
public class Song {
    //歌曲文件名
    private String mFileName;

    //歌曲名称
    private String mSongName;

    //歌曲名字的长度
    private int mNameLength;

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        this.mSongName = songName;

        mNameLength = songName.length();
    }

    public int getNameLength() {
        return mNameLength;
    }

    public char[] getNameCharacters() {
        return mSongName.toCharArray();
    }
}
