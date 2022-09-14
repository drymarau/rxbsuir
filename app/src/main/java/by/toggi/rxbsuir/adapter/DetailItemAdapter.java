package by.toggi.rxbsuir.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.activity.LessonActivity.DetailItem;

public class DetailItemAdapter extends RecyclerView.Adapter<DetailItemAdapter.ViewHolder> {

    private static final int VIEW_TYPE_DETAIL = 0;
    private static final int VIEW_TYPE_DETAIL_SUMMARY = 1;

    private final Context mContext;
    private final List<DetailItem> mDetailItemList;

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
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        var detailItem = mDetailItemList.get(position);
        holder.mIcon.setVisibility(detailItem.isIconVisible() ? View.VISIBLE : View.GONE);
        holder.setIsFirst(detailItem.isIconVisible());
        if (position < mDetailItemList.size() - 1) {
            holder.setIsLast(!(detailItem.getType() == mDetailItemList.get(position + 1).getType()));
        } else if (position == mDetailItemList.size() - 1) {
            holder.setIsLast(true);
        }
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

        final ImageView mIcon;
        final TextView mText;
        @Nullable final TextView mSummary;

        private boolean isFirst;
        private boolean isLast;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.lesson_detail_icon);
            mText = itemView.findViewById(R.id.lesson_detail_text);
            mSummary = itemView.findViewById(R.id.lesson_detail_summary);
        }

        public boolean isLast() {
            return isLast;
        }

        public void setIsLast(boolean isLast) {
            this.isLast = isLast;
        }

        public boolean isFirst() {
            return isFirst;
        }

        public void setIsFirst(boolean isFirst) {
            this.isFirst = isFirst;
        }
    }
}
