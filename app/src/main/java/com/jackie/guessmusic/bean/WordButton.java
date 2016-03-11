package com.jackie.guessmusic.bean;

import android.widget.Button;

/**
 * Created by Jackie on 2016/3/10.
 * 歌曲文字选项
 */
public class WordButton {
    private int mIndex;

    private boolean mIsVisible;

    private String mWordString;

    private Button mWordButton;

    public WordButton() {
        mIsVisible = true;
        mWordString = "";
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.mIsVisible = isVisible;
    }

    public String getWordString() {
        return mWordString;
    }

    public void setWordString(String wordString) {
        this.mWordString = wordString;
    }

    public Button getWordButton() {
        return mWordButton;
    }

    public void setWordButton(Button wordButton) {
        this.mWordButton = wordButton;
    }
}
