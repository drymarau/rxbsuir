package by.toggi.rxbsuir.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

public data class HomeScreen(
    val onStudentGroupsClick: () -> Unit,
    val onEmployeesClick: () -> Unit,
    val onBackClick: () -> Unit
)

@Composable
public fun HomeScreen(screen: HomeScreen, modifier: Modifier = Modifier) {
    HomeScreen(
        onStudentGroupsClick = screen.onStudentGroupsClick,
        onEmployeesClick = screen.onEmployeesClick,
        onBackClick = screen.onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    onStudentGroupsClick: () -> Unit,
    onEmployeesClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackClick)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.home_title))
                }
            )
        },
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(it)) {
            HomeItem(
                text = stringResource(R.string.home_item_student_groups),
                onClick = onStudentGroupsClick
            )
            HomeItem(
                text = stringResource(R.string.home_item_employees),
                onClick = onEmployeesClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeItem(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ListItem(
        headlineText = {
            Text(text = text)
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}
