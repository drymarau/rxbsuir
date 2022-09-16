package by.toggi.rxbsuir.mvp.presenter;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.LessonListView;

public class LessonListPresenter extends Presenter<LessonListView> {

    private final Type mType;

    @Inject
    public LessonListPresenter(Type type) {
        mType = type;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        detachView();
    }

    /**
     * Sets group number and updates the list.
     *
     * @param syncId the group number
     */
    public void setSyncId(@Nullable String syncId, Boolean isGroupSchedule) {
        onCreate();
    }

    /**
     * Sets subgroup filter.
     *
     * @param filter subgroup filter
     */
    public void setSubgroupFilter(SubgroupFilter filter) {
        onCreate();
    }

    /**
     * Sets searchQuery.
     *
     * @param searchQuery searchQuery query
     */
    public void setSearchQuery(String searchQuery) {
        onCreate();
    }

    @Override
    public String getTag() {
        return LessonListPresenter.class.getSimpleName() + "_" + mType;
    }

    public enum SubgroupFilter {

        BOTH, FIRST, SECOND, NONE

    }

    public enum Type {

        TODAY, TOMORROW, WEEK_ONE, WEEK_TWO, WEEK_THREE, WEEK_FOUR;
    }
}
