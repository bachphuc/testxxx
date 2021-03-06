package com.learn.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.service.SUser;

public class LoginActivity extends Activity implements Button.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button) findViewById(R.id.bt_login);
        button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar photo clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                onLogin();
                break;
        }
    }

    private void onLogin() {
        SUser sUser = (SUser) DMobi.getService(SUser.class);
        EditText emailInput = (EditText) findViewById(R.id.tb_email);
        EditText passwordInput = (EditText) findViewById(R.id.tb_password);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login....");
        progressDialog.setMessage("Wait some minute...");
        progressDialog.show();

        sUser.login(emailInput.getText().toString(), passwordInput.getText().toString(), new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                progressDialog.hide();
                if (o != null) {
                    DMobi.fireEvent(Event.EVENT_UPDATE_PROFILE, o);
                    DMobi.fireEvent(Event.EVENT_LOADMORE_FEED, o);
                    DMobi.showToast("Login successfully.");
                    finish();
                }
            }
        });
    }
}
