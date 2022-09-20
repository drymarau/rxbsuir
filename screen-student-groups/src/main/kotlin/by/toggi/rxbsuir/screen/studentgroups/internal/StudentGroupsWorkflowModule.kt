package by.toggi.rxbsuir.screen.studentgroups.internal

import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsWorkflow
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface StudentGroupsWorkflowModule {

    @Binds
    fun StudentGroupsWorkflowImpl.bindStudentGroupsWorkflow(): StudentGroupsWorkflow
}
