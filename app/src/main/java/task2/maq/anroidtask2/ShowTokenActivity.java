package task2.maq.anroidtask2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowTokenActivity extends AppCompatActivity {

    private TokenManager mTokenManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_token_layout);
        mTokenManager = ((MainApp) getApplication()).getTokenManager();
        TextView tokenView = (TextView) findViewById(R.id.token_field);
        TextView dateView = (TextView) findViewById(R.id.date_field);
        tokenView.setText(getString(R.string.token_text) + mTokenManager.getToken());
        dateView.setText(getString(R.string.date_text)+mTokenManager.getExpiresIn());
    }
}
