package task2.maq.anroidtask2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final int statusOk = 200;

    private RequestTask mRequestTask;

    private TokenManager mTokenManager;

    private String mToken;

    private String mRequestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTokenManager = ((MainApp) getApplication()).getTokenManager();
        mToken = mTokenManager.getToken();
        mRequestUrl = getString(R.string.request_url);
        if (mToken == null) {
            Log.i("app2", "startWebActivity");
            startWebActivity();
        } else {
            Log.i("app2", "executeRequestTask");
            mRequestTask = new RequestTask();
            mRequestTask.execute(mRequestUrl, mToken);
        }
    }

    @Override
    protected void onStop() {
        if (mRequestTask != null && !mRequestTask.isCancelled()) {
            mRequestTask.cancel(true);
        }
        super.onStop();
    }

    private void startWebActivity(){
        Intent intent = new Intent(this, WebActivity.class);
        startActivity(intent);
    }

    private void startShowTokenActivity() {
        Intent intent = new Intent(this, ShowTokenActivity.class);
        startActivity(intent);
    }

    private void startWrongTokenActivity() {
        Intent intent = new Intent(this, WrongTokenActivity.class);
        startActivity(intent);
    }

    private void startConnectionErrActivity() {
        Intent intent = new Intent(this, ConnectionErrActivity.class);
        startActivity(intent);
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
                    mTokenManager.savePrefs();
                    startShowTokenActivity();
                } else if (requestResult == RequestResult.WRONG_TOKEN) {
                    startWrongTokenActivity();
                } else if (requestResult == RequestResult.CONNECTION_ERROR) {
                    Log.i("app2", "connection error");
                    startConnectionErrActivity();
                }
            }
        }
    }

    public enum RequestResult {
        OK,
        WRONG_TOKEN,
        CONNECTION_ERROR;
    }
}
