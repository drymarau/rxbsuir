package by.toggi.rxbsuir;

import com.google.gson.Gson;

public class SyncIdItem {

    private final int mId;
    private final String mTitle;
    private final boolean mIsGroupSchedule;


    /**
     * Instantiates a new Sync id item.
     *
     * @param id              the id
     * @param title           the title
     * @param isGroupSchedule the is group schedule
     */
    public SyncIdItem(int id, String title, boolean isGroupSchedule) {
        mId = id;
        mTitle = title;
        mIsGroupSchedule = isGroupSchedule;
    }

    /**
     * From json.
     *
     * @param json the json
     * @return the sync id item
     */
    public static SyncIdItem fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SyncIdItem.class);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return mId;
    }

    /**
     * Gets sync id.
     *
     * @return the sync id
     */
    public String getSyncId() {
        return String.valueOf(mId);
    }

    /**
     * Is group schedule.
     *
     * @return the boolean
     */
    public boolean isGroupSchedule() {
        return mIsGroupSchedule;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * To json.
     *
     * @return the string
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof SyncIdItem)) {
            return false;
        }

        SyncIdItem syncIdItem = (SyncIdItem) o;

        return mId == syncIdItem.mId &&
                mTitle.equals(syncIdItem.mTitle) &&
                mIsGroupSchedule == syncIdItem.mIsGroupSchedule;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (mIsGroupSchedule ? 1 : 0);
        result = 31 * result + mId;
        result = 31 * result + mTitle.hashCode();
        return result;
    }
}
