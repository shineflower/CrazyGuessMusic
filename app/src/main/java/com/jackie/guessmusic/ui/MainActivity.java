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
import android.widget.TextView;
import android.widget.Toast;

import com.jackie.guessmusic.R;
import com.jackie.guessmusic.bean.IWordButtonClickListener;
import com.jackie.guessmusic.bean.Song;
import com.jackie.guessmusic.bean.WordButton;
import com.jackie.guessmusic.data.Constants;
import com.jackie.guessmusic.view.WordGridView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements IWordButtonClickListener {
    //唱片相关动画
    private Animation mDiskAnim, mPinInAnim, mPinOutAnim;
    private ImageView mDiskImageView, mPinImageView;

    //播放按钮
    private ImageButton mPlayButton;
    private LinearLayout mWordSelectedContainer;
    private WordGridView mWordGridView;

    private List<WordButton> mAllWordList;
    private List<WordButton> mSelectedWordList;

    private Song mCurrentSong;
    //当前关索引
    private int mCurrentStageIndex = 0;

    // 当前金币数量
    private int mCurrentCoins = Constants.TOTAL_COINS;

    // 金币View
    private TextView mCurrentCoinTextView;

    //记录当前删除文字的个数
    private int mCanDeleteWordCount = 0;

    private boolean mIsRunning = false;

    /** 答案状态 —— 正确 */
    private final static int STATUS_ANSWER_RIGHT = 1;

    /** 答案状态 —— 错误 */
    private final static int STATUS_ANSWER_WRONG = 2;

    /** 答案状态 —— 不完整 */
    private final static int STATUS_ANSWER_LACK = 3;

    private final static int SHINE_COUNT = 5;

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

        mCurrentCoinTextView = (TextView) findViewById(R.id.top_bar_text_coin);
        mCurrentCoinTextView.setText(mCurrentCoins + "");
        mDiskImageView = (ImageView) findViewById(R.id.image_disk);
        mPinImageView = (ImageView) findViewById(R.id.image_pin);
        mPlayButton = (ImageButton) findViewById(R.id.play_start);

        mWordSelectedContainer = (LinearLayout) findViewById(R.id.word_selected_container);
        mWordGridView = (WordGridView) findViewById(R.id.gridview);
    }

    private void initData() {
        // 初始化当前关数据
        initCurrentStageData();

        // 处理删除按键事件
        handleDeleteWord();

        // 处理提示按键事件
        handleTipAnswer();
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

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsRunning && mPinImageView != null) {
                    mIsRunning = true;
                    mPlayButton.setVisibility(View.GONE);
                    mPinImageView.startAnimation(mPinInAnim);
                }
            }
        });
        mWordGridView.setOnWordButtonClickListener(this);
    }

    private void initCurrentStageData() {
        //读取当前关的歌曲信息
        mCurrentSong = loadCurrentStageSong(mCurrentStageIndex++);

        //初始化已选择文字框
        mSelectedWordList = initSelectedWord();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(140, 140);
        for (int i = 0; i < mSelectedWordList.size(); i++) {
            mWordSelectedContainer.addView(mSelectedWordList.get(i).getWordButton(), params);
        }

        //初始化待选文字框
        mAllWordList = initAllWord();
        mWordGridView.updateData(mAllWordList);
    }

    private List<WordButton> initSelectedWord() {
        List<WordButton> selectedWordList = new ArrayList<>();

        for (int i = 0; i < mCurrentSong.getNameLength(); i++) {
            final WordButton wordButton = new WordButton();
            Button button = (Button) getLayoutInflater().inflate(R.layout.item_gridview, null);
            button.setTextColor(Color.WHITE);
            button.setBackgroundResource(R.drawable.game_word_blank);
            wordButton.setWordButton(button);
            wordButton.getWordButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearAnswer(wordButton);
                }
            });
            selectedWordList.add(wordButton);
        }

        return selectedWordList;
    }

    private List<WordButton> initAllWord() {
        List<WordButton> allWordList = new ArrayList<>();
        for (int i = 0; i < WordGridView.COUNT_WORDS; i++) {
            WordButton wordButton = new WordButton();
            wordButton.setWordString(generateWords()[i]);
            allWordList.add(wordButton);
        }

        return allWordList;
    }

    private Animation initAnimation(int resId) {
        Animation animation = AnimationUtils.loadAnimation(this, resId);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

    private Song loadCurrentStageSong(int index) {
        Song song = new Song();
        String[] stage = Constants.SONG_INFO[index];
        song.setFileName(stage[Constants.INDEX_FILE_NAME]);
        song.setSongName(stage[Constants.INDEX_SONG_NAME]);
        return song;
    }

    //生成随机汉字
    private char getRandomCharacter() {
        Random random = new Random();
//        int highPosition = 0xB0 + random.nextInt(0xF7 - 0xB0);  //汉字高位的取值范围，会产生很多生僻字
        int highPosition = 0xB0 + random.nextInt(39);
        int lowPosition = 0xA1 + random.nextInt(0xFE - 0xA1);

        byte[] data = new byte[2];
        data[0] = Integer.valueOf(highPosition).byteValue();
        data[1] = Integer.valueOf(lowPosition).byteValue();
        String word = "";
        try {
            word = new String(data, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return word.charAt(0);
    }

    //生成所有待选的文字
    private String[] generateWords() {
        String[] words = new String[WordGridView.COUNT_WORDS];

        //存入歌名
        for (int i = 0; i < mCurrentSong.getNameLength(); i++) {
            words[i] = mCurrentSong.getNameCharacters()[i] + "";
        }

        //获取随机文字，并存入数组
        for (int i = mCurrentSong.getNameLength(); i < words.length; i++) {
            words[i] = getRandomCharacter() + "";
        }

        // 打乱文字顺序：首先从所有元素中随机选取一个与第一个元素进行交换，
        // 然后在第二个之后选择一个元素与第二个交换，知道最后一个元素。
        // 这样能够确保每个元素在每个位置的概率都是1/n。
//        for (int i = WordGridView.COUNT_WORDS - 1; i >= 0; i--) {
//            int index = new Random.nextInt(i + 1);
//
//            String tempWord = words[index];
//            words[index] = words[i];
//            words[i] = tempWord;
//        }

        return words;
    }

    @Override
    public void onWordButtonClick(WordButton wordButton) {
        setSelectedWord(wordButton);

        //检查答案
        int checkResult = checkAnswer();
        if (checkResult == STATUS_ANSWER_LACK) {
            //设置文字状态为Normal
            for (int i = 0; i < mSelectedWordList.size(); i++) {
                mSelectedWordList.get(i).getWordButton().setTextColor(Color.WHITE);
            }
        } else if (checkResult == STATUS_ANSWER_RIGHT) {
            //通关并获得奖励
            handlePassEvent();
        } else if (checkResult == STATUS_ANSWER_WRONG) {
            //闪烁提示用户
            shiningWords();
        }
    }

    //设置答案
    private void setSelectedWord(WordButton wordButton) {
        for (int i = 0; i < mSelectedWordList.size(); i++) {
            if (mSelectedWordList.get(i).getWordString().length() == 0) {
                mSelectedWordList.get(i).setWordString(wordButton.getWordString());
                mSelectedWordList.get(i).getWordButton().setText(wordButton.getWordString());
                mSelectedWordList.get(i).setIndex(wordButton.getIndex());
                wordButton.setIsVisible(true);

                //设置可选文字框可见性
                setWordButtonVisible(wordButton, View.INVISIBLE);

                break;
            }
        }
    }

    private void setWordButtonVisible(WordButton wordButton, int visibility) {
        wordButton.getWordButton().setVisibility(visibility);
        wordButton.setIsVisible(visibility == View.VISIBLE ? true : false);
    }

    //清除答案
    private void clearAnswer(WordButton wordButton) {
        wordButton.setWordString("");
        wordButton.getWordButton().setText("");

        setWordButtonVisible(mAllWordList.get(wordButton.getIndex()), View.VISIBLE);
    }

    private int checkAnswer() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mSelectedWordList.size(); i++) {
            if (mSelectedWordList.get(i).getWordString().length() == 0) {
                return STATUS_ANSWER_LACK;
            } else {
                builder.append(mSelectedWordList.get(i).getWordString());
            }
        }

        return mCurrentSong.getSongName().equals(builder.toString()) ? STATUS_ANSWER_RIGHT : STATUS_ANSWER_WRONG;
    }

    private void shiningWords() {
        TimerTask timerTask = new TimerTask() {
            int shineCount = 0;
            boolean isChange = false;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (shineCount++ > SHINE_COUNT) {
                            cancel();
                        } else {
                            for (int i = 0; i < mSelectedWordList.size(); i++) {
                                mSelectedWordList.get(i).getWordButton().setTextColor(isChange ? Color.RED : Color.WHITE);
                            }

                            isChange = !isChange;
                        }
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 1, 150);
    }

    private void handlePassEvent() {
//        LinearLayout passLayout = (LinearLayout) findViewById(R.id.pass_view);
//        passLayout.setVisibility(View.VISIBLE);
        Toast.makeText(this, "过关", Toast.LENGTH_SHORT).show();
    }

    private void handleDeleteWord() {
        ImageButton deleteButton = (ImageButton)findViewById(R.id.delete_word);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                deleteOneWord();
            }
        });
    }

    private void handleTipAnswer() {

    }

    private void deleteOneWord() {
        //金币不够
        if (!handleCoins(-getDeleteWordCoins())) {
            Toast.makeText(this, "金币不够", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // 将这个索引对应的WordButton设置为不可见
            //只剩下正确答案时
            if (WordGridView.COUNT_WORDS - mCurrentSong.getNameLength() > mCanDeleteWordCount) {
                setWordButtonVisible(findNotAnswerWord(), View.INVISIBLE);
            }
        }
    }

    /**
     * 增加或者减少指定数量的金币
     *
     * @param data  增加/减少金币的数量
     * @return true 增加/减少成功，false 失败
     */
    private boolean handleCoins(int data) {
        // 判断当前总的金币数量是否可被减少
        if (mCurrentCoins + data >= 0) {
            mCurrentCoins += data;

            mCurrentCoinTextView.setText(mCurrentCoins + "");

            return true;
        } else {
            // 金币不够
            return false;
        }
    }

    /**
     * 从配置文件里读取删除操作所要用的金币
     *
     * @return
     */
    private int getDeleteWordCoins() {
        return this.getResources().getInteger(R.integer.pay_delete_word);
    }

    /**
     * 从配置文件里读取提示操作所要用的金币
     *
     * @return
     */
    private int getTipCoins() {
        return this.getResources().getInteger(R.integer.pay_tip_answer);
    }

    /**
     * 找到一个不是答案的文件，并且当前是可见的
     */
    private WordButton findNotAnswerWord() {
        WordButton wordButton;

        while(true) {
            int index = new Random().nextInt(WordGridView.COUNT_WORDS);
            wordButton = mAllWordList.get(index);

            if (wordButton.isVisible() && !isAnswerWord(wordButton)) {
                mCanDeleteWordCount++;
                return wordButton;
            }
        }
    }

    /**
     * 判断某个文字是不是答案
     * @param wordButton
     * @return
     */
    private boolean isAnswerWord(WordButton wordButton) {
        boolean result = false;

        for (int i = 0; i < mCurrentSong.getNameLength(); i++) {
            if (wordButton.getWordString().equals("" + mCurrentSong.getNameCharacters()[i])) {
                result = true;
                break;
            }
        }

        return result;
    }
}
