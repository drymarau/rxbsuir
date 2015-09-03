package by.toggi.rxbsuir.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import by.toggi.rxbsuir.fragment.LessonListFragment;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter;

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
            switch (position) {
                case 0:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.WEEK_ONE);
                case 1:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.WEEK_TWO);
                case 2:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.WEEK_THREE);
                case 3:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.WEEK_FOUR);
            }
        } else {
            // Today tab and weeks are shown
            switch (position) {
                case 0:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.TODAY);
                case 1:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.TOMORROW);
                case 2:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.WEEK_ONE);
                case 3:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.WEEK_TWO);
                case 4:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.WEEK_THREE);
                case 5:
                    return LessonListFragment.newInstance(LessonListPresenter.Type.WEEK_FOUR);
            }
        }
        return null;
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
