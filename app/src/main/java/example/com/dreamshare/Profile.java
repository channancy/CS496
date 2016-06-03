package example.com.dreamshare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Profile extends AppCompatActivity {

    // TAG for debugging with Log
    private static final String TAG = Profile.class.getSimpleName();
    public String jsonData;
    public ArrayList<Dream> dreams = new ArrayList<Dream>();

    private String user_key;
    private String email;
    private String getUrl;

    // SessionManager class
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        // SessionManager class instance
        session = new SessionManager(getApplicationContext());

        // Check if user is logged in
        session.checkLogin();

        // Get data that was passed to this activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_key = extras.getString("user_key");
        }

        // If no data was passed to this activity
        // (user trying to access own profile)
        // then make GET request using own email
        if (user_key == null) {
            // Get user data from session
            HashMap<String, String> user = session.getUserDetails();

            // email
            email = user.get(SessionManager.KEY_EMAIL);

            // Construct url
            getUrl = "http://dreamshare3-1328.appspot.com/dreams/email/" + email;

            Log.v("email is", email);
        }
        // Else user is trying to access another user's profile
        // then make GET request using the other user's key
        else {
            // Construct url
            getUrl = "http://dreamshare3-1328.appspot.com/dreams/users/" + user_key;

            Log.v(TAG, "Viewing Profile:" + user_key);
        }

        // Make GET request for dreams
        new JSONParse().execute();
    }

    // AsyncTask (do not perform networking operation on the main thread)
    private class JSONParse extends AsyncTask<Void, Void, Void> {

//        private ProgressDialog mProgressDialog;

        // Show loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            mProgressDialog = new ProgressDialog(GetMyDreams.this);
//            mProgressDialog.setMessage("Loading dreams...");
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setCancelable(true);
//            mProgressDialog.show();
        }

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

            } catch (IOException e) {
                Log.v(TAG, "Error doInBackground", e);
            }

            return null;
        }

        // Parse JSON and update UI (populate ListView)
        @Override
        protected void onPostExecute(Void param) {
            // Close loading message
//            mProgressDialog.dismiss();

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
                    String userFname = dreamJson.getString("userFname");
                    String userLname = dreamJson.getString("userLname");
                    String userLinitial = userLname.substring(0, 1);
                    String username = userFname + " " + userLinitial + ".";
                    String description = dreamJson.getString("description");
                    String date = dreamJson.getString("date");
                    String dateFormatted = "";
                    String location = dreamJson.getString("location");

                    // Format date
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    try {
                        Date dateParsed = dt.parse(date);
                        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                        dateFormatted = dt1.format(dateParsed);

                    } catch (ParseException e) {
                        Log.v(TAG, "Error onPostExecute", e);
                    }

                    Dream dream = new Dream(key, user_key, username, description, dateFormatted, location);

                    // Add to ArrayList
                    dreams.add(dream);

                    // Custom adapter for displaying list of dreams
                    // Pass 0 for PUBLIC view (no edit/delete)
                    DreamAdapter adapter = new DreamAdapter(Profile.this, dreams, 0);
                    ListView listView = (ListView) findViewById(R.id.dreamListView);
                    listView.setAdapter(adapter);

                    // Display name of user
                    TextView name = (TextView) findViewById(R.id.tvName);
                    name.setText(username);
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
        // whenever we return to this activity (example: hitting back button)
        new JSONParse().execute();
    }

    @OnClick(R.id.homeButton)
    void OnClickHome() {
        Intent intent = new Intent(this, GetPublicDreams.class);
        startActivity(intent);
    }

    @OnClick(R.id.profileButton)
    void OnClickProfile() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    @OnClick(R.id.accountButton)
    void OnClickAccount() {
        Intent intent = new Intent(this, Account.class);
        startActivity(intent);
    }
}
