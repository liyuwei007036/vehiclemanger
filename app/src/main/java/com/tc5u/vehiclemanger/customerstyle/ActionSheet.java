package com.tc5u.vehiclemanger.customerstyle;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tc5u.vehiclemanger.R;

/**
 * 仿IOS选择照片
 */
public class ActionSheet {

    public static final int CHOOSE_PICTURE = 100;
    public static final int TAKE_PICTURE = 200;
    public static final int CANCEL = 300;

    public interface OnActionSheetSelected {
        void onClick(int whichButton);
    }

    public ActionSheet() {
    }

    public static Dialog showSheet(Context context, final OnActionSheetSelected actionSheetSelected, OnCancelListener cancelListener) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheet);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.view_action_sheet, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        TextView picTextView = layout.findViewById(R.id.picTextView);
        TextView camTextView = layout.findViewById(R.id.camTextView);
        TextView cancelTextView = layout.findViewById(R.id.cancelTextView);


        picTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSheetSelected.onClick(CHOOSE_PICTURE);
                dialog.dismiss();
            }
        });

        camTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSheetSelected.onClick(TAKE_PICTURE);
                dialog.dismiss();
            }
        });
        cancelTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSheetSelected.onClick(CANCEL);
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(lp);
        dialog.setCanceledOnTouchOutside(true);
        if (cancelListener != null)
            dialog.setOnCancelListener(cancelListener);

        dialog.setContentView(layout);
        dialog.show();

        return dialog;
    }

}
