package by.toggi.rxbsuir.screen.studentgroups.internal

import android.os.Parcel
import by.toggi.rxbsuir.api.StudentGroup
import kotlinx.parcelize.Parceler

internal object StudentGroupParceler : Parceler<StudentGroup> {

    override fun create(parcel: Parcel): StudentGroup = StudentGroup(
        name = parcel.readString()!!,
        course = when (val course = parcel.readInt()) {
            -1 -> null
            else -> course
        }
    )

    override fun StudentGroup.write(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(course ?: -1)
    }
}
