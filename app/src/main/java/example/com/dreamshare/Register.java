package example.com.dreamshare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    // TAG for debugging with Log
    private static final String TAG = Register.class.getSimpleName();

    // ButterKnife find and cast view
    @BindView(R.id.firstName)
    public EditText fname;
    @BindView(R.id.lastName)
    public EditText lname;
    @BindView(R.id.registerEmail)
    public EditText email;
    @BindView(R.id.loginPassword)
    public EditText password;
    @BindView(R.id.confirmPassword)
    public EditText confirmPassword;

    private String fnameText;
    private String lnameText;
    private String emailText;
    private String passwordText;
    private String confirmPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    // Make POST request
    // OkHttp recipe: https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/PostForm.java
    OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {
        RequestBody formBody = new FormBody.Builder()
                .add("fname", fnameText)
                .add("lname", lnameText)
                .add("email", emailText)
                .add("password", passwordText)
                .build();
        Request request = new Request.Builder()
                .url("http://dreamshare3-1328.appspot.com/users")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        // Use OkHttp's async method
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("Response was", response.body().string());
            }
        });
    }

    // ButterKnife OnClick
    @OnClick(R.id.registerButton)
    void OnClick() {
        boolean valid = true;

        // Get user input
        fnameText = fname.getText().toString();
        lnameText = lname.getText().toString();
        emailText = email.getText().toString();
        passwordText = password.getText().toString();
        confirmPasswordText = confirmPassword.getText().toString();

        // Check if user left any fields blank
        if (fnameText.length() == 0 || lnameText.length() == 0 || emailText.length() == 0 || passwordText.length() == 0 || confirmPasswordText.length() == 0) {

            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        // Check if user entered a valid password
        if (passwordText.length() < 5 || passwordText.length() > 20) {
            Toast.makeText(this, "Password length must be between 5 and 20 characters.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        // Check if correctly confirmed password
        if (!passwordText.equals(confirmPasswordText)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        // Run POST request if user input is valid
        if (valid == true) {
            try {
                run();

                Toast.makeText(this, "Successfully registered!", Toast.LENGTH_SHORT).show();

                // Clear fields
                fname.setText("");
                lname.setText("");
                email.setText("");
                password.setText("");
                confirmPassword.setText("");
                // Move cursor back to first name field
                fname.requestFocus();

            } catch (Exception e) {
                Log.v(TAG, "Error OnClick", e);
            }
        }
    }
}
