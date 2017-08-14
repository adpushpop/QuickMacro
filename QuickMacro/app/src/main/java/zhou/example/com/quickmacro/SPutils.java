package zhou.example.com.quickmacro;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhou on 2017/8/9.
 */

public class SPutils {
    private static String RECORDING = "recording";

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(RECORDING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(RECORDING, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }
}
