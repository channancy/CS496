package example.com.dreamshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nancy on 5/3/2016.
 * Source cited: https://github.com/codepath/android-custom-array-adapter-demo/blob/master/app/src/main/java/com/codepath/example/customadapterdemo/CustomUsersAdapter.java
 */
public class DreamAdapter extends ArrayAdapter<Dream> implements View.OnClickListener {

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
        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);

        // Populate the data into the template view using the data object
        tvUser.setText(dream.getUser());
        tvDescription.setText(dream.getDescription());
        tvDate.setText(dream.getDate());

        // Lookup view for buttons and set listeners
        TextView tvProfile = (TextView) convertView.findViewById(R.id.tvProfile);
        tvProfile.setOnClickListener(this);

        TextView tvEdit = (TextView) convertView.findViewById(R.id.tvEdit);
        tvEdit.setOnClickListener(this);

        TextView tvDelete = (TextView) convertView.findViewById(R.id.tvDelete);
        tvDelete.setOnClickListener(this);

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tvProfile:
                Toast.makeText(c, "Clicked Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvEdit:
                Toast.makeText(c, "Clicked Edit", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvDelete:

                DeleteTask deleteTask = new DeleteTask(c);
                deleteTask.execute();

                Toast.makeText(c, "Clicked Delete", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }

    // AsyncTask (do not perform networking operation on the main thread)
    private class DeleteTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private ProgressDialog mProgressDialog;

        public DeleteTask(Context context) {
            mContext = context;
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

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://dreamshare2-1314.appspot.com/dream/5644406560391168");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection httpCon = null;
            try {
                httpCon = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpCon.setDoOutput(true);
            httpCon.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded" );
            try {
                httpCon.setRequestMethod("DELETE");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            try {
                httpCon.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
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

        }
    }
}
