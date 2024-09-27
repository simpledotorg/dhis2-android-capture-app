package org.dhis2.usescases.teiDashboard.dialogs.scheduling

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import org.dhis2.bindings.app
import org.dhis2.commons.data.EventCreationType
import org.dhis2.commons.dialogs.PeriodDialog
import org.dhis2.commons.dialogs.calendarpicker.CalendarPicker
import org.dhis2.commons.dialogs.calendarpicker.OnDatePickerListener
import org.dhis2.form.R
import org.dhis2.form.model.EventMode
import org.dhis2.usescases.eventsWithoutRegistration.eventCapture.EventCaptureActivity
import org.hisp.dhis.android.core.enrollment.Enrollment
import org.hisp.dhis.android.core.program.ProgramStage
import java.util.Date
import javax.inject.Inject

class SchedulingDialog : BottomSheetDialogFragment() {

    companion object {
        const val SCHEDULING_DIALOG = "SCHEDULING_DIALOG"
        const val SCHEDULING_DIALOG_RESULT = "SCHEDULING_DIALOG_RESULT"
        const val SCHEDULING_EVENT_SKIPPED = "SCHEDULING_EVENT_SKIPPED"
        const val SCHEDULING_EVENT_DUE_DATE_UPDATED = "SCHEDULING_EVENT_DUE_DATE_UPDATED"
        const val PROGRAM_STAGE_UID = "PROGRAM_STAGE_UID"
        const val EVENT_UID = "EVENT_UID"

        private const val TAG_LAUNCH_MODE = "SCHEDULING_DIALOG_LAUNCH_MODE"

        fun newSchedule(
            enrollment: Enrollment,
            programStages: List<ProgramStage>,
            showYesNoOptions: Boolean,
            eventCreationType: EventCreationType,
        ): SchedulingDialog {
            return SchedulingDialog().apply {
                val launchMode = LaunchMode.NewSchedule(
                    enrollment = enrollment,
                    programStages = programStages,
                    showYesNoOptions = showYesNoOptions,
                    eventCreationType = eventCreationType,
                )
                putLaunchModeArguments(launchMode)
            }
        }

        fun enterEvent(
            eventUid: String,
            showYesNoOptions: Boolean,
            eventCreationType: EventCreationType,
        ) = SchedulingDialog().apply {
            val launchMode = LaunchMode.EnterEvent(
                eventUid = eventUid,
                showYesNoOptions = showYesNoOptions,
                eventCreationType = eventCreationType,
            )
            putLaunchModeArguments(launchMode)
        }

        private fun SchedulingDialog.putLaunchModeArguments(launchMode: LaunchMode) {
            val launchModeString = Gson().toJson(launchMode)
            arguments?.putString(TAG_LAUNCH_MODE, launchModeString)
        }
    }

    private val launchMode: LaunchMode by lazy {
        val launchModeString = arguments?.getString(TAG_LAUNCH_MODE)!!
        Gson().fromJson(launchModeString, LaunchMode::class.java)
    }

    @Inject
    lateinit var factory: SchedulingViewModelFactory.Factory

    val viewModel: SchedulingViewModel by viewModels {
        factory.build(launchMode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app().userComponent()?.plus(SchedulingModule())?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.onEventScheduled = {
            setFragmentResult(SCHEDULING_DIALOG_RESULT, bundleOf(PROGRAM_STAGE_UID to it))
            dismiss()
        }

        viewModel.onEventSkipped = {
            setFragmentResult(SCHEDULING_EVENT_SKIPPED, bundleOf(EVENT_UID to it))
            dismiss()
        }

        viewModel.onDueDateUpdated = {
            setFragmentResult(SCHEDULING_EVENT_DUE_DATE_UPDATED, bundleOf())
            dismiss()
        }

        viewModel.onEnterEvent = { eventUid, programUid ->
            val bundle = EventCaptureActivity.getActivityBundle(
                eventUid,
                programUid,
                EventMode.SCHEDULE,
            )
            Intent(activity, EventCaptureActivity::class.java).apply {
                putExtras(bundle)
                startActivity(this)
            }

            dismiss()
        }

        viewModel.showCalendar = {
            showCalendarDialog()
        }

        viewModel.showPeriods = {
            showPeriodDialog()
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed,
            )
            setContent {
                SchedulingDialogUi(
                    viewModel = viewModel,
                    programStages = viewModel.programStages,
                    orgUnitUid = viewModel.enrollment?.organisationUnit(),
                    launchMode = launchMode,
                    onDismiss = { dismiss() },
                )
            }
        }
    }

    private fun showCalendarDialog() {
        val dialog = CalendarPicker(requireContext())
        dialog.setInitialDate(viewModel.eventDate.value.currentDate)
        dialog.setMinDate(viewModel.eventDate.value.minDate)
        dialog.setMaxDate(viewModel.eventDate.value.maxDate)
        dialog.isFutureDatesAllowed(viewModel.eventDate.value.allowFutureDates)
        dialog.setListener(
            object : OnDatePickerListener {
                override fun onNegativeClick() {
                    // Unused
                }
                override fun onPositiveClick(datePicker: DatePicker) {
                    viewModel.onDateSet(
                        datePicker.year,
                        datePicker.month,
                        datePicker.dayOfMonth,
                    )
                }
            },
        )
        dialog.show()
    }

    private fun showPeriodDialog() {
        PeriodDialog()
            .setPeriod(viewModel.eventDate.value.periodType)
            .setMinDate(viewModel.eventDate.value.minDate)
            .setMaxDate(viewModel.eventDate.value.maxDate)
            .setPossitiveListener { selectedDate: Date ->
                viewModel.setUpEventReportDate(selectedDate)
            }
            .show(requireActivity().supportFragmentManager, PeriodDialog::class.java.simpleName)
    }

    sealed interface LaunchMode {

        val showYesNoOptions: Boolean
        val eventCreationType: EventCreationType

        data class NewSchedule(
            val enrollment: Enrollment,
            val programStages: List<ProgramStage>,
            override val showYesNoOptions: Boolean,
            override val eventCreationType: EventCreationType,
        ) : LaunchMode

        data class EnterEvent(
            val eventUid: String,
            override val showYesNoOptions: Boolean,
            override val eventCreationType: EventCreationType,
        ) : LaunchMode
    }
}
