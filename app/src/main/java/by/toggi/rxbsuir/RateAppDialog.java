package by.toggi.rxbsuir;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.LocalDate;

/**
 * This class shows a new RateAppDialog based on first launch date,
 * launch counts and user preference. It supplies 3 buttons - Rate (opens apps Play Store page),
 * No, thanks (dismisses the dialog, and forces it to never appear again unless
 * {@link SharedPreferences} are cleared) and Later (dialog will still appear after specific amount
 * of time and launches).
 */
public class RateAppDialog {

    private static final String PREFERENCE_SUFFIX = ".rad_preferences";
    private static final String KEY_LAUNCH_COUNT = "launch_count";
    private static final String KEY_SHOULD_SHOW = "should_show";
    private static final String KEY_LAUNCH_DATE = "launch_date";

    private final Context mContext;
    private final int mMaxCount;
    private final int mRepeatCount;
    private final int mGracePeriod;
    private final SharedPreferences mPreferences;
    private final boolean mShouldShow;
    private final LocalDate mDate;
    private int mLaunchCount;

    private RateAppDialog(Context context) {
        mContext = context;
        mMaxCount = context.getResources().getInteger(R.integer.rad_max_count);
        mRepeatCount = context.getResources().getInteger(R.integer.rad_repeat_count);
        mGracePeriod = context.getResources().getInteger(R.integer.rag_grace_period_days);
        mPreferences = context.getSharedPreferences(
                mContext.getPackageName() + PREFERENCE_SUFFIX,
                Context.MODE_PRIVATE);
        mShouldShow = mPreferences.getBoolean(KEY_SHOULD_SHOW, true);
        mDate = LocalDate.parse(mPreferences.getString(KEY_LAUNCH_DATE, LocalDate.now().toString()));
        mLaunchCount = mPreferences.getInt(KEY_LAUNCH_COUNT, 0);
    }

    /**
     * New instance of RateAppDialog.
     *
     * @param context the context
     * @return the rate app dialog
     */
    public static RateAppDialog newInstance(Context context) {
        return new RateAppDialog(context);
    }

    /**
     * Shows Rate App dialog based on launch counts.
     */
    public void show() {
        if (mShouldShow) {
            putDate(mDate);
            if (LocalDate.now().isAfter(mDate.plusDays(mGracePeriod))) {
                mLaunchCount++;
                mPreferences.edit().putInt(KEY_LAUNCH_COUNT, mLaunchCount).apply();
                if (mLaunchCount >= mMaxCount) disableDialog();
                if (mLaunchCount % mRepeatCount == 0) {
                    getDialog().show();
                }
            }
        }
    }

    private AlertDialog getDialog() {
        return new MaterialAlertDialogBuilder(mContext)
                .setTitle(R.string.rad_title)
                .setMessage(R.string.rad_content)
                .setPositiveButton(R.string.rad_positive, (dialog, which) -> {
                    Utils.openPlayStorePage(mContext);
                    disableDialog();
                })
                .setNegativeButton(R.string.rad_negative, (dialog, which) -> disableDialog())
                .setNeutralButton(R.string.rad_neutral, (dialog, which) -> putDate(LocalDate.now()))
                .setOnDismissListener(dialog -> putDate(LocalDate.now()))
                .create();
    }

    private void disableDialog() {
        mPreferences.edit().putBoolean(KEY_SHOULD_SHOW, false).apply();
    }

    private void putDate(LocalDate date) {
        mPreferences.edit().putString(KEY_LAUNCH_DATE, date.toString()).apply();
    }

}
