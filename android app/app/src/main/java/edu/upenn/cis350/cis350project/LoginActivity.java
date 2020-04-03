package edu.upenn.cis350.cis350project;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public boolean validateInputs() {
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
        if (validateInputs()) {
            ProgressBar spinner = findViewById(R.id.loading_spinner);
            spinner.setVisibility(View.VISIBLE);
        }

    }

    public void onSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}
