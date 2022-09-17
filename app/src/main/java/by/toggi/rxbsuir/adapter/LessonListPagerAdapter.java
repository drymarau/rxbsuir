package by.toggi.rxbsuir.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class LessonListPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] mTabs;

    public LessonListPagerAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        mTabs = tabs;
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        return new Fragment();
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
