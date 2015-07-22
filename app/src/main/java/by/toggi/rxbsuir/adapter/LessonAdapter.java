package by.toggi.rxbsuir.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

    public static final int VIEW_TYPE_LESSON = 0;
    public static final int VIEW_TYPE_LESSON_WITH_WEEKDAY = 1;
    private List<Lesson> mLessonList;

    public LessonAdapter(List<Lesson> lessonList) {
        mLessonList = lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        mLessonList = lessonList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_LESSON_WITH_WEEKDAY;
        }
        String weekday = mLessonList.get(position).getWeekday();
        if (weekday.equals(mLessonList.get(position - 1).getWeekday())) {
            return VIEW_TYPE_LESSON;
        } else {
            return VIEW_TYPE_LESSON_WITH_WEEKDAY;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_LESSON:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item_lesson_two_line, viewGroup, false);
                break;
            case VIEW_TYPE_LESSON_WITH_WEEKDAY:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item_lesson_with_weekday, viewGroup, false);
                break;
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Lesson lesson = mLessonList.get(position);
        viewHolder.lessonType.setText(lesson.getLessonType());
        viewHolder.lessonSubjectSubgroup.setText(lesson.getSubjectWithSubgroup());
        viewHolder.lessonClass.setText(lesson.getPrettyAuditoryList());
        viewHolder.lessonTime.setText(lesson.getPrettyLessonTime());
        if (viewHolder.lessonWeekday != null) {
            viewHolder.lessonWeekday.setText(lesson.getWeekday());
        }
    }

    @Override
    public int getItemCount() {
        return mLessonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.lesson_type) TextView lessonType;
        @Bind(R.id.lesson_subject_subgroup) TextView lessonSubjectSubgroup;
        @Bind(R.id.lesson_class) TextView lessonClass;
        @Bind(R.id.lesson_time) TextView lessonTime;

        @Nullable @Bind(R.id.lesson_weekday) TextView lessonWeekday;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
