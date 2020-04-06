package edu.upenn.cis350.cis350project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.upenn.cis350.cis350project.api.APIHandler;
import edu.upenn.cis350.cis350project.api.APIResponse;
import edu.upenn.cis350.cis350project.api.APIResponseWrapper;
import edu.upenn.cis350.cis350project.api.LoginResponse;

public class LoginActivity extends AppCompatActivity {

    public static final String USERNAME_TAG = "username";
    public static final String STATUS = "status";
    public static final String ERROR = "error";

    public static final String PREFS_LOGIN_DATA = "login data";
    public static final String PREFS_LOGGED_IN = "logged in";
    public static final String PREFS_LOGGED_IN_USER = "logged in user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_LOGIN_DATA, Context.MODE_PRIVATE);
        Boolean loggedIn = prefs.getBoolean(LoginActivity.PREFS_LOGGED_IN, false);
        String user = prefs.getString(LoginActivity.PREFS_LOGGED_IN_USER, null);

        if (loggedIn && user != null) {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra(LoginActivity.USERNAME_TAG, user);
            startActivity(intent);
        }

        Intent intent = getIntent();
        if (intent.getStringExtra(STATUS) != null) {
            if (intent.getStringExtra(STATUS).equals(ERROR)) {
                ((TextView) findViewById(R.id.warning_text)).setText("There was an issue signing in. Please log in again.");
            }
        }

    }

    public boolean validateLoginInputs() {
        String username = ((EditText) findViewById(R.id.username_input)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_input)).getText().toString();
        if (username.length() == 0) {
            ((TextView) findViewById(R.id.warning_text)).setText("Please enter your username.");
            return false;
        } else if (password.length() == 0) {
            ((TextView) findViewById(R.id.warning_text)).setText("Please enter your password.");
            return false;
        } else {
            return true;
        }
    }

    public void onLogin(View view) {
        if (validateLoginInputs()) {
            final String username = ((EditText) findViewById(R.id.username_input)).getText().toString();
            final String password = ((EditText) findViewById(R.id.password_input)).getText().toString();
            ProgressBar spinner = findViewById(R.id.loading_spinner);
            spinner.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.warning_text)).setText("");
            APIHandler a = new APIHandler();
            final Activity activity = this;
            a.validateLogin(username, password, new APIResponseWrapper() {
                @Override
                public void onResponse(APIResponse response) {
                    LoginResponse loginResponse = (LoginResponse) response;
                    if (loginResponse == null) {
                        ((TextView) findViewById(R.id.warning_text)).setText("Server error.");
                        ((ProgressBar) findViewById(R.id.loading_spinner)).setVisibility(View.INVISIBLE);
                    } else if (loginResponse.getSuccessful()) {
                        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_LOGIN_DATA, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(LoginActivity.PREFS_LOGGED_IN, true);
                        editor.putString(LoginActivity.PREFS_LOGGED_IN_USER, username);
                        editor.commit();
                        Intent intent = new Intent(activity, HomePageActivity.class);
                        intent.putExtra(USERNAME_TAG, username);
                        startActivity(intent);
                    } else {
                        ((TextView) findViewById(R.id.warning_text)).setText(loginResponse.getMessage());
                        ((ProgressBar) findViewById(R.id.loading_spinner)).setVisibility(View.INVISIBLE);
                        ((EditText) findViewById(R.id.password_input)).setText("");
                    }
                }
            });
        }

    }

    public void onSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}
