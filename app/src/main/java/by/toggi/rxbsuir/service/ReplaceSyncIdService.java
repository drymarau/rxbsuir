package by.toggi.rxbsuir.service;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences.Preference;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.dagger.PerService;
import dagger.android.AndroidInjection;
import dagger.android.ContributesAndroidInjector;

/**
 * This IntentService runs by the system upon package update to switch values of sync id.
 * This is due unexpected API change, where bsuir.by now uses id for endpoints, rather than name
 */
public class ReplaceSyncIdService extends IntentService {

  @Inject @Named(PreferenceHelper.FAVORITE_SYNC_ID) Preference<String> mFavoriteSyncIdPreference;
  @Inject @Named(PreferenceHelper.FAVORITE_IS_GROUP_SCHEDULE) Preference<Boolean>
      mFavoriteIsGroupSchedulePreference;
  @Inject @Named(PreferenceHelper.SYNC_ID) Preference<String> mSyncIdPreference;
  @Inject @Named(PreferenceHelper.IS_GROUP_SCHEDULE) Preference<Boolean> mIsGroupSchedulePreference;

  public ReplaceSyncIdService() {
    super(ReplaceSyncIdService.class.getSimpleName());
  }

  @Override public void onCreate() {
    AndroidInjection.inject(this);
    super.onCreate();
  }

  @Override protected void onHandleIntent(Intent intent) {
    changeSyncId(mSyncIdPreference, mIsGroupSchedulePreference);
    changeSyncId(mFavoriteSyncIdPreference, mFavoriteIsGroupSchedulePreference);
    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        .edit()
        .putBoolean(PreferenceHelper.IS_SURPRISE_API_FIX_APPLIED, true)
        .apply();
  }

  private void changeSyncId(Preference<String> syncIdPreference,
      Preference<Boolean> isGroupSchedulePreference) {
    var b = isGroupSchedulePreference.get();
    if (b != null && b) {
    }
  }

  @dagger.Module public interface Module {

    @PerService @ContributesAndroidInjector ReplaceSyncIdService contribute();
  }
}
