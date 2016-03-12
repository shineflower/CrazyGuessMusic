package com.jackie.guessmusic.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import java.io.IOException;

/**
 * Created by Jackie on 2016/3/12.
 * 音乐播放
 */
public class MediaPlayer {

    private static android.media.MediaPlayer mSongMediaPlayer;
    private static String[] TONE_NAME = { "enter.mp3", "cancel.mp3", "coin.mp3" };
    private static android.media.MediaPlayer[] mToneMediaPlayer = new android.media.MediaPlayer[TONE_NAME.length];

    /**
     * 确认音效
     */
    public static final int TONE_INDEX_ENTER = 0;
    /**
     * 取消音效
     */
    public static final int TONE_INDEX_CANCEL = 1;
    /**
     * 金币音效
     */
    public static final int TONE_INDEX_COIN = 2;

    /**
     * 播放歌曲
     * @param context   上下文
     * @param fileName  文件名
     */
    public static void playSong(Context context, String fileName) {
        if (mSongMediaPlayer == null) {
            mSongMediaPlayer = new android.media.MediaPlayer();
        }

        //强制重置
        mSongMediaPlayer.reset();

        //加载音乐
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            mSongMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mSongMediaPlayer.prepare();
            mSongMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放音效
     * @param context    上下文
     * @param index      音效的下标
     */
    public static void playTone(Context context, int index) {
        if (mToneMediaPlayer[index] == null) {
            mToneMediaPlayer[index] = new android.media.MediaPlayer();
        }

        mToneMediaPlayer[index].reset();

        //加载音效
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd(TONE_NAME[index]);
            mToneMediaPlayer[index].setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mToneMediaPlayer[index].prepare();
            mToneMediaPlayer[index].start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (mSongMediaPlayer != null) {
            mSongMediaPlayer.stop();
        }
    }
}
