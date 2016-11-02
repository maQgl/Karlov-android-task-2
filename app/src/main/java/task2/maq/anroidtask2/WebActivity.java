package task2.maq.anroidtask2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Calendar;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    private String clientId;

    private String authUrl;

    private String redirectUrl;

    private TokenManager tokenManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        webView = (WebView) findViewById(R.id.web_view);
        redirectUrl = getString(R.string.redirect_url);
        authUrl = getString(R.string.auth_url);
        clientId = getString(R.string.client_id);
        tokenManager = ((MainApp) getApplication()).getTokenManager();
    }

    private void startConnectionErrActivity() {
        Intent intent = new Intent(this, ConnectionErrActivity.class);
        startActivity(intent);
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
                int seconds = Calendar.getInstance().get(Calendar.SECOND);
                int expiresIn = Integer.parseInt(urls[4].split("&")[0]);
                tokenManager.setToken(urls[1].split("&")[0]);
                tokenManager.setExpiresAt(seconds+expiresIn);
                finish();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            startConnectionErrActivity();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }
}
