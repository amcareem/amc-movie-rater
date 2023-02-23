package csdmovie.assignment.controller;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import csdmovie.assignment.R;
import csdmovie.assignment.database.DBQuery;
import csdmovie.assignment.model.Admin;
import csdmovie.assignment.model.Major;
import csdmovie.assignment.model.User;
import csdmovie.assignment.model.UserManager;
import csdmovie.assignment.model.UserManagerSingleton;

public class AdminRegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }
    /**
     * Attempts to register the user when they click on register after filling out the required
     * fields.
     *
     * @param v The view that called this function (the REGISTER button)
     *
     * @return True if the user was succesfully resgistered, false otherwise
     */
    public boolean onAdminRegisterButtonClicked(View v) {
        EditText userIDText      = (EditText) findViewById(R.id.registrationNameText);
        EditText passText        = (EditText) findViewById(R.id.registrationPassText);
        EditText reEnterPassText = (EditText) findViewById(R.id.registrationReEnterPassText);
        EditText secretText      = (EditText) findViewById(R.id.registrationSecretText);
        if (!secretText.getText().toString().equals("csdmovie")) {
            CharSequence text = "Incorrect secret";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userIDText.getText().toString().isEmpty()
                || passText.getText().toString().isEmpty()
                || reEnterPassText.getText().toString().isEmpty()) {
            CharSequence text = getString(R.string.enter_all_fields);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

            return false;
        }
/**
 * Author:    Ahamed Careem
 **/
        String userName          = userIDText.getText().toString();
        String password        = passText.getText().toString();
        String reEnterPassword = reEnterPassText.getText().toString();

        if (!password.equals(reEnterPassword)) {
            CharSequence text = getString(R.string.registration_passwords_do_not_match);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return false;
        }

        UserManager um = UserManagerSingleton.getUserManager();
        DBQuery query = new DBQuery();

        if (um.findUserByID(userName) != null) {
            CharSequence text = getString(R.string.registration_username_taken);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return false;
        }

        int userID = query.addUser(userName, password, null, true);
        um.addUser(new Admin(userID, userName, password));

        Log.d("Registration Activity", "UserID = " + userID);

        startActivity(new Intent(this, OpeningScreenActivity.class));

        return true;
    }
    /**
     * Cancels the login request (does not add a new user) and takes the user back to the opening
     * screen.
     *
     * @param v The view that called this method (the CANCEL button)
     */
    public void cancelRequest(View v) {
        finish();
    }
}
