package by.toggi.rxbsuir.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import javax.inject.Inject;

import by.toggi.rxbsuir.fragment.ScheduleFragment;

public class SchedulePagerAdapter extends FragmentStatePagerAdapter {

    private final String[] mTabs;

    @Inject
    public SchedulePagerAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        mTabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return ScheduleFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return mTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs[position];
    }
}
