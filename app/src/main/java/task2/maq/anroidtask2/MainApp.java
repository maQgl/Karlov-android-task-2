package task2.maq.anroidtask2;

import android.app.Application;

public class MainApp extends Application {

    private static TokenManager tokenManager;

    public TokenManager getTokenManager() {
        if (tokenManager == null) {
            tokenManager = new TokenManager(getSharedPreferences("prefs", MODE_PRIVATE));
        }
        return tokenManager;
    }

}
