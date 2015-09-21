package com.learn.mobile.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;

/**
 * Created by 09520_000 on 9/21/2015.
 */
public class PromptTextDialog extends DialogFragment {
    private static final String TAG = PromptTextDialog.class.getSimpleName();
    View view;
    EditText editText;
    String hintText = "";
    String title = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.promp_text_dialog, null);
        editText = (EditText) view.findViewById(R.id.tb_text);
        editText.setHint(hintText);
        builder.setView(view)
                .setPositiveButton(DMobi.translate("OK"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        if (mListener != null) {
                            mListener.onDialogSuccessClick(PromptTextDialog.this, editText.getText().toString());
                        }
                        PromptTextDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(DMobi.translate("Cancel"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        if (mListener != null) {
                            mListener.onDialogCancelClick(PromptTextDialog.this);
                        }
                        PromptTextDialog.this.getDialog().cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.setTitle(DMobi.translate(title));
        return dialog;
    }

    public void setTitle(String s) {
        title = s;
    }

    public void setHintText(String s) {
        hintText = s;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogSuccessClick(DialogFragment dialog, String value);

        public void onDialogCancelClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
