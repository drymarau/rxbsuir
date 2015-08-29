package by.toggi.rxbsuir.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import by.toggi.rxbsuir.fragment.TodayFragment;
import by.toggi.rxbsuir.fragment.WeekFragment;

public class LessonListPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] mTabs;

    public LessonListPagerAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        mTabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (getCount() == 4) {
            // Only weeks are shown
            return WeekFragment.newInstance(position + 1);
        } else {
            if (position == 0) {
                // Today tab and weeks are shown
                return TodayFragment.newInstance();
            }
        }
        return WeekFragment.newInstance(position);
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
