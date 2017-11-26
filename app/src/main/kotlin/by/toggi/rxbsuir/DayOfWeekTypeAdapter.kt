package by.toggi.rxbsuir

import android.os.Parcel
import org.threeten.bp.DayOfWeek
import paperparcel.TypeAdapter

object DayOfWeekTypeAdapter : TypeAdapter<DayOfWeek> {

  override fun readFromParcel(source: Parcel): DayOfWeek {
    return DayOfWeek.of(source.readInt())
  }

  override fun writeToParcel(value: DayOfWeek, dest: Parcel, flags: Int) {
    dest.writeInt(value.value)
  }
}
