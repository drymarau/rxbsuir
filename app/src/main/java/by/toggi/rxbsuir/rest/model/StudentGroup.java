package by.toggi.rxbsuir.rest.model;

import android.support.annotation.NonNull;

public class StudentGroup {

    public int id;
    public String name;

    StudentGroup() {
    }

    @Override
    @NonNull
    public String toString() {
        return name;
    }
}
