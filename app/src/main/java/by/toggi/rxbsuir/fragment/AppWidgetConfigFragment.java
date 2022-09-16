package by.toggi.rxbsuir.fragment;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.SyncIdItem;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.mvp.presenter.AppWidgetConfigPresenter;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;
import by.toggi.rxbsuir.mvp.view.AppWidgetConfigView;
import dagger.hilt.android.AndroidEntryPoint;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

@AndroidEntryPoint
public class AppWidgetConfigFragment extends PreferenceFragmentCompat
        implements AppWidgetConfigView, Preference.OnPreferenceClickListener {

    @Inject
    AppWidgetConfigPresenter mPresenter;
    @Inject
    com.f2prateek.rx.preferences.Preference.Adapter<SyncIdItem> mAdapter;

    private int mAppWidgetId;
    private final List<SyncIdItem> mSyncIdItemList = new ArrayList<>();
    private com.f2prateek.rx.preferences.Preference<SyncIdItem> mSyncIdItemPreference;
    private com.f2prateek.rx.preferences.Preference<SubgroupFilter> mSubgroupFilterPreference;
    private CompositeSubscription mSubscription;

    public static AppWidgetConfigFragment newInstance(int id) {
        var fragment = new AppWidgetConfigFragment();
        var args = new Bundle();
        args.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        var args = getArguments();
        if (args != null) {
            mAppWidgetId =
                    args.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        getPreferenceManager().setSharedPreferencesName(
                PreferenceHelper.getWidgetPreferencesName(mAppWidgetId));

        mSyncIdItemPreference =
                PreferenceHelper.getSyncIdItemRxPreference(getActivity(), mAppWidgetId, mAdapter);
        mSubgroupFilterPreference =
                PreferenceHelper.getSubgroupFilterRxPreference(getActivity(), mAppWidgetId);

        setPreferencesFromResource(R.xml.appwidget_preferences, rootKey);

        findPreference(PreferenceHelper.WIDGET_SYNC_ID_ITEM).setOnPreferenceClickListener(this);
        findPreference(PreferenceHelper.SUBGROUP_FILTER).setOnPreferenceClickListener(this);

        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSubscription = new CompositeSubscription(mSyncIdItemPreference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(syncIdItem -> {
                    if (syncIdItem != null) {
                        findPreference(PreferenceHelper.WIDGET_SYNC_ID_ITEM).setSummary(syncIdItem.getTitle());
                    }
                }), mSubgroupFilterPreference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subgroupFilter -> {
                    var preference = findPreference(PreferenceHelper.SUBGROUP_FILTER);
                    switch (subgroupFilter) {
                        case BOTH:
                            preference.setSummary(R.string.action_filter_both);
                            break;
                        case FIRST:
                            preference.setSummary(R.string.action_filter_first);
                            break;
                        case SECOND:
                            preference.setSummary(R.string.action_filter_second);
                            break;
                        case NONE:
                            preference.setSummary(R.string.action_filter_none);
                            break;
                    }
                }));
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.unsubscribeComposite(mSubscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case PreferenceHelper.WIDGET_SYNC_ID_ITEM:
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle(R.string.widget_sync_id_item)
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                var item = mSyncIdItemList.get(position);
                                mSyncIdItemPreference.set(item);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        })
                        .setSingleChoiceItems(getItems(), findItemIndex(), null)
                        .show();
                return true;
            case PreferenceHelper.SUBGROUP_FILTER:
                return true;
        }
        return false;
    }

    @Override
    public void updateSyncIdList(List<SyncIdItem> syncIdItemList) {
        mSyncIdItemList.clear();
        mSyncIdItemList.addAll(syncIdItemList);
        if (mSyncIdItemList.size() > 0) {
            mSyncIdItemPreference.set(mSyncIdItemList.get(0));
        } else {
            Toast.makeText(getActivity(), R.string.widget_empty_synciditem, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    private CharSequence[] getItems() {
        var size = mSyncIdItemList.size();
        var charSequences = new CharSequence[size];
        for (var i = 0; i < size; i++) {
            charSequences[i] = mSyncIdItemList.get(i).toString();
        }
        return charSequences;
    }

    private int findItemIndex() {
        return Observable.from(mSyncIdItemList)
                .filter(syncIdItem -> syncIdItem.equals(mSyncIdItemPreference.get()))
                .map(mSyncIdItemList::indexOf)
                .toBlocking()
                .firstOrDefault(0);
    }
}
