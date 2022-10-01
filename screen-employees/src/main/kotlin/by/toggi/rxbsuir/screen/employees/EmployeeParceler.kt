package by.toggi.rxbsuir.screen.employees

import android.os.Parcel
import by.toggi.rxbsuir.api.Employee
import kotlinx.parcelize.Parceler

public object EmployeeParceler : Parceler<Employee> {

    override fun create(parcel: Parcel): Employee = Employee(
        id = parcel.readLong(),
        urlId = parcel.readString()!!,
        lastName = parcel.readString()!!,
        firstName = parcel.readString()!!,
        middleName = parcel.readString()!!,
        photoLink = parcel.readString()!!,
        degree = parcel.readString()!!,
        rank = parcel.readString()
    )

    override fun Employee.write(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(urlId)
        parcel.writeString(lastName)
        parcel.writeString(firstName)
        parcel.writeString(middleName)
        parcel.writeString(photoLink)
        parcel.writeString(degree)
        parcel.writeString(rank)
    }
}
