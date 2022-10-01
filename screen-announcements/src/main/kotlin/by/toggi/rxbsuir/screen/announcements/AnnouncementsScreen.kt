package by.toggi.rxbsuir.screen.announcements

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.toggi.rxbsuir.api.Announcement

public data class AnnouncementsScreen(
    val announcements: List<Announcement>,
    val onBackClick: () -> Unit,
)

@Composable
public fun AnnouncementsScreen(screen: AnnouncementsScreen, modifier: Modifier = Modifier) {
    AnnouncementsScreen(
        announcements = screen.announcements,
        onBackClick = screen.onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnnouncementsScreen(
    announcements: List<Announcement>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = onBackClick)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Announcements")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(announcements) { announcement ->
                Announcement(announcement)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Announcement(announcement: Announcement, modifier: Modifier = Modifier) {
    ListItem(
        headlineText = {
            Text(text = announcement.content)
        },
        supportingText = {
            Text(text = with(announcement) { "$date $startTime-$endTime" })
        },
        modifier = modifier
    )
}
