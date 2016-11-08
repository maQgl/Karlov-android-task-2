package task2.maq.anroidtask2;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

public class TokenManager {

    private String token;

    private int expiresAt;

    private SharedPreferences pref;

    public TokenManager(SharedPreferences pref) {
        this.pref = pref;
        token = pref.getString("token", null);
        expiresAt = pref.getInt("expiresAt", 0);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresIn() {
        int curTime = (int) (Calendar.getInstance().getTimeInMillis()/1000);
        int diff = expiresAt - curTime;
        Log.i("app2", curTime + " " + expiresAt + " " + diff);
        int hours = diff / 3600;
        diff -= hours * 3600;
        String hoursStr = hours < 10 ? "0" + hours : "" + hours;
        int minutes = diff/ 60;
        diff -= minutes * 60;
        String minutesStr = minutes < 10 ? "0" + minutes : "" + minutes;
        int seconds = diff;
        String secondsStr = seconds < 10 ? "0" + seconds : "" + seconds;
        return (hoursStr+":"+minutesStr+":"+secondsStr);
    }

    public void setExpiresAt(int expiresAtseconds) {
        this.expiresAt = expiresAtseconds;

    }

    public void savePrefs() {
        pref.edit().putString("token", token).putInt("expiresAt", expiresAt).commit();
    }
}