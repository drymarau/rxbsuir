package by.toggi.rxbsuir.model;

import android.os.Parcelable;
import android.text.TextUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Lesson implements Parcelable {

    Long _id;
    String syncId;
    List<String> auditoryList;
    List<Employee> employeeList;
    LocalTime lessonTimeStart;
    LocalTime lessonTimeEnd;
    String lessonType;
    String note;
    int numSubgroup;
    List<String> studentGroupList;
    String subject;
    List<Integer> weekNumberList;
    DayOfWeek weekday;
    boolean isGroupSchedule;

    public Long getId() {
        return _id;
    }

    /**
     * Gets pretty auditory list ("122-1, 135-1").
     *
     * @return pretty auditory list
     */
    public String getPrettyAuditoryList() {
        if (auditoryList != null) {
            return TextUtils.join(", ", auditoryList);
        } else {
            return "";
        }
    }

    /**
     * Gets pretty employee list ("Дик С.К., Смирнов А.В.").
     *
     * @return the pretty employee list
     */
    public String getPrettyEmployeeList() {
        if (employeeList != null) {
            List<String> employeeStringList = new ArrayList<>(employeeList.size());
            for (Employee employee : employeeList) {
                employeeStringList.add(employee.getShortFullName());
            }
            return TextUtils.join(", ", employeeStringList);
        }
        return "";
    }

    /**
     * Gets pretty lesson time start ("8:00").
     *
     * @return the pretty lesson time start
     */
    public String getPrettyLessonTimeStart() {
        return lessonTimeStart.toString();
    }

    /**
     * Gets pretty lesson time end ("9:45").
     *
     * @return the pretty lesson time end
     */
    public String getPrettyLessonTimeEnd() {
        return lessonTimeEnd.toString();
    }

    public String getLessonType() {
        return lessonType;
    }

    public String getNote() {
        return note;
    }

    /**
     * Gets pretty student group list ("111801, 011801").
     *
     * @return the pretty student group list
     */
    public String getPrettyStudentGroupList() {
        return TextUtils.join(", ", studentGroupList);
    }

    /**
     * Gets subject with subgroup ("ЭМАСиК (1)").
     *
     * @return the subject with subgroup
     */
    public String getSubjectWithSubgroup() {
        if (lessonType.equals("ЛР") || (lessonType.equals("ПЗ") && numSubgroup != 0)) {
            return String.format("%s (%d)", subject, numSubgroup);
        } else {
            return subject;
        }
    }

    public DayOfWeek getWeekday() {
        return weekday;
    }

    /**
     * Gets pretty weekday.
     *
     * @return the pretty weekday
     */
    public String getPrettyWeekday() {
        var prettyWeekday = weekday.getDisplayName(TextStyle.FULL, Locale.getDefault());
        return prettyWeekday.substring(0, 1).toUpperCase() + prettyWeekday.substring(1);
    }

    public boolean isGroupSchedule() {
        return isGroupSchedule;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeString(this.syncId);
        dest.writeStringList(this.auditoryList);
        dest.writeList(this.employeeList);
        dest.writeSerializable(this.lessonTimeStart);
        dest.writeSerializable(this.lessonTimeEnd);
        dest.writeString(this.lessonType);
        dest.writeString(this.note);
        dest.writeInt(this.numSubgroup);
        dest.writeStringList(this.studentGroupList);
        dest.writeString(this.subject);
        dest.writeList(this.weekNumberList);
        dest.writeInt(this.weekday == null ? -1 : this.weekday.ordinal());
        dest.writeByte(this.isGroupSchedule ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(android.os.Parcel source) {
        this._id = (Long) source.readValue(Long.class.getClassLoader());
        this.syncId = source.readString();
        this.auditoryList = source.createStringArrayList();
        this.employeeList = new ArrayList<Employee>();
        source.readList(this.employeeList, Employee.class.getClassLoader());
        this.lessonTimeStart = (LocalTime) source.readSerializable();
        this.lessonTimeEnd = (LocalTime) source.readSerializable();
        this.lessonType = source.readString();
        this.note = source.readString();
        this.numSubgroup = source.readInt();
        this.studentGroupList = source.createStringArrayList();
        this.subject = source.readString();
        this.weekNumberList = new ArrayList<Integer>();
        source.readList(this.weekNumberList, Integer.class.getClassLoader());
        int tmpWeekday = source.readInt();
        this.weekday = tmpWeekday == -1 ? null : DayOfWeek.values()[tmpWeekday];
        this.isGroupSchedule = source.readByte() != 0;
    }

    protected Lesson(android.os.Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.syncId = in.readString();
        this.auditoryList = in.createStringArrayList();
        this.employeeList = new ArrayList<>();
        in.readList(this.employeeList, Employee.class.getClassLoader());
        this.lessonTimeStart = (LocalTime) in.readSerializable();
        this.lessonTimeEnd = (LocalTime) in.readSerializable();
        this.lessonType = in.readString();
        this.note = in.readString();
        this.numSubgroup = in.readInt();
        this.studentGroupList = in.createStringArrayList();
        this.subject = in.readString();
        this.weekNumberList = new ArrayList<>();
        in.readList(this.weekNumberList, Integer.class.getClassLoader());
        int tmpWeekday = in.readInt();
        this.weekday = tmpWeekday == -1 ? null : DayOfWeek.values()[tmpWeekday];
        this.isGroupSchedule = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Lesson> CREATOR = new Parcelable.Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(android.os.Parcel source) {
            return new Lesson(source);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };
}
