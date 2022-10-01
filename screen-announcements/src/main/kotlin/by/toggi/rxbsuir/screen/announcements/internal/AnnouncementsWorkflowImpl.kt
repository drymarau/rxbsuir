package by.toggi.rxbsuir.screen.announcements.internal

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import by.toggi.rxbsuir.api.Announcement
import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.screen.announcements.AnnouncementsOutput
import by.toggi.rxbsuir.screen.announcements.AnnouncementsProps
import by.toggi.rxbsuir.screen.announcements.AnnouncementsScreen
import by.toggi.rxbsuir.screen.announcements.AnnouncementsWorkflow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnnouncementsWorkflowImpl @Inject constructor(private val client: BsuirClient) :
    AnnouncementsWorkflow {

    @Composable
    override fun render(
        props: AnnouncementsProps,
        onOutput: (AnnouncementsOutput) -> Unit,
    ): AnnouncementsScreen {
        var state by rememberSaveable { mutableStateOf(AnnouncementsState()) }
        GetAnnouncementsEffect(props.employee.urlId) {
            state = AnnouncementsState(it)
        }
        return AnnouncementsScreen(
            announcements = state.announcements,
            onBackClick = {
                onOutput(AnnouncementsOutput.OnBack)
            }
        )
    }

    @Composable
    private fun GetAnnouncementsEffect(
        urlId: String,
        onAnnouncements: (List<Announcement>) -> Unit,
    ) {
        val currentOnAnnouncements by rememberUpdatedState(onAnnouncements)
        LaunchedEffect(client, urlId) {
            val announcements = try {
                client.getAnnouncements(urlId)
            } catch (t: Throwable) {
                emptyList()
            }
            currentOnAnnouncements(announcements)
        }
    }
}

@TypeParceler<Announcement, AnnouncementParceler>
@Parcelize
@JvmInline
private value class AnnouncementsState(val announcements: List<Announcement> = emptyList()) :
    Parcelable

private object AnnouncementParceler : Parceler<Announcement> {

    override fun create(parcel: Parcel): Announcement {
        return Announcement(
            id = parcel.readLong(),
            content = parcel.readString()!!,
            date = LocalDate.parse(parcel.readString()!!),
            startTime = LocalTime.parse(parcel.readString()!!),
            endTime = LocalTime.parse(parcel.readString()!!)
        )
    }

    override fun Announcement.write(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(content)
        parcel.writeString(date.toString())
        parcel.writeString(startTime.toString())
        parcel.writeString(endTime.toString())
    }
}
