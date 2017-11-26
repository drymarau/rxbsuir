package by.toggi.rxbsuir

import android.os.Parcel
import org.threeten.bp.LocalTime
import paperparcel.TypeAdapter

object LocalTimeTypeAdapter : TypeAdapter<LocalTime> {

  override fun writeToParcel(value: LocalTime, dest: Parcel, flags: Int) {
    dest.writeLong(value.toNanoOfDay())
  }

  override fun readFromParcel(source: Parcel): LocalTime {
    return LocalTime.ofNanoOfDay(source.readLong())
  }
}
