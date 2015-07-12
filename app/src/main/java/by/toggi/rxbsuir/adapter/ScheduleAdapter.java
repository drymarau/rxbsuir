package by.toggi.rxbsuir.adapter;

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

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<Lesson> mLessonList;

    public ScheduleAdapter(List<Lesson> lessonList) {
        mLessonList = lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        mLessonList = lessonList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_schedule, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Lesson lesson = mLessonList.get(position);
        viewHolder.lessonType.setText(lesson.getLessonType());
        viewHolder.lessonSubjectSubgroup.setText(lesson.getSubject());
        viewHolder.lessonClass.setText(lesson.getAuditoryList() == null ? "" : lesson.getAuditoryList().toString());
        viewHolder.lessonTime.setText(lesson.getLessonTime().replace("-", "\n"));
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
