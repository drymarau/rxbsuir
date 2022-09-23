package by.toggi.rxbsuir

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface RxBsuirScreen : Parcelable {

    @Parcelize
    object Home : RxBsuirScreen

    @Parcelize
    object StudentGroups : RxBsuirScreen
}
