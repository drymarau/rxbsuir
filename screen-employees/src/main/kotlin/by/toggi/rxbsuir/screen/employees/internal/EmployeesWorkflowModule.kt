package by.toggi.rxbsuir.screen.employees.internal

import by.toggi.rxbsuir.screen.employees.EmployeesWorkflow
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface EmployeesWorkflowModule {

    @Binds
    fun EmployeesWorkflowImpl.bindEmployees(): EmployeesWorkflow
}
