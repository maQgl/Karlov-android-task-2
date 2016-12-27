package task2.maq.anroidtask2;

import android.app.Application;

import task2.maq.anroidtask2.data.DataManager;
import task2.maq.anroidtask2.data.DataManagerImpl;
import task2.maq.anroidtask2.data.local.LocalDataManager;
import task2.maq.anroidtask2.data.local.LocalDataManagerImpl;
import task2.maq.anroidtask2.data.remote.RemoteDataManager;
import task2.maq.anroidtask2.data.remote.RemoteDataManagerImpl;

public class MainApp extends Application {

    private static TokenManager tokenManager;

    private static DataManager dataManager;

    public DataManager getDataManager() {
        if (dataManager == null) {
            RemoteDataManager remoteDataManager =
                    new RemoteDataManagerImpl(getString(R.string.host), getString(R.string.posts),
                            getString(R.string.users), getTokenManager(),
                            getString(R.string.image));
            LocalDataManager localDataManager =
                    new LocalDataManagerImpl(getBaseContext());
            dataManager = new DataManagerImpl(remoteDataManager, localDataManager);
        }
        return dataManager;
    }

    public TokenManager getTokenManager() {
        if (tokenManager == null) {
            tokenManager = new TokenManager(getSharedPreferences("prefs", MODE_PRIVATE));
        }
        return tokenManager;
    }

}
