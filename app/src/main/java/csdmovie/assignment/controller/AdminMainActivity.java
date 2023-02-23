/**
 * Author:    Ahamed Careem
 **/
package csdmovie.assignment.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import csdmovie.assignment.R;
import csdmovie.assignment.model.APIComm;
import csdmovie.assignment.model.Movie;
import csdmovie.assignment.model.MovieManager;
import csdmovie.assignment.model.User;
import csdmovie.assignment.model.UserManager;
import csdmovie.assignment.model.UserManagerSingleton;

public class AdminMainActivity extends AppCompatActivity {

    ListView userList;
    UserManager userManager;
    ArrayList<User> userArrayList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        populateUsers(null);

        context = this;
        userList = (ListView) findViewById(R.id.listView2);
        Log.d("UserList", "filled userlist.");
        userManager = UserManagerSingleton.getUserManager();


        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = ((User) userList.getItemAtPosition(position)).getUserName();
                startEdit(selection);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateUsers(null);
    }

    public void populateUsers(View v) {
        userList = (ListView) findViewById(R.id.listView2);
        userArrayList = new ArrayList<>(UserManager.getUsers());

        if (userArrayList.isEmpty()) {
            CharSequence text = "No results found";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, new String[0]);
            userList.setAdapter(adapter);
            return;
        }

        ArrayAdapter<User> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, userArrayList);
        userList.setAdapter(adapter);
    }

    public void startEdit(String selection) {
        Intent intent = new Intent(this, EditUserActivity.class);
        intent.putExtra("userId", selection);
        startActivity(intent);
    }


}
