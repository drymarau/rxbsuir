package by.toggi.rxbsuir.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private static final int VIEW_TYPE_LESSON_ONE_LINE = 0;
    private static final int VIEW_TYPE_LESSON_TWO_LINE = 1;
    private static final int VIEW_TYPE_LESSON_THREE_LINE = 2;

    private final LessonListPresenter.Type mType;
    private final OnItemClickListener mListener;
    private List<Lesson> mLessonList;

    public LessonAdapter(OnItemClickListener listener, List<Lesson> lessonList, LessonListPresenter.Type type) {
        mListener = listener;
        mLessonList = lessonList;
        mType = type;
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

        if (lesson.getPrettyAuditoryList().isEmpty()) {
            return VIEW_TYPE_LESSON_ONE_LINE;
        }
        return lesson.getPrettyEmployeeList().isEmpty()
                ? VIEW_TYPE_LESSON_TWO_LINE
                : VIEW_TYPE_LESSON_THREE_LINE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_LESSON_ONE_LINE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item_lesson_one_line, viewGroup, false);
                break;
            case VIEW_TYPE_LESSON_TWO_LINE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item_lesson_two_line, viewGroup, false);
                break;
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
        viewHolder.itemView.setOnClickListener(view -> mListener.onItemClicked(lesson));
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
        switch (mType) {
            case TODAY:
                viewHolder.setHeaderText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
                break;
            case TOMORROW:
                viewHolder.setHeaderText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
                break;
            case WEEK_ONE:
            case WEEK_TWO:
            case WEEK_THREE:
            case WEEK_FOUR:
                viewHolder.setHeaderText(lesson.getPrettyWeekday());
                break;
        }
        if (position < mLessonList.size() - 1) {
            viewHolder.setIsLast(!lesson.getWeekday().equals(mLessonList.get(position + 1).getWeekday()));
        } else if (position == mLessonList.size() - 1) {
            viewHolder.setIsLast(true);
        }
        if (position == 0) {
            viewHolder.setIsFirst(true);
        } else {
            viewHolder.setIsFirst(!lesson.getWeekday().equals(mLessonList.get(position - 1).getWeekday()));
        }
        if (lesson.getNote() != null && !lesson.getNote().isEmpty()) {
            viewHolder.mLessonSubjectSubgroup.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_note_small, 0);
        } else {
            viewHolder.mLessonSubjectSubgroup.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return mLessonList.size();
    }

    public interface OnItemClickListener {

        void onItemClicked(Lesson lesson);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.lesson_type) TextView mLessonType;
        @Bind(R.id.lesson_subject_subgroup) TextView mLessonSubjectSubgroup;
        @Bind(R.id.lesson_time_start) TextView mLessonTimeStart;
        @Bind(R.id.lesson_time_end) TextView mLessonTimeEnd;

        @Nullable @Bind(R.id.lesson_employee) TextView mLessonEmployee;
        @Nullable @Bind(R.id.lesson_class) TextView mLessonClass;

        private String mHeaderText;
        private boolean mIsLast;
        private boolean mIsFirst;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public String getHeaderText() {
            return mHeaderText;
        }

        public void setHeaderText(String headerText) {
            mHeaderText = headerText;
        }

        public boolean isLast() {
            return mIsLast;
        }

        public void setIsLast(boolean isLast) {
            mIsLast = isLast;
        }

        public boolean isFirst() {
            return mIsFirst;
        }

        public void setIsFirst(boolean isFirst) {
            mIsFirst = isFirst;
        }
    }

}
