package example.com.dreamshare;

/**
 * Sources cited:
 * http://stackoverflow.com/questions/28221555/how-does-okhttp-get-json-string/29680883#29680883
 * https://www.learn2crack.com/2013/10/android-asynctask-json-parsing-example.html
 * https://priyankacool10.wordpress.com/2014/03/30/how-to-use-asynctask-in-android/
 * http://alvinalexander.com/android/android-asynctask-void-void-void-null-parameters-signature
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetDreams extends AppCompatActivity {

    // TAG for debugging with Log
    private static final String TAG = GetDreams.class.getSimpleName();
    public String jsonData;
    public ArrayList<Dream> dreams = new ArrayList<Dream>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_dreams);

        // ButterKnife generates code to perform view look-ups
        // and configure listeners into methods, etc.
        // https://github.com/JakeWharton/butterknife
        ButterKnife.bind(this);
    }

    // AsyncTask (do not perform networking operation on the main thread)
    private class JSONParse extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgressDialog;

        // Show loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(GetDreams.this);
            mProgressDialog.setMessage("Loading dreams...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }

        // Make GET request and store JSON response
        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://dreamshare3-1328.appspot.com/dreams")
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

        // Parse JSON and update UI (populate ListView)
        @Override
        protected void onPostExecute(Void param) {
            // Close loading message
            mProgressDialog.dismiss();

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                JSONArray jsonArray = jsonObject.getJSONArray("dreams");

                JSONObject dreamJson;

                // Clear ArrayList so no duplicates of previously added dreams
                dreams.clear();

                // Go through JSON Array to get individual JSON Objects
                for (int i = 0; i < jsonArray.length(); i++) {
                    dreamJson = jsonArray.getJSONObject(i);

                    String key = dreamJson.getString("key");
                    String user_key = dreamJson.getString("user");
                    String username = dreamJson.getString("username");
                    String description = dreamJson.getString("description");
                    String date = dreamJson.getString("date");

                    Dream dream = new Dream(key, user_key, username, description, date);

                    // Add to ArrayList
                    dreams.add(dream);

                    // Custom adapter for displaying list of dreams
                    DreamAdapter adapter = new DreamAdapter(GetDreams.this, dreams);
                    ListView listView = (ListView) findViewById(R.id.dreamListView);
                    listView.setAdapter(adapter);
                }

            } catch (JSONException e) {
                Log.v(TAG, "Error onPostExecute", e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call in onStart() so that list is re-populated with current data
        // whenever we return to this activity (example: hitting back button from PostDream)
        new JSONParse().execute();
    }

    // Navigate to form to share dream
    // ButterKnife OnClick
    @OnClick(R.id.shareDreamButton) void OnClick() {
        Intent intent = new Intent(this, PostDream.class);
        startActivity(intent);
    }
}
