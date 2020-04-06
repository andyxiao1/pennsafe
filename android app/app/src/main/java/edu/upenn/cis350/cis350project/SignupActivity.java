package edu.upenn.cis350.cis350project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.upenn.cis350.cis350project.api.APIHandler;
import edu.upenn.cis350.cis350project.api.APIResponse;
import edu.upenn.cis350.cis350project.api.APIResponseWrapper;
import edu.upenn.cis350.cis350project.api.LoginResponse;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    private boolean validateInputs() {
        String username = ((EditText) findViewById(R.id.username_input)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_input)).getText().toString();
        String confirm = ((EditText) findViewById(R.id.confirm_password_input)).getText().toString();
        TextView warningText = findViewById(R.id.warning_text);

        if (username.length() == 0 || password.length() == 0 || confirm.length() == 0) {
            warningText.setText("Please fill out all fields.");
            return false;
        } else if (!password.equals(confirm)) {
            warningText.setText("Passwords do not match.");
            ((EditText) findViewById(R.id.confirm_password_input)).setText("");
            return false;
        }
        warningText.setText("");
        return true;
    }

    public void onNextClick(View view) {
        ((TextView) findViewById(R.id.warning_text)).setText("");
        if (validateInputs()) {
            final String username = ((EditText) findViewById(R.id.username_input)).getText().toString();
            final String password = ((EditText) findViewById(R.id.password_input)).getText().toString();
            final ProgressBar spinner = findViewById(R.id.loading_spinner);
            spinner.setVisibility(View.VISIBLE);
            final Activity activity = this;
            APIHandler apiHandler = new APIHandler();
            apiHandler.signup(username, password, new APIResponseWrapper() {
                @Override
                public void onResponse(APIResponse response) {
                    spinner.setVisibility(View.INVISIBLE);
                    LoginResponse loginResponse = (LoginResponse) response;
                    if (loginResponse == null) {
                        ((TextView) findViewById(R.id.warning_text)).setText("Server error.");
                    } else if (loginResponse.getSuccessful()) {
                        Intent intent = new Intent(activity, HomePageActivity.class);
                        intent.putExtra(LoginActivity.USERNAME_TAG, username);
                        startActivity(intent);
                    } else {
                        ((TextView) findViewById(R.id.warning_text)).setText(loginResponse.getMessage());
                        ((EditText) findViewById(R.id.password_input)).setText("");
                        ((EditText) findViewById(R.id.confirm_password_input)).setText("");
                    }
                }
            });
        }
    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
