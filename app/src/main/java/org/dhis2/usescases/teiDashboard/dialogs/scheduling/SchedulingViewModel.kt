package org.dhis2.usescases.teiDashboard.dialogs.scheduling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.dhis2.commons.bindings.event
import org.dhis2.commons.data.EventCreationType
import org.dhis2.commons.date.DateUtils
import org.dhis2.commons.resources.DhisPeriodUtils
import org.dhis2.commons.resources.EventResourcesProvider
import org.dhis2.commons.resources.ResourceManager
import org.dhis2.usescases.eventsWithoutRegistration.eventDetails.data.EventDetailsRepository
import org.dhis2.usescases.eventsWithoutRegistration.eventDetails.domain.ConfigureEventCatCombo
import org.dhis2.usescases.eventsWithoutRegistration.eventDetails.domain.ConfigureEventReportDate
import org.dhis2.usescases.eventsWithoutRegistration.eventDetails.models.EventCatCombo
import org.dhis2.usescases.eventsWithoutRegistration.eventDetails.models.EventDate
import org.dhis2.usescases.eventsWithoutRegistration.eventDetails.providers.EventDetailResourcesProvider
import org.dhis2.usescases.teiDashboard.dialogs.scheduling.SchedulingDialog.LaunchMode
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.enrollment.Enrollment
import org.hisp.dhis.android.core.event.EventStatus
import org.hisp.dhis.android.core.program.ProgramStage
import org.hisp.dhis.mobile.ui.designsystem.component.SelectableDates
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SchedulingViewModel(
    val d2: D2,
    val resourceManager: ResourceManager,
    val eventResourcesProvider: EventResourcesProvider,
    val periodUtils: DhisPeriodUtils,
    private val launchMode: LaunchMode,
) : ViewModel() {

    lateinit var repository: EventDetailsRepository
    lateinit var configureEventReportDate: ConfigureEventReportDate
    lateinit var configureEventCatCombo: ConfigureEventCatCombo

    private val _programStage: MutableStateFlow<ProgramStage?> = MutableStateFlow(null)
    val programStage: StateFlow<ProgramStage?> get() = _programStage

    var showCalendar: (() -> Unit)? = null
    var showPeriods: (() -> Unit)? = null
    var onEventScheduled: ((String) -> Unit)? = null
    var onEventSkipped: ((String) -> Unit)? = null

    private val _eventDate: MutableStateFlow<EventDate> = MutableStateFlow(EventDate())
    val eventDate: StateFlow<EventDate> get() = _eventDate

    private val _eventCatCombo: MutableStateFlow<EventCatCombo> = MutableStateFlow(EventCatCombo())
    val eventCatCombo: StateFlow<EventCatCombo> get() = _eventCatCombo

    val programStages: List<ProgramStage>
        get() = when (launchMode) {
            is LaunchMode.NewSchedule -> launchMode.programStages
            is LaunchMode.EnterEvent -> emptyList()
        }

    val enrollment: Enrollment?
        get() = when (launchMode) {
            is LaunchMode.NewSchedule -> launchMode.enrollment
            is LaunchMode.EnterEvent -> null
        }

    init {
        viewModelScope.launch {
            val programStage = when (launchMode) {
                is LaunchMode.NewSchedule -> launchMode.programStages.first()
                is LaunchMode.EnterEvent -> {
                    val eventProgramStageId = d2.event(launchMode.eventUid)?.programStage()
                    d2.programModule().programStages().uid(eventProgramStageId).blockingGet()
                }
            }

            _programStage.value = programStage

            when (launchMode) {
                is LaunchMode.NewSchedule -> loadNewScheduleConfiguration(launchMode)
                is LaunchMode.EnterEvent -> loadEventDueDate(launchMode, programStage)
            }
        }
    }

    private fun loadEventDueDate(
        launchMode: LaunchMode.EnterEvent,
        programStage: ProgramStage?,
    ) {
        val event = d2.event(launchMode.eventUid)
        val programId = event?.program()
        val dueDate = event?.dueDate()

        _eventDate.value = EventDate(
            label = programStage?.dueDateLabel() ?: eventDetailResourcesProvider(programId.orEmpty()).provideDueDate(),
            currentDate = dueDate,
            dateValue = DateUtils.uiDateFormat().format(dueDate ?: ""),
        )
    }

    private fun loadNewScheduleConfiguration(launchMode: LaunchMode.NewSchedule) {
        val programId = launchMode.enrollment.program().orEmpty()

        repository = EventDetailsRepository(
            d2 = d2,
            programUid = programId,
            eventUid = null,
            programStageUid = programStage.value?.uid(),
            fieldFactory = null,
            eventCreationType = EventCreationType.SCHEDULE,
            onError = resourceManager::parseD2Error,
        )
        configureEventReportDate = ConfigureEventReportDate(
            creationType = EventCreationType.SCHEDULE,
            resourceProvider = eventDetailResourcesProvider(programId),
            repository = repository,
            periodType = programStage.value?.periodType(),
            periodUtils = periodUtils,
            enrollmentId = launchMode.enrollment.uid(),
            scheduleInterval = programStage.value?.standardInterval() ?: 0,
        )
        configureEventCatCombo = ConfigureEventCatCombo(repository = repository)

        loadProgramStage()
    }

    private fun eventDetailResourcesProvider(programId: String) = EventDetailResourcesProvider(
        programUid = programId,
        programStage = programStage.value?.uid(),
        resourceManager = resourceManager,
        eventResourcesProvider = eventResourcesProvider,
    )

    private fun loadProgramStage() {
        viewModelScope.launch {
            configureEventReportDate().collect {
                _eventDate.value = it
            }

            configureEventCatCombo().collect {
                _eventCatCombo.value = it
            }
        }
    }

    fun getSelectableDates(): SelectableDates {
        val maxDate = if (!eventDate.value.allowFutureDates) {
            SimpleDateFormat("ddMMyyyy", Locale.US).format(Date(System.currentTimeMillis() - 1000))
        } else if (eventDate.value.maxDate != null) {
            SimpleDateFormat("ddMMyyyy", Locale.US).format(eventDate.value.maxDate)
        } else {
            "12112124"
        }
        val minDate = if (eventDate.value.minDate != null) {
            SimpleDateFormat("ddMMyyyy", Locale.US).format(eventDate.value.minDate)
        } else {
            "12111924"
        }

        return SelectableDates(minDate, maxDate)
    }

    fun setUpEventReportDate(selectedDate: Date? = null) {
        viewModelScope.launch {
            when (launchMode) {
                is LaunchMode.NewSchedule -> {
                    configureEventReportDate(selectedDate)
                        .flowOn(Dispatchers.IO)
                        .collect {
                            _eventDate.value = it
                        }
                }
                is LaunchMode.EnterEvent -> {
                    _eventDate.value = _eventDate.value.copy(
                        currentDate = selectedDate,
                        dateValue = DateUtils.uiDateFormat().format(selectedDate ?: ""),
                    )
                }
            }
        }
    }

    fun onClearEventReportDate() {
        _eventDate.value = eventDate.value.copy(
            currentDate = null,
            dateValue = null,
        )
    }

    fun setUpCategoryCombo(categoryOption: Pair<String, String?>? = null) {
        viewModelScope.launch {
            configureEventCatCombo(categoryOption)
                .flowOn(Dispatchers.IO)
                .collect {
                    _eventCatCombo.value = it
                }
        }
    }

    fun onClearCatCombo() {
        _eventCatCombo.value = eventCatCombo.value.copy(isCompleted = false)
    }

    fun showPeriodDialog() {
        programStage.value?.periodType()?.let {
            showPeriods?.invoke()
        }
    }

    fun onDateSet(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar[year, month - 1, day, 0, 0] = 0
        calendar[Calendar.MILLISECOND] = 0
        val selectedDate = calendar.time
        setUpEventReportDate(selectedDate)
    }

    fun updateStage(stage: ProgramStage) {
        _programStage.value = stage

        when (launchMode) {
            is LaunchMode.NewSchedule -> {
                loadNewScheduleConfiguration(launchMode = launchMode)
            }

            is LaunchMode.EnterEvent -> {
                // no-op
            }
        }
    }

    fun scheduleEvent() {
        viewModelScope.launch {
            val eventDate = eventDate.value.currentDate ?: return@launch

            when (launchMode) {
                is LaunchMode.NewSchedule -> {
                    repository.scheduleEvent(
                        enrollmentUid = launchMode.enrollment.uid(),
                        dueDate = eventDate,
                        orgUnitUid = launchMode.enrollment.organisationUnit(),
                        categoryOptionComboUid = eventCatCombo.value.uid,
                    ).flowOn(Dispatchers.IO)
                        .collect {
                            if (it != null) {
                                onEventScheduled?.invoke(programStage.value?.uid() ?: "")
                            }
                        }
                }
                is LaunchMode.EnterEvent -> {
                    // TODO: Update event
                }
            }
        }
    }

    fun onCancelEvent() {
        viewModelScope.launch {
            when (launchMode) {
                is LaunchMode.EnterEvent -> {
                    d2.eventModule().events().uid(launchMode.eventUid).setStatus(EventStatus.SKIPPED)
                    onEventSkipped?.invoke(launchMode.eventUid)
                }
                is LaunchMode.NewSchedule -> {
                    // no-op
                }
            }
        }
    }
}
