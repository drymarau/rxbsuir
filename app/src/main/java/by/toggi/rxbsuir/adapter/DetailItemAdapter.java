package by.toggi.rxbsuir.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.activity.LessonActivity.DetailItem;

public class DetailItemAdapter extends RecyclerView.Adapter<DetailItemAdapter.ViewHolder> {

    private static final int VIEW_TYPE_DETAIL = 0;
    private static final int VIEW_TYPE_DETAIL_SUMMARY = 1;

    private final Context mContext;
    private List<DetailItem> mDetailItemList;

    public DetailItemAdapter(Context context, List<DetailItem> detailItemList) {
        mContext = context;
        mDetailItemList = detailItemList;
    }

    public void setDetailItemList(List<DetailItem> detailItemList) {
        mDetailItemList.clear();
        mDetailItemList.addAll(detailItemList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDetailItemList.get(position).getType() == DetailItem.Type.WEEKDAY) {
            return VIEW_TYPE_DETAIL_SUMMARY;
        }
        return VIEW_TYPE_DETAIL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_DETAIL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_detail, parent, false);
                break;
            case VIEW_TYPE_DETAIL_SUMMARY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_detail_summary, parent, false);
                break;
            default:
                throw new IllegalArgumentException("Unknown viewType: " + viewType);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailItem detailItem = mDetailItemList.get(position);
        holder.mIcon.setVisibility(detailItem.isIconVisible() ? View.VISIBLE : View.GONE);
        switch (detailItem.getType()) {
            case TIME:
                holder.mIcon.setImageResource(R.drawable.ic_time);
                break;
            case EMPLOYEE:
                holder.mIcon.setImageResource(R.drawable.ic_employee);
                break;
            case GROUP:
                holder.mIcon.setImageResource(R.drawable.ic_group);
                break;
            case AUDITORY:
                holder.mIcon.setImageResource(R.drawable.ic_auditory);
                break;
            case WEEKDAY:
                holder.mIcon.setImageResource(R.drawable.ic_action_today);
                break;
            case NOTE:
                holder.mIcon.setImageResource(R.drawable.ic_note);
                break;
        }
        holder.mText.setText(detailItem.getText());
        if (holder.mSummary != null) {
            holder.mSummary.setText(detailItem.getSummary() == null
                    ? mContext.getString(R.string.weekly)
                    : mContext.getResources().getQuantityString(R.plurals.week, detailItem.getSummary().length(), detailItem.getSummary()));
        }
    }

    @Override
    public int getItemCount() {
        return mDetailItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.lesson_detail_icon) ImageView mIcon;
        @Bind(R.id.lesson_detail_text) TextView mText;
        @Nullable @Bind(R.id.lesson_detail_summary) TextView mSummary;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
