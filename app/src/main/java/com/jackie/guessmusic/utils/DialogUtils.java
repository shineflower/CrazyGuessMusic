package com.jackie.guessmusic.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jackie.guessmusic.R;
import com.jackie.guessmusic.bean.IAlertDialogClickListener;

/**
 * Created by Jackie on 2016/3/12.
 * 对话框
 */
public class DialogUtils {
    private static AlertDialog mAlertDialog;

    public static void showDialog(final Context context, String message, final IAlertDialogClickListener onAlertDialogClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog_no_border);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_view, null);
        builder.setView(dialogView);

        TextView messageTextView = (TextView) dialogView.findViewById(R.id.dialog_msg);
        messageTextView.setText(message);

        ImageButton okButton = (ImageButton) dialogView.findViewById(R.id.dialog_ok);
        ImageButton cancelButton = (ImageButton) dialogView.findViewById(R.id.dialog_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlertDialog != null ) {
                    mAlertDialog.dismiss();
                }

                if (onAlertDialogClickListener != null) {
                    onAlertDialogClickListener.onClick();
                }

                //播放音效
                MediaPlayer.playTone(context, MediaPlayer.TONE_INDEX_ENTER);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlertDialog != null ) {
                    mAlertDialog.dismiss();
                }

                //播放音效
                MediaPlayer.playTone(context, MediaPlayer.TONE_INDEX_CANCEL);
            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }
}
