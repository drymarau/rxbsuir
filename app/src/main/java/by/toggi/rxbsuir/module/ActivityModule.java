package by.toggi.rxbsuir.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import by.toggi.rxbsuir.Activity;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Provides
    @Activity
    Context provideActivityContext() {
        return mActivity;
    }

    @Provides
    @Activity
    AppCompatActivity provideActivity() {
        return mActivity;
    }

}
