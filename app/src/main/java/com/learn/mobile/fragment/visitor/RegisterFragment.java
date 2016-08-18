package com.learn.mobile.fragment.visitor;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.event.DEventType;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.service.SUser;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {
    private OnFragmentInteractionListener mListener;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private static final String TIME_PATTERN = "HH:mm";
    private View rootView;

    private int month;
    private int day;
    private int year;
    private String gender;

    private boolean bUploadAvatar = true;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tb_birthday);
        textView.setOnClickListener(this);

        Button button = (Button) view.findViewById(R.id.bt_show_login);
        button.setOnClickListener(this);

        button = (Button) view.findViewById(R.id.bt_end_step_info);
        button.setOnClickListener(this);

        Spinner spinner = (Spinner) view.findViewById(R.id.cb_gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender, R.layout.simple_spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        rootView = view;
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(view);
        }
    }

    public void setLister(Context context) {
        if (mListener == null) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_birthday:
                DatePickerDialog dialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show(getFragmentManager(), "datePicker");
                break;
            case R.id.bt_show_login:
                onButtonPressed(v);
                break;
            case R.id.bt_end_step_info:
                onRegister(v);
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        month = monthOfYear;
        this.year = year;
        day = dayOfMonth;
        updateTime();
    }

    private void updateTime() {
        TextView textView = (TextView) rootView.findViewById(R.id.tb_birthday);
        textView.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gender = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View view);
    }

    private void onRegister(View v) {
        SUser sUser = (SUser) DMobi.getService(SUser.class, SUser.SIGNUP_TAG);

        HashMap<String, Object> registerData = new HashMap<String, Object>();

        // get full name field
        EditText editText = (EditText) rootView.findViewById(R.id.tb_fullName);
        String str = editText.getText().toString();
        if (DUtils.isEmpty(str)) {
            DMobi.alert(getActivity(), "Full name can not be null.");
            return;
        }
        registerData.put("full_name", str);

        // get full name field
        editText = (EditText) rootView.findViewById(R.id.tb_email);
        str = editText.getText().toString();
        if (DUtils.isEmpty(str)) {
            DMobi.alert(getActivity(), "Email can not be null.");
            return;
        }
        registerData.put("email", str);

        // get password field
        editText = (EditText) rootView.findViewById(R.id.tb_password);
        String sPassword = editText.getText().toString();
        if (DUtils.isEmpty(sPassword)) {
            DMobi.alert(getActivity(), "Password can not be null.");
            return;
        }
        registerData.put("password", sPassword);

        // get password confirm field
        editText = (EditText) rootView.findViewById(R.id.tb_password_confirm);
        String sPasswordConfirm = editText.getText().toString();
        if (DUtils.isEmpty(sPasswordConfirm)) {
            DMobi.alert(getActivity(), "Password confirm can not be null.");
            return;
        }

        if (!sPassword.equals(sPasswordConfirm)) {
            DMobi.alert(getActivity(), "Password not match.");
            return;
        }

        if (DUtils.isEmpty(month) || DUtils.isEmpty(year) || DUtils.isEmpty(day)) {
            DMobi.alert(getActivity(), "Birthday can not be null.");
            return;
        }

        registerData.put("month", month);
        registerData.put("year", year);
        registerData.put("day", day);

        if (DUtils.isEmpty(gender)) {
            DMobi.alert(getActivity(), "Gender can not be null.");
            return;
        }

        registerData.put("gender", gender);

        /* CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.cb_team_and_service);
        if (!checkBox.isChecked()) {
            DMobi.alert(getActivity(), "You must accept with our team and service.");
            return;
        } */

        sUser.setRegisterData(registerData);

        if (bUploadAvatar) {
            onButtonPressed(v);
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Register....");
            progressDialog.setMessage("Wait some minute...");
            progressDialog.show();
            sUser.register(new DResponse.Complete() {
                @Override
                public void onComplete(Boolean status, Object o) {
                    progressDialog.hide();
                    if (o != null) {
                        DMobi.fireEvent(DEventType.EVENT_UPDATE_PROFILE, o);
                        DMobi.fireEvent(DEventType.EVENT_LOADMORE_FEED, o);
                        DMobi.fireEvent(DEventType.EVENT_LOGIN_SUCCESS, o);
                        DMobi.showToast("Register successfully.");
                        getActivity().finish();
                    }
                }
            });
        }

    }
}
