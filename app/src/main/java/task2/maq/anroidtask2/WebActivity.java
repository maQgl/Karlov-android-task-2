package task2.maq.anroidtask2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Calendar;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    private String clientId;

    private String authUrl;

    private String redirectUrl;

    private TokenManager tokenManager;

    private String mToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        tokenManager = ((MainApp) getApplication()).getTokenManager();
        mToken = tokenManager.getToken();

        webView = (WebView) findViewById(R.id.web_view);
        redirectUrl = getString(R.string.redirect_url);
        authUrl = getString(R.string.auth_url);
        clientId = getString(R.string.client_id);
        tokenManager = ((MainApp) getApplication()).getTokenManager();
    }

    private void showConnectionError(){
        TextView textView = (TextView) findViewById(R.id.connection_error_text);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (((MainApp)getApplication()).isConnectionError()) {
            showConnectionError();
        }
        else if (mToken != null) {
            startSplashActivity();
        } else {
            webView.setWebViewClient(new OAuthWebClient());
            webView.loadUrl(authUrl+"&client_id="+clientId);
        }
    }


    private void startSplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
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
                Intent intent = new Intent(WebActivity.this, ShowTokenActivity.class);
                startActivity(intent);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            showConnectionError();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }
}
