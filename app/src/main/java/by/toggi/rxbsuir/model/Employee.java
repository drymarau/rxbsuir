package by.toggi.rxbsuir.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Employee implements Parcelable {

    public int id;
    public List<String> academicDepartment;
    public String firstName;
    public String middleName;
    public String lastName;
    public boolean isCached;

    @Override
    @NonNull
    public String toString() {
        if (middleName == null) {
            return String.format("%s %s", lastName, firstName);
        } else {
            return String.format("%s %s %s", lastName, firstName, middleName);
        }
    }

    public String getShortFullName() {
        if (middleName == null) {
            return String.format("%s %s.", lastName, firstName.charAt(0));
        } else {
            return String.format("%s %s.%s.", lastName, firstName.charAt(0), middleName.charAt(0));
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeStringList(this.academicDepartment);
        dest.writeString(this.firstName);
        dest.writeString(this.middleName);
        dest.writeString(this.lastName);
        dest.writeByte(this.isCached ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.academicDepartment = source.createStringArrayList();
        this.firstName = source.readString();
        this.middleName = source.readString();
        this.lastName = source.readString();
        this.isCached = source.readByte() != 0;
    }

    protected Employee(Parcel in) {
        this.id = in.readInt();
        this.academicDepartment = in.createStringArrayList();
        this.firstName = in.readString();
        this.middleName = in.readString();
        this.lastName = in.readString();
        this.isCached = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel source) {
            return new Employee(source);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
}
