package com.learn.mobile.customview.dialog.v4;

/**
 * Created by BachPhuc on 3/18/2016.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.global.Constant;
import com.learn.mobile.library.dmobi.global.DConfig;

/**
 * Created by phuclb on 11/24/2015.
 */
public class SettingSiteDialog extends DialogFragment {
    public static final String TAG = SettingSiteDialog.class.getSimpleName();
    private int selectedOption;
    private String[] options;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        options = getResources().getStringArray(R.array.siteHosts);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int selected = 0;
        DMobi.log(TAG, DConfig.getSetting(Constant.SITE_URL));
        for (int i = 0; i < options.length; i++) {
            DMobi.log(TAG, options[i]);
            if (DConfig.getSetting(Constant.SITE_URL).equals(options[i])) {
                selected = i;
                break;
            }
        }
        // Set the dialog title
        builder.setTitle("Setting Site")
                .setSingleChoiceItems(options, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedOption = which;
                    }
                })
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        DMobi.log(TAG, "Update site host " + options[selectedOption]);
                        DConfig.setSetting(Constant.SITE_URL, options[selectedOption]);
                        DConfig.resetHostConfig();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
}
