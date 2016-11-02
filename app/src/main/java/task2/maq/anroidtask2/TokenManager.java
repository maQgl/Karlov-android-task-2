package task2.maq.anroidtask2;

import android.content.SharedPreferences;

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
        Calendar calendar = Calendar.getInstance();
        int curTime = calendar.get(Calendar.SECOND);
        calendar.setTimeInMillis((expiresAt-curTime)*1000l);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        String hoursStr = hours < 10 ? "0" + hours : "" + hours;
        int minutes = calendar.get(Calendar.MINUTE);
        String minutesStr = minutes < 10 ? "0" + minutes : "" + minutes;
        int seconds = calendar.get(Calendar.SECOND);
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