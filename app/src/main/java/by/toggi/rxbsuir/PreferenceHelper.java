package by.toggi.rxbsuir;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import java.io.File;

public class PreferenceHelper {

    public static final String IS_GROUP_SCHEDULE = "is_group_schedule";
    public static final String IS_DARK_THEME = "is_dark_theme";
    public static final String SYNC_ID = "sync_id";
    public static final String TITLE = "title";
    public static final String ITEM_ID = "item_id";
    public static final String SUBGROUP_FILTER = "subgroup_filter";
    public static final String FAVORITE_SYNC_ID = "favorite_sync_id";
    public static final String FAVORITE_IS_GROUP_SCHEDULE = "favorite_is_group_schedule";
    public static final String FAVORITE_TITLE = "favorite_title";
    public static final String NOTIFICATION_TIME = "notification_time";
    public static final String NOTIFICATION_SOUND_ENABLED = "notification_sound_enabled";
    public static final String IS_TODAY_ENABLED = "is_today_enabled";
    public static final String IS_SURPRISE_API_FIX_APPLIED = "is_surprise_api_fix_applied";
    public static final String ARE_CIRCLES_COLORED = "are_circles_colored";

    // App widget preferences
    public static final String WIDGET_PREFERENCES = "by.toggi.rxbsuir.preferences.appwidget";
    public static final String WIDGET_SYNC_ID_ITEM = "widget_sync_id_item";

    private PreferenceHelper() {
    }

    /**
     * Gets widget preferences name.
     *
     * @param id the id
     * @return the widget preferences name
     */
    public static String getWidgetPreferencesName(int id) {
        return WIDGET_PREFERENCES + "_" + id;
    }

    /**
     * Gets widget preferences file.
     *
     * @param context the context
     * @param id      the id
     * @return the widget preferences file
     */
    public static File getWidgetPreferencesFile(Context context, int id) {
        return new File(context.getFilesDir().getParent() + "/shared_prefs/" +
                getWidgetPreferencesName(id) + ".xml");
    }

    /**
     * Gets sync id item rx preference.
     *
     * @param context the context
     * @param id the id
     * @return the sync id item rx preference
     */
    public static Preference<SyncIdItem> getSyncIdItemRxPreference(Context context, int id) {
        return getRxSharedPreferences(context, id).getObject(WIDGET_SYNC_ID_ITEM, SyncIdItemAdapter.INSTANCE);
    }

    /**
     * Gets sync id item preference.
     *
     * @param context the context
     * @param id the id
     * @return the sync id item preference
     */
    @Nullable
    public static SyncIdItem getSyncIdItemPreference(Context context, int id) {
        return getSyncIdItemRxPreference(context, id).get();
    }

    private static RxSharedPreferences getRxSharedPreferences(Context context, int id) {
        return RxSharedPreferences.create(getWidgetPreferences(context, id));
    }

    private static SharedPreferences getWidgetPreferences(Context context, int id) {
        return context.getSharedPreferences(getWidgetPreferencesName(id), Context.MODE_PRIVATE);
    }

    private static final class SyncIdItemAdapter implements Preference.Adapter<SyncIdItem> {

        static final SyncIdItemAdapter INSTANCE = new SyncIdItemAdapter();

        @Override
        @Nullable
        public SyncIdItem get(@NonNull String key, @NonNull SharedPreferences preferences) {
            return SyncIdItem.fromJson(preferences.getString(key, null));
        }

        @Override
        public void set(@NonNull String key, @NonNull SyncIdItem syncIdItem, @NonNull SharedPreferences.Editor editor) {
            editor.putString(key, syncIdItem.toJson());
        }

    }

}
