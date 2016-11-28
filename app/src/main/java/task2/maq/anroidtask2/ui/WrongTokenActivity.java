package task2.maq.anroidtask2.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import task2.maq.anroidtask2.MainApp;
import task2.maq.anroidtask2.R;
import task2.maq.anroidtask2.TokenManager;

public class WrongTokenActivity extends AppCompatActivity {

    Button authButton;

    TokenManager tokenManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrong_token);
        tokenManager = ((MainApp) getApplication()).getTokenManager();

        authButton = (Button) findViewById(R.id.auth_button);

        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenManager.setToken(null);
                tokenManager.setExpiresAt(0);
                tokenManager.savePrefs();
                finish();
            }
        });
    }
}
