package com.learn.mobile.customview.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.global.DConfig;

import java.util.ArrayList;

/**
 * Created by phuclb on 11/24/2015.
 */
public class SettingSiteDialog extends DialogFragment {

    private int selectedOption;
    private String[] options;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        options = getResources().getStringArray(R.array.siteHosts);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Setting Site")
                .setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
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
                        DConfig.setSetting(DConfig.SITE_URL, options[selectedOption]);
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