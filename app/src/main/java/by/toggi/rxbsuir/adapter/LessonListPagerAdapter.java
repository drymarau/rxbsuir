package by.toggi.rxbsuir.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import by.toggi.rxbsuir.fragment.LessonListFragment;

public class LessonListPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] mTabs;

    public LessonListPagerAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        mTabs = tabs;
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        if (getCount() == 4) {
            // Only weeks are shown
            switch (position) {
                case 0:
                    return LessonListFragment.newInstance(LessonListFragment.Type.WEEK_ONE);
                case 1:
                    return LessonListFragment.newInstance(LessonListFragment.Type.WEEK_TWO);
                case 2:
                    return LessonListFragment.newInstance(LessonListFragment.Type.WEEK_THREE);
                case 3:
                    return LessonListFragment.newInstance(LessonListFragment.Type.WEEK_FOUR);
            }
        } else {
            // Today tab and weeks are shown
            switch (position) {
                case 0:
                    return LessonListFragment.newInstance(LessonListFragment.Type.TODAY);
                case 1:
                    return LessonListFragment.newInstance(LessonListFragment.Type.TOMORROW);
                case 2:
                    return LessonListFragment.newInstance(LessonListFragment.Type.WEEK_ONE);
                case 3:
                    return LessonListFragment.newInstance(LessonListFragment.Type.WEEK_TWO);
                case 4:
                    return LessonListFragment.newInstance(LessonListFragment.Type.WEEK_THREE);
                case 5:
                    return LessonListFragment.newInstance(LessonListFragment.Type.WEEK_FOUR);
            }
        }
        throw new NullPointerException();
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
