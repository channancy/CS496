package example.com.dreamshare;

/**
 * Image source: http://thinkspacecreative.com/wp-content/uploads/2015/02/Dream_Icon-01-e1453912253309.png
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Start extends AppCompatActivity {

    // Session Manager Class
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        // If logged in, skip Login/Register and go to GetPublicDreams
        if (session.isLoggedIn()) {
            Intent intent = new Intent(this, GetPublicDreams.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call in onStart() so that if logged in, skip Login/Register and go to GetPublicDreams
        // whenever we return to this activity (example: hitting back button)
        if (session.isLoggedIn()) {
            Intent intent = new Intent(this, GetPublicDreams.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.loginButton)
    void OnClickLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @OnClick(R.id.registerButton)
    void OnClickRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
