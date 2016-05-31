package example.com.dreamshare;

/**
 * Sources cited:
 * https://androidkennel.org/android-tutorial-getting-the-users-location/
 * http://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
 * http://developer.android.com/training/permissions/requesting.html
 * http://stackoverflow.com/questions/28135338/okhttp-library-networkonmainthreadexception-on-simple-post
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDream extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText name;
    private EditText birth_year;
    private EditText location;
    private EditText description;

    private String namePost;
    private String birth_year_Post;
    private String locationPost;
    private String descriptionPost;

    private GoogleApiClient googleApiClient;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_dream);

        // ButterKnife generates code to perform view look-ups
        // and configure listeners into methods, etc.
        // https://github.com/JakeWharton/butterknife
        ButterKnife.bind(this);

        // Find views without ButterKnife for the sake of practice
        name = (EditText) findViewById(R.id.nameField);
        birth_year = (EditText) findViewById(R.id.birthYearField);
        location = (EditText) findViewById(R.id.locationField);
        description = (EditText) findViewById(R.id.dreamDescriptionField);

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        // Request permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    // Get latitude and longitude and determine city
    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(lat, lon, 1);
                String city = addresses.get(0).getLocality();
                location.setText(city);
                Log.v("Main2", city);

            } catch (IOException e) {
                Log.d("Main2", "Error onConnected", e);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    // Make POST request
    // OkHttp recipe: https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/PostForm.java
    OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {
        RequestBody formBody = new FormBody.Builder()
                .add("name", namePost)
                .add("birth_year", birth_year_Post)
                .add("location", locationPost)
                .add("dream", descriptionPost)
                .build();
        Request request = new Request.Builder()
                .url("http://dreamshare-1300.appspot.com/dreams")
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
    @OnClick(R.id.submitButton)
    void OnClick() {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        boolean valid = true;

        // Get user input
        namePost = name.getText().toString();
        birth_year_Post = birth_year.getText().toString();
        locationPost = location.getText().toString();
        descriptionPost = description.getText().toString();

        // Check if user left any fields blank
        if (namePost.length() == 0 || birth_year_Post.length() == 0
                || locationPost.length() == 0 || descriptionPost.length() == 0) {

            Toast.makeText(PostDream.this, "All fields are required.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        // Check if user entered a valid birth year
        if (birth_year_Post.length() > 0) {

            // Arbitrary cut off at 120 years old
            if (year - Integer.parseInt(birth_year_Post) > 120 || year - Integer.parseInt(birth_year_Post) < 0) {

                Toast.makeText(PostDream.this, "Please enter a valid birth year.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        // Run POST request if user input is valid
        if (valid == true) {
            try {
                run();

                Toast.makeText(PostDream.this, "Your dream has been shared!", Toast.LENGTH_SHORT).show();

                // Clear fields
                name.setText("");
                birth_year.setText("");
                location.setText("");
                description.setText("");
                // Move cursor back to name field
                name.requestFocus();

            } catch (Exception e) {
                Log.v("Main2", "Error OnClick", e);
            }
        }
    }
}
