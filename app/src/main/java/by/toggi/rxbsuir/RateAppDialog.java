package by.toggi.rxbsuir;

import android.content.Context;
import android.content.SharedPreferences;

import com.afollestad.materialdialogs.MaterialDialog;

public class RateAppDialog {

    private static final String PREFERENCE_SUFFIX = ".rad_preferences";
    private static final String KEY_LAUNCH_COUNT = "launch_count";
    private static final String KEY_SHOULD_SHOW = "should_show";

    private RateAppDialog() {
    }

    /**
     * Shows Rate App dialog based on launch counts.
     *
     * @param context the context
     */
    public static void show(Context context) {
        int maxCount = context.getResources().getInteger(R.integer.rad_max_count);
        int repeatCount = context.getResources().getInteger(R.integer.rad_repeat_count);
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName() + PREFERENCE_SUFFIX,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean shouldShow = preferences.getBoolean(KEY_SHOULD_SHOW, true);
        int launchCount = preferences.getInt(KEY_LAUNCH_COUNT, 0);
        if (shouldShow) {
            launchCount++;
            editor.putInt(KEY_LAUNCH_COUNT, launchCount).apply();
            if (launchCount >= maxCount) disableDialog(editor);
            if (launchCount % repeatCount == 0) {
                getDialog(context, editor).show();
            }
        }
    }

    private static MaterialDialog getDialog(Context context, SharedPreferences.Editor editor) {
        return new MaterialDialog.Builder(context)
                .title(R.string.rad_title)
                .content(R.string.rad_content)
                .positiveText(R.string.rad_positive)
                .negativeText(R.string.rad_negative)
                .neutralText(R.string.rad_neutral)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        Utils.openPlayStorePage(context);
                        disableDialog(editor);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        disableDialog(editor);
                    }
                }).build();
    }

    private static void disableDialog(SharedPreferences.Editor editor) {
        editor.putBoolean(KEY_SHOULD_SHOW, false).apply();
    }

}
