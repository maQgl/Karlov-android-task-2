package task2.maq.anroidtask2;

import android.app.Application;

public class MainApp extends Application {

    private static TokenManager tokenManager;

    private boolean connectionError;

    public TokenManager getTokenManager() {
        if (tokenManager == null) {
            tokenManager = new TokenManager(getSharedPreferences("prefs", MODE_PRIVATE));
        }
        return tokenManager;
    }

    public boolean isConnectionError() {
        return connectionError;
    }

    public void setConnectionError(boolean connectionError) {
        this.connectionError = connectionError;
    }
}
