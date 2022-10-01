package by.toggi.rxbsuir

import android.os.Parcelable
import by.toggi.rxbsuir.api.Employee
import by.toggi.rxbsuir.screen.employees.EmployeeParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

sealed interface RxBsuirScreen : Parcelable {

    @Parcelize
    object Home : RxBsuirScreen

    @Parcelize
    object StudentGroups : RxBsuirScreen

    @Parcelize
    object Employees : RxBsuirScreen

    @TypeParceler<Employee, EmployeeParceler>
    @Parcelize
    @JvmInline
    value class Announcements(val employee: Employee) : RxBsuirScreen
}
