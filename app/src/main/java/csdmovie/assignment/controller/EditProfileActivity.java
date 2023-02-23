package csdmovie.assignment.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import csdmovie.assignment.R;
import csdmovie.assignment.database.DBQuery;
import csdmovie.assignment.model.Major;
import csdmovie.assignment.model.User;
import csdmovie.assignment.model.UserManager;
import csdmovie.assignment.model.UserManagerSingleton;

public class EditProfileActivity extends AppCompatActivity {

    private User currentUser = UserManagerSingleton.getUser();
    private String oldID;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        oldID = currentUser.getUserName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText userNameText = (EditText) findViewById(R.id.editProfileNameEdit);
        EditText bioText = (EditText) findViewById(R.id.editProfileBioEdit);

        userNameText.setText(currentUser.getUserName());
        bioText.setText(currentUser.getBio());

        Spinner majorSelector = (Spinner) findViewById(R.id.editProfileMajorSelector);
        ArrayAdapter<Major> adapter = new ArrayAdapter<Major>(this,
                android.R.layout.simple_spinner_dropdown_item, Major.values());
        majorSelector.setAdapter(adapter);
        majorSelector.setSelection(adapter.getPosition(currentUser.getMajor()));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Records the changes the user made to their profile.
     *
     * @param v The view that called this method (the SAVE CHANGES button)
     */
    public void onSaveChangesButtonPressed(View v) {
        EditText userNameText = (EditText) findViewById(R.id.editProfileNameEdit);
        EditText bioText = (EditText) findViewById(R.id.editProfileBioEdit);

        Spinner majorSelector = (Spinner) findViewById(R.id.editProfileMajorSelector);

        String newUserName = userNameText.getText().toString();
        String newBio = bioText.getText().toString();
        Major newMajor = (Major) majorSelector.getSelectedItem();

        UserManager um = UserManagerSingleton.getUserManager();

        if (newUserName.isEmpty()) {
            CharSequence text = getString(R.string.enter_all_fields);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        } else if (um.findUserByID(newUserName) != null && um.findUserByID(newUserName).getUserID() != currentUser.getUserID()) {
            CharSequence text = "Username already taken";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        } else {
            currentUser.setUserName(newUserName);
            currentUser.setMajor((Major) majorSelector.getSelectedItem());
            currentUser.setBio(newBio);

            int currentUserID = currentUser.getUserID();
            DBQuery query = new DBQuery();
            query.editUserName(currentUserID, newUserName);
            query.editMajor(currentUserID, newMajor);

            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditProfile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://csdmovie.assignment.controller/http/host/path")
        );
        //AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();


        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditProfile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://csdmovie.assignment.controller/http/host/path")
        );
        //AppIndex.AppIndexApi.end(client, viewAction);
        //client.disconnect();
    }

    /**
     * Sends user to password change activity upon pressing the button
     * @param v the view clicked within the button
     */
    public void onChangePasswordPressed(View v) {
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }
}
