package csdmovie.assignment.controller;
/**
 * Author:    Ahamed Careem
 **/
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import csdmovie.assignment.R;
import csdmovie.assignment.database.DBQuery;
import csdmovie.assignment.model.Major;
import csdmovie.assignment.model.User;
import csdmovie.assignment.model.UserManager;
import csdmovie.assignment.model.UserManagerSingleton;

public class ChangePasswordActivity extends AppCompatActivity {

    private User currentUser = UserManagerSingleton.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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

    /**
     * This method directs the user after requesting to change the password
     * @param v view passed down
     */
    public void onPasswordChange(View v) {
        EditText password   = (EditText) findViewById(R.id.newPassword);
        EditText passwordReenter   = (EditText) findViewById(R.id.reEnterPassword);

        String newPassword = password.getText().toString();
        String reentry = passwordReenter.getText().toString();

        if (newPassword.isEmpty() || reentry.isEmpty()) {
            CharSequence text = getString(R.string.enter_all_fields);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(reentry)) {
            CharSequence text = getString(R.string.password_must_match);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        } else {
            currentUser.setPassword(newPassword);

            DBQuery query = new DBQuery();
            query.editPassword(currentUser.getUserID(), newPassword);

            CharSequence text = getString(R.string.password_change_successful);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Sends user back on cancelled passoord
     * @param v View passed down
     */
    public void onCancelledPasswordChange(View v) {
        finish();
    }
}
