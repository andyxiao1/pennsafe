package edu.upenn.cis350.cis350project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent = getIntent();
        String user = intent.getStringExtra(LoginActivity.USERNAME_TAG);

        if (user == null) {
            intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.STATUS, LoginActivity.ERROR);
            startActivity(intent);
        } else {
            ((TextView) findViewById(R.id.user_text)).setText(user);
        }

    }

    public void onLogoutClick(View view) {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_LOGIN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(LoginActivity.PREFS_LOGGED_IN, false);
        editor.remove(LoginActivity.PREFS_LOGGED_IN_USER);
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
