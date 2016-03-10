package com.jackie.guessmusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.jackie.guessmusic.R;
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

    public static final int COUNT_WORDS = 24;

    public WordGridView(Context context) {
        this(context, null);
    }

    public WordGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WordGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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
            WordButton holder;
            if (convertView == null) {
                holder = new WordButton();
                convertView = mInflater.inflate(R.layout.item_gridview, parent, false);
                holder.setWordButton((Button) convertView);
                convertView.setTag(holder);
            } else {
                holder = (WordButton) convertView.getTag();
            }

            holder.getWordButton().setText(mWordButtonList.get(position).getWordText());
            return convertView;
        }
    }

    public void updateData(List<WordButton> wordButtonList) {
        this.mWordButtonList = wordButtonList;

        mWordAdapter = new WordAdapter();
        setAdapter(mWordAdapter);
    }
}
