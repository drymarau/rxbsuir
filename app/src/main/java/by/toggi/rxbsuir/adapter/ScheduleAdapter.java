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
import by.toggi.rxbsuir.model.Schedule;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<Schedule> mScheduleList;

    public ScheduleAdapter(List<Schedule> scheduleList) {
        mScheduleList = scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        mScheduleList = scheduleList;
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
        Schedule lesson = mScheduleList.get(position);
        viewHolder.lessonType.setText(lesson.lessonType);
        viewHolder.lessonSubjectSubgroup.setText(lesson.subject);
        viewHolder.lessonClass.setText(lesson.auditory == null ? "" : lesson.auditory.toString());
        viewHolder.lessonTime.setText(lesson.lessonTime.replace("-", "\n"));
    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
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
