package example.com.dreamshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nancy on 5/3/2016.
 * Source cited: https://github.com/codepath/android-custom-array-adapter-demo/blob/master/app/src/main/java/com/codepath/example/customadapterdemo/CustomUsersAdapter.java
 */
public class DreamAdapter extends ArrayAdapter<Dream> implements View.OnClickListener {
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
        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        tvUser.setOnClickListener(this);

        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);

        // Populate the data into the template view using the data object
        tvUser.setText(dream.getUser());
        tvDescription.setText(dream.getDescription());
        tvDate.setText(dream.getDate());

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tvUser:
                //
        }
    }
}
