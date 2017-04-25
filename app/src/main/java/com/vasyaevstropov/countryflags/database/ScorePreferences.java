package com.vasyaevstropov.countryflags.database;
import android.content.Context;
import android.content.SharedPreferences;

public class ScorePreferences {

    public static final String STORAGE_NAME = "ConsecutimeWins";

    private static SharedPreferences preferences = null;
    private static SharedPreferences.Editor editor = null;
    private static Context context = null;

    public static void init(Context cntxt) {
        context = cntxt;
    }

    private static void init() {
        preferences = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void addProperty(Boolean win) {
        if (preferences == null) {
            init();
        }
        if (win) {
            int consecutimeWins = preferences.getInt("consecutimeWins", 0);
            consecutimeWins = consecutimeWins + 1;
            editor.putInt("consecutimeWins", consecutimeWins);
            editor.apply();
        } else {
            int consecutimeWins = 0;
            editor.putInt("consecutimeWins", consecutimeWins);
            editor.commit();
        }
    }

    public static Integer getProperty() {
        if (preferences == null) {
            init();
        }
        return preferences.getInt("consecutimeWins", 0);
    }
}

