package com.jackie.guessmusic.bean;

import android.widget.Button;

/**
 * Created by Jackie on 2016/3/10.
 * 歌曲文字选项
 */
public class WordButton {
    private int mIndex;
    private boolean mIsVisible;
    private String mWordText;

    private Button mWordButton;

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public boolean isIsVisible() {
        return mIsVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.mIsVisible = isVisible;
    }

    public String getWordText() {
        return mWordText;
    }

    public void setWordText(String wordText) {
        this.mWordText = wordText;
    }

    public Button getWordButton() {
        return mWordButton;
    }

    public void setWordButton(Button wordButton) {
        this.mWordButton = wordButton;
    }
}
