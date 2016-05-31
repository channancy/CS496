package example.com.dreamshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
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
