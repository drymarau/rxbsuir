package by.toggi.rxbsuir.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDate;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.db.model.LessonWithDate;

public class LessonWithDateAdapter extends RecyclerView.Adapter<LessonWithDateAdapter.ViewHolder> implements LessonViewType {


    private List<LessonWithDate> mLessonWithDateList;

    public LessonWithDateAdapter(List<LessonWithDate> lessonWithDateList) {
        mLessonWithDateList = lessonWithDateList;
    }

    /**
     * Sets lesson list and updates the {@link RecyclerView}.
     *
     * @param lessonWithDateList the lesson list
     */
    public void setLessonWithDateList(List<LessonWithDate> lessonWithDateList) {
        mLessonWithDateList.clear();
        mLessonWithDateList.addAll(lessonWithDateList);
        notifyDataSetChanged();
    }

    /**
     * Gets today position.
     *
     * @return the today position
     */
    public int getTodayPosition() {
        LocalDate localDate = LocalDate.now();
        if (localDate.isBefore(mLessonWithDateList.get(0).getLocalDate())) {
            return 0;
        }
        for (LessonWithDate lessonWithDate : mLessonWithDateList) {
            LocalDate lessonDate = lessonWithDate.getLocalDate();
            if (lessonDate.isEqual(lessonDate) || lessonDate.isAfter(lessonDate)) {
                return mLessonWithDateList.indexOf(lessonWithDate);
            }
        }
        return mLessonWithDateList.size() - 1;
    }

    @Override
    public int getItemViewType(int position) {
        LessonWithDate lessonWithDate = mLessonWithDateList.get(position);
        Lesson lesson = lessonWithDate.getLesson();

        if (position != 0 && lessonWithDate.getPrettyDate().equals(mLessonWithDateList.get(position - 1).getPrettyDate())) {
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
        LessonWithDate lessonWithDate = mLessonWithDateList.get(position);
        Lesson lesson = lessonWithDate.getLesson();
        viewHolder.mLessonType.setText(lesson.getLessonType());
        viewHolder.mLessonSubjectSubgroup.setText(lesson.getSubjectWithSubgroup());
        viewHolder.mLessonTimeStart.setText(lesson.getPrettyLessonTimeStart());
        viewHolder.mLessonTimeEnd.setText(lesson.getPrettyLessonTimeEnd());
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
        viewHolder.setWeekDay(lessonWithDate.getPrettyDate());
    }

    @Override
    public int getItemCount() {
        return mLessonWithDateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.lesson_type) TextView mLessonType;
        @Bind(R.id.lesson_subject_subgroup) TextView mLessonSubjectSubgroup;
        @Bind(R.id.lesson_time_start) TextView mLessonTimeStart;
        @Bind(R.id.lesson_time_end) TextView mLessonTimeEnd;

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
