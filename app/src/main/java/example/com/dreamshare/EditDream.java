package example.com.dreamshare;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditDream extends AppCompatActivity {

    // TAG for debugging with Log
    private static final String TAG = EditDream.class.getSimpleName();

    private String dream_key;
    private String getUrl;
    private String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dream);
        ButterKnife.bind(this);

        // Get data that was passed to this activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dream_key = extras.getString("dream_key");
        }

        Log.v(TAG, "Editing Dream:" + dream_key);

        // Make GET request for dream description using data that was passed in
        new getDescription().execute();
    }

    // AsyncTask (do not perform networking operation on the main thread)
    private class getDescription extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgressDialog;

        // Show loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(EditDream.this);
            mProgressDialog.setMessage("Retrieving dream description...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }

        // Make GET request and store JSON response
        @Override
        protected Void doInBackground(Void... params) {

            getUrl = "http://dreamshare3-1328.appspot.com/dreams/" + dream_key;

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getUrl)
                    .build();

            try {
                // Synchronous GET with OkHttp since we are in AsyncTask
                Response response = client.newCall(request).execute();
                // Convert to string using OkHttp string() method
                jsonData = response.body().string();

            } catch (IOException e) {
                Log.v(TAG, "Error doInBackground", e);
            }

            return null;
        }

        // Parse JSON and populate dream description
        @Override
        protected void onPostExecute(Void param) {
            // Close loading message
            mProgressDialog.dismiss();

            JSONObject dreamObject = null;
            try {
                dreamObject = new JSONObject(jsonData);
                String description = dreamObject.getString("description");

                // Lookup view and set text
                EditText etDescription = (EditText) findViewById(R.id.dreamDescriptionField);
                etDescription.setText(description);

            } catch(JSONException e) {
                Log.v(TAG, "Error onPostExecute", e);
            }
        }
    }

    @OnClick(R.id.editButton)
    void onClick() {

    }
}
