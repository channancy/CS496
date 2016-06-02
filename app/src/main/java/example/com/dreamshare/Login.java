package example.com.dreamshare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    // TAG for debugging with Log
    private static final String TAG = Login.class.getSimpleName();

    // ButterKnife find and cast views
    @BindView(R.id.loginEmail)
    public EditText email;
    @BindView(R.id.loginPassword)
    public EditText password;

    private String emailText;
    private String passwordText;
    private String getUrl;
    private String jsonData;
    private String userFname;
    private String userLname;
    private String userEmail;
    private String correctPassword;

    // SessionManager class
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // SessionManager class instance
        session = new SessionManager(getApplicationContext());
    }

    // AsyncTask (do not perform networking operation on the main thread)
    private class authenticateUser extends AsyncTask<Void, Void, Void> {

        // Make GET request and store JSON response
        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getUrl)
                    .build();

            try {
                // Synchronous GET with OkHttp since we are in AsyncTask
                Response response = client.newCall(request).execute();
                // Convert to string using OkHttp string() method
                jsonData = response.body().string();
                Log.v("Response was", jsonData);

            } catch (IOException e) {
                Log.v(TAG, "Error doInBackground", e);
            }

            return null;
        }

        // Parse JSON and check password
        @Override
        protected void onPostExecute(Void param) {

            JSONObject userObject = null;
            try {
                userObject = new JSONObject(jsonData);
                userFname = userObject.getString("fname");
                userLname = userObject.getString("lname");
                userEmail = userObject.getString("email");
                correctPassword = userObject.getString("password");

                // If password does not match database
                if (!passwordText.equals(correctPassword)) {
                    Toast.makeText(Login.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                }
                // Correct password
                else {
                    // Set session variables
                    session.createLoginSession(userFname, userLname, userEmail);

                    // Redirect to GetDreams
                    Intent intent = new Intent(Login.this, GetDreams.class);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                Log.v(TAG, "Error onPostExecute", e);
            }
        }
    }

    @OnClick(R.id.loginButton)
    void onClick() {
        boolean valid = true;

        // Get user input
        emailText = email.getText().toString();
        passwordText = password.getText().toString();

        // Check if user left any fields blank
        if (emailText.length() == 0 || passwordText.length() == 0) {

            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        getUrl = "http://dreamshare3-1328.appspot.com/users/email/" + emailText;

        // If user input is valid, try to authenticate user
        if (valid == true) {
            try {
                new authenticateUser().execute();
            } catch (Exception e) {
                Log.v(TAG, "Error OnClick", e);
            }
        }
    }
}
