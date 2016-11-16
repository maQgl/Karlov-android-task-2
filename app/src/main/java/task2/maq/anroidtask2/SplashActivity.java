package task2.maq.anroidtask2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private final int statusOk = 200;

    private RequestTask mRequestTask;

    private TokenManager mTokenManager;

    private String mRequestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTokenManager = ((MainApp) getApplication()).getTokenManager();

        mRequestUrl = getString(R.string.request_url);
        Log.i("app2", "executeRequestTask");
    }

    private void startWebActivity() {
        Intent intent = new Intent(this, WebActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            mTokenManager.setToken(data.getStringExtra("token"));
            mTokenManager.setExpiresAt(data.getIntExtra("expiresIn", 0));
            mTokenManager.savePrefs();
            startShowTokenActivity();
        } else if (resultCode == 3) {
            startConnectionErrorActivity();
        }
    }

    @Override
    protected void onStop() {
        if (mRequestTask != null && !mRequestTask.isCancelled()) {
            mRequestTask.cancel(true);
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTokenManager.getToken() == null) {
            startWebActivity();
        } else {
            checkToken();
        }
    }

    private void checkToken() {
        if (mRequestTask == null) {
            mRequestTask = new RequestTask();
            mRequestTask.execute(mRequestUrl, mTokenManager.getToken());
        }
    }

    private void startShowTokenActivity() {
        Intent intent = new Intent(this, ShowTokenActivity.class);
        startActivity(intent);
        finish();
    }

    private void startConnectionErrorActivity() {
        Intent intent = new Intent(this, ConnectionErrorActivity.class);
        startActivity(intent);
        finish();
    }

    private void startWrongTokenActivity() {
        Intent intent = new Intent(this, WrongTokenActivity.class);
        startActivity(intent);
        finish();
    }

    private class RequestTask extends AsyncTask<String, Void, RequestResult> {

        @Override
        protected RequestResult doInBackground(String... params) {
            String urlString = params[0];
            String token = params[1];
            try {
                URL url = new URL(urlString + "?access_token=" + token);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int status = connection.getResponseCode();
                if (status == statusOk) {
                    return RequestResult.OK;
                }
            } catch (IOException e) {
                return RequestResult.CONNECTION_ERROR;
            }
            return RequestResult.WRONG_TOKEN;
        }

        @Override
        protected void onPostExecute(RequestResult requestResult) {
            if (!isCancelled()) {
                if (requestResult == RequestResult.OK) {
                    startShowTokenActivity();
                } else if (requestResult == RequestResult.WRONG_TOKEN) {
                    Log.i("app2", "WrongTokenActivity");
                    startWrongTokenActivity();
                } else if (requestResult == RequestResult.CONNECTION_ERROR) {
                    startConnectionErrorActivity();
                }
            }
        }
    }

    public enum RequestResult {
        OK,
        WRONG_TOKEN,
        CONNECTION_ERROR
    }
}
