package by.toggi.rxbsuir.model;

import androidx.annotation.NonNull;

public class StudentGroup {

    public int id;
    public String name;

    @Override
    @NonNull
    public String toString() {
        return name;
    }
}
