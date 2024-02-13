package org.dhis2.usescases.searchTrackEntity.searchparameters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.dhis2.commons.orgunitselector.OrgUnitSelectorScope
import org.dhis2.commons.resources.ColorUtils
import org.dhis2.commons.resources.ResourceManager
import org.dhis2.form.model.FieldUiModel
import org.dhis2.form.model.FieldUiModelImpl
import org.dhis2.form.ui.event.RecyclerViewUiEvents
import org.dhis2.form.ui.intent.FormIntent
import org.dhis2.usescases.searchTrackEntity.SearchTEIViewModel
import org.dhis2.usescases.searchTrackEntity.searchparameters.model.SearchParametersUiState
import org.dhis2.usescases.searchTrackEntity.searchparameters.provider.provideParameterSelectorItem
import org.hisp.dhis.android.core.common.ValueType
import org.hisp.dhis.mobile.ui.designsystem.component.Button
import org.hisp.dhis.mobile.ui.designsystem.component.ButtonStyle
import org.hisp.dhis.mobile.ui.designsystem.component.parameter.ParameterSelectorItem
import org.hisp.dhis.mobile.ui.designsystem.theme.Shape
import org.hisp.dhis.mobile.ui.designsystem.theme.SurfaceColor

@Composable
fun SearchParametersScreen(
    resourceManager: ResourceManager,
    uiState: SearchParametersUiState,
    intentHandler: (FormIntent) -> Unit,
    showOrgUnit: (
        uid: String,
        preselectedOrgUnits: List<String>,
        orgUnitScope: OrgUnitSelectorScope,
    ) -> Unit,
    onSearchClick: () -> Unit,
    onClear: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val snackBarHostState = scaffoldState.snackbarHostState
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val callback = remember {
        object : FieldUiModel.Callback {
            override fun intent(intent: FormIntent) {
                intentHandler.invoke(intent)
            }

            override fun recyclerViewUiEvents(uiEvent: RecyclerViewUiEvents) {
                when (uiEvent) {
                    is RecyclerViewUiEvents.OpenOrgUnitDialog ->
                        showOrgUnit(
                            uiEvent.uid,
                            uiEvent.value?.let { listOf(it) } ?: emptyList(),
                            uiEvent.orgUnitSelectorScope ?: OrgUnitSelectorScope.UserSearchScope(),
                        )

                    else -> {}
                }
            }
        }
    }

    uiState.minAttributesMessage?.let { message ->
        coroutineScope.launch {
            uiState.shouldShowMinAttributeWarning.collectLatest {
                if (it) {
                    snackBarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }
    }

    Scaffold(
        backgroundColor = Color.Transparent,
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 48.dp,
                ),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White, shape = Shape.LargeTop)
                .padding(it),
        ) {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .verticalScroll(rememberScrollState()),
            ) {
                uiState.items.forEach { fieldUiModel ->
                    fieldUiModel.setCallback(callback)
                    ParameterSelectorItem(
                        model = provideParameterSelectorItem(
                            resources = resourceManager,
                            focusManager = focusManager,
                            fieldUiModel = fieldUiModel,
                            callback = callback,
                        ),
                    )
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = ButtonStyle.TEXT,
                    text = "Clear search",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = "Clear search",
                            tint = SurfaceColor.Primary,
                        )
                    },
                ) {
                    focusManager.clearFocus()
                    onClear()
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp, 16.dp, 8.dp),
                style = ButtonStyle.FILLED,
                text = "Search",
            ) {
                onSearchClick()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchFormPreview() {
    SearchParametersScreen(
        resourceManager = ResourceManager(LocalContext.current, ColorUtils()),
        uiState = SearchParametersUiState(
            items = listOf(
                FieldUiModelImpl(
                    uid = "uid1",
                    layoutId = 1,
                    label = "Label 1",
                    autocompleteList = emptyList(),
                    optionSetConfiguration = null,
                    valueType = ValueType.TEXT,
                ),
                FieldUiModelImpl(
                    uid = "uid2",
                    layoutId = 2,
                    label = "Label 2",
                    autocompleteList = emptyList(),
                    optionSetConfiguration = null,
                    valueType = ValueType.TEXT,
                ),
            ),
        ),
        intentHandler = {},
        showOrgUnit = { _, _, _ -> },
        onSearchClick = {},
        onClear = {},
    )
}

fun initSearchScreen(
    composeView: ComposeView,
    viewModel: SearchTEIViewModel,
    program: String?,
    teiType: String,
    resources: ResourceManager,
    showOrgUnit: (
        uid: String,
        preselectedOrgUnits: List<String>,
        orgUnitScope: OrgUnitSelectorScope,
    ) -> Unit,
    onClear: () -> Unit,
) {
    viewModel.fetchSearchParameters(
        programUid = program,
        teiTypeUid = teiType,
    )
    composeView.setContent {
        SearchParametersScreen(
            resourceManager = resources,
            uiState = viewModel.uiState,
            onSearchClick = viewModel::onSearchClick,
            intentHandler = viewModel::onParameterIntent,
            showOrgUnit = showOrgUnit,
            onClear = {
                onClear()
                viewModel.clearQueryData()
            },
        )
    }
}
