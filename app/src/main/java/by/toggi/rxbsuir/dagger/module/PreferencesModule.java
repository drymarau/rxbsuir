package by.toggi.rxbsuir.dagger.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;

import java.time.LocalTime;

import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.SyncIdItem;
import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;
import dagger.Module;
import dagger.Provides;

@Module public class PreferencesModule {

  @Provides @PerApp SharedPreferences provideSharedPreferences(Application application) {
    return PreferenceManager.getDefaultSharedPreferences(application);
  }

  @Provides @Named(PreferenceHelper.IS_FAM_ENABLED) boolean provideIsFamEnabled(
      SharedPreferences preferences) {
    return preferences.getBoolean(PreferenceHelper.IS_FAM_ENABLED, true);
  }

  @Provides @PerApp SubgroupFilter provideSubgroupFilter(Preference<SubgroupFilter> preference) {
    return preference.get();
  }

  @Provides @PerApp RxSharedPreferences provideRxSharedPreferences(SharedPreferences preferences) {
    return RxSharedPreferences.create(preferences);
  }

  @Provides @PerApp @Named(PreferenceHelper.SYNC_ID) Preference<String> provideRxSyncId(
      RxSharedPreferences preferences) {
    return preferences.getString(PreferenceHelper.SYNC_ID, null);
  }

  @Provides @PerApp @Named(PreferenceHelper.IS_GROUP_SCHEDULE)
  Preference<Boolean> provideRxIsGroupSchedule(RxSharedPreferences preferences) {
    return preferences.getBoolean(PreferenceHelper.IS_GROUP_SCHEDULE, true);
  }

  @Provides @PerApp @Named(PreferenceHelper.TITLE) Preference<String> provideRxTitle(
      RxSharedPreferences preferences, Application application) {
    return preferences.getString(PreferenceHelper.TITLE, application.getString(R.string.app_name));
  }

  @Provides @PerApp Preference<Integer> provideRxItemId(RxSharedPreferences preferences) {
    return preferences.getInteger(PreferenceHelper.ITEM_ID, -1);
  }

  @Provides @PerApp Preference<SubgroupFilter> provideRxSubgroupFilter(
      RxSharedPreferences preferences) {
    return preferences.getEnum(PreferenceHelper.SUBGROUP_FILTER, SubgroupFilter.BOTH,
        SubgroupFilter.class);
  }

  @Provides @PerApp @Named(PreferenceHelper.FAVORITE_SYNC_ID)
  Preference<String> provideRxFavoriteSyncId(RxSharedPreferences preferences) {
    return preferences.getString(PreferenceHelper.FAVORITE_SYNC_ID, null);
  }

  @Provides @PerApp @Named(PreferenceHelper.FAVORITE_IS_GROUP_SCHEDULE)
  Preference<Boolean> provideRxFavoriteIsGroupSchedule(RxSharedPreferences preferences) {
    return preferences.getBoolean(PreferenceHelper.FAVORITE_IS_GROUP_SCHEDULE, true);
  }

  @Provides @PerApp @Named(PreferenceHelper.FAVORITE_TITLE)
  Preference<String> provideRxFavoriteTitle(RxSharedPreferences preferences) {
    return preferences.getString(PreferenceHelper.FAVORITE_TITLE, null);
  }

  @Provides @PerApp Preference<LocalTime> provideRxNotificationTime(
      RxSharedPreferences preferences) {
    return preferences.getObject(PreferenceHelper.NOTIFICATION_TIME, LocalTime.of(7, 0),
        LocalTimeAdapter.INSTANCE);
  }

  @Provides @PerApp @Named(PreferenceHelper.NOTIFICATION_SOUND_ENABLED)
  Preference<Boolean> provideRxNotificationSoundEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean(PreferenceHelper.NOTIFICATION_SOUND_ENABLED, false);
  }

  @Provides @PerApp @Named(PreferenceHelper.IS_TODAY_ENABLED)
  Preference<Boolean> provideRxIsTodayEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean(PreferenceHelper.IS_TODAY_ENABLED, true);
  }

  @Provides @PerApp @Named(PreferenceHelper.ARE_CIRCLES_COLORED)
  Preference<Boolean> provideRxAreCirclesColored(RxSharedPreferences preferences) {
    return preferences.getBoolean(PreferenceHelper.ARE_CIRCLES_COLORED, false);
  }

  @Provides @PerApp Preference.Adapter<SyncIdItem> provideSyncIdItemAdapter(Gson gson) {
    return new SyncIdItemAdapter(gson);
  }

  private static final class LocalTimeAdapter implements Preference.Adapter<LocalTime> {

    static final LocalTimeAdapter INSTANCE = new LocalTimeAdapter();

    @Override public LocalTime get(@NonNull String key, @NonNull SharedPreferences preferences) {
      return LocalTime.parse(preferences.getString(key, "07:00"));
    }

    @Override public void set(@NonNull String key, @NonNull LocalTime localTime,
        @NonNull SharedPreferences.Editor editor) {
      editor.putString(key, localTime.toString());
    }
  }

  private static final class SyncIdItemAdapter implements Preference.Adapter<SyncIdItem> {

    private final Gson mGson;

    private SyncIdItemAdapter(Gson gson) {
      mGson = gson;
    }

    @Override @Nullable
    public SyncIdItem get(@NonNull String key, @NonNull SharedPreferences preferences) {
      return mGson.fromJson(preferences.getString(key, null), SyncIdItem.class);
    }

    @Override public void set(@NonNull String key, @NonNull SyncIdItem syncIdItem,
        @NonNull SharedPreferences.Editor editor) {
      editor.putString(key, mGson.toJson(syncIdItem));
    }
  }
}
