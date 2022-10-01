package by.toggi.rxbsuir.screen.announcements.internal

import by.toggi.rxbsuir.screen.announcements.AnnouncementsWorkflow
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AnnouncementsWorkflowModule {

    @Binds
    fun AnnouncementsWorkflowImpl.bindAnnouncementsWorkflow(): AnnouncementsWorkflow
}
