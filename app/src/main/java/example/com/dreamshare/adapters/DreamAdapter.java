package example.com.dreamshare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.dreamshare.Dream;
import example.com.dreamshare.R;

/**
 * Created by Nancy on 5/3/2016.
 * Source cited: https://github.com/codepath/android-custom-array-adapter-demo/blob/master/app/src/main/java/com/codepath/example/customadapterdemo/CustomUsersAdapter.java
 */
public class DreamAdapter extends ArrayAdapter<Dream> {
    public DreamAdapter(Context context, ArrayList<Dream> dreams) {
        super(context, 0, dreams);
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
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvBirthYear = (TextView) convertView.findViewById(R.id.tvBirthYear);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
        TextView tvDreamDescription = (TextView) convertView.findViewById(R.id.tvDreamDescription);
        // Populate the data into the template view using the data object
        tvName.setText(dream.getName());
        tvBirthYear.setText(dream.getBirthYear() + "");
        tvLocation.setText(dream.getLocation());
        tvDreamDescription.setText(dream.getDreamDescription());

        // Return the completed view to render on screen
        return convertView;
    }
}
