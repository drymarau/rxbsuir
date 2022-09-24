package by.toggi.rxbsuir.screen.employees

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import by.toggi.rxbsuir.api.Employee
import coil.compose.AsyncImage

public data class EmployeesScreen(val employees: List<Employee>, val onBackClick: () -> Unit)

@Composable
public fun EmployeesScreen(screen: EmployeesScreen, modifier: Modifier = Modifier) {
    EmployeesScreen(
        employees = screen.employees,
        onBackClick = screen.onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmployeesScreen(
    employees: List<Employee>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackClick)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Employees")
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
            items(employees, Employee::id) { employee ->
                Employee(employee)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Employee(employee: Employee, modifier: Modifier = Modifier) {
    ListItem(
        leadingContent = {
            AsyncImage(
                model = employee.photoLink,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            )
        },
        headlineText = {
            Text(
                text = employee.lastName + " " + employee.firstName + " " + employee.middleName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier
    )
}
