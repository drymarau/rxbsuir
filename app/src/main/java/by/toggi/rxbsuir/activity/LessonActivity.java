package by.toggi.rxbsuir.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.parceler.Parcels;

import by.toggi.rxbsuir.db.model.Lesson;

public class LessonActivity extends AppCompatActivity {

    private static final String EXTRA_LESSON = "by.toggi.rxbsuir.extra.lesson";

    public static void start(Context context, Lesson lesson) {
        Intent intent = new Intent(context, LessonActivity.class);
        intent.putExtra(EXTRA_LESSON, Parcels.wrap(lesson));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Lesson lesson = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_LESSON));
        Toast.makeText(this, lesson.getPrettyLesson(), Toast.LENGTH_SHORT).show();
    }

}
