package by.toggi.rxbsuir.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.db.model.Lesson;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    public static final int VIEW_TYPE_LESSON_ONE_LINE_WITH_WEEKDAY = 1;
    public static final int VIEW_TYPE_LESSON_TWO_LINE_WITH_WEEKDAY = 3;
    public static final int VIEW_TYPE_LESSON_THREE_LINE_WITH_WEEKDAY = 5;
    private static final int VIEW_TYPE_LESSON_ONE_LINE = 0;
    private static final int VIEW_TYPE_LESSON_TWO_LINE = 2;
    private static final int VIEW_TYPE_LESSON_THREE_LINE = 4;
    private List<Lesson> mLessonList;

    public LessonAdapter(List<Lesson> lessonList) {
        mLessonList = lessonList;
    }

    /**
     * Sets lesson list and updates the {@link RecyclerView}.
     *
     * @param lessonList the lesson list
     */
    public void setLessonList(List<Lesson> lessonList) {
        mLessonList.clear();
        mLessonList.addAll(lessonList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Lesson lesson = mLessonList.get(position);

        if (position != 0 && lesson.getWeekday().equals(mLessonList.get(position - 1).getWeekday())) {
            if (lesson.getPrettyAuditoryList().isEmpty()) {
                return VIEW_TYPE_LESSON_ONE_LINE;
            }
            return lesson.getPrettyEmployeeList().isEmpty()
                    ? VIEW_TYPE_LESSON_TWO_LINE
                    : VIEW_TYPE_LESSON_THREE_LINE;
        } else {
            if (lesson.getPrettyAuditoryList().isEmpty()) {
                return VIEW_TYPE_LESSON_ONE_LINE_WITH_WEEKDAY;
            }
            return lesson.getPrettyEmployeeList().isEmpty()
                    ? VIEW_TYPE_LESSON_TWO_LINE_WITH_WEEKDAY
                    : VIEW_TYPE_LESSON_THREE_LINE_WITH_WEEKDAY;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_LESSON_ONE_LINE_WITH_WEEKDAY:
            case VIEW_TYPE_LESSON_ONE_LINE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item_lesson_one_line, viewGroup, false);
                break;
            case VIEW_TYPE_LESSON_TWO_LINE_WITH_WEEKDAY:
            case VIEW_TYPE_LESSON_TWO_LINE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item_lesson_two_line, viewGroup, false);
                break;
            case VIEW_TYPE_LESSON_THREE_LINE_WITH_WEEKDAY:
            case VIEW_TYPE_LESSON_THREE_LINE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item_lesson_three_line, viewGroup, false);
                break;
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Lesson lesson = mLessonList.get(position);
        viewHolder.mLessonType.setText(lesson.getLessonType());
        viewHolder.mLessonSubjectSubgroup.setText(lesson.getSubjectWithSubgroup());
        viewHolder.mLessonTime.setText(lesson.getPrettyLessonTime());
        if (viewHolder.mLessonClass != null) {
            viewHolder.mLessonClass.setText(lesson.getPrettyAuditoryList());
        }
        if (viewHolder.mLessonEmployee != null) {
            if (lesson.isGroupSchedule()) {
                viewHolder.mLessonEmployee.setText(lesson.getPrettyEmployeeList());
            } else {
                viewHolder.mLessonEmployee.setText(lesson.getPrettyStudentGroupList());
            }
        }
        viewHolder.setWeekDay(lesson.getWeekday());
    }

    @Override
    public int getItemCount() {
        return mLessonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.lesson_type) TextView mLessonType;
        @Bind(R.id.lesson_subject_subgroup) TextView mLessonSubjectSubgroup;
        @Bind(R.id.lesson_time) TextView mLessonTime;

        @Nullable @Bind(R.id.lesson_employee) TextView mLessonEmployee;
        @Nullable @Bind(R.id.lesson_class) TextView mLessonClass;

        private String mWeekDay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public String getWeekDay() {
            return mWeekDay;
        }

        public void setWeekDay(String weekDay) {
            mWeekDay = weekDay;
        }
    }

}
