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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nancy on 5/3/2016.
 * Sources cited:
 * https://github.com/codepath/android-custom-array-adapter-demo/blob/master/app/src/main/java/com/codepath/example/customadapterdemo/CustomUsersAdapter.java
 * http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
 * http://stackoverflow.com/questions/5339941/android-how-to-use-getapplication-and-getapplicationcontext-from-non-activity
 * http://stackoverflow.com/questions/4127725/how-can-i-remove-a-button-or-make-it-invisible-in-android
 * http://stackoverflow.com/questions/12302172/setvisibilitygone-view-becomes-invisible-but-still-occupies-space
 */
public class DreamAdapter extends ArrayAdapter<Dream> implements View.OnClickListener {

    // Context so we can work with GetPublicDreams as needed
    // (start GetPublicDreams, display toast messages in GetPublicDreams, etc)
    Context c;
    int m;

    public DreamAdapter(Context context, ArrayList<Dream> dreams, int mode) {
        super(context, 0, dreams);
        c = context;
        m = mode;
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
        // Set tag with database key for the user
        tvUsername.setTag(dream.getUser_key());
        tvUsername.setOnClickListener(this);

        // Mode 0 = Public; Mode 1 = Mine
        // If Mine, enable Edit and Delete
        if (m == 1) {
            ImageView tvEdit = (ImageView) convertView.findViewById(R.id.tvEdit);
            ImageView tvDelete = (ImageView) convertView.findViewById(R.id.tvDelete);

            // Set tag with database key for the dream
            tvEdit.setTag(dream.getKey());
            tvEdit.setOnClickListener(this);

            // Set tag with database key for the dream
            tvDelete.setTag(dream.getKey());
            tvDelete.setOnClickListener(this);
        }
        // If Public, hide Edit and Delete
        else {
            LinearLayout layout_to_hide = (LinearLayout) convertView.findViewById(R.id.layout_to_hide);
            layout_to_hide.setVisibility(View.GONE);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View v) {

        // Get tag with database key for the dream/user
        String key;
        key = (String) v.getTag();

        // Check which textView was clicked
        switch (v.getId()) {

            // Profile
            case R.id.tvUsername:
                Toast.makeText(c, "Viewing Profile for User:" + key, Toast.LENGTH_SHORT).show();
                break;

            // Edit
            case R.id.tvEdit:
                // Pass dream_key to EditDream activity
                Intent intent = new Intent(c, EditDream.class);
                intent.putExtra("dream_key", key);
                c.startActivity(intent);
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

            // Start GetPublicDreams to see results of delete
            Intent intent = new Intent(c, GetPublicDreams.class);
            c.startActivity(intent);
        }
    }
}
