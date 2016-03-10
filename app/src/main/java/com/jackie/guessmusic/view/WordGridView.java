package com.jackie.guessmusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.jackie.guessmusic.R;
import com.jackie.guessmusic.bean.IWordButtonClickListener;
import com.jackie.guessmusic.bean.WordButton;

import java.util.List;

/**
 * Created by Jackie on 2016/3/10.
 * 歌曲文字选项GridView
 */
public class WordGridView extends GridView {
    private WordAdapter mWordAdapter;
    private List<WordButton> mWordButtonList;

    private Context mContext;
    private LayoutInflater mInflater;
    private IWordButtonClickListener mOnWordButtonClickListener;

    public static final int COUNT_WORDS = 24;

    public WordGridView(Context context) {
        this(context, null);
    }

    public WordGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WordGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    private class WordAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mWordButtonList.size();
        }

        @Override
        public Object getItem(int position) {
            return mWordButtonList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final WordButton holder;
            if (convertView == null) {
                holder = new WordButton();
                convertView = mInflater.inflate(R.layout.item_gridview, parent, false);
                holder.setWordButton((Button) convertView);
                holder.setIndex(position);
                convertView.setTag(holder);
            } else {
                holder = (WordButton) convertView.getTag();
            }

            holder.getWordButton().setText(mWordButtonList.get(position).getWordText());

            //加载动画
            Animation mScaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale);
            mScaleAnimation.setStartOffset(position * 100);  //设置动画延迟时间
            convertView.startAnimation(mScaleAnimation);

            //相应点击事件
            holder.getWordButton().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnWordButtonClickListener != null) {
                        mOnWordButtonClickListener.onWordButtonClick(holder);
                    }
                }
            });
            return convertView;
        }
    }

    public void updateData(List<WordButton> wordButtonList) {
        this.mWordButtonList = wordButtonList;

        mWordAdapter = new WordAdapter();
        setAdapter(mWordAdapter);
    }

    public void setOnWordButtonClickListener(IWordButtonClickListener onWordButtonClickListener) {
        this.mOnWordButtonClickListener = onWordButtonClickListener;
    }
}
