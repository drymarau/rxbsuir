package by.toggi.rxbsuir.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.SubheaderItemDecoration;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.model.Lesson;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.components.FragmentComponent;

@AndroidEntryPoint
public class LessonListFragment extends Fragment implements LessonAdapter.OnItemClickListener {

    public static final String KEY_LAYOUT_MANAGER_STATE = "layout_manager_state";
    private static final String ARGS_VIEW_TYPE = "week_number";

    private Type mType;
    private LinearLayoutManager mLayoutManager;

    /**
     * Instantiates a new {@code WeekFragment}.
     *
     * @param type view type of the presenter
     * @return the week fragment
     */
    public static LessonListFragment newInstance(Type type) {
        var args = new Bundle();
        args.putSerializable(ARGS_VIEW_TYPE, type);
        var fragment = new LessonListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        var args = getArguments();
        if (args != null) {
            mType = (Type) args.getSerializable(ARGS_VIEW_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lesson_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        TextView mEmptyState = view.findViewById(R.id.empty_state);

        switch (mType) {
            case TODAY:
                mEmptyState.setText(R.string.empty_state_today);
                break;
            case TOMORROW:
                mEmptyState.setText(R.string.empty_state_tomorrow);
                break;
            default:
                mEmptyState.setText(R.string.empty_state_week);
                break;
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        LessonAdapter mAdapter = new LessonAdapter(this, mType);

        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SubheaderItemDecoration(getActivity(), mRecyclerView));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LAYOUT_MANAGER_STATE, mLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onItemClicked(Lesson lesson) {
    }

    @dagger.Module
    @InstallIn(FragmentComponent.class)
    public static class Module {

        @Provides
        Type provide(Fragment fragment) {
            return (Type) fragment.getArguments().getSerializable(ARGS_VIEW_TYPE);
        }
    }

    public enum Type {

        TODAY, TOMORROW, WEEK_ONE, WEEK_TWO, WEEK_THREE, WEEK_FOUR;
    }
}
