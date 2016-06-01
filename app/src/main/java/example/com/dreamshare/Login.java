package example.com.dreamshare;

import android.content.Intent;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    // TAG for debugging with Log
    private static final String TAG = Login.class.getSimpleName();

    @BindView(R.id.loginEmail) public EditText email;
    @BindView(R.id.loginPassword) public EditText password;

    private String emailText;
    private String passwordText;
    private String getUrl;
    private String jsonData;
    private String userFname;
    private String userLname;
    private String userEmail;
    private String correctPassword;

    // Session Manager Class
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Session Manager
        session = new SessionManager(getApplicationContext());
    }

    // Make GET request
    // OkHttp recipe: https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/PostForm.java
    OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {
        Request request = new Request.Builder()
                .url(getUrl)
                .build();

        Call call = client.newCall(request);

        // Use OkHttp's async method
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonData = response.body().string();
                Log.v("Response was", jsonData);

                JSONObject userObject = null;
                try {
                    userObject = new JSONObject(jsonData);
                    userFname = userObject.getString("fname");
                    userLname = userObject.getString("lname");
                    userEmail = userObject.getString("email");
                    correctPassword = userObject.getString("password");

                } catch (JSONException e) {
                    Log.v(TAG, "Error onResponse", e);
                }
            }
        });
    }

    @OnClick (R.id.loginButton) void onClick() {
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

        // Run GET request if user input is valid
        if (valid == true) {
            try {
                run();

                // If password does not match database
                if (!passwordText.equals(correctPassword)) {
                    Toast.makeText(this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                }
                // Correct password
                else {
                    session.createLoginSession(userFname, userEmail);

                    Intent intent = new Intent(this, GetDreams.class);
                    startActivity(intent);
                }

            } catch (Exception e) {
                Log.v(TAG, "Error OnClick", e);
            }
        }
    }
}
