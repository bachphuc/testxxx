package com.learn.turtorial1;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.learn.turtorial1.library.dmobi.DMobi;
import com.learn.turtorial1.library.dmobi.global.DConfig;
import com.learn.turtorial1.library.dmobi.request.Dresponse;
import com.learn.turtorial1.service.SUser;

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
        // Handle action bar item clicks here. The action bar will
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
        switch (v.getId()){
            case R.id.bt_login:
                onLogin();
                break;
        }
    }

    private void onLogin(){
        SUser sUser = (SUser) DMobi.getService(SUser.class);
        EditText emailInput = (EditText)findViewById(R.id.tb_email);
        EditText passwordInput = (EditText)findViewById(R.id.tb_password);
        sUser.login(emailInput.getText().toString(), passwordInput.getText().toString(), new Dresponse.Complete() {
            @Override
            public void onComplete(Object o) {
                boolean bLogin = (boolean)o;
                if(bLogin){
                    Toast toast = Toast.makeText(DConfig.getContext(), "Login successfully.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
                else{
                    Toast toast = Toast.makeText(DConfig.getContext(), "Login fail. Please check your email and password again.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
