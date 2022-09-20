package by.toggi.rxbsuir

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@OptIn(WorkflowUiExperimentalApi::class)
@HiltViewModel
class RxBsuirViewModel @Inject constructor(
    workflow: RxBsuirWorkflow,
    handle: SavedStateHandle,
) : ViewModel() {

    private val _output = MutableSharedFlow<RxBsuirOutput>(extraBufferCapacity = 1)

    val rendering by lazy {
        renderWorkflowIn(
            workflow = workflow,
            scope = viewModelScope,
            savedStateHandle = handle,
            onOutput = _output::emit
        )
    }
    val output = _output.asSharedFlow()
}
