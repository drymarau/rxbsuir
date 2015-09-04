package by.toggi.rxbsuir.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.db.model.Lesson;

public class LessonActivity extends AppCompatActivity {

    private static final String EXTRA_LESSON = "by.toggi.rxbsuir.extra.lesson";

    @Bind(R.id.toolbar) Toolbar mToolbar;

    public static void start(Context context, Lesson lesson) {
        Intent intent = new Intent(context, LessonActivity.class);
        intent.putExtra(EXTRA_LESSON, Parcels.wrap(lesson));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        ButterKnife.bind(this);

        setupToolbar();

        Lesson lesson = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_LESSON));
        Toast.makeText(this, lesson.getPrettyLesson(), Toast.LENGTH_SHORT).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

}
