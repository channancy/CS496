package example.com.dreamshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nancy on 5/3/2016.
 * Source cited: https://github.com/codepath/android-custom-array-adapter-demo/blob/master/app/src/main/java/com/codepath/example/customadapterdemo/CustomUsersAdapter.java
 */
public class DreamAdapter extends ArrayAdapter<Dream> implements View.OnClickListener {

    // Context so we can work with GetDreams as needed
    // (start GetDreams, display toast messages in GetDreams, etc)
    Context c;

    public DreamAdapter(Context context, ArrayList<Dream> dreams) {
        super(context, 0, dreams);
        c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Dream dream = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dream_list_item, parent, false);
        }

        // Lookup view for data population
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);

        // Populate the data into the template view using the data object
        tvUsername.setText(dream.getUsername());
        tvDescription.setText(dream.getDescription());
        tvDate.setText(dream.getDate());
        tvLocation.setText(dream.getLocation());

        // Lookup view for buttons and set listeners
        TextView tvProfile = (TextView) convertView.findViewById(R.id.tvProfile);
        // Set tag with database key for the user
        tvProfile.setTag(dream.getUser_key());
        tvProfile.setOnClickListener(this);

        TextView tvEdit = (TextView) convertView.findViewById(R.id.tvEdit);
        // Set tag with database key for the dream
        tvEdit.setTag(dream.getKey());
        tvEdit.setOnClickListener(this);

        TextView tvDelete = (TextView) convertView.findViewById(R.id.tvDelete);
        // Set tag with database key for the dream
        tvDelete.setTag(dream.getKey());
        tvDelete.setOnClickListener(this);

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View v) {

        // Get tag with database key for the dream/user
        String key;
        key = (String) v.getTag();

        // Check which textView was clicked
        switch(v.getId()) {

            // Profile
            case R.id.tvProfile:
                Toast.makeText(c, "Viewing Profile for User:" + key, Toast.LENGTH_SHORT).show();
                break;

            // Edit
            case R.id.tvEdit:
                Toast.makeText(c, "Editing Dream:" + key, Toast.LENGTH_SHORT).show();
                break;

            // Delete
            case R.id.tvDelete:
                // Pass context and key
                DeleteTask deleteTask = new DeleteTask(c, key);
                deleteTask.execute();
                Toast.makeText(c, "Dream Deleted", Toast.LENGTH_SHORT).show();

                break;

            default:
                break;
        }
    }

    // AsyncTask (do not perform networking operation on the main thread)
    private class DeleteTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private String mKey;
        private ProgressDialog mProgressDialog;

        public DeleteTask(Context context, String key) {
            mContext = context;
            mKey = key;
        }

        // Show loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Deleting dream...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }

        // Make DELETE request
        @Override
        protected Void doInBackground(Void... params) {

            Log.v("DreamAdapter", "doInBackground dream key:" + mKey);
            String urlStr = "http://dreamshare3-1328.appspot.com/dreams/" + mKey;

            URL url = null;
            HttpURLConnection httpCon = null;

            try {
                url = new URL(urlStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpCon.setRequestMethod("DELETE");
                httpCon.connect();
                httpCon.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            // Close loading message
            mProgressDialog.dismiss();

            // Start GetDreams to see results of delete
            Intent intent = new Intent(c, GetDreams.class);
            c.startActivity(intent);
        }
    }
}
