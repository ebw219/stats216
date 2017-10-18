package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Kelli on 10/15/17.
 * Found on stack overflow -- https://stackoverflow.com/questions/12744337/how-to-keep-android-applications-always-be-logged-in-state
 */


public class SaveSharedPreference
{
    static final String PREF_USER_NAME = "defaultUser";
    static final String PREF_RAND_VAL = "0";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static void setRandVal(Context ctx, String randVal)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_RAND_VAL, randVal);
        editor.apply();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getRandVal(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_RAND_VAL, "");
    }
}