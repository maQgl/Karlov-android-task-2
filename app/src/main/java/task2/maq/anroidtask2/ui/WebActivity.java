package task2.maq.anroidtask2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Calendar;

import task2.maq.anroidtask2.R;

public class WebActivity extends AppCompatActivity {

    private final int RESULT_OK = 2;

    private final int CONNECTION_ERROR = 3;

    private WebView webView;

    private String clientId;

    private String authUrl;

    private String redirectUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);


        webView = (WebView) findViewById(R.id.web_view);
        redirectUrl = getString(R.string.redirect_url);
        authUrl = getString(R.string.auth_url);
        clientId = getString(R.string.client_id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.setWebViewClient(new OAuthWebClient());
        webView.loadUrl(authUrl+"&client_id="+clientId);
    }

    private class OAuthWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("app2", url);
            if (url.startsWith(redirectUrl)) {
                String[] urls = url.split("=");
                int seconds = (int) (Calendar.getInstance().getTimeInMillis()/1000);
                int expiresIn = Integer.parseInt(urls[4].split("&")[0]);
                String token = urls[1].split("&")[0];
                Intent intent = new Intent();
                intent.putExtra("token", token);
                intent.putExtra("expiresIn", expiresIn + seconds);
                setResult(RESULT_OK, intent);
                finish();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            setResult(CONNECTION_ERROR, null);
            finish();
        }
    }
}
