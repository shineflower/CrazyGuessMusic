package com.jackie.guessmusic.utils;

import android.content.Context;

import com.jackie.guessmusic.data.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jackie on 2016/3/12.
 * 保存和加载数据
 */
public class DataUtils {

    private static final String SAVE_DATA_FILE_NAME = "data.dat";

    public static final int INDEX_LOAD_DATA_STAGE = 0;

    public static final int INDEX_LOAD_DATA_COIN = 1;

    /**
     * 保存数据
     * @param context               上下文
     * @param currentStageIndex     当前关索引
     * @param coins                 当前金币
     */
    public static void saveData(Context context, int currentStageIndex, int coins) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(SAVE_DATA_FILE_NAME, Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);

            dos.writeInt(currentStageIndex);
            dos.writeInt(coins);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载数据
     * @param context               上下文
     */
    public static  int[] loadData(Context context) {
        FileInputStream fis = null;

        int data[] = { 0, Constants.TOTAL_COINS };
        try {
            fis = context.openFileInput(SAVE_DATA_FILE_NAME);
            DataInputStream dis = new DataInputStream(fis);

            data[INDEX_LOAD_DATA_STAGE] = dis.readInt();
            data[INDEX_LOAD_DATA_COIN] = dis.readInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }
}
