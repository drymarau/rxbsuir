package by.toggi.rxbsuir.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import by.toggi.rxbsuir.db.model.Lesson;

public class LessonActivity extends AppCompatActivity {

    public static void start(Context context, Lesson lesson) {
        context.startActivity(new Intent(context, LessonActivity.class).putExtra("test", lesson.getPrettyLesson()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, getIntent().getStringExtra("test"), Toast.LENGTH_SHORT).show();
    }

}
