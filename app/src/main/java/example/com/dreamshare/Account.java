package example.com.dreamshare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Account extends AppCompatActivity {

    // Session Manager Class
    private SessionManager session;
    private String fname;
    private String lname;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        // Check if user is logged in
        session.checkLogin();

        // Get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // first name
        fname = user.get(SessionManager.KEY_FNAME);

        // last name
        lname = user.get(SessionManager.KEY_LNAME);

        // email
        email = user.get(SessionManager.KEY_EMAIL);

        // Lookup views
        TextView username = (TextView) findViewById(R.id.tvName);
        TextView useremail = (TextView) findViewById(R.id.tvEmail);

        // Set text
        username.setText(fname + " " + lname);
        useremail.setText(email);
    }

    // Logout
    @OnClick(R.id.logoutButton)
    void OnClick() {
        session.logoutUser();
    }
}
