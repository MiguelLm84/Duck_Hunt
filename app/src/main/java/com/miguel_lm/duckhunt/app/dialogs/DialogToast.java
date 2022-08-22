package com.miguel_lm.duckhunt.app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.miguel_lm.duckhunt.R;


public class DialogToast {

    private static DialogToast instance = null;

    //Instance of the DialogToast for the creation of Toast custom.
    public static DialogToast getInstance() {
        if(instance == null){
            instance = new DialogToast();
        }
        return instance;
    }

    //Method to display custom Toast.
    public void showDialogToast(Context context, Activity activity, String text, boolean activeIcon) {

        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastView = inflater.inflate(R.layout.custom_toast, activity.findViewById(R.id.mylayout));
        TextView tv_message_toast = toastView.findViewById(R.id.tv_message_toast);
        ImageView imageview_icon = toastView.findViewById(R.id.imageview_icon);
        if(activeIcon){
            imageview_icon.setVisibility(View.VISIBLE);
        } else {
            imageview_icon.setVisibility(View.GONE);
        }
        tv_message_toast.setText(text);
        toast.setView(toastView);
        toast.show();
    }
}
