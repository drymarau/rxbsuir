package by.toggi.rxbsuir.screen.studentgroups

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
import by.toggi.rxbsuir.api.StudentGroup

public data class StudentGroupsScreen(
    val studentGroups: List<StudentGroup>,
    val onBackClick: () -> Unit
)

@Composable
public fun StudentGroupsScreen(screen: StudentGroupsScreen, modifier: Modifier = Modifier) {
    StudentGroupsScreen(
        studentGroups = screen.studentGroups,
        onBackClick = screen.onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudentGroupsScreen(
    studentGroups: List<StudentGroup>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackClick)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Student groups")
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
            items(studentGroups, StudentGroup::id) { studentGroup ->
                StudentGroup(studentGroup)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentGroup(studentGroup: StudentGroup, modifier: Modifier = Modifier) {
    ListItem(
        headlineText = {
            Text(text = studentGroup.name)
        },
        modifier = modifier
    )
}
