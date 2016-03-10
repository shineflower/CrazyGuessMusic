package com.jackie.guessmusic.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jackie.guessmusic.R;
import com.jackie.guessmusic.bean.WordButton;
import com.jackie.guessmusic.view.WordGridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    //唱片相关动画
    private Animation mDiskAnim, mPinInAnim, mPinOutAnim;
    private ImageView mDiskImageView, mPinImageView;

    //播放按钮
    private ImageButton mPlayButton;

    private List<WordButton> mAllWordList;
    private List<WordButton> mSelectedWordList;

    private LinearLayout mWordSelectedContainer;
    private WordGridView mWordGridView;
    private boolean mIsRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mDiskAnim = initAnimation(R.anim.rotate);
        mPinInAnim = initAnimation(R.anim.rotate_pin_in);
        mPinInAnim.setFillAfter(true);
        mPinOutAnim = initAnimation(R.anim.rotate_pin_out);
        mPinOutAnim.setFillAfter(true);

        mDiskImageView = (ImageView) findViewById(R.id.image_disk);
        mPinImageView = (ImageView) findViewById(R.id.image_pin);
        mPlayButton = (ImageButton) findViewById(R.id.play_start);

        mWordSelectedContainer = (LinearLayout) findViewById(R.id.word_selected_container);
        mWordGridView = (WordGridView) findViewById(R.id.gridview);
    }

    private void initData() {
        //初始化待选文字框
        initAllWord();

        //初始化已选择文字框
        initSelectedWord();

        mWordGridView.updateData(mAllWordList);
    }

    private void initAllWord() {
        mAllWordList = new ArrayList<>();
        for (int i = 0; i < WordGridView.COUNT_WORDS; i++) {
            WordButton wordButton = new WordButton();
            wordButton.setWordText("好");
            mAllWordList.add(wordButton);
        }
    }

    private void initSelectedWord() {
        mSelectedWordList = new ArrayList<>();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(140, 140);
        for (int i = 0; i < 4; i++) {
            Button button = (Button) getLayoutInflater().inflate(R.layout.item_gridview, null);
            button.setTextColor(Color.WHITE);
            button.setBackgroundResource(R.drawable.game_word_blank);
            mWordSelectedContainer.addView(button, params);

            WordButton wordButton = new WordButton();
            wordButton.setWordButton(button);
            mSelectedWordList.add(wordButton);
        }
    }

    private void initEvent() {
        mDiskAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPinImageView.startAnimation(mPinOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mPinInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDiskImageView.startAnimation(mDiskAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mPinOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunning = false;
                mPlayButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mPlayButton.setOnClickListener(this);
    }

    private Animation initAnimation(int animId) {
        Animation animation = AnimationUtils.loadAnimation(this, animId);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_start:
                handPlayButton();
                break;
        }
    }

    private void handPlayButton() {
        if (!mIsRunning && mPinImageView != null) {
            mIsRunning = true;
            mPlayButton.setVisibility(View.GONE);
            mPinImageView.startAnimation(mPinInAnim);
        }
    }
}
